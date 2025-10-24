import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class ClinicaApp {

    private final List<Cliente> clientes = new ArrayList<>();
    private int siguienteIdCliente = 1;

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
                    case 10 -> facturas();
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
        Facturas factura = new Facturas("Jose", "555-1234", "Firulais", "2024-06-15", 100.0);
        JFfractura jfFactura = new JFfractura();
        
        factura.mostrarFactura();
        jfFactura.setVisible(true);
        jfFactura.setLocationRelativeTo(null);
        jfFactura.getFactura(factura);
    }

    private void mostrarMenu() {
        System.out.println("=== Clinica Veterinaria ===");
        System.out.println("1. Crear cliente");
        System.out.println("2. Editar cliente");
        System.out.println("3. Buscar cliente");
        System.out.println("4. Registrar mascota");
        System.out.println("5. Listar clientes");
        System.out.println("0. Salir");
    }

    private void crearCliente(Scanner scanner) {
        System.out.println("--- Crear cliente ---");
        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        String telefono = leerTextoObligatorio(scanner, "Telefono: ");
        Cliente cliente = new Cliente(siguienteIdCliente++, nombre, telefono);
        clientes.add(cliente);
        System.out.println("Cliente creado con ID " + cliente.getId());
    }

    private void editarCliente(Scanner scanner) {
        if (clientes.isEmpty()) {
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

    private void buscarCliente(Scanner scanner) {
        if (clientes.isEmpty()) {
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
        for (Cliente cliente : clientes) {
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

    private void registrarMascota(Scanner scanner) {
        if (clientes.isEmpty()) {
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

    private Mascota crearPerro(Scanner scanner) {
        System.out.println("--- Registrar perro ---");
        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        int edad = leerEnteroNoNegativo(scanner, "Edad (años): ");
        System.out.print("Raza (opcional): ");
        String raza = scanner.nextLine().trim();
        return new Perro(nombre, edad, raza);
    }

    private Mascota crearGato(Scanner scanner) {
        System.out.println("--- Registrar gato ---");
        String nombre = leerTextoObligatorio(scanner, "Nombre: ");
        int edad = leerEnteroNoNegativo(scanner, "Edad (años): ");
        System.out.print("Color (opcional): ");
        String color = scanner.nextLine().trim();
        return new Gato(nombre, edad, color);
    }

    private void listarClientes() {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        System.out.println("--- Clientes ---");
        for (Cliente cliente : clientes) {
            System.out.println(cliente.resumen());
        }
    }

    private Cliente encontrarClientePorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

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

    private static class Cliente {
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

    private static abstract class Mascota {
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

    private static class Perro extends Mascota {
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

    private static class Gato extends Mascota {
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
