/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
 
   // Atributos
    private final PanelReservas vista;
    private final GestorHotel gestor;

    // Constructor
    public ReservaControlador(PanelReservas vista) {

        this.vista = vista;
        this.gestor = GestorHotel.getInstancia();

        vista.getBtnRegistrar().addActionListener( e -> registrarReserva());
        vista.getBtnLimpiar().addActionListener( e -> vista.limpiarCampos());
        vista.getCmbHabitacion().addActionListener( e -> calcularImporte());
        vista.getTxtFechaEntrada().addActionListener( e -> calcularImporte());
        vista.getTxtFechaSalida().addActionListener(e -> calcularImporte());
        cargarDatos();
        actualizarTabla();
    }

    // Cargar huéspedes y habitaciones
    public void cargarDatos() {

        vista.getCmbHuesped().removeAllItems();

        for (String identificacion :
                gestor.obtenerIdentificacionesHuespedes()) {

            Huesped huesped =
                    gestor.buscarHuesped(identificacion);

            if (huesped != null) {
                vista.getCmbHuesped().addItem(huesped);
            }
        }

        vista.getCmbHabitacion().removeAllItems();

        for (Habitacion habitacion :
                gestor.obtenerHabitaciones()) {

            vista.getCmbHabitacion().addItem(habitacion);
        }

        vista.mostrarRecepcionista(gestor.getUsuarioActivo());
        vista.mostrarEstado("Confirmada");
    }

    // Calcular noches
    private long calcularNoches(LocalDate entrada,
            LocalDate salida) {

        long noches = 0;

        LocalDate fecha = entrada;

        while (fecha.isBefore(salida)) {
            noches++;
            fecha = fecha.plusDays(1);
        }

        return noches;
    }

    // Calcular importe automáticamente
    private void calcularImporte() {

        vista.mostrarNoches(0);
        vista.mostrarImporte(0);

        Habitacion habitacion = (Habitacion)
                vista.getCmbHabitacion().getSelectedItem();

        if (habitacion == null) {
            return;
        }

        String entrada = vista.getTxtFechaEntrada()
                .getText().trim();

        String salida = vista.getTxtFechaSalida()
                .getText().trim();

        if (entrada.isEmpty() || salida.isEmpty()) {
            return;
        }

        try {

            LocalDate fechaEntrada = LocalDate.parse(entrada);
            LocalDate fechaSalida = LocalDate.parse(salida);

            if (fechaEntrada.isBefore(LocalDate.now())
                    || !fechaSalida.isAfter(fechaEntrada)) {
                return;
            }

            long noches = calcularNoches(
                    fechaEntrada, fechaSalida);

            long importe = noches * habitacion.getTarifa();

            vista.mostrarNoches(noches);
            vista.mostrarImporte(importe);

        } catch (DateTimeParseException ex) {

            vista.mostrarNoches(0);
            vista.mostrarImporte(0);
        }
    }

    // Registrar reserva
    private void registrarReserva() {

        try {
            Huesped huesped = (Huesped)
                    vista.getCmbHuesped().getSelectedItem();
            Habitacion habitacion = (Habitacion)
                    vista.getCmbHabitacion().getSelectedItem();
            if (huesped == null || habitacion == null) {
                throw new IllegalArgumentException("Debe seleccionar huésped y habitación.");
            }
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
            calcularImporte();
            vista.mostrarCodigo(codigo);
            vista.mostrarEstado("Confirmada");
            actualizarTabla();
            vista.mostrarMensaje("Reserva registrada correctamente. Código: "+ codigo);
        } catch (NumberFormatException ex) {
            vista.mostrarError("La cantidad de personas debe ser un número entero.");
        } catch (DateTimeParseException ex) {
            vista.mostrarError( "Las fechas deben tener el formato AAAA-MM-DD.");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }

    public void actualizarTabla() {
        vista.actualizarTabla( gestor.obtenerResumenReservas());
    }
}
