/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author kevin
 */
public class Huesped {
    private String identificacion;
    private String nombreCompleto;
    private String telefono;
    private String correoElectronico;
    
     public Huesped(String identificacion, String nombreCompleto,
            String telefono, String correoElectronico) {

        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
    }
     
     public String getIdentificacion(){
         return identificacion;
     }
     
     public String getNombreCompleto(){
         return nombreCompleto;
     }
     
     public String getTelefono(){
         return telefono;
     }
     
     public String getCorreoElectronico(){
         return correoElectronico;
     }
     
     @Override
     public String toString(){
         return nombreCompleto;
     }
     
}
