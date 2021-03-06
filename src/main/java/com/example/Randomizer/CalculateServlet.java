package com.example.Randomizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "Calculate", urlPatterns = "/calc")
public class CalculateServlet extends HttpServlet {

    private static final String [] ACTIONS = {"+", "-", "/", "*"};
    private static SecretKey key;
    private static final IvParameterSpec IV_PARAMETER_SPEC = AESUtil.generateIv();
    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";

    private static final Set<String> previousExamples = ConcurrentHashMap.newKeySet();

    static {
        try {
            key = AESUtil.generateKey(128);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int randomNumber1 = getRandomNumber();
        int randomNumber2 = getRandomNumber();
        String action = getRandomAction();
        String code = buildJson(action, randomNumber1, randomNumber2);
        String encryptedCode = encryptCode(code);

        request.setAttribute("number1", randomNumber1);
        request.setAttribute("number2", randomNumber2);
        request.setAttribute("action", action);
        request.setAttribute("code", encryptedCode);
        request.getRequestDispatcher("/WEB-INF/calculate.jsp").forward(request, response);
    }

    private String buildJson(String action, int firstNumber, int secondNumber) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Answer answer = new Answer();
        answer.setAction(action);
        answer.setFirstNumber(firstNumber);
        answer.setSecondNumber(secondNumber);
        return gson.toJson(answer);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        var answer = request.getParameter("answer");
        if (answer.isBlank() || isNotNumber(answer)) {
            request.setAttribute("answerIsCorrect", false);
            provideResult(request, response);
        }
        var code = request.getParameter("code");
        if (previousExamples.contains(code)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } else {
            previousExamples.add(code);
        }
        compareAnswer(request, answer, code);
        provideResult(request, response);
    }

    private void compareAnswer(HttpServletRequest request, String answer, String code) {
        var gson = new Gson();
        String answerToDecrypt = decryptCode(code);
        Answer jsonAnswer = gson.fromJson(answerToDecrypt, Answer.class);
        var calculatedAnswer = calculateAnswer(jsonAnswer.getFirstNumber(), jsonAnswer.getSecondNumber(), jsonAnswer.getAction());
        var numericAnswer = BigDecimal.valueOf(Double.parseDouble(answer)).setScale(0, RoundingMode.HALF_UP);
        var answerIsCorrect = calculatedAnswer.equals(numericAnswer);
        request.setAttribute("answerIsCorrect", answerIsCorrect);
    }

    private void provideResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/result.jsp").forward(request, response);
    }

    private String encryptCode(String code) {
        String encryptedCode = null;

        try {
            encryptedCode = AESUtil.encrypt(ALGORITHM, code, key, IV_PARAMETER_SPEC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedCode;
    }

    private String decryptCode(String code) {
        String answerToDecrypt = null;
        try {
            answerToDecrypt = AESUtil.decrypt(ALGORITHM, code, key, IV_PARAMETER_SPEC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answerToDecrypt;
    }

    private boolean isNotNumber(String answer) {
        return !answer.matches("\\d+");
    }

    private BigDecimal calculateAnswer(int one, int two, String action) {
        switch (action) {
            case "+":
                return BigDecimal.valueOf(one + two);
            case "-":
                return BigDecimal.valueOf(one - two);
            case "/":
                return BigDecimal.valueOf((double)one/(double)two).setScale(0, RoundingMode.HALF_UP);
            case "*":
                return BigDecimal.valueOf((long) one * two);
        }
        return BigDecimal.ZERO;
    }

    private int getRandomNumber() {
        return ThreadLocalRandom.current().nextInt(100, 1001);
    }

    private String getRandomAction() {
        int index = ThreadLocalRandom.current().nextInt(ACTIONS.length);
        return ACTIONS[index];
    }

}
