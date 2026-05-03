/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;
import java.sql.*;
/**
 *
 * @author Usuario
 */
public class Conexion_db {
    
    static String url ="jdbc:postgresql://localhost:5432/hotel_db";
    static String user ="admin_hotel";
    static String pass ="1234";
    
    public static Connection conectar(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión exitosa");
        }catch(SQLException e){
            System.out.println("Error de Conexión");
            e.printStackTrace();
        }
        return conn;
    }
}
