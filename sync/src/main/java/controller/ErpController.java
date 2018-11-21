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
import java.util.ArrayList;
import java.util.Properties;
import model.MItem;
import org.apache.log4j.Logger;

/**
 *
 * @author 'Kasun Chamara'
 */
public class ErpController {

    private final DataSourceWrapper mssqlDataSourceWrapper;
    private static final Logger LOGGER = Logger.getLogger(ErpController.class);

    public ErpController() throws SQLException {
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
            dbName = prop.getProperty("erpDatabase");
            user = prop.getProperty("erpUser");
//            pswd = prop.getProperty("erpPassword");
            pswd = "Rockball789!";

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.mssqlDataSourceWrapper = new DataSourceWrapper(dbName, user, pswd);
    }

    public DataSourceWrapper getConnection() {
        return mssqlDataSourceWrapper;
    }

    public static ArrayList<MItem> getItems(Connection erp) throws SQLException {
        String sql = "select style_barcode.intBarCodeId as barcode,\n"
                + "	style_barcode.strBarCode as name,\n"
                + "	style_barcode.strBarCodeName as print_desc,\n"
                + "	style_barcode.strSizeRef as size,\n"
                + "	style_barcode.dblUnitPrice as unit_price,\n"
                + "	style_barcode.strDepartment as department,\n"
                + "	style_barcode.strCategory as caregory,\n"
                + "	style_barcode.strStyle as style,\n"
                + "	style_barcode.strStyleRepeat as style_repeat\n"
                + "from style_barcode\n"
                + "where style_barcode.is_sync!=1 or style_barcode.is_sync is null";
        PreparedStatement preparedStatement = erp.prepareStatement(sql);
        ResultSet rst = preparedStatement.executeQuery();
        ArrayList<MItem> list = new ArrayList<>();
        while (rst.next()) {
            MItem item = new MItem();
            item.setBarcode(rst.getString(1));
            item.setName(rst.getString(2));
            item.setPrintDescription(rst.getString(3));
            item.setSize(rst.getString(4));
            item.setUnitPrice(rst.getBigDecimal(5));
            item.setDepartment(rst.getString(6));
            item.setCategory(rst.getString(7));
            item.setStyle(rst.getString(8));
            item.setStyleRepeat(rst.getString(9));
            list.add(item);
        }
        LOGGER.debug(list.size() + " route data found");
        return list;
    }

    public static Integer itemUpdate(String barcode, Connection erp) throws SQLException {
        String sql = "update style_barcode set is_sync=1 where intBarCodeId=?";
        PreparedStatement preparedStatement = erp.prepareStatement(sql);
        preparedStatement.setString(1, barcode);
        return preparedStatement.executeUpdate();

    }
}
