/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import recursividad.CalculosRecursivos;
/**
 *
 * @author matam
 */

public final class GestorHotel {

    private static final GestorHotel INSTANCIA = new GestorHotel();

    private final Map<String, Huesped> huespedes = new LinkedHashMap<>();
    private final Map<String, Habitacion> habitaciones = new LinkedHashMap<>();
    private final Map<String, RegistroReserva> reservas = new LinkedHashMap<>();

    private int siguienteCodigo = 1;
    private String usuarioActivo;

    private GestorHotel() {
    }

    public static GestorHotel getInstancia() {
        return INSTANCIA;
    }

    public synchronized void establecerUsuarioActivo(String usuario) {

        if (!"recepcion1".equals(usuario) && !"recepcion2".equals(usuario)) {
            throw new IllegalArgumentException("Recepcionista no válido.");
        }

        if (usuarioActivo != null) {
            throw new IllegalStateException("Ya hay una sesión activa.");
        }

        usuarioActivo = usuario;
    }

    public synchronized String getUsuarioActivo() {
        return usuarioActivo;
    }

    public synchronized void cerrarSesion() {
        usuarioActivo = null;
    }

    public synchronized void registrarHuesped(
            String identificacion, Huesped huesped) {

        if (identificacion == null || identificacion.isBlank()|| huesped == null) {
            throw new IllegalArgumentException("La identificación y el huésped son obligatorios.");
        }

        String clave = identificacion.trim();

        if (huespedes.containsKey(clave)) {
            throw new IllegalArgumentException( "Ya existe un huésped con esa identificación.");
        }

        huespedes.put(clave, huesped);
    }

    public synchronized Huesped buscarHuesped(String identificacion) {
        return identificacion == null? null : huespedes.get(identificacion.trim());
    }

    public synchronized List<String> obtenerIdentificacionesHuespedes() {
        return Collections.unmodifiableList( new ArrayList<>(huespedes.keySet()));
    }

    public synchronized void registrarHabitacion(Habitacion habitacion) {

        if (habitacion == null || habitacion.getNumero() == null|| habitacion.getNumero().isBlank()) {
            throw new IllegalArgumentException( "El número de habitación es obligatorio.");
        }

        String numero = habitacion.getNumero().trim();

        if (habitaciones.containsKey(numero)) {
            throw new IllegalArgumentException( "Esa habitación ya está registrada.");
        }

        habitaciones.put(numero, habitacion);
    }

    public synchronized Habitacion buscarHabitacion(String numero) {return numero == null
                ? null : habitaciones.get(numero.trim());
    }

    public synchronized List<Habitacion> obtenerHabitaciones() {
        return Collections.unmodifiableList(new ArrayList<>(habitaciones.values()));
    }

    public synchronized String registrarReserva(Huesped huesped, Habitacion habitacion,
            int cantidadPersonas,LocalDate entrada,LocalDate salida,Recepcionista recepcionista) {

        Objects.requireNonNull(huesped,"Debe seleccionar un huésped.");
        Objects.requireNonNull(habitacion,"Debe seleccionar una habitación.");
        Objects.requireNonNull(entrada,"La fecha de entrada es obligatoria.");
        Objects.requireNonNull(salida, "La fecha de salida es obligatoria.");
        Objects.requireNonNull(recepcionista, "Debe indicar el recepcionista.");

        if (!huespedes.containsValue(huesped)) {
            throw new IllegalArgumentException("El huésped no está registrado.");
        }

        Habitacion registrada = habitaciones.get(habitacion.getNumero());

        if (registrada != habitacion) {
            throw new IllegalArgumentException("La habitación no está registrada.");
        }

        if (entrada.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La entrada no puede ser anterior a hoy.");
        }

        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException( "La salida debe ser posterior a la entrada.");
        }

        if (cantidadPersonas <= 0|| cantidadPersonas > habitacion.getCapacidad()) {
            throw new IllegalArgumentException("La cantidad de personas excede la capacidad o no es válida.");
        }

        if (!estaDisponibleDurante(habitacion, entrada, salida)) {
            throw new IllegalArgumentException( "La habitación tiene una reserva superpuesta.");
        }

        String codigo = String.format("R%04d", siguienteCodigo++);

        Reserva reserva = new Reserva(codigo, huesped, habitacion,
                cantidadPersonas, entrada, salida, recepcionista);

        long noches = ChronoUnit.DAYS.between(entrada, salida);
        long importe = Math.multiplyExact(noches, habitacion.getTarifa());
        reservas.put(codigo, new RegistroReserva( reserva, entrada, salida, importe,
                obtenerIdHuesped(huesped), usuarioActivo));

