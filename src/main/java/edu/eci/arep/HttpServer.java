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
                String response = handleApiRequest(filePath, method, in);
                out.println(response);
            } else {
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

    public static String handleApiRequest(String path, String method, BufferedReader in) {
        if (path.startsWith("/app/hello")) {
            String name = "Unknown";

            if (method.equals("GET")) {
                name = extractQueryParam(path, "name");
            } else if (method.equals("POST")) {
                name = extractNameFromJson(in);
            }
            if (name == null) {
                name = "Unknown";
            }
            return createJsonResponse(200, "{\"message\": \"Hello, " + name + "!\"}");

        } else if (path.startsWith("/app/PI")) {
            return createJsonResponse(200, "{\"value\": " + Math.PI + "}");

        } else if (path.startsWith("/app/square")) {
            String numberStr = extractQueryParam(path, "number");
            if (numberStr != null) {
                try {
                    int number = Integer.parseInt(numberStr);
                    return createJsonResponse(200, "{\"number\": " + number + ", \"square\": " + (number * number) + "}");
                } catch (NumberFormatException e) {
                    return createJsonResponse(400, "{\"error\": \"Invalid number format\"}");
                }
            }
            return createJsonResponse(400, "{\"error\": \"Missing number parameter\"}");
        }

        return createJsonResponse(404, "{\"error\": \"API endpoint not found\"}");
    }

    static String extractQueryParam(String path, String key) {
        int queryIndex = path.indexOf("?");
        if (queryIndex != -1 && queryIndex < path.length() - 1) {
            String[] params = path.substring(queryIndex + 1).split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length > 1 && keyValue[0].equals(key)) {
                    return keyValue[1];
                }
            }
        }
        return null;
    }

    static String extractNameFromJson(BufferedReader in) {
        try {
            StringBuilder body = new StringBuilder();
            String line;
            while (in.ready() && (line = in.readLine()) != null) {
                body.append(line);
            }

            String bodyStr = body.toString();
            int nameIndex = bodyStr.indexOf("\"name\":");
            if (nameIndex != -1) {
                int start = bodyStr.indexOf("\"", nameIndex + 7) + 1;
                int end = bodyStr.indexOf("\"", start);
                if (start != -1 && end != -1) {
                    return bodyStr.substring(start, end);
                }
            }
        } catch (IOException e) {
            return "Error reading request body";
        }
        return "Unknown";
    }

    static String createJsonResponse(int statusCode, String jsonBody) {
        String statusMessage = switch (statusCode) {
            case 200 -> "200 OK";
            case 400 -> "400 Bad Request";
            case 404 -> "404 Not Found";
            case 500 -> "500 Internal Server Error";
            default -> statusCode + " Unknown";
        };

        return "HTTP/1.1 " + statusMessage + "\r\n" +
                "Content-Type: application/json\r\n" +
                "\r\n" +
                jsonBody;
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

