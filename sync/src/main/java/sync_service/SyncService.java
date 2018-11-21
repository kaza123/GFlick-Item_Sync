/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sync_service;

import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import transaction.TransactionHandler;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SyncService {

    private ScheduledExecutorService scheduledExecutorService;
    private static SyncService instance;
    private static final Logger LOGGER = Logger.getLogger(SyncService.class);

    public static SyncService getInstance() throws SQLException {
        if (instance == null) {
            instance = new SyncService();
        }

        return instance;
    }

    private SyncService() throws SQLException {
        
    }

    public void start() throws ConcurrentModificationException {
        if (scheduledExecutorService != null) {
            throw new ConcurrentModificationException("Sync process already running");
        }

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(getSyncRunnable(), 0, 1, TimeUnit.HOURS);
        LOGGER.info("Synchronization service started successfully");
    }

    public void stop() throws ConcurrentModificationException {
        if (scheduledExecutorService == null) {
            throw new ConcurrentModificationException("Sync process not started");
        }

        scheduledExecutorService.shutdown();
        scheduledExecutorService = null;
        LOGGER.info("Synchronization service stopped successfully");
    }

    private Runnable getSyncRunnable() {
        Runnable syncRunnable = () -> {
            try {
                LOGGER.info("START SYNCHRONIZATION !");

                saveItem();

            } catch (SQLException ex) {
                LOGGER.fatal("Synchronization failed", ex);
            }
        };
        return syncRunnable;
    }

    public void saveItem() throws SQLException {
        new TransactionHandler().save();
    }
}
