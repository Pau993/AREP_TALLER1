/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

/**
 *
 * @author paula.paez
 */
import java.net.*;
import java.io.*;

public class HttpServer {

    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            //Escuchar o intenetar conectarse al 35000
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        //Recibe las peticiones que haga al localhost
        boolean running = true;
        Socket clientSocket = null;
        while (running) {
            
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept(); //Si hay conexión devuelve el socket
            } catch (IOException e) {
                System.err.println("Accept failed." + e.getMessage());
                System.exit(1);
            }

            //Los convierte en binario para luego traducirlo a String

            
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println(out); //Envia al Browser

            //Almacena caracteres en memoria

            //Hasta que deja de recibir caracteres que deja de leer, comunicaciones en la que no se sabe cuando dejar de recibir datos
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;

            boolean isFirstLine = true;
            String file = "";

            //Imprimir linea por linea los mensajes, imprimir el mensaje que me inyectó
            while ((inputLine = in.readLine()) != null) {

                if (isFirstLine) {
                    file = inputLine.split(" ")[1]; //Segunda linea de la cadena
                    isFirstLine = false;
                }

                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            //URI más básico que la url, no establece el protocolo con el que se está hablando, tiene un componente cercano al URL
            //Todas las URL son URI, pero no todas las URI son URL
            URI requestedFile = new URI(file);
            System.out.println("file: " + requestedFile);
            

            if (requestedFile.getPath().startsWith("/app/hello")) {
                System.out.println("---------------------------" + requestedFile.getPath() + "----------------" + requestedFile.getQuery());
                outputLine = helloRestService(requestedFile.getPath(), requestedFile.getQuery());
                outputLine = helloRestService(requestedFile.getPath(), requestedFile.readFile());
                out.println(outputLine);

            
            } else {
                //La primera linea del mensaje viene la dirección
                outputLine = returnIndex();
                out.println(outputLine);
            }
            out.close();
            in.close();
        }
            clientSocket.close();
            serverSocket.close();
        
    }

    private static String returnIndex(){
        return "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/html\r\n"
                        + "\r\n"
                        + "<!DOCTYPE html>\n"
                        + "<html>\n"
                        + "    <head>\n"
                        + "        <title>Form Example</title>\n"
                        + "        <meta charset=\"UTF-8\">\n"
                        + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    </head>\n"
                        + "    <body>\n"
                        + "        <h1>Form with GET</h1>\n"
                        + "        <form action=\"/hello\">\n"
                        + "            <label for=\"name\">Name:</label><br>\n"
                        + "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n"
                        + "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n"
                        + "        </form> \n"
                        + "        <div id=\"getrespmsg\"></div>\n"
                        + "\n"
                        + "        <script>\n"
                        + "            function loadGetMsg() {\n"
                        + "                let nameVar = document.getElementById(\"name\").value;\n"
                        + "                const xhttp = new XMLHttpRequest();\n"
                        + "                xhttp.onload = function() {\n"
                        + "                    document.getElementById(\"getrespmsg\").innerHTML =\n"
                        + "                    this.responseText;\n"
                        + "                }\n"
                        + "                xhttp.open(\"GET\", \"/app/hello?name=\"+nameVar);\n"
                        + "                xhttp.send();\n"
                        + "            }\n"
                        + "        </script>\n"
                        + "\n"
                        + "        <h1>Form with POST</h1>\n"
                        + "        <form action=\"/hellopost\">\n"
                        + "            <label for=\"postname\">Name:</label><br>\n"
                        + "            <input type=\"text\" id=\"postname\" name=\"name\" value=\"John\"><br><br>\n"
                        + "            <input type=\"button\" value=\"Submit\" onclick=\"loadPostMsg(postname)\">\n"
                        + "        </form>\n"
                        + "        \n"
                        + "        <div id=\"postrespmsg\"></div>\n"
                        + "        \n"
                        + "        <script>\n"
                        + "            function loadPostMsg(name){\n"
                        + "                let url = \"/hellopost?name=\" + name.value;\n"
                        + "\n"
                        + "                fetch (url, {method: 'POST'})\n"
                        + "                    .then(x => x.text())\n"
                        + "                    .then(y => document.getElementById(\"postrespmsg\").innerHTML = y);\n"
                        + "            }\n"
                        + "        </script>\n"
                        + "    </body>\n"
                        + "</html>";
    }

    private static File readFile(String file){
        String carpeta = System.getProperty("user.dir");
        File carpetaLeer = new File(carpeta + "mavenproject2\\src\\main\\java\\com\\mycompany\\mavenproject2\\Files\\" + file);
        return carpetaLeer;
        
    }

    //La llave nos indica que nos atraiga el valor de donde está corriendo el archivo

    private static String helloRestService(String path, String query) {
        String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: aplication/json\r\n"
                + "\r\n"
                + "{\"name\":\"John\", \"age\":30, \"car\":" + System.getProperty("user.dir") + "}";
        return response;
    }
}
