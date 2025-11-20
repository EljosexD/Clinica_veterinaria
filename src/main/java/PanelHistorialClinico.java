import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelHistorialClinico extends JPanel {

    private JTable tablaHistorial;

    public PanelHistorialClinico() {
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());

        JLabel titulo = new JLabel("Historial Clínico", SwingConstants.CENTER);
        titulo.setFont(new java.awt.Font("Segoe UI", 1, 24));
        add(titulo, java.awt.BorderLayout.NORTH);

        tablaHistorial = new JTable();
        tablaHistorial.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Paciente", "Fecha", "Motivo", "Doctor"}
        ));

        add(new JScrollPane(tablaHistorial), java.awt.BorderLayout.CENTER);
    }

    private void cargarDatos() {
        DefaultTableModel model = (DefaultTableModel) tablaHistorial.getModel();
        model.setRowCount(0);

        for (HistorialMedico.Cita c : HistorialMedico.getCitas()) {
            model.addRow(new Object[]{c.paciente, c.fecha, c.motivo, c.doctor});
        }
    }
}
