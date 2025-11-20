import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ClinicaApp {

    // Registro en memoria de todos los clientes y sus mascotas asociadas (compartido con la GUI).
    private static final List<Cliente> CLIENTES = new ArrayList<>();
    // Id incremental sencillo para diferenciar clientes en la sesión actual.
    private static int siguienteIdCliente = 1;
    // Gestor encapsulado de citas para mantener esa lógica separada.
    private final AgendadorCitas agendadorCitas = new AgendadorCitas();

    public void run() {
        Locale.setDefault(Locale.US);
        
        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;

            while (!salir) {
                mostrarMenu();
                
                System.out.print("Seleccione una opcion: ");
                int opcion = leerEntero(scanner);
                
                switch (opcion) {
                    case 1 -> crearCliente(scanner);
                    case 2 -> editarCliente(scanner);
                    case 3 -> buscarCliente(scanner);
                    case 4 -> registrarMascota(scanner);
                    case 5 -> listarClientes();
                    case 6 -> agendadorCitas.agendarCita(scanner, CLIENTES);
                    case 7 -> agendadorCitas.listarCitas();
                    case 11 -> listarMascotas();
                    case 13 -> facturas();
                    case 0 -> {
                        System.out.println("Hasta luego.");
                        salir = true;
                    }
                    default -> System.out.println("Opcion no valida.");
                }
                
                System.out.println();
            }
        }
    }
    private void facturas(){
        FacturaEfectivo facturaE = new FacturaEfectivo("Jose", "555-1234", "Firulais", "2024-06-15", 100.0);
        FacturaDigital facturaD = new FacturaDigital("Jose", "555-1234", "Firulais", "2024-06-15", 100.0);

        facturaE.generarFacturaPdf();
        facturaD.generarFacturaPdf();
        // jfFactura.setVisible(true);
        // jfFactura.setLocationRelativeTo(null);
        // jfFactura.getFactura(factura);
    }

    private void mostrarMenu() {
        System.out.println("=== Clinica Veterinaria ===");
        System.out.println("1. Crear cliente");
        System.out.println("2. Editar cliente");
        System.out.println("3. Buscar cliente");
        System.out.println("4. Registrar mascota");
        System.out.println("5. Listar clientes");
        System.out.println("6. Agendar cita");
        System.out.println("7. Listar citas");
        System.out.println("0. Salir");
    }

    // Flujo básico para capturar la información mínima de un cliente.
    private void crearCliente(Scanner scanner) {
        System.out.println("--- Crear cliente ---");
        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        String telefono = leerTextoObligatorio(scanner, "Telefono: ");
        Cliente cliente = registrarCliente(nombre, telefono);
        System.out.println("Cliente creado con ID " + cliente.getId());
    }

    // Permite actualizar nombre/teléfono de un cliente ya registrado.
    private void editarCliente(Scanner scanner) {
        if (CLIENTES.isEmpty()) {
            System.out.println("No hay clientes para editar.");
            return;
        }
        listarClientes();
        System.out.print("Ingrese ID del cliente a editar: ");
        int id = leerEntero(scanner);
        Cliente cliente = encontrarClientePorId(id);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.print("Nuevo nombre (enter para mantener " + cliente.getNombre() + "): ");
        String nuevoNombre = scanner.nextLine().trim();
        if (!nuevoNombre.isEmpty()) {
            cliente.setNombre(nuevoNombre);
        }
        System.out.print("Nuevo telefono (enter para mantener " + cliente.getTelefono() + "): ");
        String nuevoTelefono = scanner.nextLine().trim();
        if (!nuevoTelefono.isEmpty()) {
            cliente.setTelefono(nuevoTelefono);
        }
        System.out.println("Cliente actualizado.");
    }

    // Búsqueda simple por nombre o teléfono dentro de la lista actual.
    private void buscarCliente(Scanner scanner) {
        if (CLIENTES.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.print("Ingrese nombre o telefono a buscar: ");
        String termino = scanner.nextLine().trim().toLowerCase(Locale.ROOT);
        if (termino.isEmpty()) {
            System.out.println("Busqueda vacia, intente nuevamente.");
            return;
        }
        boolean encontrado = false;
        for (Cliente cliente : CLIENTES) {
            if (cliente.coincide(termino)) {
                if (!encontrado) {
                    System.out.println("--- Resultados ---");
                }
                encontrado = true;
                mostrarDetalleCliente(cliente);
            }
        }
        if (!encontrado) {
            System.out.println("No se encontraron clientes con el termino indicado.");
        }
    }

    // Vincula una nueva mascota (perro o gato) al cliente seleccionado.
    private void registrarMascota(Scanner scanner) {
        if (CLIENTES.isEmpty()) {
            System.out.println("Debe registrar al menos un cliente primero.");
            return;
        }
        listarClientes();
        System.out.print("Ingrese ID del cliente: ");
        int id = leerEntero(scanner);
        Cliente cliente = encontrarClientePorId(id);
        if (cliente == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }
        System.out.println("Seleccione tipo de mascota:");
        System.out.println("1. Perro");
        System.out.println("2. Gato");
        Mascota mascota = null;
        while (mascota == null) {
            System.out.print("Opcion: ");
            int opcion = leerEntero(scanner);
            switch (opcion) {
                case 1 -> mascota = crearPerro(scanner);
                case 2 -> mascota = crearGato(scanner);
                default -> System.out.println("Tipo no valido, intente nuevamente.");
            }
        }
        cliente.agregarMascota(mascota);
        System.out.println("Mascota registrada para el cliente " + cliente.getNombre() + ".");
    }

    // Crea la instancia de perro con datos obligatorios y opcionales.
    private Mascota crearPerro(Scanner scanner) {
        System.out.println("--- Registrar perro ---");
        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        int edad = leerEnteroNoNegativo(scanner, "Edad (años): ");
        System.out.print("Raza (opcional): ");
        String raza = scanner.nextLine().trim();
        return new Perro(nombre, edad, raza);
    }

    // Crea la instancia de gato con datos obligatorios y opcionales.
    private Mascota crearGato(Scanner scanner) {
        System.out.println("--- Registrar gato ---");
        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        int edad = leerEnteroNoNegativo(scanner, "Edad (años): ");
        System.out.print("Color (opcional): ");
        String color = scanner.nextLine().trim();
        return new Gato(nombre, edad, color);
    }

    // Muestra el resumen de todos los clientes registrados.
    private void listarClientes() {
        if (CLIENTES.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("--- Clientes ---");
        for (Cliente cliente : CLIENTES) {
            System.out.println(cliente.resumen());
        }
    }

    // Listas de mascotas 
    // Recorre cada cliente mostrando el detalle de sus mascotas.
    private void listarMascotas() {
        System.out.println("--- Mascotas ---");
        for (Cliente cliente : CLIENTES) {
            System.out.print("Cliente: " + cliente.getNombre());
            for (Mascota mascota : cliente.getMascotas()) {
                System.out.println("  - " + mascota.descripcion());
            }
        }
    }

    // Utilidad para localizar rápidamente un cliente por su ID asignado.
    private Cliente encontrarClientePorId(int id) {
        for (Cliente cliente : CLIENTES) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    // Presenta los datos del cliente junto con el inventario de mascotas.
    private void mostrarDetalleCliente(Cliente cliente) {
        System.out.println(cliente.resumen());
        if (cliente.getMascotas().isEmpty()) {
            System.out.println("  Sin mascotas registradas.");
        } else {
            for (Mascota mascota : cliente.getMascotas()) {
                System.out.println("  - " + mascota.descripcion());
            }
        }
    }

    // Garantiza entradas de texto no vacías para datos críticos.
    private String leerTextoObligatorio(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("El valor no puede estar vacio.");
        }
    }

    // Metodos de acceso compartidos para paneles GUI (repositorio en memoria).
    public static synchronized Cliente registrarCliente(String nombre, String telefono) {
        if (nombre == null || nombre.trim().isEmpty() || telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre y telefono son obligatorios");
        }
        Cliente cliente = new Cliente(siguienteIdCliente++, nombre.trim(), telefono.trim());
        CLIENTES.add(cliente);
        return cliente;
    }

    public static synchronized void agregarMascota(int clienteId, Mascota mascota) {
        Cliente cliente = null;
        for (Cliente c : CLIENTES) {
            if (c.getId() == clienteId) {
                cliente = c;
                break;
            }
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado para registrar la mascota");
        }
        cliente.agregarMascota(mascota);
    }

    public static synchronized List<Cliente> obtenerClientes() {
        return new ArrayList<>(CLIENTES);
    }

    public static synchronized boolean hayClientesConMascotas() {
        for (Cliente cliente : CLIENTES) {
            if (!cliente.getMascotas().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static synchronized String resumenClientes() {
        if (CLIENTES.isEmpty()) {
            return "No hay clientes registrados.";
        }
        StringBuilder sb = new StringBuilder();
        for (Cliente cliente : CLIENTES) {
            sb.append(cliente.resumen()).append(System.lineSeparator());
            if (cliente.getMascotas().isEmpty()) {
                sb.append("  Sin mascotas registradas.").append(System.lineSeparator());
                continue;
            }
            for (Mascota mascota : cliente.getMascotas()) {
                sb.append("  - ").append(mascota.descripcion()).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    // Lector robusto para enteros desde consola, reutilizado en todo el flujo.
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

    // Variante que además impide valores negativos (por ejemplo, edad).
    private int leerEnteroNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            int valor = leerEntero(scanner);
            if (valor >= 0) {
                return valor;
            }
            System.out.println("El numero debe ser mayor o igual a cero.");
        }
    }

    // Representa a un cliente de la clínica y almacena sus mascotas.
    static class Cliente {
        private final int id;
        private String nombre;
        private String telefono;
        private final List<Mascota> mascotas = new ArrayList<>();

        Cliente(int id, String nombre, String telefono) {
            this.id = id;
            this.nombre = nombre;
            this.telefono = telefono;
        }

        int getId() {
            return id;
        }

        String getNombre() {
            return nombre;
        }

        void setNombre(String nombre) {
            this.nombre = nombre;
        }

        String getTelefono() {
            return telefono;
        }

        void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        List<Mascota> getMascotas() {
            return mascotas;
        }

        void agregarMascota(Mascota mascota) {
            mascotas.add(mascota);
        }

        boolean coincide(String termino) {
            String nombreLower = nombre.toLowerCase(Locale.ROOT);
            String telefonoLower = telefono.toLowerCase(Locale.ROOT);
            return nombreLower.contains(termino) || telefonoLower.contains(termino);
        }

        String resumen() {
            return "#" + id + " - " + nombre + " | Tel: " + telefono;
        }
    }

    // Clase base para modelar información compartida entre mascotas.
    static abstract class Mascota {
        private final String nombre;
        private final int edad;

        Mascota(String nombre, int edad) {
            this.nombre = nombre;
            this.edad = edad;
        }

        String getNombre() {
            return nombre;
        }

        int getEdad() {
            return edad;
        }

        abstract String getTipo();

        protected abstract String detalle();

        String descripcion() {
            String base = getTipo() + ": " + nombre + " (" + edad + " anios)";
            String extra = detalle();
            return extra.isEmpty() ? base : base + " - " + extra;
        }
    }

    // Especialización para perros con su campo adicional de raza.
    static class Perro extends Mascota {
        private final String raza;

        Perro(String nombre, int edad, String raza) {
            super(nombre, edad);
            this.raza = raza;
        }

        @Override
        String getTipo() {
            return "Perro";
        }

        @Override
        protected String detalle() {
            return raza.isEmpty() ? "" : "Raza: " + raza;
        }
    }

    // Especialización para gatos con su campo adicional de color.
    static class Gato extends Mascota {
        private final String color;

        Gato(String nombre, int edad, String color) {
            super(nombre, edad);
            this.color = color;
        }

        @Override
        String getTipo() {
            return "Gato";
        }

        @Override
        protected String detalle() {
            return color.isEmpty() ? "" : "Color: " + color;
        }
    }
}
