<%--
  Created by IntelliJ IDEA.
  User: cmoxygen
  Date: 19.03.2022
  Time: 12:43
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Авторизация</title>

    <script type="text/javascript" src="js.cookie.min.js"></script>
    <script type="text/javascript" src="jquery-3.6.0.min.js"></script>

    <script type="text/javascript" src="index.js"></script>

</head>
<body>
АВТОРИЗАЦИЯ
<br>

<label for="login-input-username">username: </label>
<input type="text" id="login-input-username" maxlength="20" required>
<br>
<label for="login-input-password">password:</label>
<input type="password" id="login-input-password" maxlength="20" required>
<br>
<button type="submit" id="login-sign-in-button">Авторизироваться</button>

<a href="reg.jsp">Зарегистрироваться</a>

</body>
</html>