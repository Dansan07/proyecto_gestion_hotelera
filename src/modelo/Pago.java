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
public class Pago {
    
    private int id_pago;
    private double monto;
    private String metodo_pago;
    private Date fecha_pago; 
    private Reserva reserva; 

    public Pago() {
    }

    public Pago(int id_pago, double monto, String metodo_pago, Date fecha_pago, Reserva reserva) {
        this.id_pago = id_pago;
        this.monto = monto;
        this.metodo_pago = metodo_pago;
        this.fecha_pago = fecha_pago;
        this.reserva = reserva;
    }

    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMetodo_pago() {
        return metodo_pago;
    }

    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    public Date getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(Date fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
    
    
}
