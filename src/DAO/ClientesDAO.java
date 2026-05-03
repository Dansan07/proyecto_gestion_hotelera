/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import conexion.Conexion_db;
import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;

/**
 *
 * @author Usuario
 */
public class ClientesDAO {
    
    public static void insertar(Cliente cliente) {
        String sql = "INSERT INTO cliente(nombre, apellido, telefono, email, documento)"
                   + "VALUES(?, ?, ?, ?, ?)";
        
        try(Connection conn = Conexion_db.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getEmail());
            pstmt.setString(5, cliente.getDocumento());

            pstmt.executeUpdate();
            System.out.println("Usuario insertado");
            
        }catch(SQLException e){
            System.out.println("Error INSERT: " + e.getMessage());
        }
    }
    
    public static void listar(JTable tablaClientes) {

        String sql = "SELECT * FROM cliente";
        
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("id");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Teléfono");
        modelo.addColumn("Email");
        modelo.addColumn("Documento");
        modelo.addColumn("fecha Registro");

        try (Connection conn = Conexion_db.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                
                Object[] fila = new Object[7];

                fila[0] = rs.getInt("id_cliente");
                fila[1] = rs.getString("nombre");
                fila[2] = rs.getString("apellido");
                fila[3] = rs.getString("telefono");
                fila[4] = rs.getString("email");
                fila[5] = rs.getString("documento");
                fila[6] = rs.getString("fecha_registro");

                modelo.addRow(fila);
            }
        tablaClientes.setModel(modelo);
        } catch (SQLException e) {
            System.out.println("Error cargando tabla: " + e.getMessage());
        }
    }
    
    public static void actualizar(Cliente cliente) {
        
        String sql = "UPDATE cliente "
                + "SET nombre = ?, apellido = ?,"
                + "telefono = ?, email = ? "
                + "WHERE documento = ?";

        try (Connection conn = Conexion_db.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getEmail());            
            pstmt.setString(5, cliente.getDocumento());

            int filas = pstmt.executeUpdate();
            System.out.println("Filas actualizadas: " + filas);

        } catch (SQLException e) {
            System.out.println("Error UPDATE: " + e.getMessage());
        }
    }
    
    public static void eliminar(String documento) {

        String sql = "DELETE FROM cliente WHERE documento = ?";

        try (Connection conn = Conexion_db.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, documento);

            int filas = pstmt.executeUpdate();
            System.out.println("Filas eliminadas: " + filas);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Al borrar: Puede que el Cliente tenga una Reserva Activa.\n"
                    + "No se puede Eliminar.");
        }
    }
    
}
