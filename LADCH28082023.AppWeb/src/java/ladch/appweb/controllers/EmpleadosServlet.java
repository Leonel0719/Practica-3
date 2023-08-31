/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package ladch.appweb.controllers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import ladch.accesoadatos.EmpleadosDAL;
import ladch.entidades.Empleados;
import java.sql.*;
import java.util.List;

/**
 * @author Leonel
 */
@WebServlet(name = "EmpleadosServlet", urlPatterns = {"/Empleados"})
public class EmpleadosServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        //Si queremos agregar dirigimos a la vista de agregar libro
        if ("Add".equals(action)) {
            // QUIERO QUE ME REDIRIGA A LA VISTA DE AGREGAR NUEVO LIBRO
            request.getRequestDispatcher("Views/Empleados/Add.jsp").forward(request, response);
        } else if ("Edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("Id")); // Usa "Id" en lugar de "id"
            try {
                Empleados empleado = EmpleadosDAL.obtenerEmpleadoPorId(id);
                request.setAttribute("empleado", empleado);

                request.getRequestDispatcher("Views/Empleados/Edit.jsp").forward(request, response);

            } catch (Exception ex) {
                Logger.getLogger(EmpleadosServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                // Aca se obtiene los libros
                List<Empleados> Empleados = EmpleadosDAL.listarEmpleados();
                request.setAttribute("empleados", Empleados);

                request.getRequestDispatcher("Views/Empleados/Listar.jsp").forward(request, response);
            } catch (Exception ex) {
                Logger.getLogger(EmpleadosServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("Add".equals(action)) {

            String Nombre = request.getParameter("nombre");
            String Apellido = request.getParameter("apellido");
            String Correo = request.getParameter("correo");
            String Puesto = request.getParameter("puesto");

            Empleados nuevoEmpleado = new Empleados(0, Nombre, Apellido, Correo, Puesto);

            try {
                int result = EmpleadosDAL.agregarEmpleados(nuevoEmpleado);

                if (result > 0) {
                    response.sendRedirect("Empleados");
                }

            } catch (SQLException e) {

                e.printStackTrace();
                response.getWriter().println("An error occurred.");
            }
        } else if ("Edit".equals(action)) {
            int Id = Integer.parseInt(request.getParameter("id"));
            String Nombre = request.getParameter("nombre");
            String Apellido = request.getParameter("apellido");
            String Correo = request.getParameter("correo");
            String Puesto = request.getParameter("puesto");

            Empleados Actualizar = new Empleados(Id, Nombre, Apellido, Correo, Puesto);

            try {
                int result = EmpleadosDAL.editarEmpleados(Actualizar);
                if (result > 0) {
                    response.sendRedirect("Empleados");
                }
            } catch (SQLException e) {

                e.printStackTrace();
                response.getWriter().println("An error occurred.");
            }
        }
    }// </editor-fold>
}
