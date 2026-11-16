/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.time.LocalDate;
import java.util.List;
import modelo.GestorHotel;
/**
 *
 * @author matam
 */
public class SimulacionControlador {

    private final GestorHotel gestor = GestorHotel.getInstancia();

    public List<GestorHotel.ResumenReserva> simular(
            LocalDate fechaReferencia) {

        return gestor.simularVencimientos(fechaReferencia);
    }
}