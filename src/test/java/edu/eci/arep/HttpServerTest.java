package edu.eci.arep;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.*;
import java.net.Socket;

public class HttpServerTest {

    @Test
    public void testGetMimeTypeHtml() {
        String mimeType = HttpServer.getMimeType("/index.html");
        assertEquals("text/html", mimeType);
    }

    @Test
    public void testGetMimeTypeCss() {
        String mimeType = HttpServer.getMimeType("/style.css");
        assertEquals("text/css", mimeType);
    }

    @Test
    public void testHandleApiRequest() {
        // Probar una solicitud válida a la API
        String response = HttpServer.handleApiRequest("/app/hello?name=John", "GET");
        assertTrue(response.contains("Hello, John"));

        // Probar una solicitud inválida
        response = HttpServer.handleApiRequest("/app/unknown", "GET");
        assertTrue(response.contains("API endpoint not found"));
    }

    @Test
    public void testHandleStaticFileRequest() throws IOException {
        // Crear un mock de Socket
        Socket mockSocket = mock(Socket.class);
        OutputStream mockOutputStream = mock(OutputStream.class);

        // Hacer que el mock del socket devuelva el mock del OutputStream
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        // Simular el comportamiento del método
        String filePath = "/index.html";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(outputStream);

        // Llamar al método con el Socket simulado
        HttpServer.handleStaticFileRequest(filePath, out, mockSocket);
        // Verificar la salida
        String output = outputStream.toString();
        assertFalse(output.contains("HTTP/1.1 200 OK"));
        assertFalse(output.contains("Content-Type: text/html"));
    }

    @Test
    public void testHandleApiRequestNoName() {
        // Prueba con un nombre no pasado en la URL
        String response = HttpServer.handleApiRequest("/app/hello", "GET");
        assertTrue(response.contains("Hello, Unknown"));
    }
}
