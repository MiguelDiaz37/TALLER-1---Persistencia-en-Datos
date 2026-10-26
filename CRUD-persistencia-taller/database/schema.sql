-- Tabla de productos (SQLite)

CREATE TABLE IF NOT EXISTS productos (
    codigo      TEXT PRIMARY KEY,
    nombre      TEXT NOT NULL,
    descripcion TEXT,
    precio      REAL NOT NULL CHECK (precio >= 0),
    cantidad    INTEGER NOT NULL CHECK (cantidad >= 0)
);

-- Datos de prueba
INSERT OR IGNORE INTO productos (codigo, nombre, descripcion, precio, cantidad) VALUES
    ('P001', 'Arroz', 'Bolsa de arroz blanco 1 kg', 4500, 30),
    ('P002', 'Leche', 'Leche entera 1 L', 3800, 24),
    ('P003', 'Café', 'Café molido 500 g', 15900, 12);
