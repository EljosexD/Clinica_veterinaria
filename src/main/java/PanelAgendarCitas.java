import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class PanelAgendarCitas extends JPanel {

    private JComboBox<ClinicaApp.Cliente> comboClientes;
    private JComboBox<ClinicaApp.Mascota> comboMascotas;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtMotivo;
    private JLabel lblEstado;

    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ROOT);
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT);

    public PanelAgendarCitas() {
        initComponents();
        cargarClientes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Agendar Nueva Cita", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
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
        comboClientes.addActionListener(e -> cargarMascotasDelCliente());

        comboMascotas = new JComboBox<>();
        comboMascotas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ClinicaApp.Mascota mascota) {
                    setText(mascota.descripcion());
                }
                return this;
            }
        });

        txtFecha = new JTextField();
        txtHora = new JTextField();
        txtMotivo = new JTextField();

        int fila = 0;
        gbc.gridx = 0;
        gbc.gridy = fila;
        formulario.add(new JLabel("Cliente:"), gbc);
        gbc.gridy = ++fila;
        formulario.add(comboClientes, gbc);

        gbc.gridy = ++fila;
        formulario.add(new JLabel("Mascota:"), gbc);
        gbc.gridy = ++fila;
        formulario.add(comboMascotas, gbc);

        gbc.gridy = ++fila;
        formulario.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridy = ++fila;
        formulario.add(txtFecha, gbc);

        gbc.gridy = ++fila;
        formulario.add(new JLabel("Hora (HH:mm, 24h):"), gbc);
        gbc.gridy = ++fila;
        formulario.add(txtHora, gbc);

        gbc.gridy = ++fila;
        formulario.add(new JLabel("Motivo (opcional):"), gbc);
        gbc.gridy = ++fila;
        formulario.add(txtMotivo, gbc);

        add(formulario, BorderLayout.CENTER);

        JPanel pie = new JPanel(new BorderLayout(5, 5));
        JButton btnGuardar = new JButton("Guardar Cita");
        btnGuardar.addActionListener(e -> guardarCita());
        lblEstado = new JLabel("Seleccione un cliente y mascota registrados en el sistema.");
        pie.add(lblEstado, BorderLayout.CENTER);
        pie.add(btnGuardar, BorderLayout.EAST);
        add(pie, BorderLayout.SOUTH);
    }

    private void cargarClientes() {
        DefaultComboBoxModel<ClinicaApp.Cliente> model = new DefaultComboBoxModel<>();
        for (ClinicaApp.Cliente cliente : ClinicaApp.obtenerClientes()) {
            model.addElement(cliente);
        }
        comboClientes.setModel(model);
        comboClientes.setEnabled(model.getSize() > 0);
        cargarMascotasDelCliente();
        actualizarEstado();
    }

    private void cargarMascotasDelCliente() {
        ClinicaApp.Cliente cliente = (ClinicaApp.Cliente) comboClientes.getSelectedItem();
        DefaultComboBoxModel<ClinicaApp.Mascota> model = new DefaultComboBoxModel<>();
        if (cliente != null) {
            for (ClinicaApp.Mascota mascota : cliente.getMascotas()) {
                model.addElement(mascota);
            }
        }
        comboMascotas.setModel(model);
        comboMascotas.setEnabled(model.getSize() > 0);
        actualizarEstado();
    }

    private void actualizarEstado() {
        if (!comboClientes.isEnabled()) {
            lblEstado.setText("Debe crear clientes antes de agendar citas.");
        } else if (!comboMascotas.isEnabled()) {
            lblEstado.setText("El cliente seleccionado no tiene mascotas registradas.");
        } else {
            lblEstado.setText("Ingrese fecha y hora con formato yyyy-MM-dd y HH:mm.");
        }
    }

    private void guardarCita() {
        ClinicaApp.Cliente cliente = (ClinicaApp.Cliente) comboClientes.getSelectedItem();
        ClinicaApp.Mascota mascota = (ClinicaApp.Mascota) comboMascotas.getSelectedItem();

        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Debe registrar al menos un cliente para agendar citas.");
            return;
        }
        if (mascota == null) {
            JOptionPane.showMessageDialog(this, "El cliente seleccionado no tiene mascotas registradas.");
            return;
        }

        LocalDate fecha;
        try {
            fecha = LocalDate.parse(txtFecha.getText().trim(), formatoFecha);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Fecha invalida. Use el formato yyyy-MM-dd.");
            return;
        }

        LocalTime hora;
        try {
            hora = LocalTime.parse(txtHora.getText().trim(), formatoHora);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Hora invalida. Use el formato HH:mm en 24 horas.");
            return;
        }

        String motivo = txtMotivo.getText().trim();
        if (motivo.isEmpty()) {
            motivo = "Consulta general";
        }

        String paciente = cliente.getNombre() + " - " + mascota.getTipo() + " " + mascota.getNombre();
        HistorialMedico.agregarCita(paciente, fecha.toString(), motivo, "Dr. Perez");
        JOptionPane.showMessageDialog(this, "Cita agendada para " + paciente + ".");

        txtFecha.setText("");
        txtHora.setText("");
        txtMotivo.setText("");
    }
}
