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
       validarDatos(codigo, huesped, habitacion,
                cantidadPersonas, fechaEntrada,
                fechaSalida, recepcionista);
        this.codigo = codigo;
        this.huesped = huesped;
        this.habitacion = habitacion;
        this.cantidadPersonas = cantidadPersonas;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.recepcionista = recepcionista;
        this.fechaSalidaReal = null;
        this.estado = EstadoReserva.CONFIRMADA;
        this.cantidadNoches = 0;
        calcularNoches();
        this.importeTotal = cantidadNoches * habitacion.getTarifa();
    }
    
     private void validarDatos(String codigo, Huesped huesped,Habitacion habitacion, int cantidadPersonas,LocalDate fechaEntrada, LocalDate fechaSalida,Recepcionista recepcionista) {
         
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El código es obligatorio.");
        }
        if (huesped == null || habitacion == null
                || recepcionista == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar huésped, habitación y recepcionista.");
        }
        if (fechaEntrada == null || fechaSalida == null) {
            throw new IllegalArgumentException(
                    "Las fechas son obligatorias.");
        }
        if (fechaEntrada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de entrada no puede ser anterior a hoy.");
        }
        if (!fechaSalida.isAfter(fechaEntrada)) {
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la entrada.");
        }
        if (cantidadPersonas <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de personas debe ser mayor que cero.");
        }
        if (cantidadPersonas > habitacion.getCapacidad()) {
            throw new IllegalArgumentException(
                    "La cantidad de personas supera la capacidad de la habitación.");
        }
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
    
    public long getImporteTotal(){
        return importeTotal;
    }
    
    public EstadoReserva getEstado(){
        return estado;
    }
    
    public Recepcionista getRecepcionista(){
        return recepcionista;
    }
    
    public void realizarCheckIn(){
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
