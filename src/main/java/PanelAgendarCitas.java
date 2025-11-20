import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class PanelAgendarCitas extends JPanel {

    private JTextField txtCliente;
    private JTextField txtMascota;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtMotivo;
    private JButton btnGuardar;

    public PanelAgendarCitas() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Agendar Nueva Cita", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel();
        formulario.setLayout(new GridLayout(6, 2, 10, 10));
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formulario.add(new JLabel("Nombre del Cliente:"));
        txtCliente = new JTextField();
        formulario.add(txtCliente);

        formulario.add(new JLabel("Nombre de la Mascota:"));
        txtMascota = new JTextField();
        formulario.add(txtMascota);

        formulario.add(new JLabel("Fecha (YYYY-MM-DD):"));
        txtFecha = new JTextField();
        formulario.add(txtFecha);

        formulario.add(new JLabel("Hora (HH:MM):"));
        txtHora = new JTextField();
        formulario.add(txtHora);

        formulario.add(new JLabel("Motivo:"));
        txtMotivo = new JTextField();
        formulario.add(txtMotivo);

        btnGuardar = new JButton("Guardar Cita");
        btnGuardar.addActionListener(e -> guardarCita());

        add(formulario, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void guardarCita() {
        String cliente = txtCliente.getText().trim();
        String mascota = txtMascota.getText().trim();
        String fechaStr = txtFecha.getText().trim();
        String horaStr = txtHora.getText().trim();
        String motivo = txtMotivo.getText().trim();

        if (cliente.isEmpty() || mascota.isEmpty() || fechaStr.isEmpty() || horaStr.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);

            // Guardamos en el historial
            HistorialMedico.agregarCita(
                cliente,
                fechaStr,
                motivo,
                "Dr. Pérez" // puedes cambiarlo
            );

            JOptionPane.showMessageDialog(this, "Cita guardada correctamente.");

            txtCliente.setText("");
            txtMascota.setText("");
            txtFecha.setText("");
            txtHora.setText("");
            txtMotivo.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: formato de fecha u hora incorrecto.");
        }
    }
}