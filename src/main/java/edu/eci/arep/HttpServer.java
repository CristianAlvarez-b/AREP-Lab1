package edu.eci.arep;
import java.net.*;
import java.io.*;
import java.nio.file.*;

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
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, filePath = "", method = "";
            boolean isFirstLine = true;

            while ((inputLine = in.readLine()) != null) {
                if (isFirstLine) {
                    String[] requestParts = inputLine.split(" ");
                    method = requestParts[0];
                    filePath = requestParts[1];
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            if (filePath.startsWith("/app")) {
                // Handle API requests
                String response = handleApiRequest(filePath, method);
                out.println(response);
            } else {
                // Handle static file requests
                handleStaticFileRequest(filePath, out, clientSocket);
            }

            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    static void handleStaticFileRequest(String filePath, PrintWriter out, Socket clientSocket) {
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }

        String basePath = "public";
        File file = new File(basePath + filePath);

        if (file.exists() && !file.isDirectory()) {
            try {
                String mimeType = getMimeType(filePath);
                byte[] fileContent = Files.readAllBytes(file.toPath());

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: " + mimeType);
                out.println("Content-Length: " + fileContent.length);
                out.println();
                out.flush();
                clientSocket.getOutputStream().write(fileContent);
                clientSocket.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<html><body><h1>404 Not Found</h1></body></html>");
        }
    }

    static String handleApiRequest(String path, String method) {
        if (path.startsWith("/app/hello")) {
            String name = "Unknown";
            int queryIndex = path.indexOf("?");

            if (queryIndex != -1 && queryIndex < path.length() - 1) {
                String[] params = path.substring(queryIndex + 1).split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue[0].equals("name") && keyValue.length > 1) {
                        name = keyValue[1];
                    }
                }
            }

            return "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    "{\"message\": \"Hello, " + name + "!\"}";
        } else {
            return "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: application/json\r\n" +
                    "\r\n" +
                    "{\"error\": \"API endpoint not found\"}";
        }
    }

    static String getMimeType(String filePath) {
        if (filePath.endsWith(".html")) return "text/html";
        if (filePath.endsWith(".css")) return "text/css";
        if (filePath.endsWith(".js")) return "application/javascript";
        if (filePath.endsWith(".png")) return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
        if (filePath.endsWith(".gif")) return "image/gif";
        return "application/octet-stream";
    }
}

