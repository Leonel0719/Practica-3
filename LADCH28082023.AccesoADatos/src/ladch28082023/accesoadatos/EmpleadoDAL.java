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
    
    private static String getSelect(Empleado xEmp){
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
            sql = "INSERT INTO Empleado(Title, Author, Year) VALUES(?, ?, ?)";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setString(3, book.getYear());
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
    
    static int asignarDatosResultSet(Book book, ResultSet pResultSet, int pIndex) throws Exception {
        pIndex++;
        book.setId(pResultSet.getInt(pIndex)); // index 1
        pIndex++;
        book.setTitle(pResultSet.getString(pIndex)); // index 2
        pIndex++;
        book.setAuthor(pResultSet.getString(pIndex)); // index 3
        pIndex++;
        book.setYear(pResultSet.getString(pIndex)); // index 4
        return pIndex;
    }
    
    private static void getData(PreparedStatement pPS, ArrayList<Book> book) throws Exception {
        try (ResultSet resultSet = ComunDB.obtenerResultSet(pPS);) {
            while (resultSet.next()) { 
                Book books = new Book(); 
                asignarDatosResultSet(books, resultSet, 0);
                book.add(books);
            }
            resultSet.close();
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public static Book getById(Book book) throws Exception {
        Book books = new Book();
        ArrayList<Book> xBook = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = getSelect(book);
            sql += " WHERE r.Id=?";
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                ps.setInt(1, book.getId());
                getData(ps, xBook);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        if (xBook.size() > 0) { // Verificar si el ArrayList de Rol trae mas de un registro en tal caso solo debe de traer uno
            books = xBook.get(0); // Si el ArrayList de Rol trae un registro o mas obtenemos solo el primero 
        }
        return book;
    }

    public static ArrayList<Book> getAll() throws Exception {
        ArrayList<Book> books = new ArrayList<>();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = getSelect(new Book());
            sql += addOrderBy(new Book());
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                getData(ps, books);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        } 
        catch (SQLException ex) {
            throw ex;
        }
        return books;
    }
    
    static void querySelect(Book book, ComunDB.UtilQuery pUtilQuery) throws SQLException {
        PreparedStatement statement = pUtilQuery.getStatement();
        if (book.getId() > 0) {
            pUtilQuery.AgregarWhereAnd(" r.Id=? ");
            if (statement != null) {
                statement.setInt(pUtilQuery.getNumWhere(), book.getId()); 
            }
        } if (book.getTitle() != null && book.getTitle().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.TitleLIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + book.getTitle() + "%"); 
            }
        } if (book.getAuthor() != null && book.getAuthor().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.AuthorLIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + book.getAuthor() + "%"); 
            }
        } if (book.getYear() != null && book.getYear().trim().isEmpty() == false) {
            pUtilQuery.AgregarWhereAnd(" r.YearLIKE ? ");
            if (statement != null) {
                statement.setString(pUtilQuery.getNumWhere(), "%" + book.getYear() + "%"); 
            }
        }
    }

    public static ArrayList<Book> Search(Book book) throws Exception {
        ArrayList<Book> books = new ArrayList();
        try (Connection conn = ComunDB.obtenerConexion();) {
            String sql = getSelect(book);
            ComunDB comundb = new ComunDB();
            ComunDB.UtilQuery utilQuery = comundb.new UtilQuery(sql, null, 0); 
            querySelect(book, utilQuery);
            sql = utilQuery.getSQL(); 
            sql += addOrderBy(book);
            try (PreparedStatement ps = ComunDB.createPreparedStatement(conn, sql);) {
                utilQuery.setStatement(ps);
                utilQuery.setSQL(null);
                utilQuery.setNumWhere(0); 
                querySelect(book, utilQuery);
                getData(ps, books);
                ps.close();
            } catch (SQLException ex) {
                throw ex;
            }
            conn.close();
        }
        catch (SQLException ex) {
            throw ex;
        }
        return books;
    }
}
