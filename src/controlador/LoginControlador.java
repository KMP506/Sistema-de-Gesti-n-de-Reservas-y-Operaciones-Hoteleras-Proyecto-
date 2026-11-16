/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.GestorHotel;
import vista.FrmLogin;
import vista.FrmHotel;
/**
 *
 * @author kevin
 */
public class LoginControlador {

    private final FrmLogin vista;
    private final GestorHotel gestor;

    public LoginControlador(FrmLogin vista) {
        this.vista = vista;
        this.gestor = GestorHotel.getInstancia();
        vista.getBtnIniciarSesion().addActionListener( e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuario = vista.getTxtUsuario().getText().trim();
        String contrasena = new String(vista.getTxtContrasena().getPassword());
        try {
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar el usuario y la contraseña.");
            }
            boolean usuarioValido =(usuario.equals("recepcion1")&& contrasena.equals("Hotel123"))||(usuario.equals("recepcion2")&& contrasena.equals("Hotel456"));
            if (!usuarioValido) {
                throw new IllegalArgumentException("Usuario o contraseña incorrectos.");
            }

            gestor.establecerUsuarioActivo(usuario);
            FrmHotel hotel = new FrmHotel();
            hotel.setLocationRelativeTo(null);
            hotel.setVisible(true);
            vista.dispose();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}
