
import gui.FingerprintSyncGUI;
import gui.SystemTrayUtil;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author 'Kasun Chamara'
 */
public class FingerprintSync {

    private static FingerprintSyncGUI fingerprintSyncGUI = null;

    public static void main(String[] args) throws SQLException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            fingerprintSyncGUI = new FingerprintSyncGUI();
            fingerprintSyncGUI.start();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | SQLException ex) {
            java.util.logging.Logger.getLogger(FingerprintSyncGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        SystemTrayUtil.initSystemTray(fingerprintSyncGUI);
    }
}
