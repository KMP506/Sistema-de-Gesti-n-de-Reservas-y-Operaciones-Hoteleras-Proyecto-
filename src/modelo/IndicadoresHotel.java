/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author matam
 */

public final class IndicadoresHotel {

    private final int totalHabitaciones;
    private final int habitacionesOcupadas;
    private final int habitacionesNoOcupadas;
    private final int reservasConfirmadas;
    private final int reservasVencidas;
    private final long ingresosEsperados;

    public IndicadoresHotel(int totalHabitaciones, int habitacionesOcupadas,
            int habitacionesNoOcupadas, int reservasConfirmadas,
            int reservasVencidas, long ingresosEsperados) {

        this.totalHabitaciones = totalHabitaciones;
        this.habitacionesOcupadas = habitacionesOcupadas;
        this.habitacionesNoOcupadas = habitacionesNoOcupadas;
        this.reservasConfirmadas = reservasConfirmadas;
        this.reservasVencidas = reservasVencidas;
        this.ingresosEsperados = ingresosEsperados;
    }

    public int getTotalHabitaciones() {
        return totalHabitaciones;
    }

    public int getHabitacionesOcupadas() {
        return habitacionesOcupadas;
    }

    public int getHabitacionesNoOcupadas() {
        return habitacionesNoOcupadas;
    }

    public int getReservasConfirmadas() {
        return reservasConfirmadas;
    }

    public int getReservasVencidas() {
        return reservasVencidas;
    }

    public long getIngresosEsperados() {
        return ingresosEsperados;
    }
}