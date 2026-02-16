package net.daifo.servlet.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import net.daifo.servlet.models.MessageModel;

public class HelloController extends HttpServlet {
    @Override
    public void init() {
        System.out.println("Servlet initialized");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/hello-mvc".equals(path)) {
            MessageModel msg = new MessageModel("Hello from MVC!");
            req.setAttribute("message", msg);
            req.getRequestDispatcher("/hello.jsp").forward(req, resp);

        } else {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<h1>Hello from a Java Servlet!</h1>");
        }
    }

    @Override
    public void destroy() {
        System.out.println("Servlet destroyed");
    }
}