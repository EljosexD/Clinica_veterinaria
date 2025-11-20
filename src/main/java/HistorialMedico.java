import java.util.ArrayList;
import java.util.List;

public class HistorialMedico {

    public static class Cita {
        public String paciente;
        public String fecha;
        public String motivo;
        public String doctor;

        public Cita(String paciente, String fecha, String motivo, String doctor) {
            this.paciente = paciente;
            this.fecha = fecha;
            this.motivo = motivo;
            this.doctor = doctor;
        }
    }

    private static final List<Cita> citas = new ArrayList<>();

    public static void agregarCita(String paciente, String fecha, String motivo, String doctor) {
        citas.add(new Cita(paciente, fecha, motivo, doctor));
    }

    public static List<Cita> getCitas() {
        return citas;
    }
}
