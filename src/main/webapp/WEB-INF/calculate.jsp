<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <title>Calculation Result</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="css/calculate.css">
    <link rel="stylesheet" href="https://www.w3schools.com/lib/w3-colors-2021.css">
    <link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-red.css">
</head>
<body>
<div class="container w3-display-container w3-animate-opacity w3-2021-inkwell">
    <img class="image" src="image/random.jpg">
    <div class="box w3-display-middle w3-round-xxlarge">
    </div>
    <div class="card w3-card w3-round-xlarge w3-display-middle">
        <h1 class="random w3-center w3-animate-opacity">Please, calculate: <span class="w3-badge w3-animate-zoom w3-red number">${number1} ${action} ${number2} = </span>
        </h1>

        <form method="POST">
            <input type="hidden" id="number1" name="number1" value="${number1}">
            <input type="hidden" id="number2" name="number2" value="${number2}">
            <input type="hidden" id="action" name="action" value="${action}">
            <input class="w3-input w3-border w3-round-large" type="text" id="answer" placeholder="Answer" name="answer" value="${answer}">
            <div class="box">
                <button type="submit" href="calc" class="w3-button w3-medium w3-round-xxlarge w3-theme-gradient w3-text-white">Check Answer</button>
            </div>
        </form>
    </div>
</div>

</body>
</html>
