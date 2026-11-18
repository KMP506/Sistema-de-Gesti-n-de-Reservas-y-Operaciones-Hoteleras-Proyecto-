/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;
import modelo.Habitacion;
import modelo.HabitacionDoble;
import modelo.HabitacionFamiliar;
import modelo.HabitacionIndividual;
        
/**
 *
 * @author kevin
 */
public class HabitacionFactory {
    public static Habitacion crearHabitacion(String tipo, String numero){
        switch(tipo){
            case "Individual": return new HabitacionIndividual(numero);
            case "Doble": return new HabitacionDoble(numero);
            case "Familiar": return new HabitacionFamiliar(numero);
            default: throw new IllegalArgumentException("Tipo de habitacion no valida..");
        }
    }
}
