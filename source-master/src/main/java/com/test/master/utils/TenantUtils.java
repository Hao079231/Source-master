package com.test.master.utils;

import com.test.master.dto.ErrorCode;
import com.test.master.exception.BadRequestException;
import com.test.master.model.DbConfig;
import com.test.master.model.ServerProvider;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

public class TenantUtils {
    public static String parseDatabaseNameFromConnectionString(String url) {
        String cleanString = url.substring("jdbc:mysql://".length(), url.indexOf("?"));
        return cleanString.substring(cleanString.indexOf("/") + 1);
    }

    public static void removeTenantResources(DbConfig dbConfig) {
        if (dbConfig != null && dbConfig.getServerProvider() != null) {
            String dbName = parseDatabaseNameFromConnectionString(dbConfig.getUrl());
            DataSource adminDataSource = createAdminDataSource(dbConfig.getServerProvider());

            try (
                Connection connection = adminDataSource.getConnection();
                Statement stmt = connection.createStatement();
            ) {
                stmt.execute("DROP DATABASE IF EXISTS `" + dbName + "`;");
                stmt.execute("DROP USER IF EXISTS '" + dbConfig.getUsername() + "';");
                stmt.execute("DROP USER IF EXISTS '" + dbConfig.getUsername() + "'@'localhost';");
                stmt.execute("FLUSH PRIVILEGES;");
                System.out.println("Tenant resources dropped successfully...");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new BadRequestException("[Restaurant] Failed to remove tenant resources", ErrorCode.DB_CONFIG_ERROR_DROP);
            }
        }
    }

    private static DataSource createAdminDataSource(ServerProvider provider) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(provider.getMySqlRootUser());
        dataSource.setPassword(provider.getMySqlRootPassword());
        dataSource.setJdbcUrl(provider.getMySqlJdbcUrl());
        dataSource.setDriverClassName(provider.getDriverClassName());
        return dataSource;
    }
}
