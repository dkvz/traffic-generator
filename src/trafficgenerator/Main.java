/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trafficgenerator;

/**
 *
 * @author Alain
 */
public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame frm = new MainFrame();
                frm.setLocationRelativeTo(null);
                frm.setVisible(true);
            }
        });
    }

}
