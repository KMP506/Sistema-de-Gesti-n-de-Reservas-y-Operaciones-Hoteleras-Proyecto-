/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import factory.HabitacionFactory;
import modelo.Habitacion;
import modelo.GestorHotel;
import vista.PanelHabitaciones;
/**
 *
 * @author kevin
 */
public class HabitacionesControlador {
  
    private final PanelHabitaciones vista;
    private final GestorHotel gestor;

    public HabitacionesControlador(PanelHabitaciones vista) {

        this.vista = vista;
        this.gestor = GestorHotel.getInstancia();
        vista.getBtnRegistrar().addActionListener( e -> registrarHabitacion());
        vista.getBtnLimpiar().addActionListener(e -> vista.limpiarCampos());
        vista.getCmbTipo().addActionListener( e -> actualizarTipo());
        actualizarTabla();
        actualizarTipo();
    }

    private void registrarHabitacion() {

        String numero = vista.getTxtNumero().getText().trim();
        String tipo = (String) vista.getCmbTipo().getSelectedItem();
        try {
            if (numero.isEmpty()) {
                throw new IllegalArgumentException("El número de habitación es obligatorio.");
            }
            if (tipo == null) {
                throw new IllegalArgumentException("Debe seleccionar un tipo de habitación.");
            }
            Habitacion habitacion =HabitacionFactory.crearHabitacion(tipo, numero);
            gestor.registrarHabitacion(habitacion);
            vista.mostrarMensaje( "Habitación registrada correctamente.");
            actualizarTabla();
            vista.limpiarCampos();
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void actualizarTipo() {

        String tipo = (String) vista.getCmbTipo().getSelectedItem();
        if (tipo == null) {
            return;
        }
        try {

            Habitacion habitacion =HabitacionFactory.crearHabitacion(tipo, "");
            vista.mostrarCapacidad(habitacion.getCapacidad());
            vista.mostrarTarifa(habitacion.getTarifa());
        } catch (IllegalArgumentException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    private void actualizarTabla() {
        vista.actualizarTabla(gestor.obtenerHabitaciones());
    }
}
