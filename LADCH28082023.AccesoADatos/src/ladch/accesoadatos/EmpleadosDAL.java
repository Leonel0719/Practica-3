/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ladch.accesoadatos;
import java.sql.*;
import java.util.ArrayList;
import ladch.entidades.Empleados;
/**
 * @author Leonel
 */
public class EmpleadosDAL {
    
    public static ArrayList<Empleados> listarEmpleados() throws Exception {
        ArrayList<Empleados> empleados = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion()) {
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM Empleado")) {

                while (resultSet.next()) {
                    int Id = resultSet.getInt("Id");
                    String Nombre = resultSet.getString("Nombre");
                    String Apellido = resultSet.getString("Apellido");
                    String Correo = resultSet.getString("Correo");
                    String Puesto = resultSet.getString("Puesto");

                    Empleados empleado = new Empleados(Id, Nombre, Apellido, Correo, Puesto);
                    empleados.add(empleado);
                }
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return empleados;
    }

    public static int agregarEmpleados(Empleados Empleados) throws SQLException {
        int result = 0;
        try (Connection conn = ComunDB.obtenerConexion(); PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Empleado (Nombre,Apellido, Correo, Puesto) VALUES (?, ?, ?, ?)")) {

            statement.setString(1, Empleados.getNombre());
            statement.setString(2, Empleados.getApellido());
            statement.setString(3, Empleados.getCorreo());
            statement.setString(4, Empleados.getPuesto());

            result = statement.executeUpdate();
        }
        return result;
    }

    public static int editarEmpleados(Empleados Empleados) throws SQLException {
        int result = 0;
        try (Connection conn = ComunDB.obtenerConexion(); PreparedStatement statement = conn.prepareStatement(
                "UPDATE Empleado SET Nombre = ?, Apellido = ?, Correo = ?, Puesto = ? WHERE Id = ?")) {

            statement.setString(1, Empleados.getNombre());
            statement.setString(2, Empleados.getApellido());
            statement.setString(3, Empleados.getCorreo());
            statement.setString(4, Empleados.getPuesto());
            statement.setInt(5, Empleados.getId());

            result = statement.executeUpdate();
        }
        return result;
    }

    public static Empleados obtenerEmpleadoPorId(int empleadoId) throws Exception {
        Empleados empleado = null;
        String query = "SELECT * FROM Empleado WHERE Id = ?";

        try (Connection conn = ComunDB.obtenerConexion(); PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, empleadoId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("Id");
                    String nombre = resultSet.getString("Nombre");
                    String apellido = resultSet.getString("Apellido");
                    String correo = resultSet.getString("Correo");
                    String puesto = resultSet.getString("Puesto");

                    empleado = new Empleados(id, nombre, apellido, correo, puesto);
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Error al obtener el empleado por ID", ex);
        }
        return empleado;
    }
    
}
