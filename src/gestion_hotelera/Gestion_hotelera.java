/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestion_hotelera;

import conexion.Conexion_db;
import vista.AppLogin;
import java.awt.EventQueue;

/**
 *
 * @author Usuario
 */
public class Gestion_hotelera {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Conexion_db.conectar();
        EventQueue.invokeLater(() -> {
            AppLogin vista = new AppLogin();
            vista.setVisible(true);
        });
    }
    
}
