/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kevin
 */
public class HabitacionIndividual extends Habitacion {
    
    public HabitacionIndividual(String numero) {
        super(numero);
    }
    
    @Override
    public int getCapacidad(){
        return 1;
    }
    
    @Override
    public long getTarifa(){
        return 25000;
    }
}
