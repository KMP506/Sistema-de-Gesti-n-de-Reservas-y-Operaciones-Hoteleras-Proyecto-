/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hilos;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;
import modelo.GestorHotel;
import modelo.IndicadoresHotel;
/**
 *
 * @author matam
 */
public class ActualizadorIndicadores implements Runnable {

    private final GestorHotel gestor;

    private final Consumer<IndicadoresHotel> actualizarVista;

    private final Runnable alTerminar;

    public ActualizadorIndicadores(
            GestorHotel gestor,
            Consumer<IndicadoresHotel> actualizarVista,
            Runnable alTerminar) {

        this.gestor = gestor;
        this.actualizarVista = actualizarVista;
        this.alTerminar = alTerminar;
    }

    @Override
    public void run() {

        log("Inicia ejecución.");

        try {

            for (int ciclo = 1; ciclo <= 5; ciclo++) {

                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                IndicadoresHotel indicadores =
                        gestor.calcularIndicadores();

                log("Ciclo " + ciclo
                        + "/5: indicadores actualizados.");

                if (actualizarVista != null) {

                    SwingUtilities.invokeLater(
                            () -> actualizarVista.accept(indicadores));
                }

                if (ciclo < 5) {
                    Thread.sleep(1000);
                }
            }

        } catch (InterruptedException ex) {

            Thread.currentThread().interrupt();

            log("Ejecución interrumpida.");

        } catch (RuntimeException ex) {

            log("Error: " + ex.getMessage());

        } finally {

            log("Finaliza ejecución.");

            if (alTerminar != null) {
                alTerminar.run();
            }
        }
    }

    private void log(String mensaje) {

        System.out.printf(
                "[%s] [%s] %s%n",
                LocalTime.now().format(
                        DateTimeFormatter.ofPattern("HH:mm:ss")),
                Thread.currentThread().getName(),
                mensaje);
    }
}