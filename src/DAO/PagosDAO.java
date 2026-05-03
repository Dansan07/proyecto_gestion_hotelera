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
import modelo.Pago;

/**
 *
 * @author Usuario
 */
public class PagosDAO {
    
    public static void insertar(Pago pago) {
        String sql = "INSERT INTO pago(monto, metodo_pago, id_reserva)"
                   + "VALUES(?, ?, ?)";
        
        try(Connection conn = Conexion_db.conectar();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            
            pstmt.setDouble(1, pago.getMonto());
            pstmt.setString(2, pago.getMetodo_pago());
            pstmt.setInt(3, pago.getReserva().getId_reserva());

            pstmt.executeUpdate();
            System.out.println("Usuario insertado");
            
        }catch(SQLException e){
            System.out.println("Error INSERT: " + e.getMessage());
        }
    }
    
    public static void listar(JTable tablaClientes, String documento) {

        String sql = "SELECT r.id_reserva, r.fecha_reserva, r.fecha_entrada,"
                + "r.fecha_salida, r.estado, c.nombre, h.numero_habitacion "
                + "FROM reserva r "
                + "JOIN cliente c ON "
                + "r.id_cliente = c.id_cliente "
                + "JOIN habitacion h ON "
                + "r.id_habitacion = h.id_habitacion "
                + "WHERE r.id_cliente = "
                + "(SELECT id_cliente FROM cliente WHERE documento = ?)";
        
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("id Reserva");
        modelo.addColumn("Reservacion");
        modelo.addColumn("Entrada");
        modelo.addColumn("Salida");
        modelo.addColumn("Estado");
        modelo.addColumn("Cliente");
        modelo.addColumn("Habitacion");

        try (Connection conn = Conexion_db.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            
            pstmt.setString(1, documento);
            
         try(ResultSet rs = pstmt.executeQuery();){   

            while (rs.next()) {
                
                Object[] fila = new Object[7];

                fila[0] = rs.getInt("id_reserva");
                fila[1] = rs.getString("fecha_reserva");
                fila[2] = rs.getString("fecha_entrada");
                fila[3] = rs.getString("fecha_salida");
                fila[4] = rs.getString("estado");
                fila[5] = rs.getString("nombre");
                fila[6] = rs.getString("numero_habitacion");

                modelo.addRow(fila);
            }
            tablaClientes.setModel(modelo);
        }
        } catch (SQLException e) {
            System.out.println("Error cargando tabla: " + e.getMessage());
        }
    }
}
