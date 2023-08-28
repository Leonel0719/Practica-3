package ladch28082023.accesoadatos;

import java.sql.*;
import java.util.ArrayList;
import ladch.pkg28082023.entidades.Empleado;

/**
 * @author Leonel
 */
public class EmpleadoDAL {

    static String getFields() {
        return "r.Id, r.Nombre, r.Apellido, r.Correo, r.Puesto ";
    }

    private static String getSelect(Empleado xEmp) {
        String sql = "SELECT ";
        if (xEmp.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.SQLSERVER) {
            sql += "TOP " + xEmp.getTop_aux() + " ";
        }
        sql += (getFields() + " FROM Empleado r");
        return sql;
    }

    private static String addOrderBy(Empleado xEmp) {
        String sql = " ORDER BY r.Id DESC";
        if (xEmp.getTop_aux() > 0 && ComunDB.TIPODB == ComunDB.TipoDB.MYSQL) {
            sql += " LIMIT " + xEmp.getTop_aux() + " ";
        }
        return sql;
    }

    public static int create(Empleado xEmp) throws Exception {
        int result;
        String sql;
        try (Connection conn = ComunDB.obtenerConexion();) {
            sql = "INSERT INTO Empleado(Nombre, Apellido, Correo, Puesto) VALUES(?, ?, ?, ?)";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setString(1, xEmp.getNombre());
                ps.setString(2, xEmp.getApellido());
                ps.setString(3, xEmp.getCorreo());
                ps.setString(4, xEmp.getPuesto());
                result = ps.executeUpdate();
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return result;
    }

    static int asignarDatosResultSet(Empleado xEmp, ResultSet pResultSet, int pIndex) throws Exception {
        pIndex++;
        xEmp.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        xEmp.setNombre(pResultSet.getString(pIndex)); // index 2
        pIndex++;
        xEmp.setApellido(pResultSet.getString(pIndex)); // index 3
        pIndex++;
        xEmp.setCorreo(pResultSet.getString(pIndex)); // index 4
        pIndex++;
        xEmp.setPuesto(pResultSet.getString(pIndex)); // index 5
        return pIndex;
    }

    private static void getData(PreparedStatement pPS, ArrayList<Empleado> xEmp) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) {
            while (resultSet.next()) {
                Empleado xEmps = new Empleado();
                asignarDatosResultSet(xEmps, resultSet, 0);
                xEmp.add(xEmps);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public static Empleado getById(Empleado xEmp) throws Exception {
        Empleado xEmps = new Empleado();
        ArrayList<Empleado> xEmple = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = getSelect(xEmp);
            sql += " WHERE r.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, xEmp.getId());
                getData(ps, xEmple);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        if (xEmple.size() > 0) { // Verificar si el ArrayList de Rol trae mas de un registro en tal caso solo debe de traer uno
            xEmp = xEmple.get(0); // Si el ArrayList de Rol trae un registro o mas obtenemos solo el primero 
        }
        return xEmp;
    }

    public static ArrayList<Empleado> getAll() throws Exception {
        ArrayList<Empleado> xEmple = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = getSelect(new Empleado());
            sql += addOrderBy(new Empleado());
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                getData(ps, xEmple);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return xEmple;
    }

    static void querySelect(Empleado xEmp, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (xEmp.getId() > 0) {
            pUtilQuery.AgregarWhereAnd(" r.Id=? ");
            if (statement != null) {
                statement.setInt(pUtilQuery.getNumWhere(), xEmp.getId());
            }
        }
        if (xEmp.getNombre() != null && xEmp.getNombre().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.NombreLIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + xEmp.getNombre() + "%");
            }
        }
        if (xEmp.getApellido() != null && xEmp.getApellido().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.ApellidoLIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + xEmp.getApellido() + "%");
            }
        }
        if (xEmp.getCorreo() != null && xEmp.getCorreo().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.CorreoLIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + xEmp.getCorreo() + "%");
            }
        }
        if (xEmp.getPuesto() != null && xEmp.getPuesto().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.PuestoLIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + xEmp.getPuesto() + "%");
            }
        }
    }

    public static ArrayList<Empleado> Search(Empleado xEmp) throws Exception {
        ArrayList<Empleado> xEmps = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = getSelect(xEmp);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0);
            querySelect(xEmp, utilQuery);
            sql = utilQuery.getSQL();
            sql += addOrderBy(xEmp);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0);
                querySelect(xEmp, utilQuery);
                getData(ps, xEmps);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return xEmps;
    }
}
