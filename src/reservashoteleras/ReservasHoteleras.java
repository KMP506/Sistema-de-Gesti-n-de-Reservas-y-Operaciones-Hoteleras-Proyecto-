/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package reservashoteleras;
import javax.swing.SwingUtilities;
import vista.FrmLogin;
/**
 *
 * @author kevin
 */
public class ReservasHoteleras {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {

            FrmLogin login = new FrmLogin();
            login.setLocationRelativeTo(null);
            login.setVisible(true);

        });
    }
  
}
