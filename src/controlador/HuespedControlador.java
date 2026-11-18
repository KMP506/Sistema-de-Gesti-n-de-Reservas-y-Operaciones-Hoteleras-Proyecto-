/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import modelo.Huesped;
import modelo.GestorHotel;
import vista.PanelHuespedes;
/**
 *
 * @author kevin
 */
public class HuespedControlador {
    private final PanelHuespedes vista;
    private final GestorHotel gestor;
    
    public HuespedControlador(PanelHuespedes vista) {

        this.vista = vista;
        this.gestor = GestorHotel.getInstancia();

        vista.getBtnRegistrar().addActionListener(
                e -> registrarHuesped());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());

        actualizarTabla();
    }
    
    private void registrarHuesped() {

        String identificacion = vista.getTxtIdentificacion()
                .getText().trim();

        String nombre = vista.getTxtNombre()
                .getText().trim();

        String telefono = vista.getTxtTelefono()
                .getText().trim();

        String correo = vista.getTxtCorreo()
                .getText().trim();

        try {

            if (identificacion.isEmpty()
                    || nombre.isEmpty()
                    || telefono.isEmpty()
                    || correo.isEmpty()) {

                throw new IllegalArgumentException(
                        "Todos los campos son obligatorios.");
            }

            int posicionArroba = correo.indexOf("@");

            if (posicionArroba <= 0
                    || posicionArroba != correo.lastIndexOf("@")
                    || correo.indexOf(".", posicionArroba + 2)
                    <= posicionArroba + 1
                    || correo.endsWith(".")) {

                throw new IllegalArgumentException(
                        "El correo electrónico no es válido.");
            }

            Huesped huesped = new Huesped(
                    identificacion, nombre, telefono, correo);

            gestor.registrarHuesped(identificacion, huesped);

            vista.mostrarMensaje(
                    "Huésped registrado correctamente.");

            actualizarTabla();
            vista.limpiarCampos();

        } catch (IllegalArgumentException ex) {

            vista.mostrarError(ex.getMessage());
        }
    }

    private void actualizarTabla() {

        java.util.List<String> identificaciones =
                gestor.obtenerIdentificacionesHuespedes();

        java.util.List<Huesped> huespedes =
                new java.util.ArrayList<>();

        for (String identificacion : identificaciones) {

            Huesped huesped = gestor.buscarHuesped(identificacion);

            if (huesped != null) {
                huespedes.add(huesped);
            }
        }

        vista.actualizarTabla(huespedes);
    }
    
}
