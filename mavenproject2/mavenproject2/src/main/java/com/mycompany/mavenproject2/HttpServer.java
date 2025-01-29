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

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            System.out.println("Listo para recibir ...");

            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String inputLine;
                boolean isFirstLine = true;
                String requestedFile = "";

                while ((inputLine = in.readLine()) != null) {
                    if (isFirstLine) {
                        requestedFile = inputLine.split(" ")[1];
                        isFirstLine = false;
                    }
                    if (!in.ready()) {
                        break;
                    }
                }

                // Manejo de rutas
                if (requestedFile.startsWith("/app/hello")) {
                    String query = requestedFile.contains("?") ? requestedFile.split("\\?", 2)[1] : null;
                    String response = helloRestService(requestedFile, query);
                    out.println(response);
                } else if (requestedFile.equals("/")) {
                    out.println(returnIndex());
                } else {
                    serveFile(out, requestedFile);
                }

            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }

        serverSocket.close();
    }

    private static void serveFile(PrintWriter out, String filePath) {
        String baseDir = System.getProperty("user.dir") + "/src/main/resources"; // Cambia según tu estructura
        File file = new File(baseDir, filePath);

        if (!file.exists() || file.isDirectory()) {
            out.println("HTTP/1.1 404 Not Found\r\n");
            out.println("Content-Type: text/html\r\n\r\n");
            out.println("<h1>404 Not Found</h1>");
            return;
        }

        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            String contentType = getContentType(filePath);
            out.println("HTTP/1.1 200 OK\r\n");
            out.println("Content-Type: " + contentType + "\r\n\r\n");

            String line;
            while ((line = fileReader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException e) {
            out.println("HTTP/1.1 500 Internal Server Error\r\n\r\n");
            out.println("<h1>500 Internal Server Error</h1>");
        }
    }

    private static String returnIndex() {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>File Browser</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "        <script>\n"
                + "            function loadFiles() {\n"
                + "                fetch('/app/files')\n"
                + "                .then(response => response.json())\n"
                + "                .then(files => {\n"
                + "                    let fileList = document.getElementById('file-list');\n"
                + "                    fileList.innerHTML = '';\n"
                + "                    files.forEach(file => {\n"
                + "                        let button = document.createElement('button');\n"
                + "                        button.textContent = file;\n"
                + "                        button.onclick = () => loadFileContent(file);\n"
                + "                        fileList.appendChild(button);\n"
                + "                    });\n"
                + "                });\n"
                + "            }\n"
                + "            function loadFileContent(fileName) {\n"
                + "                fetch('/app/files/' + fileName)\n"
                + "                .then(response => response.text())\n"
                + "                .then(content => {\n"
                + "                    document.getElementById('file-content').textContent = content;\n"
                + "                });\n"
                + "            }\n"
                + "        </script>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>File Browser</h1>\n"
                + "        <button onclick=\"loadFiles()\">Cargar Archivos</button>\n"
                + "        <div id=\"file-list\"></div>\n"
                + "        <h2>Contenido del Archivo:</h2>\n"
                + "        <pre id=\"file-content\"></pre>\n"
                + "    </body>\n"
                + "</html>";
    }
    
    private static String getContentType(String filePath) {
        if (filePath.endsWith(".html") || filePath.endsWith(".htm")) {
            return "text/html";
        } else if (filePath.endsWith(".css")) {
            return "text/css";
        } else if (filePath.endsWith(".js")) {
            return "application/javascript";
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filePath.endsWith(".png")) {
            return "image/png";
        } else if (filePath.endsWith(".gif")) {
            return "image/gif";
        } else {
            return "application/octet-stream";
        }
    }

    
    private static String helloRestService(String path, String query) {
        String name = "Guest";
        if (query != null && query.startsWith("name=")) {
            name = query.split("=", 2)[1];
        }

        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n\r\n"
                + "{\"message\":\"Hello, " + name + "!\"}";
    }
}
