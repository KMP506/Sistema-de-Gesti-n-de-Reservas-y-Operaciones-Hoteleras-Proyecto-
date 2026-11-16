/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.time.LocalDate;
import modelo.GestorHotel;
/**
 *
 * @author matam
 */
public class DisponibilidadControlador {

    private final GestorHotel gestor = GestorHotel.getInstancia();

    public GestorHotel.ConsultaDisponibilidad consultar(LocalDate fecha) {
        return gestor.consultarDisponibilidad(fecha);
    }
}