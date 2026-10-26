package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:productos.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Crea la tabla la primera vez que se ejecuta el programa
    public static void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS productos ("
                + "codigo TEXT PRIMARY KEY, "
                + "nombre TEXT NOT NULL, "
                + "descripcion TEXT, "
                + "precio REAL NOT NULL CHECK (precio >= 0), "
                + "cantidad INTEGER NOT NULL CHECK (cantidad >= 0))";
        try (Connection con = conectar(); Statement st = con.createStatement()) {
            st.execute(sql);
        }
    }
}
