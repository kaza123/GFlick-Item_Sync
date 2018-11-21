/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db_connections;

import com.mchange.v2.c3p0.DataSources;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author 'Kasun Chamara'
 */
public class DataSourceWrapper {

    private final DataSource dataSource;

    public DataSourceWrapper(String jdbcUrl, String user, String password) throws SQLException {
        DataSource unpooledDataSource = DataSources.unpooledDataSource(jdbcUrl, user, password);
        this.dataSource = DataSources.pooledDataSource(unpooledDataSource);
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

}
