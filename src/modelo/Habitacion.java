/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kevin
 */
public abstract class Habitacion {
    private String numero;
    
    public Habitacion(String numero){
       this.numero = numero; 
    }
    
    public String getNumero(){
        return this.numero;
    }
    
    public abstract int getCapacidad();
    public abstract long getTarifa();
}
