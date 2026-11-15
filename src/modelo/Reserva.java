/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.time.LocalDate;
/**
 *
 * @author kevin
 */
public class Reserva {
    
    private String codigo;
    private Huesped huesped;
    private Habitacion habitacion;
    private int cantidadPersonas;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private LocalDate fechaSalidaReal;
    private long cantidadNoches;
    private long importeTotal;
    private EstadoReserva estado;
    private Recepcionista recepcionista;
    
    public Reserva(String codigo, Huesped huesped, Habitacion habitacion, int cantidadPersonas, LocalDate fechaEntrada, LocalDate fechaSalida, Recepcionista recepcionista){
        this.codigo = codigo;
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.cantidadPersonas = cantidadPersonas;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.recepcionista = recepcionista;
        this.fechaSalidaReal = null;
        this.fechaSalidaReal = null;
        this.estado = EstadoReserva.CONFIRMADA;
        this.cantidadNoches = 0;
        calcularNoches();
        this.importeTotal = cantidadNoches * habitacion.getTarifa();
    }
    public void calcularNoches(){
        cantidadNoches = 0;
        LocalDate fecha = fechaEntrada;
        
        while (fecha.isBefore(fechaSalida)){
            cantidadNoches++;
            fecha = fecha.plusDays(1);
        }
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public Huesped getHuesped(){
        return huesped;
    }
    
    public Habitacion getHabitacion(){
        return habitacion;
    }
    
    public int getCantidadPersonas(){
        return  cantidadPersonas;
    }
    
    public LocalDate getFechaSalidaReal(){
        return fechaSalidaReal;
    }
    
    public long getCantidadNoches(){
        return cantidadNoches;
    }
    
    public EstadoReserva getEstado(){
        return estado;
    }
    
    public Recepcionista getRecepcionista(){
        return recepcionista;
    }
    
    public void realiarCheckIn(){
        if (estado == EstadoReserva.CONFIRMADA && fechaEntrada.equals(LocalDate.now())){
            estado = EstadoReserva.HOSPEDADO;
        }else{
            throw new IllegalStateException("No se puede realizar el check-in");
        }
    }
    
    public void realizarCheckOut(){
        
        if (estado == EstadoReserva.HOSPEDADO){
            
            
            fechaSalidaReal = LocalDate.now();
            estado = EstadoReserva.FINALIZADA;
        }else{
            throw new IllegalStateException("No sen puede realizar el check-out");
        }
    }
    
    public void marcarVencida(){
        
        if (estado == EstadoReserva.CONFIRMADA && fechaEntrada.isBefore(LocalDate.now())){
            estado = EstadoReserva.VENCIDA;
        }else{
            throw new IllegalStateException("La reserva no cumple las condiciones para vencer.");
        }
    }
}
