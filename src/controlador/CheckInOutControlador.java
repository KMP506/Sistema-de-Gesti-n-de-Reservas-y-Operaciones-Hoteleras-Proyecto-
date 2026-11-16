/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.util.List;
import modelo.GestorHotel;
/**
 *
 * @author matam
 */
public class CheckInOutControlador {

    private final GestorHotel gestor = GestorHotel.getInstancia();

    public void hacerCheckIn(String codigo) {
        gestor.realizarCheckIn(codigo);
    }

    public void hacerCheckOut(String codigo) {
        gestor.realizarCheckOut(codigo);
    }

    public List<GestorHotel.ResumenReserva> consultarReservas() {
        return gestor.obtenerResumenReservas();
    }
}