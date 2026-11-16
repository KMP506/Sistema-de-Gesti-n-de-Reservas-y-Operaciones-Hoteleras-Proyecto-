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
import vista.PanelInicio;
/**
 *
 * @author matam
 */
public class HotelControlador {

    private final GestorHotel gestor = GestorHotel.getInstancia();

    private final FrmHotel ventana;
    private final PanelInicio inicio;

    private final AtomicBoolean procesosActivos =
            new AtomicBoolean(false);

    public HotelControlador(FrmHotel ventana, PanelInicio inicio) {
        this.ventana = ventana;
        this.inicio = inicio;
    }

    public void cargarSesion() {

        if (gestor.getUsuarioActivo() != null) {

            int cambiadas = gestor.revisarVencimientosReales();

            System.out.println(
                    "[Inicio de sesión] Revisión de vencimientos realizada. Cambios: "
                    + cambiadas);

            inicio.mostrarMensaje(
                    "Revisión inicial completada: "
                    + cambiadas + " reserva(s) vencida(s).");

        } else {

            inicio.mostrarMensaje(
                    "Sin sesión activa. El inicio de sesión lo integra Persona 1.");
        }

        inicio.mostrarIndicadores(gestor.calcularIndicadores());
    }

    public void actualizarIndicadores() {
        inicio.mostrarIndicadores(gestor.calcularIndicadores());
    }

    public void iniciarProcesos() {

        if (!procesosActivos.compareAndSet(false, true)) {

            JOptionPane.showMessageDialog(
                    ventana, "Ya hay una supervisión en ejecución.");

            return;
        }

        inicio.setProcesosEnCurso(true);

        inicio.mostrarMensaje(
                "Dos hilos iniciados: cinco ciclos simultáneos.");

        AtomicInteger pendientes = new AtomicInteger(2);

        Runnable terminar = () -> {

            if (pendientes.decrementAndGet() == 0) {

                IndicadoresHotel finales =
                        gestor.calcularIndicadores();

                SwingUtilities.invokeLater(() -> {

                    inicio.mostrarIndicadores(finales);

                    ventana.refrescarPanelesOperativos();

                    inicio.setProcesosEnCurso(false);

                    inicio.mostrarMensaje(
                            "Supervisión finalizada. Indicadores consolidados.");

                    procesosActivos.set(false);
                });
            }
        };

        Thread supervisor = new Thread(
                new SupervisorReservas(gestor, terminar),
                "SupervisorReservas");

        Thread actualizador = new Thread(
                new ActualizadorIndicadores(
                        gestor, inicio::mostrarIndicadores, terminar),
                "ActualizadorIndicadores");

        supervisor.start();

        actualizador.start();
    }

    public void cambiarUsuario() {

        if (procesosActivos.get()) {

            JOptionPane.showMessageDialog(
                    ventana,
                    "Termine la supervisión antes de cambiar de usuario.");

            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventana,
                "¿Deseas cerrar la sesión actual y cambiar de recepcionista?",
                "Cambiar usuario",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {

            gestor.cerrarSesion();

            ventana.volverALogin();
        }
    }
}