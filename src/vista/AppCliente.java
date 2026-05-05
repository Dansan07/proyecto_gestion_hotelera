/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import DAO.ClientesDAO;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import modelo.Cliente;

/**
 *
 * @author Usuario
 */
public class AppCliente extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AppCliente.class.getName());

    /**
     * Creates new form AppCliente
     */
    public AppCliente() {
        initComponents();
        AppLogin login = new AppLogin();
        setJMenuBar(crearBarra(420, 585, this));
        mostrarClientes();
    }
    
    public void mostrarDatosFormulario(){
        int fila = (tablaClientes.getSelectedRow() == -1) ? 0 : tablaClientes.getSelectedRow();
        
        txtNombre.setText(tablaClientes.getValueAt(fila, 1).toString());
        txtApellido.setText(tablaClientes.getValueAt(fila, 2).toString());
        txtTelefono.setText(tablaClientes.getValueAt(fila, 3).toString());
        txtEmail.setText(tablaClientes.getValueAt(fila, 4).toString());
        txtDocumento.setText(tablaClientes.getValueAt(fila, 5).toString());
        lblFechaRegistro.setText(tablaClientes.getValueAt(fila, 6).toString());
    }    
    private boolean validarDatos(){
        boolean ok = true;
        if (txtNombre.getText().isEmpty() ||
            txtApellido.getText().isEmpty() ||
            txtTelefono.getText().isEmpty()||
            txtEmail.getText().isEmpty() ||
            txtDocumento.getText().isEmpty()){
            
            JOptionPane.showMessageDialog(null, "Todos los Datos son obligatorios");
            ok = false;
        }
        return ok;
    }    
    public JMenuBar crearBarra(int width, int height, JFrame frame){
        setTitle("Gestion Hotelera");
        setSize(width, height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JMenuBar barra = new JMenuBar();
        
        JMenu registrar = new JMenu("Registrar");
        JMenu gestionar = new JMenu("Gestionar");
        JMenu configurar = new JMenu("Configurar");
        
        JMenuItem item_cliente = new JMenuItem("Cliente");
        JMenuItem item_habitacion = new JMenuItem("Habitacion");
        JMenuItem item_pago = new JMenuItem("Pago");
        JMenuItem item_reserva = new JMenuItem("Reservas");
        JMenuItem item_tipo_habitacion = new JMenuItem("Tipo de Habitacion");
        JMenuItem item_salir = new JMenuItem("Cerrar Sesión");
        
        barra.add(registrar);
        barra.add(gestionar);
        barra.add(configurar);
        
        gestionar.add(item_pago);
        gestionar.add(item_reserva);
        registrar.add(item_cliente);
        registrar.add(item_habitacion);
        configurar.add(item_tipo_habitacion);
        configurar.add(item_salir);
        
        item_pago.addActionListener(e ->{
            EventQueue.invokeLater(()->{
                AppPago vista = new AppPago();
                vista.setVisible(true);
            });
            frame.dispose();
        });
        item_cliente.addActionListener(e ->{
            EventQueue.invokeLater(()->{
                AppCliente vista = new AppCliente();
                vista.setVisible(true);
            });
            frame.dispose();
        });
        item_tipo_habitacion.addActionListener(e->{
            EventQueue.invokeLater(()->{
                AppTipoHabitacion vista = new AppTipoHabitacion();
                vista.setVisible(true);
            });
            frame.dispose();        
        });
        item_salir.addActionListener(e -> {
            EventQueue.invokeLater(()->{
                int opcion = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea Cerrar sesión?", "Confirmar Salida", JOptionPane.YES_NO_OPTION);
                if (opcion == JOptionPane.YES_OPTION){
                    AppLogin vista = new AppLogin();                
                    vista.setVisible(true);
                    frame.dispose();
                }                
            });            
        });
        
        return barra;        
    }
    private void limpiarFormulario(){
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDocumento.setText("");
        lblFechaRegistro.setText("-");
    }
    private Cliente obtenerDatosCliente(){
        Cliente cliente = new Cliente();
        
        cliente.setNombre(txtNombre.getText().toString().trim());
        cliente.setApellido(txtApellido.getText().toString().trim());
        cliente.setTelefono(txtTelefono.getText().toString().trim());
        cliente.setEmail(txtEmail.getText().toString().trim());
        cliente.setDocumento(txtDocumento.getText().toString().trim());
        
        return cliente;
    }
    
    
    private void guardarCliente(){        
        ClientesDAO.insertar(obtenerDatosCliente());
    }    
    private void mostrarClientes(){
        ClientesDAO.listar(tablaClientes);
    }
    private void actualizarCliente(){
        ClientesDAO.actualizar(obtenerDatosCliente());
    }
    private void eliminarCliente(){
        ClientesDAO.eliminar(txtDocumento.getText().toString().trim());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtApellido = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblFechaRegistro = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        btnGuardarCliente = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaClientes = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Formulario Clientes"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Nombre");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 52, 66, 22));
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 52, 200, -1));

        jLabel2.setText("Apellido");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 86, 66, 22));
        jPanel1.add(txtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 86, 200, -1));

        jLabel3.setText("Teléfono");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 120, 66, 22));
        jPanel1.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 120, 200, -1));

        jLabel4.setText("Email");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 154, 66, 22));
        jPanel1.add(txtEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 154, 200, -1));

        jLabel5.setText("Documento");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 188, 66, 22));

        jLabel6.setText("Fecha de Registro");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 222, 102, 22));

        lblFechaRegistro.setText("-");
        jPanel1.add(lblFechaRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 222, 134, 22));
        jPanel1.add(txtDocumento, new org.netbeans.lib.awtextra.AbsoluteConstraints(114, 188, 200, -1));

        btnGuardarCliente.setText("Agregar");
        btnGuardarCliente.addActionListener(this::btnGuardarClienteActionPerformed);
        jPanel1.add(btnGuardarCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(42, 262, -1, -1));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(121, 262, -1, -1));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(309, 262, -1, -1));

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 262, -1, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 310));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Tabla Clientes"));

        tablaClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaClientes);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 400, 210));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed
        // TODO add your handling code here:
        if(validarDatos()){
            guardarCliente();
            mostrarClientes();
        }        
    }//GEN-LAST:event_btnGuardarClienteActionPerformed

    private void tablaClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaClientesMouseClicked
        // TODO add your handling code here:
        mostrarDatosFormulario();
    }//GEN-LAST:event_tablaClientesMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        if (validarDatos()){
            eliminarCliente();
            mostrarClientes();
        }        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        if (validarDatos()){
            actualizarCliente(); 
            mostrarClientes();
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AppCliente().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFechaRegistro;
    private javax.swing.JTable tablaClientes;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
