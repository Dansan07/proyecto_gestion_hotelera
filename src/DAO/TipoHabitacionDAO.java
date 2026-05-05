/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import conexion.Conexion_db;
import modelo.TipoHabitacion;

/**
 *
 * @author Usuario
 */
public class TipoHabitacionDAO {
    
    public void insertar(TipoHabitacion t) {

    String sql = "INSERT INTO tipo_habitacion(nombre_tipo, precio_noche, capacidad) VALUES (?, ?, ?)";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, t.getNombre_tipo());
        pstmt.setDouble(2, t.getPrecio_noche());
        pstmt.setInt(3, t.getCapacidad());

        pstmt.executeUpdate();

    } catch (Exception e) {
        System.out.println(e);
    }
}

    public java.util.List<TipoHabitacion> listar() {

    java.util.List<TipoHabitacion> lista = new java.util.ArrayList<>();

    String sql = "SELECT * FROM tipo_habitacion";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {

            TipoHabitacion t = new TipoHabitacion(
                rs.getInt("id_tipo"),
                rs.getString("nombre_tipo"),
                rs.getDouble("precio_noche"),
                rs.getInt("capacidad")
            );

            lista.add(t);
        }

    } catch (Exception e) {
        System.out.println(e);
    }

    return lista;
}

    public void eliminar(int id_tipo) {

    String sql = "DELETE FROM tipo_habitacion WHERE id_tipo=?";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setInt(1, id_tipo);

        pstmt.executeUpdate();

    } catch (Exception e) {
        System.out.println(e);
    }
}

    public void actualizar(TipoHabitacion t) {

    String sql = "UPDATE tipo_habitacion SET nombre_tipo=?, precio_noche=?, capacidad=? WHERE id_tipo=?";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, t.getNombre_tipo());
        pstmt.setDouble(2, t.getPrecio_noche());
        pstmt.setInt(3, t.getCapacidad());
        pstmt.setInt(4, t.getId_tipo());

        pstmt.executeUpdate();

    } catch (Exception e) {
        System.out.println(e);
    }
}
}
