<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <title>Calculation Result</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="css/result.css">
</head>
<body>
<div class="container w3-display-container w3-animate-opacity w3-2021-inkwell">
    <img class="image" src="image/random.jpg">
    <div class="box w3-display-middle w3-round-xxlarge">
    </div>
    <div class="card w3-card w3-round-xlarge w3-display-middle">
        <h1 class="random w3-center w3-animate-opacity">Result</h1>


        <c:if test="${answerIsCorrect}">
            <p class="success">That is right!</p>
        </c:if>
        <c:if test="${answerIsCorrect == false}">
            <p class="error">Answer is wrong!</p>
        </c:if>

    </div>
</div>

</body>
</html>
