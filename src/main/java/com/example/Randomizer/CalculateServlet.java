package com.example.Randomizer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "Calculate", urlPatterns = "/calc")
public class CalculateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Random randomizer = new Random();
        int result = 100 + randomizer.nextInt(901);
        request.setAttribute("number", result);
        request.getRequestDispatcher("WEB-INF/calculate.jsp").forward(request, response);
    }

}
