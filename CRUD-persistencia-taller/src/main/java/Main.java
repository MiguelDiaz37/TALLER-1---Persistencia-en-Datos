import dao.ConexionDB;
import dao.ProductoDAO;
import modelo.Producto;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ProductoDAO dao = new ProductoDAO();

    public static void main(String[] args) {
        try {
            ConexionDB.crearTabla();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            return;
        }

        int opcion;
        do {
            System.out.println("\n--- CRUD PERSISTENCIA TALLER ---");
            System.out.println("1. Registrar producto");
            System.out.println("2. Ver todos los productos");
            System.out.println("3. Buscar producto");
            System.out.println("4. Actualizar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("0. Salir");
            opcion = leerEntero("Opcion: ");

            try {
                switch (opcion) {
                    case 1 -> registrar();
                    case 2 -> mostrar(dao.listar());
                    case 3 -> buscar();
                    case 4 -> actualizar();
                    case 5 -> eliminar();
                    case 0 -> System.out.println("Saliendo...");
                    default -> System.out.println("Opcion no valida");
                }
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    static void registrar() throws SQLException {
        String codigo = leerTexto("Codigo: ").toUpperCase();
        if (dao.buscarPorCodigo(codigo) != null) {
            System.out.println("Ya existe un producto con ese codigo");
            return;
        }
        String nombre = leerTexto("Nombre: ");
        System.out.print("Descripcion: ");
        String descripcion = sc.nextLine();
        double precio = leerPrecio("Precio: ");
        int cantidad = leerEntero("Cantidad: ");

        dao.insertar(new Producto(codigo, nombre, descripcion, precio, cantidad));
        System.out.println("Producto registrado");
    }

    static void buscar() throws SQLException {
        System.out.print("Buscar por 1) codigo o 2) nombre: ");
        String tipo = sc.nextLine();
        if (tipo.equals("1")) {
            Producto p = dao.buscarPorCodigo(leerTexto("Codigo: ").toUpperCase());
            if (p == null) {
                System.out.println("No se encontro el producto");
            } else {
                System.out.println(p);
            }
        } else if (tipo.equals("2")) {
            mostrar(dao.buscarPorNombre(leerTexto("Nombre: ")));
        } else {
            System.out.println("Opcion no valida");
        }
    }

    static void actualizar() throws SQLException {
        Producto p = dao.buscarPorCodigo(leerTexto("Codigo del producto: ").toUpperCase());
        if (p == null) {
            System.out.println("No se encontro el producto");
            return;
        }
        System.out.println(p);
        System.out.println("(Deje vacio para no cambiar)");

        System.out.print("Nueva descripcion: ");
        String desc = sc.nextLine();
        if (!desc.isBlank()) p.setDescripcion(desc);

        System.out.print("Nuevo precio: ");
        String precio = sc.nextLine();
        if (!precio.isBlank()) {
            Double nuevoPrecio = validarPrecio(precio);
            if (nuevoPrecio != null) p.setPrecio(nuevoPrecio);
        }

        System.out.print("Nueva cantidad: ");
        String cantidad = sc.nextLine();
        if (!cantidad.isBlank()) {
            Integer nuevaCantidad = validarCantidad(cantidad);
            if (nuevaCantidad != null) p.setCantidad(nuevaCantidad);
        }

        dao.actualizar(p);
        System.out.println("Producto actualizado");
    }

    static void eliminar() throws SQLException {
        String codigo = leerTexto("Codigo del producto: ").toUpperCase();
        Producto p = dao.buscarPorCodigo(codigo);
        if (p == null) {
            System.out.println("No se encontro el producto");
            return;
        }
        System.out.println(p);
        System.out.print("Seguro que desea eliminarlo? (S/N): ");
        if (sc.nextLine().equalsIgnoreCase("S")) {
            dao.eliminar(codigo);
            System.out.println("Producto eliminado");
        } else {
            System.out.println("Cancelado");
        }
    }

    static void mostrar(List<Producto> lista) {
        if (lista.isEmpty()) {
            System.out.println("No hay productos");
        }
        for (Producto p : lista) {
            System.out.println(p);
        }
    }

    // ---- Lectura y validacion de datos ----

    static String leerTexto(String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = sc.nextLine().trim();
            if (texto.isEmpty()) System.out.println("El campo no puede estar vacio");
        } while (texto.isEmpty());
        return texto;
    }

    static double leerPrecio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            Double precio = validarPrecio(sc.nextLine());
            if (precio != null) return precio;
        }
    }

    static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            Integer numero = validarCantidad(sc.nextLine());
            if (numero != null) return numero;
        }
    }

    static Double validarPrecio(String texto) {
        try {
            double precio = Double.parseDouble(texto.trim());
            if (precio < 0) {
                System.out.println("El precio no puede ser negativo");
                return null;
            }
            return precio;
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un numero valido");
            return null;
        }
    }

    static Integer validarCantidad(String texto) {
        try {
            int numero = Integer.parseInt(texto.trim());
            if (numero < 0) {
                System.out.println("No puede ser negativo");
                return null;
            }
            return numero;
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un numero entero");
            return null;
        }
    }
}
