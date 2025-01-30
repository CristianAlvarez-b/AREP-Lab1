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
    public void testGetMimeTypeJs() {
        assertEquals("application/javascript", HttpServer.getMimeType("/script.js"));
    }

    @Test
    public void testHandleApiRequestHello() {
        String response = HttpServer.handleApiRequest("/app/hello?name=John", "GET", mock(BufferedReader.class));
        assertTrue(response.contains("\"message\": \"Hello, John!\""));
    }
    @Test
    public void testHandleApiRequestHelloWithoutName() {
        String response = HttpServer.handleApiRequest("/app/hello", "GET", mock(BufferedReader.class));
        assertTrue(response.contains("\"message\": \"Hello, Unknown!\""));
    }

    @Test
    public void testHandleApiRequestPi() {
        String response = HttpServer.handleApiRequest("/app/PI", "GET", mock(BufferedReader.class));
        assertTrue(response.contains("\"value\": 3.141592653589793"));
    }

    @Test
    public void testHandleApiRequestSquareValidNumber() {
        String response = HttpServer.handleApiRequest("/app/square?number=4", "GET", mock(BufferedReader.class));
        assertTrue(response.contains("\"number\": 4"));
        assertTrue(response.contains("\"square\": 16"));
    }
    @Test
    public void testHandleApiRequestSquareMissingParameter() {
        String response = HttpServer.handleApiRequest("/app/square", "GET", mock(BufferedReader.class));
        assertTrue(response.contains("\"error\": \"Missing number parameter\""));
    }

    @Test
    public void testHandleApiRequestSquareInvalidNumber() {
        String response = HttpServer.handleApiRequest("/app/square?number=abc", "GET", mock(BufferedReader.class));
        assertTrue(response.contains("\"error\": \"Invalid number format\""));
    }
    @Test
    public void testHandleApiRequestNotFound() {
        String response = HttpServer.handleApiRequest("/app/unknown", "GET", mock(BufferedReader.class));
        assertTrue(response.contains("\"error\": \"API endpoint not found\""));
    }

    @Test
    public void testExtractNameFromJson() throws IOException {

        String jsonInput = "{\"name\":\"Alice\"}";
        BufferedReader mockReader = new BufferedReader(new StringReader(jsonInput));

        assertEquals("Alice", HttpServer.extractNameFromJson(mockReader));
    }

    @Test
    public void testCreateJsonResponse() {
        String response = HttpServer.createJsonResponse(200, "{\"message\":\"Success\"}");
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Content-Type: application/json"));
        assertTrue(response.contains("{\"message\":\"Success\"}"));
    }

    @Test
    public void testGetMimeTypeUnknown() {
        assertEquals("application/octet-stream", HttpServer.getMimeType("/file.unknown"));
    }
    @Test
    public void testHandleStaticFileRequest() throws IOException {
        Socket mockSocket = mock(Socket.class);
        OutputStream mockOutputStream = mock(OutputStream.class);


        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        String filePath = "/index.html";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(outputStream);


        HttpServer.handleStaticFileRequest(filePath, out, mockSocket);

        String output = outputStream.toString();
        assertTrue(output.contains("HTTP/1.1 200 OK"));
        assertTrue(output.contains("Content-Type: text/html"));
    }
    @Test
    public void testHandleStaticFileRequestNotFound() throws IOException {

        Socket mockSocket = mock(Socket.class);
        OutputStream mockOutputStream = new ByteArrayOutputStream();
        when(mockSocket.getOutputStream()).thenReturn(mockOutputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter out = new PrintWriter(outputStream, true);

        HttpServer.handleStaticFileRequest("/nonexistent.html", out, mockSocket);

        String response = outputStream.toString();
        assertTrue(response.contains("HTTP/1.1 404 Not Found"));
        assertTrue(response.contains("<h1>404 Not Found</h1>"));
    }

    @Test
    public void testExtractQueryParam() {
        assertEquals("John", HttpServer.extractQueryParam("/app/hello?name=John", "name"));
        assertEquals("42", HttpServer.extractQueryParam("/app/square?number=42", "number"));
        assertNull(HttpServer.extractQueryParam("/app/hello", "name"));
    }



}
