# Web Server

This project implements a simple web server that supports multiple consecutive requests without concurrency. It is designed to read local disk files and return them upon request, including HTML pages, JavaScript files, CSS, and images. Additionally, a web application is created to test the server, which features asynchronous communication with backend REST services.
## Getting Started

These instructions will guide you through obtaining a copy of the project running on your local machine for development and testing purposes
### Prerequisites

To run this project, you must have Java installed on your system. Follow the steps below to install Java and Maven (which is used for managing dependencies).
1. **Install Java:**

    Download and install the Java JDK (version 11 or higher). You can follow the instructions on the [official Java website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

2. **Install Maven:**

   - Maven is used to manage project dependencies. You can download and install Maven from [here](https://maven.apache.org/download.cgi).

   - After installation, verify if Maven is correctly installed by running: mvn -v. This should show the installed Maven version.
  
### Installing
> [!NOTE]
> You should perform the following steps from a Bash terminal or PowerShell on Windows.

o set up your development environment:
1. **Clone the repository:**
```bash
   git clone https://github.com/CristianAlvarez-b/AREP-Lab1
```
2. **Navigate to the project directory:**
```bash
   cd AREP-Lab1
```
3. **Build the project with Maven:**
```bash
   mvn clean install
```
  This will compile the code and package it into an executable JAR file.
4. **Run the server:**
```bash
   java -jar target/HttpServer-1.0-SNAPSHOT.jar
```
   The server will start and listen on port 35000 by default. You can now access the web server `http://localhost:35000`.

### Running the Tests
Automated tests are included to ensure the server and web application functionality.
> [!NOTE]
> You should test in a diferent Bash terminal or PowerShell on Windows.
1. **Ejecutar pruebas unitarias:**
   Run unit tests: To run the automated tests, use the following Maven command:
   ```bash
   mvn test
   ```
   This will run all the unit tests and show the results in the terminal.

## Test
### Unit Test

![image](https://github.com/user-attachments/assets/12c81bad-e2df-40e6-853a-c9c89d45394f)

Example of a test:
```java
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
}
```
This test verifies the behavior of the handleStaticFileRequest method in the HttpServer class. It mocks a Socket and its OutputStream using Mockito, sets up a PrintWriter to write to a ByteArrayOutputStream, and then calls the handleStaticFileRequest method with a file path (/index.html). The test asserts that the response includes the correct HTTP status (HTTP/1.1 200 OK) and content type (Content-Type: text/html), ensuring that the server correctly handles a static file request and returns the expected response.

### Functional Test
#### Static Files
1. Load HTML files::
   - index.html:
     
     ```Bash
      http://localhost:35000/index.html
     ```
     
     ![image](https://github.com/user-attachments/assets/ba9c2271-83af-44e2-9e8b-376397e2e427)

   - About.html:
     
     ```Bash
     http://localhost:35000/about.html
     ```
     
     ![image](https://github.com/user-attachments/assets/d9cd5492-6de5-4aec-b51a-f7b8776aa6e2)


2. Load images:

   - jpg:
     
     ```Bash
     http://localhost:35000/img/banner.jpg
     ```

    ![image](https://github.com/user-attachments/assets/b11ac037-6f55-47ae-9eb1-e009a272d9e7)
     
   - png:
     
      ```Bash
     http://localhost:35000/img/about.png
     ```

    ![image](https://github.com/user-attachments/assets/f85bc11e-db6b-4a84-9101-c96730c8b024)
 ### Javascript
 
```Bash
http://localhost:35000/js/app.js
```
 
 ![image](https://github.com/user-attachments/assets/e8b67605-7cc6-4f6c-8422-040d812b7269)


 ### Api

  - Hello:
      - Without params:
        
        ```Bash
        http://localhost:35000/app/hello
        ```
        
        ![image](https://github.com/user-attachments/assets/37e78d69-7b01-4de8-9a39-2b486b293a2d)
        
      - With params:
        
        ```Bash
        http://localhost:35000/app/hello?name=raul
        ```
        
        ![image](https://github.com/user-attachments/assets/41a54c92-f0c3-454d-95f1-4556353fda33)

    
  - PI:
    
     ```Bash
     http://localhost:35000/app/PI
     ```

    ![image](https://github.com/user-attachments/assets/0e6ab9e7-1138-4c58-91fd-dc014dd37f0d)

  - Square:
    
     ```Bash
     http://localhost:35000/app/square?number=4
     ```

    ![image](https://github.com/user-attachments/assets/73828afb-9ab3-4a40-bccb-28ea7049d365)


    

### Built with
- Java: The programming language used
- Maven: Dependency management and build tool
- JUnit: Testing framework
- Mockito: Mocking framework for unit testing

### Author
- Cristian Javier Alvarez Baquero
  
### License
This project is licensed under the MIT license: see the LICENSE.md file for details.

### Explanation of Sections:
- **Getting Started**: Instructions for setting up the development environment.
- **Prerequisites**: What tools you need and how to install them (Java and Maven).
- **Installing**: How to clone the repository, build, and run the project.
- **Running the Tests**: How to run the tests and what types of tests are included (unit and functional).
- **Built with**: Tools and libraries used in the project.
- **License**: License type (MIT) and link to the license file.

