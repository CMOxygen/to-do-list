<%--
  Created by IntelliJ IDEA.
  User: cmoxygen
  Date: 08.05.2022
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация</title>


    <script type="text/javascript" src="js.cookie.min.js"></script>
    <script type="text/javascript" src="jquery-3.6.0.min.js"></script>

    <script type="text/javascript" src="reg.js"></script>
</head>
<body>
РЕГИСТРАЦИЯ
<br>
<label for="reg-input-username">username: </label>
<input type="text" id="reg-input-username" maxlength="20" required>
<br>
<label for="reg-input-password">password:</label>
<input type="password" id="reg-input-password" maxlength="20" required>
<br>
<button type="submit" id="reg-sign-up-button">РЕГИСТРАЦИЯ</button>

</body>
</html>
