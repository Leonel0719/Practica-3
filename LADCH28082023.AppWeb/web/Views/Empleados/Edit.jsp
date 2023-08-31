<%@page import="ladch.entidades.Empleados"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%Empleados empleado = (Empleados) request.getAttribute("empleado");%>  
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Editar</title>
    </head>
     <body>
        <h1>Editar</h1>
        <form action="Empleados" method="post">
            <input type="hidden" name="action" value="Edit">
            <input type="hidden" name="id" value="<%=empleado.getId()%>">
            Nombre: <input type="text" name="nombre" value="<%=empleado.getNombre()%>" required><br>
            Apellido: <input type="text" name="apellido" value="<%=empleado.getApellido()%>" required><br>
            Correo: <input type="text" name="correo" value="<%=empleado.getCorreo()%>" required><br>
            Puesto: <input type="text" name="puesto" value="<%=empleado.getPuesto()%>" required><br>
            <input type="submit" value="Guardar ">
        </form>
        <a href="Empleados">Lista de Empleados</a>
     </body>
</html>
