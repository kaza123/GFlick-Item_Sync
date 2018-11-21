/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import controller.ErpController;
import controller.PosController;
import db_connections.DataSourceWrapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import model.MItem;
import org.apache.log4j.Logger;

/**
 *
 * @author kasun
 */
public class TransactionHandler {

    private final DataSourceWrapper erpDataSourceWrapper;
    private final DataSourceWrapper posDataSourceWrapper;
    private static final Logger LOGGER = Logger.getLogger(TransactionHandler.class);

    public TransactionHandler() throws SQLException {
        this.erpDataSourceWrapper = new ErpController().getConnection();
        this.posDataSourceWrapper = new PosController().getConnection();
    }

    public Integer save() throws SQLException {
        Integer count = 0;
        System.out.println("Saving..");
        try (Connection erp1 = erpDataSourceWrapper.getConnection()) {

            ArrayList<MItem> itemList = ErpController.getItems(erp1);
            if (!itemList.isEmpty()) {

                System.out.println("item size " + itemList.size());
                for (MItem mItem : itemList) {
                    try (Connection pos = posDataSourceWrapper.getConnection()) {
                        try (Connection erp = erpDataSourceWrapper.getConnection()) {
                            erp.setAutoCommit(false);
                            pos.setAutoCommit(false);

                            //save
                            Integer saveItem = PosController.getInstance().saveItem(mItem, pos);
                            if (saveItem > 0) {
//                        update
                                Integer update = ErpController.itemUpdate(mItem.getBarcode(), erp);
                                if (update > 0) {
                                    System.out.println("Item " + mItem.getBarcode() + " " + mItem.getName() + "Save Success !");
                                    count++;
                                    pos.commit();
                                    erp.commit();
                                    pos.setAutoCommit(true);
                                    erp.setAutoCommit(true);
                                    pos.close();
                                    erp.close();
                                    LOGGER.info(count + " ITRM(S) SYNCHRONIZATION SUCCESS !");
                                    
                                } else {
                                    System.out.println("Item " + mItem.getBarcode() + " " + mItem.getName() + "Update Fail !");
                                    pos.rollback();
                                    pos.setAutoCommit(true);
                                    erp.rollback();
                                    erp.setAutoCommit(true);
                                    pos.close();
                                    erp.close();
                                    LOGGER.error("SYNCHRONIZATION FAILED !");
                                    return -1;
                                }
                            } else {
                                throw new RuntimeException(mItem.getBarcode() + " Item Save Fail !");
                            }
                        }
                    }
                }
            } else {
                System.out.println("Empty Sychronization Item");
            }
        }
        return 1;
    }
}
