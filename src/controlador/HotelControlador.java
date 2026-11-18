/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import hilos.ActualizadorIndicadores;
import hilos.SupervisorReservas;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import modelo.GestorHotel;
import modelo.IndicadoresHotel;
import vista.FrmHotel;
/**
 *
 * @author matam
 */
public class HotelControlador {

    private final GestorHotel gestor = GestorHotel.getInstancia();
    private final FrmHotel ventana;

    private final AtomicBoolean procesosActivos =new AtomicBoolean(false);

    public HotelControlador(FrmHotel ventana) {
        this.ventana = ventana;
    }

    public void cargarSesion() {

        if (gestor.getUsuarioActivo() != null) {

            int cantidad = gestor.revisarVencimientosReales();

            ventana.mostrarMensaje( "Revisión realizada: " + cantidad+ " reserva(s) vencida(s).");

        } else {

            ventana.mostrarMensaje( "Sin sesión activa. Inicia sesión desde FrmLogin.");
        }

        actualizarIndicadores();
    }

    public void actualizarIndicadores() {

        ventana.mostrarIndicadores(
                gestor.calcularIndicadores());
    }

    public void iniciarProcesos() {

        if (!procesosActivos.compareAndSet(false, true)) {

            JOptionPane.showMessageDialog( ventana, "Los procesos ya están en ejecución.");

            return;
        }

        ventana.setProcesosEnCurso(true);

        ventana.mostrarMensaje( "Ejecutando supervisión e indicadores...");

        AtomicInteger pendientes = new AtomicInteger(2);

        Runnable alTerminar = () -> {

            if (pendientes.decrementAndGet() == 0) {

                IndicadoresHotel resultadoFinal = gestor.calcularIndicadores();

                SwingUtilities.invokeLater(() -> {

                    ventana.mostrarIndicadores(resultadoFinal);

                    ventana.setProcesosEnCurso(false);

                    ventana.mostrarMensaje("Procesos finalizados. Indicadores actualizados.");

                    procesosActivos.set(false);
                });
            }
        };

        Thread hilo1 = new Thread( new SupervisorReservas(gestor, alTerminar),"SupervisorReservas");

        Thread hilo2 = new Thread(
                new ActualizadorIndicadores(gestor,ventana::mostrarIndicadores,alTerminar),"ActualizadorIndicadores");

        hilo1.start();
        hilo2.start();
    }

    public void cambiarUsuario() {

        if (procesosActivos.get()) {

            JOptionPane.showMessageDialog(ventana,"Espera a que finalicen los procesos.");

            return;
        }

        int opcion = JOptionPane.showConfirmDialog(ventana,
                "¿Deseas cerrar la sesión actual?","Cambiar usuario",JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {

            gestor.cerrarSesion();
            ventana.volverALogin();
        }
    }
}