        return codigo;
    }

    public synchronized boolean estaDisponibleDurante(
            Habitacion habitacion,
            LocalDate entrada,
            LocalDate salida) {

        if (habitacion == null || entrada == null || salida == null || !salida.isAfter(entrada)) {
            return false;
        }

        for (RegistroReserva existente : reservas.values()) {
            if (bloquea(existente.reserva.getEstado())
                    && existente.reserva.getHabitacion().getNumero().equals(habitacion.getNumero())
                    && entrada.isBefore(existente.salida) && salida.isAfter(existente.entrada)) {

                return false;
            }
        }

        return true;
    }

    public synchronized boolean reservaOcupaFecha(
            Reserva reserva, LocalDate fecha) {

        if (reserva == null || fecha == null) {
            return false;
        }

        RegistroReserva registro = reservas.get(reserva.getCodigo());

        return registro != null&& registro.reserva == reserva
                && bloquea(reserva.getEstado())&& !fecha.isBefore(registro.entrada)
                && fecha.isBefore(registro.salida);
    }

    private static boolean bloquea(EstadoReserva estado) {
        return estado == EstadoReserva.CONFIRMADA|| estado == EstadoReserva.HOSPEDADO;
    }

    public synchronized ConsultaDisponibilidad consultarDisponibilidad( LocalDate fecha) {

        Objects.requireNonNull(fecha, "Seleccione una fecha.");
        List<Habitacion> listaHabitaciones =new ArrayList<>(habitaciones.values());
        List<Reserva> listaReservas = copiarReservasInternas();
        int total = new CalculosRecursivos().contarHabitacionesDisponibles(listaHabitaciones, listaReservas, fecha);
        List<String> numeros = new ArrayList<>();
        for (Habitacion habitacion : listaHabitaciones) {

            boolean ocupada = false;

            for (Reserva reserva : listaReservas) {

                if (reserva.getHabitacion().getNumero() .equals(habitacion.getNumero())&& reservaOcupaFecha(reserva, fecha)) {
                    ocupada = true;
                    break;
                }
            }

            if (!ocupada) {
                numeros.add(habitacion.getNumero());
            }
        }

        return new ConsultaDisponibilidad(fecha, total, numeros);
    }

    public synchronized void realizarCheckIn(String codigo) {

        RegistroReserva registro = exigirReserva(codigo);

        registro.reserva.realiarCheckIn();
    }

    public synchronized void realizarCheckOut(String codigo) {

        RegistroReserva registro = exigirReserva(codigo);

        registro.reserva.realizarCheckOut();
    }

    public synchronized int revisarVencimientosReales() {

        int vencidas = 0;
        LocalDate hoy = LocalDate.now();

        for (RegistroReserva registro : reservas.values()) {

            if (registro.reserva.getEstado() == EstadoReserva.CONFIRMADA
                    && registro.entrada.isBefore(hoy)) {

                registro.reserva.marcarVencida();
                vencidas++;
            }
        }

        return vencidas;
    }

    public synchronized List<ResumenReserva> simularVencimientos(
            LocalDate fechaReferencia) {

        if (fechaReferencia == null || !fechaReferencia.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException("Indique una fecha de referencia futura.");
        }

        List<ResumenReserva> resultado = new ArrayList<>();

        for (RegistroReserva registro : reservas.values()) {

            if (registro.reserva.getEstado() == EstadoReserva.CONFIRMADA
                    && registro.entrada.isBefore(fechaReferencia)) {

                resultado.add(crearResumen(registro));
            }
        }

        return Collections.unmodifiableList(resultado);
    }

    public synchronized IndicadoresHotel calcularIndicadores() {

        List<Reserva> listaReservas = copiarReservasInternas();

        int ocupadas = 0;
        int confirmadas = 0;
        int vencidas = 0;

        for (Reserva reserva : listaReservas) {

            if (reserva.getEstado() == EstadoReserva.CONFIRMADA) {
                confirmadas++;
            }

            if (reserva.getEstado() == EstadoReserva.VENCIDA) {
                vencidas++;
            }
        }

        for (Habitacion habitacion : habitaciones.values()) {

            for (Reserva reserva : listaReservas) {

                if (reserva.getEstado() == EstadoReserva.HOSPEDADO
                        && reserva.getHabitacion().getNumero().equals(habitacion.getNumero())) {

                    ocupadas++;
                    break;
                }
            }
        }

        int total = habitaciones.size();

        long ingresos = new CalculosRecursivos() .calcularIngresosEsperados(listaReservas);

        return new IndicadoresHotel( total, ocupadas, total - ocupadas, confirmadas, vencidas, ingresos);
    }

    public synchronized List<ResumenReserva> obtenerResumenReservas() {

        List<ResumenReserva> resultado = new ArrayList<>();

        for (RegistroReserva registro : reservas.values()) {
            resultado.add(crearResumen(registro));
        }

        return Collections.unmodifiableList(resultado);
    }

    private RegistroReserva exigirReserva(String codigo) {

        RegistroReserva registro =
                codigo == null ? null : reservas.get(codigo.trim());

        if (registro == null) {
            throw new IllegalArgumentException( "No existe la reserva indicada.");
        }

        return registro;
    }

    private List<Reserva> copiarReservasInternas() {

        List<Reserva> lista = new ArrayList<>();

        for (RegistroReserva registro : reservas.values()) {
            lista.add(registro.reserva);
        }

        return lista;
    }

    private String obtenerIdHuesped(Huesped huesped) {

        for (Map.Entry<String, Huesped> dato : huespedes.entrySet()) {

            if (dato.getValue() == huesped) {
                return dato.getKey();
            }
        }

        return "";
    }

    private ResumenReserva crearResumen(RegistroReserva registro) {

        Reserva r = registro.reserva;

        return new ResumenReserva( r.getCodigo(), registro.idHuesped,
                r.getHabitacion().getNumero(), r.getCantidadPersonas(),
                registro.entrada, registro.salida,
                r.getFechaSalidaReal(),r.getEstado(),registro.importe,registro.usuarioRegistro);
    }

    private static final class RegistroReserva {

        final Reserva reserva;
        final LocalDate entrada;
        final LocalDate salida;
        final long importe;
        final String idHuesped;
        final String usuarioRegistro;

        RegistroReserva(Reserva reserva,LocalDate entrada,LocalDate salida,long importe, String idHuesped, String usuarioRegistro) {

            this.reserva = reserva;
            this.entrada = entrada;
            this.salida = salida;
            this.importe = importe;
            this.idHuesped = idHuesped;
            this.usuarioRegistro = usuarioRegistro;
        }
    }

    public static final class ResumenReserva {

        private final String codigo;
        private final String idHuesped;
        private final String numeroHabitacion;
        private final String usuarioRegistro;

        private final int cantidadPersonas;

        private final LocalDate fechaEntrada;
        private final LocalDate fechaSalida;
        private final LocalDate fechaSalidaReal;

        private final EstadoReserva estado;
        private final long importe;

        ResumenReserva( String codigo, String idHuesped,String numeroHabitacion, int cantidadPersonas, LocalDate fechaEntrada,
                LocalDate fechaSalida, LocalDate fechaSalidaReal, EstadoReserva estado,long importe, String usuarioRegistro) {

            this.codigo = codigo;
            this.idHuesped = idHuesped;
            this.numeroHabitacion = numeroHabitacion;
            this.cantidadPersonas = cantidadPersonas;
            this.fechaEntrada = fechaEntrada;
            this.fechaSalida = fechaSalida;
            this.fechaSalidaReal = fechaSalidaReal;
            this.estado = estado;
            this.importe = importe;
            this.usuarioRegistro = usuarioRegistro;
        }

        public String getCodigo() {
            return codigo;
        }

        public String getIdHuesped() {
            return idHuesped;
        }

        public String getNumeroHabitacion() {
            return numeroHabitacion;
        }

        public int getCantidadPersonas() {
            return cantidadPersonas;
        }

        public LocalDate getFechaEntrada() {
            return fechaEntrada;
        }

        public LocalDate getFechaSalida() {
            return fechaSalida;
        }

        public LocalDate getFechaSalidaReal() {
            return fechaSalidaReal;
        }

        public EstadoReserva getEstado() {
            return estado;
        }

        public long getImporte() {
            return importe;
        }

        public String getUsuarioRegistro() {
            return usuarioRegistro;
        }
    }

    public static final class ConsultaDisponibilidad {

        private final LocalDate fecha;
        private final int total;
        private final List<String> numeros;

        ConsultaDisponibilidad(
                LocalDate fecha,
                int total,
                List<String> numeros) {

            this.fecha = fecha;
            this.total = total;
            this.numeros = Collections.unmodifiableList(new ArrayList<>(numeros));
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public int getTotal() {
            return total;
        }

        public List<String> getNumeros() {
            return numeros;
        }
    }
}