/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Date;

/**
 *
 * @author Usuario
 */
public class Reserva {
    private int id_reserva;
    private Date fecha_reserva;
    private Date fecha_entrada;
    private Date fecha_salida;
    private String estado;
    private Cliente cliente;
    private Habitacion habitacion;

    public Reserva() {
    }

    public Reserva(int id_reserva, Date fecha_reserva, Date fecha_entrada, Date fecha_salida, String estado, Cliente cliente, Habitacion habitacion) {
        this.id_reserva = id_reserva;
        this.fecha_reserva = fecha_reserva;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.estado = estado;
        this.cliente = cliente;
        this.habitacion = habitacion;
    }

    public int getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(int id_reserva) {
        this.id_reserva = id_reserva;
    }

    public Date getFecha_reserva() {
        return fecha_reserva;
    }

    public void setFecha_reserva(Date fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }

    public Date getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(Date fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(Date fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }
    
    
}
