package com.test.master.config;

import com.test.master.dto.AccountForTokenDto;
import com.test.master.model.Permission;
import com.test.master.utils.ZipUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {
    private JdbcTemplate jdbcTemplate;

    private ObjectMapper objectMapper;

    public CustomTokenEnhancer(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        String tenantId = authentication.getOAuth2Request().getRequestParameters().get("tenantId");
        String username = authentication.getName();
        if(authentication.getOAuth2Request().getGrantType() != null){
            additionalInfo = getAdditionalInfo(null,username, authentication.getOAuth2Request().getGrantType(), null);
        }else {
            String grantType = authentication.getOAuth2Request().getRequestParameters().get("grantType");
            if(grantType.equals(SecurityConstant.GRANT_TYPE_CUSTOMER) ||
               grantType.equals(SecurityConstant.GRANT_TYPE_DRIVER)){
                Long userId = Long.parseLong(authentication.getOAuth2Request().getRequestParameters().get("userId"));
                additionalInfo = getAdditionalInfoCustom(null, username, grantType, userId);
            }
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

    private Map<String, Object> getAdditionalInfoCustom(String tenantName, String username, String grantType, Long userId) {
        Map<String, Object> additionalInfo = new HashMap<>();
        AccountForTokenDto a = getAccountByUsername(username);

        if (a != null) {
            Long accountId = userId;
            Long storeId = -1L;
            String kind = a.getKind() + "";//token kind
            Long deviceId = -1L;// id cua thiet bi, lưu ở table device để get firebase url..
            String pemission = "<>";//empty string
            Integer userKind = a.getKind(); //loại user là admin hay là gì
            Integer tabletKind = -1;
            Long orderId = -1L;
            Boolean isSuperAdmin = a.getIsSuperAdmin();
            String tenantId = "";
            additionalInfo.put("user_id", accountId);
            additionalInfo.put("user_kind", a.getKind());
            additionalInfo.put("grant_type", grantType);
            additionalInfo.put("tenant_info", tenantId);
            String DELIM = "|";
            String additionalInfoStr = ZipUtils.zipString(accountId + DELIM
                    + storeId + DELIM
                    + kind + DELIM
                    + pemission + DELIM
                    + deviceId + DELIM
                    + userKind + DELIM
                    + username + DELIM
                    + tabletKind + DELIM
                    + orderId + DELIM
                    + isSuperAdmin + DELIM
                    + tenantId);
            additionalInfo.put("additional_info", additionalInfoStr);
        }
        return additionalInfo;
    }

    private Map<String, Object> getAdditionalInfo(String tenantName, String username, String grantType, Long userId) {
        Map<String, Object> additionalInfo = new HashMap<>();
        AccountForTokenDto a = getAccountByUsername(username);

        if (a != null) {
            Long accountId = a.getId();
            Long storeId = -1L;
            String kind = a.getKind() + "";//token kind
            Long deviceId = -1L;// id cua thiet bi, lưu ở table device để get firebase url..
            String pemission = "<>";//empty string
            Integer userKind = a.getKind(); //loại user là admin hay là gì
            Integer tabletKind = -1;
            Long orderId = -1L;
            Boolean isSuperAdmin = a.getIsSuperAdmin();
            String tenantId;
            if(tenantName == null){
                if(a.getParentId() != null){
                    tenantId = getTenantByAccountId(a.getParentId());
                }else{
                    tenantId = getTenantByAccountId(a.getId());
                }
            }else{
                tenantId = getTenantInfoByTenantId(tenantName);
            }
            additionalInfo.put("user_id", accountId);
            additionalInfo.put("user_kind", a.getKind());
            additionalInfo.put("grant_type", grantType);
            additionalInfo.put("tenant_info", tenantId);
            String DELIM = "|";
            String additionalInfoStr = ZipUtils.zipString(accountId + DELIM
                    + storeId + DELIM
                    + kind + DELIM
                    + pemission + DELIM
                    + deviceId + DELIM
                    + userKind + DELIM
                    + username + DELIM
                    + tabletKind + DELIM
                    + orderId + DELIM
                    + isSuperAdmin + DELIM
                    + tenantId);
            additionalInfo.put("additional_info", additionalInfoStr);
        }
        return additionalInfo;
    }

    public AccountForTokenDto getAccountByUsername(String username) {
        try {
            String query = "SELECT id, kind, username, email, full_name, is_super_admin, parent_id " +
                    "FROM db_master_account WHERE username = ? and status = 1 limit 1";
            log.debug(query);
            List<AccountForTokenDto> dto = jdbcTemplate.query(query, new Object[]{username},  new BeanPropertyRowMapper<>(AccountForTokenDto.class));
            if (dto.size() > 0)return dto.get(0);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTenantByAccountId(Long accountId){
        try{
            String query = "select distinct coalesce(GROUP_CONCAT(CONCAT(d.name, \"&\", d.service_id) SEPARATOR ':'), '') " +
                    "from db_master_service r " +
                    "join db_master_db_config d on r.account_id = d.service_id " +
                    "where account_id = ? and status = 1 ";
            return jdbcTemplate.queryForObject(query, String.class, accountId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getTenantInfoByTenantId(String tenantId){
        try{
            String query = "select distinct coalesce(GROUP_CONCAT(CONCAT(d.name, \"&\", d.service_id) SEPARATOR ':'), '') " +
                    "from db_master_service r " +
                    "join db_master_db_config d on r.account_id = d.service_id " +
                    "where tenant_id = ? and status = 1 ";
            return jdbcTemplate.queryForObject(query, String.class, tenantId);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Permission> getListPermissionByUserKind(Integer kind) {
        try{
            String query = "select * " +
                    "from db_master_permission p " +
                    "join db_master_permission_group g on p.id = g.permission_id " +
                    "join db_master_account a on a.group_id = g.group_id " +
                    "where a.kind = ?";
            return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Permission.class), kind);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
