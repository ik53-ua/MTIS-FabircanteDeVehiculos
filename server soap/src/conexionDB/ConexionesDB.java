package conexionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionesDB {
    private static final String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
    // URL con tu BD y parámetros de compatibilidad
    private static final String URL = "jdbc:mysql://localhost:3306/FabricanteVehiculosDB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root";
    private static final String CLAVE = ""; // Sin contraseña

    public Connection conectar() {
        Connection conec = null;
        try {
            Class.forName(CONTROLADOR);
            conec = DriverManager.getConnection(URL, USUARIO, CLAVE);
            System.out.println("Conexión establecida con FabricanteVehiculosDB");
        } catch (SQLException e) {
            System.out.println("Error de SQL: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el Driver MySQL");
        }
        return conec;
    }
}