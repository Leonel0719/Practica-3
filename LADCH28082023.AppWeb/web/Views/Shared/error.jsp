<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>        
        <title>Error de la aplicación</title>

    </head>
    <body>
        <main class="container"> 
            <div class="row">
                <div class="col l12 s12">
                    <h4>Succedio el siguiente error en la aplicación</h4> 
                    <span style="color: red"><%= request.getAttribute("error") %></span> 
                </div>
            </div>            
        </main>    
    </body>
</html>