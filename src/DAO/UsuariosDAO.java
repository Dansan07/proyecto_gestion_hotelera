/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import conexion.Conexion_db;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class UsuariosDAO {
    
    public static boolean validar_usuario(String user, String pass) {

        String sql = "SELECT validar_login_usuario(?, ?) AS validacion;";
        boolean validacion = false;

        try (Connection conn = Conexion_db.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    validacion = rs.getBoolean("validacion");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error validando usuario: " + e.getMessage());
        }
        return validacion;
    }
    
}
