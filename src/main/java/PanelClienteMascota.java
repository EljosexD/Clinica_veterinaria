import javax.swing.*;
import java.awt.*;

public class PanelClienteMascota extends JPanel {

    private JTextField txtNombreCliente;
    private JTextField txtTelefono;
    private JComboBox<ClinicaApp.Cliente> comboClientes;
    private JComboBox<String> comboTipoMascota;
    private JTextField txtNombreMascota;
    private JTextField txtEdadMascota;
    private JLabel lblExtraDato;
    private JTextField txtExtraDato;
    private JTextArea areaResumen;

    public PanelClienteMascota() {
        initComponents();
        recargarClientes();
        actualizarResumen();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Clientes y Mascotas", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setLayout(new GridLayout(1, 2, 15, 0));
        contenido.setPreferredSize(new Dimension(0, 420));

        JPanel panelCliente = construirPanelCliente();
        JPanel panelMascota = construirPanelMascota();

        contenido.add(panelCliente);
        contenido.add(panelMascota);

        add(contenido, BorderLayout.CENTER);
        areaResumen = new JTextArea(5, 40);
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(areaResumen), BorderLayout.SOUTH);
    }

    private JPanel construirPanelCliente() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Crear cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtNombreCliente = new JTextField(20);
        txtTelefono = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridy++;
        panel.add(txtNombreCliente, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Telefono:"), gbc);
        gbc.gridy++;
        panel.add(txtTelefono, gbc);

        gbc.gridy++;
        JButton btnCrear = new JButton("Crear cliente");
        btnCrear.addActionListener(e -> crearCliente());
        panel.add(btnCrear, gbc);

        return panel;
    }

    private JPanel construirPanelMascota() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBorder(BorderFactory.createTitledBorder("Registrar mascota"));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        comboClientes = new JComboBox<>();
        comboClientes.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ClinicaApp.Cliente cliente) {
                    setText(cliente.resumen());
                }
                return this;
            }
        });

        comboTipoMascota = new JComboBox<>(new String[]{"Perro", "Gato"});
        comboTipoMascota.addActionListener(e -> actualizarEtiquetaExtra());

        txtNombreMascota = new JTextField(20);
        txtEdadMascota = new JTextField(20);
        lblExtraDato = new JLabel("Raza:");
        txtExtraDato = new JTextField(20);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Cliente:"), gbc);
        gbc.gridy++;
        panel.add(comboClientes, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Tipo de mascota:"), gbc);
        gbc.gridy++;
        panel.add(comboTipoMascota, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridy++;
        panel.add(txtNombreMascota, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Edad (años):"), gbc);
        gbc.gridy++;
        panel.add(txtEdadMascota, gbc);

        gbc.gridy++;
        panel.add(lblExtraDato, gbc);
        gbc.gridy++;
        panel.add(txtExtraDato, gbc);

        // Filtro de espacio para mantener el formulario alineado y visibilidad del ultimo campo
        gbc.gridy++;
        gbc.weighty = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);

        gbc.weighty = 0;
        contenedor.add(panel, BorderLayout.CENTER);

        JButton btnAgregarMascota = new JButton("Agregar mascota");
        btnAgregarMascota.addActionListener(e -> agregarMascota());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.add(btnAgregarMascota);
        contenedor.add(pie, BorderLayout.SOUTH);

        return contenedor;
    }

    private void crearCliente() {
        try {
            ClinicaApp.registrarCliente(txtNombreCliente.getText(), txtTelefono.getText());
            JOptionPane.showMessageDialog(this, "Cliente creado correctamente.");
            txtNombreCliente.setText("");
            txtTelefono.setText("");
            recargarClientes();
            actualizarResumen();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validacion", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void agregarMascota() {
        ClinicaApp.Cliente cliente = (ClinicaApp.Cliente) comboClientes.getSelectedItem();
        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Debe crear al menos un cliente primero.");
            return;
        }

        String nombreMascota = txtNombreMascota.getText().trim();
        if (nombreMascota.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre de mascota es obligatorio.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(txtEdadMascota.getText().trim());
            if (edad < 0) {
                throw new NumberFormatException("edad negativa");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Edad invalida, ingrese un numero mayor o igual a cero.");
            return;
        }

        String extra = txtExtraDato.getText().trim();
        String tipo = (String) comboTipoMascota.getSelectedItem();
        ClinicaApp.Mascota mascota;
        if ("Gato".equals(tipo)) {
            mascota = new ClinicaApp.Gato(nombreMascota, edad, extra);
        } else {
            mascota = new ClinicaApp.Perro(nombreMascota, edad, extra);
        }

        ClinicaApp.agregarMascota(cliente.getId(), mascota);
        JOptionPane.showMessageDialog(this, "Mascota registrada para " + cliente.getNombre() + ".");

        txtNombreMascota.setText("");
        txtEdadMascota.setText("");
        txtExtraDato.setText("");
        actualizarResumen();
    }

    private void recargarClientes() {
        DefaultComboBoxModel<ClinicaApp.Cliente> model = new DefaultComboBoxModel<>();
        for (ClinicaApp.Cliente c : ClinicaApp.obtenerClientes()) {
            model.addElement(c);
        }
        comboClientes.setModel(model);
        comboClientes.setEnabled(model.getSize() > 0);
    }

    private void actualizarEtiquetaExtra() {
        String tipo = (String) comboTipoMascota.getSelectedItem();
        if ("Gato".equals(tipo)) {
            lblExtraDato.setText("Color:");
        } else {
            lblExtraDato.setText("Raza:");
        }
    }

    private void actualizarResumen() {
        areaResumen.setText(ClinicaApp.resumenClientes());
        areaResumen.setCaretPosition(0);
    }
}
