import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class AgendadorCitas {

    private final List<Cita> citas = new ArrayList<>();
    private final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ROOT);
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm", Locale.ROOT);

    public void agendarCita(Scanner scanner, List<ClinicaApp.Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("Debe registrar al menos un cliente para agendar citas.");
            return;
        }
        ClinicaApp.Cliente cliente = seleccionarCliente(scanner, clientes);
        if (cliente == null) {
            return;
        }
        if (cliente.getMascotas().isEmpty()) {
            System.out.println("El cliente seleccionado no tiene mascotas registradas.");
            return;
        }
        ClinicaApp.Mascota mascota = seleccionarMascota(scanner, cliente);
        if (mascota == null) {
            return;
        }
        LocalDate fecha = leerFecha(scanner, "Fecha de la cita (yyyy-MM-dd): ");
        LocalTime hora = leerHora(scanner, "Hora de la cita (HH:mm, formato 24h): ");
        if (fecha == null || hora == null) {
            return;
        }
        System.out.print("Motivo de la cita (opcional): ");
        String motivo = scanner.nextLine().trim();
        if (motivo.isEmpty()) {
            motivo = "Consulta general";
        }
        citas.add(new Cita(cliente, mascota, fecha, hora, motivo));
        System.out.println("Cita agendada para " + cliente.getNombre() + " y su mascota " + mascota.getNombre() + ".");
    }

    public void listarCitas() {
        if (citas.isEmpty()) {
            System.out.println("No hay citas agendadas.");
            return;
        }
        System.out.println("--- Citas agendadas ---");
        for (int i = 0; i < citas.size(); i++) {
            System.out.println((i + 1) + ". " + citas.get(i).descripcion());
        }
    }

    private ClinicaApp.Cliente seleccionarCliente(Scanner scanner, List<ClinicaApp.Cliente> clientes) {
        System.out.println("--- Seleccione un cliente ---");
        for (ClinicaApp.Cliente cliente : clientes) {
            System.out.println(cliente.resumen());
        }
        System.out.print("ID del cliente: ");
        int id = leerEntero(scanner);
        for (ClinicaApp.Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        System.out.println("Cliente no encontrado.");
        return null;
    }

    private ClinicaApp.Mascota seleccionarMascota(Scanner scanner, ClinicaApp.Cliente cliente) {
        System.out.println("--- Mascotas del cliente ---");
        List<ClinicaApp.Mascota> mascotas = cliente.getMascotas();
        for (int i = 0; i < mascotas.size(); i++) {
            System.out.println((i + 1) + ". " + mascotas.get(i).descripcion());
        }
        System.out.print("Seleccione numero de mascota: ");
        int indice = leerEntero(scanner) - 1;
        if (indice < 0 || indice >= mascotas.size()) {
            System.out.println("Mascota no valida.");
            return null;
        }
        return mascotas.get(indice);
    }

    private LocalDate leerFecha(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        try {
            return LocalDate.parse(entrada, formatoFecha);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha invalida. Use el formato yyyy-MM-dd.");
            return null;
        }
    }

    private LocalTime leerHora(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String entrada = scanner.nextLine().trim();
        try {
            return LocalTime.parse(entrada, formatoHora);
        } catch (DateTimeParseException e) {
            System.out.println("Hora invalida. Use el formato HH:mm en 24 horas.");
            return null;
        }
    }

    private int leerEntero(Scanner scanner) {
        while (true) {
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                System.out.print("Ingrese un numero valido: ");
                continue;
            }
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Intente de nuevo: ");
            }
        }
    }

    private record Cita(ClinicaApp.Cliente cliente, ClinicaApp.Mascota mascota,
                        LocalDate fecha, LocalTime hora, String motivo) {
        String descripcion() {
            return fecha + " " + hora + " | " + cliente.getNombre() + " - "
                + mascota.getTipo() + " " + mascota.getNombre() + " (" + motivo + ")";
        }
    }
}
