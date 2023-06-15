package com.example.linuxtest;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.nio.charset.StandardCharsets;
import java.sql.*;

public class Application {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.start();
        System.out.println("Server started on port 8000");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            String response = getHtmlResponse();
            try {
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(responseBytes);
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getHtmlResponse() {
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<!DOCTYPE html>")
                    .append("<html lang=\"en\">")
                    .append("<head>")
                    .append("<meta charset=\"UTF-8\">")
                    .append("</head>")
                    .append("<body>")
                    .append("<h1>This is Java page</h1>")
                    .append("<table border=\"1\">")
                    .append("<tr>")
                    .append("<th>学号</th>")
                    .append("<th>姓名</th>")
                    .append("</tr>");

            Connection connection = null;
            Statement statement =null;
            ResultSet rs =null;
            try {
                connection = DriverManager.getConnection("jdbc:mysql://8.139.5.39:3306/test", "user", "123456");
                statement = connection.createStatement();
                rs = statement.executeQuery("SELECT * from students");
                while (rs.next()) {
                    htmlBuilder.append("<tr>")
                            .append("<td>").append(rs.getString("stuId")).append("</td>")
                            .append("<td>").append(rs.getString("stuName")).append("</td>")
                            .append("</tr>");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) { /* ignored */}
                }
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) { /* ignored */}
                }
                if (rs != null){
                    try{
                        rs.close();
                    } catch (SQLException e) { /* ignored */}
                }
            }
            htmlBuilder.append("</table>")
                    .append("</body>")
                    .append("</html>");

            return htmlBuilder.toString();
        }
    }
}
