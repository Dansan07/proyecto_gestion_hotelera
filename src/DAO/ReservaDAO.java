/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import conexion.Conexion_db;
import modelo.Cliente;
import modelo.Habitacion;
import modelo.Reserva;
/**
 *
 * @author Usuario
 */
public class ReservaDAO {
    public void insertar(Reserva r) {

    String sql = "INSERT INTO reservas(fecha_reserva, fecha_entrada, fecha_salida, estado, id_cliente, id_habitacion) VALUES (?, ?, ?, ?, ?, ?)";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDate(1, new java.sql.Date(r.getFecha_reserva().getTime()));
        ps.setDate(2, new java.sql.Date(r.getFecha_entrada().getTime()));
        ps.setDate(3, new java.sql.Date(r.getFecha_salida().getTime()));

        ps.setString(4, r.getEstado());

        ps.setInt(5, r.getCliente().getId_cliente());

        ps.setInt(6, r.getHabitacion().getId_habitacion());

        ps.executeUpdate();

    } catch (Exception e) {
        System.out.println(e);
    }
}
    public java.util.List<Reserva> listar() {

    java.util.List<Reserva> lista = new java.util.ArrayList<>();

    String sql = "SELECT * FROM reserva";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.Statement st = con.createStatement();
         java.sql.ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {

            Cliente c = new Cliente();
            c.setId_cliente(rs.getInt("id_cliente"));

            Habitacion h = new Habitacion();
            h.setId_habitacion(rs.getInt("id_habitacion"));

            Reserva r = new Reserva(
                rs.getInt("id_reserva"),
                rs.getDate("fecha_reserva"),
                rs.getDate("fecha_entrada"),
                rs.getDate("fecha_salida"),
                rs.getString("estado"),
                c,
                h
            );

            lista.add(r);
        }

    } catch (Exception e) {
        System.out.println(e);
    }

    return lista;
}
    public void actualizar(Reserva r) {

    String sql = "UPDATE reservas SET fecha_reserva=?, fecha_entrada=?, fecha_salida=?, estado=?, id_cliente=?, id_habitacion=? WHERE id_reserva=?";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setDate(1, new java.sql.Date(r.getFecha_reserva().getTime()));

        ps.setDate(2, new java.sql.Date(r.getFecha_entrada().getTime()));

        ps.setDate(3, new java.sql.Date(r.getFecha_salida().getTime()));

        ps.setString(4, r.getEstado());

        ps.setInt(5, r.getCliente().getId_cliente());

        ps.setInt(6, r.getHabitacion().getId_habitacion());

        ps.setInt(7, r.getId_reserva());

        ps.executeUpdate();

    } catch (Exception e) {
        System.out.println(e);
    }
}
    public void eliminar(int id) {

    String sql = "DELETE FROM reservas WHERE id_reserva=?";

    try (java.sql.Connection con = Conexion_db.conectar();
         java.sql.PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, id);

        ps.executeUpdate();

    } catch (Exception e) {
        System.out.println(e);
    }
}
}
