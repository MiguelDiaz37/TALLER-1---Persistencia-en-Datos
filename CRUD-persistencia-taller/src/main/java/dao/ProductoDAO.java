package dao;

import modelo.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Operaciones CRUD de la tabla productos
public class ProductoDAO {

    public void insertar(Producto p) throws SQLException {
        String sql = "INSERT INTO productos VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getDescripcion());
            ps.setDouble(4, p.getPrecio());
            ps.setInt(5, p.getCantidad());
            ps.executeUpdate();
        }
    }

    public List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY codigo";
        try (Connection con = ConexionDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(crearProducto(rs));
            }
        }
        return lista;
    }

    public Producto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return crearProducto(rs);
            }
        }
        return null;
    }

    public List<Producto> buscarPorNombre(String nombre) throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearProducto(rs));
            }
        }
        return lista;
    }

    public void actualizar(Producto p) throws SQLException {
        String sql = "UPDATE productos SET descripcion = ?, precio = ?, cantidad = ? WHERE codigo = ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getDescripcion());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getCantidad());
            ps.setString(4, p.getCodigo());
            ps.executeUpdate();
        }
    }

    public void eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM productos WHERE codigo = ?";
        try (Connection con = ConexionDB.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        }
    }

    private Producto crearProducto(ResultSet rs) throws SQLException {
        return new Producto(rs.getString("codigo"), rs.getString("nombre"),
                rs.getString("descripcion"), rs.getDouble("precio"), rs.getInt("cantidad"));
    }
}
