/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import com.amazonaws.secretsmanager.sql.AWSSecretsManagerPostgreSQLDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.postgresql.PGConnection;

/**
 * Connection to the Master with no Pooling
 * https://docs.aws.amazon.com/secretsmanager/latest/userguide/retrieving-secrets_jdbc.html
 * 
 * @author jensen
 */
public class PostgresDataSource {

    /**
     * These env vars are passed to all Lambdas via the global section in SAM
     */
    static final String SECRET_ARN = System.getenv("DB_MASTER_SECRET_ARN");
    static final String DB_ENDPOINT = System.getenv("DB_ENDPOINT");
    static final String DB_NAME = System.getenv("DB_NAME");
    
    // Additional properties passed to the driver
    static final Properties info;

    static {
        System.out.println("Secrets ARN is " + SECRET_ARN);
        // Load the JDBC driver
        new AWSSecretsManagerPostgreSQLDriver();

        info = new Properties();
        info.put("user", SECRET_ARN);
    }

    private static Connection getConnection() {
        try {
            // Establish the connection
            // Set the endpoint and port. You can also retrieve it from a key/value pair in the secret.
            final String URL = "jdbc-secretsmanager:postgresql://" + DB_ENDPOINT + "/" + DB_NAME;
            return DriverManager.getConnection(URL, info);
        } catch (SQLException se) {
            System.err.println(se.getMessage());
            return null;
        }
    }

    public static PGConnection getPGConnection() throws SQLException {
        return (getConnection().unwrap(PGConnection.class));
    }

    public static DSLContext getDSL() {
        return (DSL.using(getConnection(), SQLDialect.POSTGRES));
    }

}
