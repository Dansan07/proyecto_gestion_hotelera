/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import conexion.Conexion_db;
import modelo.Habitacion;
import modelo.TipoHabitacion;


/**
 *
 * @author Usuario
 */
public class HabitacionDAO {
    public void insertar(Habitacion h) {

        String sql = "INSERT INTO habitacion(numero_habitacion, disponible, id_tipo) VALUES (?, ?, ?)";

        try (java.sql.Connection con = Conexion_db.conectar();
             java.sql.PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, h.getNumero_habitacion());
            pstmt.setBoolean(2, h.isDisponible());
            pstmt.setInt(3, h.getTipoHabitacion().getId_tipo());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public java.util.List<Habitacion> listar() {

        java.util.List<Habitacion> lista = new java.util.ArrayList<>();

        String sql = """
                     select id_habitacion, numero_habitacion, disponible, nombre_tipo
                     from habitacion h
                     join tipo_habitacion th on
                     h.id_tipo = th.id_tipo""";

        try (java.sql.Connection con = Conexion_db.conectar();
             java.sql.Statement st = con.createStatement();
             java.sql.ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                TipoHabitacion t = new TipoHabitacion();
                t.setNombre_tipo(
                    rs.getString("nombre_tipo")
                );

                Habitacion h = new Habitacion(
                    rs.getInt("id_habitacion"),
                    rs.getString("numero_habitacion"),
                    rs.getBoolean("disponible"),
                    t
                );

                lista.add(h);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return lista;
    }

    public void actualizar(Habitacion h) {

        String sql = "UPDATE habitacion SET numero_habitacion=?, disponible=?, id_tipo=? WHERE id_habitacion=?";

        try (java.sql.Connection con = Conexion_db.conectar();
             java.sql.PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, h.getNumero_habitacion());
            pstmt.setBoolean(2, h.isDisponible());
            pstmt.setInt(3, h.getTipoHabitacion().getId_tipo());
            pstmt.setInt(4, h.getId_habitacion());

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminar(int id) {

        String sql = "DELETE FROM habitacion WHERE id_habitacion=?";

        try (java.sql.Connection con = Conexion_db.conectar();
             java.sql.PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            pstmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Habitacion buscarPorNumero(String numero) {

    String sql = "SELECT * FROM habitacion WHERE numero_habitacion = ?";

    try (
        java.sql.Connection con = Conexion_db.conectar();
        java.sql.PreparedStatement pstmt = con.prepareStatement(sql)
    ) {

        pstmt.setString(1, numero);

        java.sql.ResultSet rs = pstmt.executeQuery();

        if(rs.next()) {

            TipoHabitacion t = new TipoHabitacion();
            t.setId_tipo(
                rs.getInt("id_tipo")
            );

            Habitacion h = new Habitacion(
                rs.getInt("id_habitacion"),
                rs.getString("numero_habitacion"),
                rs.getBoolean("disponible"),
                t
            );

            return h;
        }

    } catch(Exception e) {

        System.out.println(e.getMessage());
    }

    return null;
}
}
