<%@ page
        import="com.cmoxygen.todolist.UserService" %><%--<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--<head>--%>
<%--    <title>ToDoList</title>--%>

<%--    <link rel="icon" href="favicon.ico">--%>

<%--    <script type="text/javascript" src="jquery-3.6.0.min.js"></script>--%>
<%--    <script type="text/javascript" src="js.cookie.min.js"></script>--%>
<%--    <script type="text/javascript" src="index.js"></script>--%>

<%--</head>--%>
<%--<body>--%>
<%--<h1><%= "Hello World!" %>--%>
<%--</h1>--%>
<%--<br/>--%>
<%--AUTHORIZE:--%>
<%--<br>--%>
<%--<label> username--%>
<%--    <input type="text" id="input-username" maxlength="20" oninput="this.value=this.value.replace(/[^A-Za-z0-9\s]/g,'');"--%>
<%--           required>--%>
<%--</label>--%>
<%--<br>--%>
<%--<label> password--%>
<%--    <input type="password" id="input-password" maxlength="20"--%>
<%--           oninput="this.value=this.value.replace(/[^A-Za-z0-9\s]/g,'');" required>--%>
<%--</label>--%>
<%--<button type="button" id="login-post">SIGN UP</button>--%>
<%--<br>--%>
<%--<p id="user-id"></p>--%>
<%--<p id="username"></p>--%>
<%--<p id="password"></p>--%>
<%--</body>--%>
<%--</html>--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ToDoList</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Open+Sans&display=swap" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="home.css">

    <script type="text/javascript" src="jquery-3.6.0.min.js"></script>
    <script type="text/javascript" src="js.cookie.min.js"></script>

    <script type="text/javascript" src="home.js"></script>
</head>
<body>

<div class="project_list" id="project-list">
    <div class="user_name" id="user-name"></div>
    <div id="user_id"></div>
</div>
<div class="project_tasks" id="project-tasks">
</div>

<button type="button" id="display-add-task-button">Создать задачу</button>
<button type="button" id="add-project-button">Создать проект</button>

<div class="add_task" id="add-task">
    <p>Создание задачи</p>
    <label> Заголовок:
        <input type="text" class="add_task_title" id="add-task-title" maxlength="75" placeholder="до 75 знаков">
    </label>
    <label> Текст:
        <input type="text" class="add_task_text" id="add-task-text" maxlength="150" placeholder="до 150 знаков">
    </label>
    <label> Срок выполнения:
        <input type="datetime-local" class="add_task_deadline" id="add-task-deadline">
    </label>
    <label> Приоритет:
        <select class="add_task_priority" id="add-task-priority">
            <option value="4">4</option>
            <option value="3">3</option>
            <option value="2">2</option>
            <option value="1">1</option>
        </select>
    </label>
    <div id="add-task-bottom-title">
        <button type="submit" class="add_task_button" id="add-task-button">Создать задачу</button>
        <button type="button" id="hide-add-task-button">X</button>
    </div>
</div>

<div class="edit_task" id="edit-task">
    <p>Изменение задачи</p>
    <label> Заголовок:
        <input type="text" class="edit_task_title" id="edit-task-title" maxlength="75" placeholder="до 75 знаков">
    </label>
    <label> Текст:
        <input type="text" class="edit_task_text" id="edit-task-text" maxlength="150" placeholder="до 150 знаков">
    </label>
    <label> Срок выполнения:
        <input type="datetime-local" class="edit_task_deadline" id="edit-task-deadline">
    </label>
    <label> Приоритет:
        <select class="edit_task_priority" id="edit-task-priority">
            <option value="4">4</option>
            <option value="3">3</option>
            <option value="2">2</option>
            <option value="1">1</option>
        </select>
    </label>
    <div id="edit-task-bottom-title">
        <button type="submit" class="edit_task_button" id="edit-task-button">Принять изменения</button>
        <button type="button" id="hide-edit-task-button">X</button>
    </div>
</div>

<div id="add-project">
    <label> Имя проекта:
        <input type="text" id="add-project-name" maxlength="20">
    </label>
    <button type="button" id="add-project-apply-button">Применить</button>
    <button type="button" id="add-project-cancel-button">Отмена</button>
</div>

</body>
</html>