<%-- 
    Document   : Listar
    Created on : 28 ago. 2023, 11:03:00
    Author     : kevin
--%>

<%@page import="ladch.entidades.Empleados"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%ArrayList<Empleados> empleados = (ArrayList<Empleados>) request.getAttribute("empleados");%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lista de Empleados</title>
    </head>
    <body>
        <h1>Lista de Empleaados</h1>
         <table border="1">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>Correo</th>
                    <th>Puesto</th>
                    <th>Editar</th>
                   
                </tr>
            </thead>
            <tbody>
                <%

                    for (Empleados empleado : empleados) {
                %>
                <tr>
                    <td><%=empleado.getNombre()%></td>
                    <td><%=empleado.getApellido()%></td>
                    <td><%=empleado.getCorreo()%></td>
                     <td><%=empleado.getPuesto()%></td>
 <th><a href="Empleados?action=Edit&Id=<%=empleado.getId() %>">Editar</a></th>
                </tr>
                <%}%>
            </tbody>
        </table>
        <br>


        <a href="Empleados?action=Add">Agregar Empleado</a>
    </body>
</html>