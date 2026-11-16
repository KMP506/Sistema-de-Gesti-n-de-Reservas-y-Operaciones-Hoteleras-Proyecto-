/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.time.LocalDate;
import modelo.GestorHotel;
import modelo.Habitacion;
import modelo.Huesped;
import modelo.Recepcionista;
import vista.PanelReservas;
/**
 *
 * @author kevin
 */
public class ReservaControlador {
 
    private final PanelReservas vista;
    private final GestorHotel gestor;

    public ReservaControlador(PanelReservas vista) {

        this.vista = vista;
        this.gestor = GestorHotel.getInstancia();
        vista.getBtnRegistrar().addActionListener(e -> registrarReserva());
        cargarDatos();
        actualizarTabla();
    }

    public void cargarDatos() {
        vista.getCmbHuesped().removeAllItems();
        for (String identificacion : gestor.obtenerIdentificacionesHuespedes()) {
            Huesped huesped = gestor.buscarHuesped(identificacion);
            if (huesped != null) {
                vista.getCmbHuesped().addItem(huesped);
            }
        }
        vista.getCmbHabitacion().removeAllItems();
        for (Habitacion habitacion : gestor.obtenerHabitaciones()) {
            vista.getCmbHabitacion().addItem(habitacion);
        }
    }

    private void registrarReserva() {
        try {
            Huesped huesped = (Huesped) vista.getCmbHuesped().getSelectedItem();
            Habitacion habitacion = (Habitacion) vista.getCmbHabitacion().getSelectedItem();
            String textoPersonas = vista.getTxtCantidadPersonas().getText().trim();
            if (textoPersonas.isEmpty()) {
                throw new IllegalArgumentException("Debe indicar la cantidad de personas.");
            }
            int cantidadPersonas = Integer.parseInt(textoPersonas);
            LocalDate fechaEntrada = LocalDate.parse(vista.getTxtFechaEntrada().getText().trim());
            LocalDate fechaSalida = LocalDate.parse(vista.getTxtFechaSalida().getText().trim());
            String usuario = gestor.getUsuarioActivo();
            if (usuario == null) {
                throw new IllegalStateException("No existe una sesión activa.");
            }
            Recepcionista recepcionista = new Recepcionista(usuario, "");
            String codigo = gestor.registrarReserva(huesped, habitacion, cantidadPersonas,fechaEntrada, fechaSalida, recepcionista);
            vista.mostrarMensaje("Reserva registrada correctamente. Código: " + codigo);
            actualizarTabla();
            vista.limpiarCampos();
        } catch (NumberFormatException ex) {
            vista.mostrarError("La cantidad de personas debe ser un número entero.");
        } catch (java.time.format.DateTimeParseException ex) {
            vista.mostrarError("Las fechas deben tener el formato AAAA-MM-DD.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    public void actualizarTabla() {
        vista.actualizarTabla(gestor.obtenerResumenReservas());
    }
}
