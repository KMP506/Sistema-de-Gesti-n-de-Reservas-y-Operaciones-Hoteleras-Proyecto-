/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package recursividad;
import java.time.LocalDate;
import java.util.List;
import modelo.EstadoReserva;
import modelo.GestorHotel;
import modelo.Habitacion;
import modelo.Reserva;
/**
 *
 * @author matam
 */
public class CalculosRecursivos {

    public long calcularIngresosEsperados(List<Reserva> reservas) {

        if (reservas == null) {
            throw new IllegalArgumentException(
                    "La lista no puede ser nula.");
        }

        return sumarIngresos(reservas, 0);
    }

    private long sumarIngresos(List<Reserva> reservas, int indice) {

        if (indice >= reservas.size()) {
            return 0L;
        }

        Reserva reserva = reservas.get(indice);

        long importe = reserva.getEstado() == EstadoReserva.CONFIRMADA
                ? Math.multiplyExact(
                        reserva.getCantidadNoches(),
                        reserva.getHabitacion().getTarifa())
                : 0L;

        return Math.addExact(
                importe, sumarIngresos(reservas, indice + 1));
    }

    public int contarHabitacionesDisponibles(
            List<Habitacion> habitaciones,
            List<Reserva> reservas,
            LocalDate fecha) {

        if (habitaciones == null || reservas == null || fecha == null) {
            throw new IllegalArgumentException(
                    "Los datos de la consulta son obligatorios.");
        }

        return contarDisponibles(habitaciones, reservas, fecha, 0);
    }

    private int contarDisponibles(
            List<Habitacion> habitaciones,
            List<Reserva> reservas,
            LocalDate fecha,
            int indice) {

        if (indice >= habitaciones.size()) {
            return 0;
        }

        Habitacion habitacion = habitaciones.get(indice);

        int actual = estaLibre(habitacion, reservas, fecha) ? 1 : 0;

        return actual + contarDisponibles(
                habitaciones, reservas, fecha, indice + 1);
    }

    private boolean estaLibre(
            Habitacion habitacion,
            List<Reserva> reservas,
            LocalDate fecha) {

        for (Reserva reserva : reservas) {

            if (reserva.getHabitacion().getNumero()
                    .equals(habitacion.getNumero())
                    && GestorHotel.getInstancia()
                            .reservaOcupaFecha(reserva, fecha)) {

                return false;
            }
        }

        return true;
    }
}