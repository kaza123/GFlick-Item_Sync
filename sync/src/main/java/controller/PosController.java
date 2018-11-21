/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import db_connections.DataSourceWrapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import model.MItem;
import org.apache.log4j.Logger;

/**
 *
 * @author 'Kasun Chamara'
 */
public class PosController {

    private final DataSourceWrapper mysqlDataSourceWrapper;
    private static final Logger LOGGER = Logger.getLogger(PosController.class);

    private static PosController instance;

    public static PosController getInstance() throws SQLException {
        if (instance == null) {
            instance = new PosController();
        }
        return instance;
    }

    public PosController() throws SQLException {
        Properties prop = new Properties();
        InputStream input = null;
        String dbName = null;
        String user = null;
        String pswd = null;
        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            // set value into variable
            dbName = prop.getProperty("posDatabase");
            user = prop.getProperty("posUser");
//            pswd = prop.getProperty("posPassword");
            pswd = "mysql_pos";

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.mysqlDataSourceWrapper = new DataSourceWrapper(dbName, user, pswd);
    }

    public DataSourceWrapper getConnection() {
        return mysqlDataSourceWrapper;
    }

    public Integer saveItem(MItem mItem, Connection pos) throws SQLException {
        String name = mItem.getName() + "-" + mItem.getSize();
        if (!"NEW".equals(mItem.getStyleRepeat())) {
            name += "-" + mItem.getStyleRepeat();
        }
        Integer getNxtIdx = getNxtIdx(pos);
        if (getNxtIdx <= 0) {
            throw new RuntimeException("Can't find next idx !");
        }
//        master save 
        
        String insertSql = "insert into pos_m_item(barcode,details,print_description,price,image_code,\n"
                + "category,sub_category,idx,cost_center) \n"
                + "values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatementInsert = pos.prepareStatement(insertSql);
        preparedStatementInsert.setString(1, mItem.getBarcode());
        preparedStatementInsert.setString(2, name);
        preparedStatementInsert.setString(3, mItem.getPrintDescription());
        preparedStatementInsert.setBigDecimal(4, mItem.getUnitPrice());
        preparedStatementInsert.setString(5, mItem.getStyle());
        preparedStatementInsert.setString(6, mItem.getDepartment());
        preparedStatementInsert.setString(7, mItem.getCategory());
        preparedStatementInsert.setInt(8, getNxtIdx);
        preparedStatementInsert.setString(9, mItem.getDepartment());

        return preparedStatementInsert.executeUpdate();
    }

    private Integer getNxtIdx(Connection pos) throws SQLException {
        String insertSql = "select max(pos_m_item.idx)+1 from pos_m_item";
        PreparedStatement preparedStatementInsert = pos.prepareStatement(insertSql);
        ResultSet rst = preparedStatementInsert.executeQuery();
        if (rst.next()) {
            return rst.getInt(1);
        }
        return 0;
    }

}
