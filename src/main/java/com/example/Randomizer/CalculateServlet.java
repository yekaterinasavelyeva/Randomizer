package com.example.Randomizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "Calculate", urlPatterns = "/calc")
public class CalculateServlet extends HttpServlet {

    private static final String [] actions = {"+", "-", "/", "*"};

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int randomNumber1 = getRandomNumber();
        int randomNumber2 = getRandomNumber();
        String action = getRandomAction();
        request.setAttribute("number1", randomNumber1);
        request.setAttribute("number2", randomNumber2);
        request.setAttribute("action", action);
        request.getRequestDispatcher("/WEB-INF/calculate.jsp").forward(request, response);    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var answer = request.getParameter("answer");
        var number1 = request.getParameter("number1");
        var number2 = request.getParameter("number2");
        var action = request.getParameter("action");
        BigDecimal calculatedAnswer = calculateAnswer(number1, number2, action);
        BigDecimal numericAnswer = BigDecimal.valueOf(Double.parseDouble(answer)).setScale(0, RoundingMode.CEILING);

        if (calculatedAnswer.equals(numericAnswer)) {
            request.setAttribute("answerIsCorrect", true);
        } else {
            request.setAttribute("answerIsCorrect", false);
        }
        request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
    }

    private BigDecimal calculateAnswer(String number1, String number2, String action) {
        int one = Integer.parseInt(number1);
        int two = Integer.parseInt(number2);
        switch (action) {
            case "+":
                return BigDecimal.valueOf(one + two);
            case "-":
                return BigDecimal.valueOf(one - two);
            case "/":
                return BigDecimal.valueOf(one/two).setScale(0, RoundingMode.CEILING);
            case "*":
                return BigDecimal.valueOf((long) one * two);
        }
        return BigDecimal.ZERO;
    }

    private int getRandomNumber() {
        return 100 + ThreadLocalRandom.current().nextInt(901);
    }

    private String getRandomAction() {
        int index = ThreadLocalRandom.current().nextInt(actions.length);
        return actions[index];
    }

}
