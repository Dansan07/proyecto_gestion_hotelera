/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Usuario
 */
public class TipoHabitacion {
    
    private int id_tipo;
    private String nombre_tipo;
    private double precio_noche;
    private int capacidad;

    public TipoHabitacion() {
    }

    public TipoHabitacion(int id_tipo, String nombre_tipo, double precio_noche, int capacidad) {
        this.id_tipo = id_tipo;
        this.nombre_tipo = nombre_tipo;
        this.precio_noche = precio_noche;
        this.capacidad = capacidad;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getNombre_tipo() {
        return nombre_tipo;
    }

    public void setNombre_tipo(String nombre_tipo) {
        this.nombre_tipo = nombre_tipo;
    }

    public double getPrecio_noche() {
        return precio_noche;
    }

    public void setPrecio_noche(double precio_noche) {
        this.precio_noche = precio_noche;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }
    
    
}
