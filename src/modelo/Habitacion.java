/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public class Habitacion {
    
    private int id_habitacion;
    private String numero_habitacion;
    private boolean disponible;
    private TipoHabitacion tipoHabitacion;

    public Habitacion() {
    }

    public Habitacion(int id_habitacion, String numero_habitacion, boolean disponible, TipoHabitacion tipoHabitacion) {
        this.id_habitacion = id_habitacion;
        this.numero_habitacion = numero_habitacion;
        this.disponible = disponible;
        this.tipoHabitacion = tipoHabitacion;
    }

    public int getId_habitacion() {
        return id_habitacion;
    }

    public void setId_habitacion(int id_habitacion) {
        this.id_habitacion = id_habitacion;
    }

    public String getNumero_habitacion() {
        return numero_habitacion;
    }

    public void setNumero_habitacion(String numero_habitacion) {
        this.numero_habitacion = numero_habitacion;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public TipoHabitacion getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }
    
    
    
}
