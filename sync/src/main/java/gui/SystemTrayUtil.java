/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author 'Kasun Chamara'
 */
public class SystemTrayUtil {

    private static SystemTrayUtil instance;

    public static SystemTrayUtil getInstance() {
        if (instance == null) {
            instance = new SystemTrayUtil();
        }
        return instance;
    }

    public static void initSystemTray(FingerprintSyncGUI fingerprintSyncGUI) {
        final PopupMenu popup = new PopupMenu();
        TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().createImage("images/2.png"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");

        MenuItem startItem = new MenuItem("Start");
        MenuItem stopItem = new MenuItem("Stop");
        MenuItem loggerItem = new MenuItem("Logger");
        MenuItem exitItem = new MenuItem("Exit");

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(startItem);
        popup.add(stopItem);
        popup.addSeparator();
        popup.add(loggerItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "GFlock Item SYNC SOFTWARE \n"
                        + "software by supervision technology (PVT) ltd");
            }
        });
        startItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    fingerprintSyncGUI.start();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(FingerprintSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                trayIcon.displayMessage("GFlock Item SYNC SOFTWARE", "Finger print server start success.. ", TrayIcon.MessageType.INFO);
            }
        });
        stopItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    fingerprintSyncGUI.stop();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(FingerprintSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                trayIcon.displayMessage("GFlock Item SYNC SOFTWARE", "Server stop success.. ", TrayIcon.MessageType.WARNING);

            }
        });
        loggerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!fingerprintSyncGUI.isVisible()) {
                    fingerprintSyncGUI.setVisible(true);
                    trayIcon.displayMessage("GFlock Item SYNC SOFTWARE", "logger viewed.. ", TrayIcon.MessageType.INFO);
                } else {
                    trayIcon.displayMessage("GFlock Item SYNC SOFTWARE", "logger already exists.. ", TrayIcon.MessageType.WARNING);
                }

            }
        });
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fingerprintSyncGUI.isVisible()) {
                    fingerprintSyncGUI.setVisible(false);
                    trayIcon.displayMessage("GFlock Item SYNC SOFTWARE", "logger closed.. ", TrayIcon.MessageType.INFO);
                }else{
                    trayIcon.displayMessage("GFlock Item SYNC SOFTWARE", "logger already closed.. ", TrayIcon.MessageType.INFO);
                }

            }
        });

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    //Obtain the image URL
    protected static Image createImage(String path) {
        URL imageURL = SystemTrayUtil.class.getResource(path);
        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL)).getImage();
        }
    }
}
