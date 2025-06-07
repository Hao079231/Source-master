package com.test.master.dto;

public class ErrorCode {
    /**
     * General error code
     * */
    public static final String GENERAL_ERROR_REQUIRE_PARAMS = "ERROR-GENERAL-0000";
    public static final String GENERAL_ERROR_STORE_LOCKED = "ERROR-GENERAL-0001";
    public static final String GENERAL_ERROR_ACCOUNT_LOCKED = "ERROR-GENERAL-0002";
    public static final String GENERAL_ERROR_SHOP_LOCKED = "ERROR-GENERAL-0003";
    public static final String GENERAL_ERROR_STORE_NOT_FOUND = "ERROR-GENERAL-0004";
    public static final String GENERAL_ERROR_ACCOUNT_NOT_FOUND = "ERROR-GENERAL-0005";

    /**
     * Db config error code
     */
    public static final String DB_CONFIG_ERROR_UNAUTHORIZED = "ERROR-DB-CONFIG-000";
    public static final String DB_CONFIG_ERROR_NOT_FOUND = "ERROR-DB-CONFIG-001";
    public static final String DB_CONFIG_ERROR_NOT_INITIALIZE = "ERROR-DB-CONFIG-002";
    public static final String DB_CONFIG_ERROR_CANNOT_CREATE_DB = "ERROR-DB-CONFIG-003";
    public static final String DB_CONFIG_ERROR_CANNOT_RESTORE_DB = "ERROR-DB-CONFIG-004";
    public static final String DB_CONFIG_ERROR_UPLOAD = "ERROR-DB-RESTORE-005";
    public static final String DB_CONFIG_UPGRADE_TENANT_ALREADY_IN_PROCESS = "ERROR-DB-CONFIG-006";
    public static final String DB_CONFIG_ERROR_DROP = "ERROR-DB-CONFIG-007";
    public static final String DB_CONFIG_SHOP_EXISTED = "ERROR-DB-CONFIG-008";
    /**
     * Starting error code Account
     * */
    public static final String ACCOUNT_ERROR_UNKNOWN = "ERROR-ACCOUNT-0000";
    public static final String ACCOUNT_ERROR_USERNAME_EXIST = "ERROR-ACCOUNT-0001";
    public static final String ACCOUNT_ERROR_NOT_FOUND = "ERROR-ACCOUNT-0002";
    public static final String ACCOUNT_ERROR_WRONG_PASSWORD = "ERROR-ACCOUNT-0003";
    public static final String ACCOUNT_ERROR_WRONG_HASH_RESET_PASS = "ERROR-ACCOUNT-0004";
    public static final String ACCOUNT_ERROR_LOCKED = "ERROR-ACCOUNT-0005";
    public static final String ACCOUNT_ERROR_OPT_INVALID = "ERROR-ACCOUNT-0006";
    public static final String ACCOUNT_ERROR_LOGIN = "ERROR-ACCOUNT-0007";
    public static final String ACCOUNT_ERROR_MERCHANT_LOGIN_ERROR_DEVICE = "ERROR-ACCOUNT-0008";
    public static final String ACCOUNT_ERROR_MERCHANT_LOGIN_ERROR_STORE = "ERROR-ACCOUNT-0009";
    public static final String ACCOUNT_ERROR_MERCHANT_LOGIN_WRONG_STORE = "ERROR-ACCOUNT-0010";
    public static final String ACCOUNT_ERROR_MERCHANT_SERVICE_NOT_REGISTER = "ERROR-ACCOUNT-0011";
    public static final String ACCOUNT_ERROR_ACCOUNT_IS_EMPLOYEE = "ERROR-ACCOUNT-0012";
    public static final String ACCOUNT_ERROR_ACCOUNT_IS_NOT_MANAGER = "ERROR-ACCOUNT-0013";
    public static final String ACCOUNT_ERROR_NOT_DELETE_SUPPER_ADMIN = "ERROR-ACCOUNT-00014";

    /**
     * Starting error code Group
     * */
    public static final String GROUP_ERROR_NOT_FOUND = "ERROR-GROUP-0000";

    /**
     * Starting error code Restaurant
     * */
    public static final String RESTAURANT_ERROR_NOT_FOUND = "ERROR-RESTAURANT-0000";
    public static final String RESTAURANT_ERROR_EXIST = "ERROR-RESTAURANT-0001";
    public static final String RESTAURANT_ERROR_UN_AUTHORIZE = "ERROR-RESTAURANT-0002";


    /**
     * Starting error code AuthenticationToken
     * */
    public static final String AUTH_TOKEN_ERROR_UNKNOWN = "ERROR-AUTH-TOKEN-0000";
    public static final String AUTH_TOKEN_ERROR_WRONG_SHOP = "ERROR-AUTH-TOKEN-0001";
    public static final String AUTH_TOKEN_ERROR_NOT_FOUND = "ERROR-AUTH-TOKEN-0002";
    public static final String AUTH_TOKEN_ERROR_INVALID = "ERROR-AUTH-TOKEN-0003";
    public static final String AUTH_TOKEN_ERROR_INVALID_STORE = "ERROR-AUTH-TOKEN-0004";
    public static final String AUTH_TOKEN_ERROR_INVALID_DEVICE = "ERROR-AUTH-TOKEN-0005";

    /**
     * Starting error code Customer
     * */
    public static final String CUSTOMER_ERROR_UNKNOWN = "ERROR-CUSTOMER-0000";
    public static final String CUSTOMER_ERROR_EXIST = "ERROR-CUSTOMER-0002";
    public static final String CUSTOMER_ERROR_UPDATE = "ERROR-CUSTOMER-0003";
    public static final String CUSTOMER_ERROR_NOT_FOUND = "ERROR-CUSTOMER-0004";
    public static final String CUSTOMER_ERROR_UN_AUTHORIZE = "ERROR-GROUP-0005";
    public static final String CUSTOMER_ERROR_NOT_ACTIVE = "ERROR-GROUP-0006";


    /**
     * Starting error code Store
     * */
    public static final String SERVICE_ERROR_UNKNOWN = "ERROR-SERVICE-0000";
    public static final String SERVICE_ERROR_NOT_FOUND = "ERROR-SERVICE-0001";
    public static final String SERVICE_ERROR_DUPLICATE_PATH = "ERROR-SERVICE-0002";
    public static final String SERVICE_ERROR_USERNAME_EXIST = "ERROR-SERVICE-0003";
    public static final String SERVICE_ERROR_WRONG_OLD_PWD = "ERROR-SERVICE-0004";
    public static final String SERVICE_ERROR_TENANT_ID_EXIST = "ERROR-SERVICE-0005";

    /**
     * Starting error code SHOP ACCOUNT
     * */
    public static final String SHOP_ACCOUNT_ERROR_UNKNOWN = "ERROR-SHOP_ACCOUNT-0000";

    /**
     * Server provider error code
     */
    public static final String SERVER_PROVIDER_ERROR_UNAUTHORIZED = "ERROR-SERVER-PROVIDER-000";
    public static final String SERVER_PROVIDER_ERROR_NOT_FOUND = "ERROR-SERVER-PROVIDER-001";
    public static final String SERVER_PROVIDER_ERROR_URL_EXISTED = "ERROR-SERVER-PROVIDER-002";
    public static final String SERVER_PROVIDER_ERROR_NOT_ANY_VALID = "ERROR-SERVER-PROVIDER-003";


    /**
     * Starting error code DELETE FILE
     * */
    public static final String FILE_ERROR_DELETE = "ERROR-FILE-0000";

}
