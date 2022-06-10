const loginServletUrl = 'login';

const createProjectServletUrl = 'create-project';
const editProjectServletUrl = 'edit-project';
const removeProjectServletUrl = 'remove-project';

const createTaskServletUrl = 'create-task';
const removeTaskServetUrl = 'remove-task';
const editTaskServletUrl = 'edit-task';

let userData;

let activeProjectId;
let activeProjectIndex = 0;
let activeTaskId;

jQuery(document).ready(function () {
    document.getElementById("add-task").style.display = "none";
    document.getElementById('edit-task').style.display = "none";
    document.getElementById('add-project').style.display = "none";

    getUserData();

    $("#display-add-task-button").click(function () {
        document.getElementById("add-task").style.display = "flex";
        document.getElementById("display-add-task-button").style.display = "none";
        document.getElementById("add-project-button").style.display = "none";
    });

    $("#add-task-button").click(function () {
        document.getElementById("add-task").style.display = "none";
        document.getElementById("display-add-task-button").style.display = "inline-block";
        document.getElementById("add-project-button").style.display = "inline-block";

        createTask();
        document.getElementById('edit-task-title').value = '';
        document.getElementById('edit-task-text').value = '';
        document.getElementById('edit-task-deadline').value = '';
        document.getElementById('edit-task-priority').value = '4';
    });

    $("#hide-add-task-button").click(function () {
        document.getElementById("add-task").style.display = "none";
        document.getElementById("display-add-task-button").style.display = "inline-block";
        document.getElementById("add-project-button").style.display = "inline-block";
    });

    $("#edit-task-button").click(function () {
        document.getElementById("edit-task").style.display = "none";
        document.getElementById("display-add-task-button").style.display = "inline-block";
        document.getElementById("add-project-button").style.display = "inline-block";

        editTask();
        activeTaskId = undefined;
        document.getElementById('edit-task-title').value = '';
        document.getElementById('edit-task-text').value = '';
        document.getElementById('edit-task-deadline').value = '';
        document.getElementById('edit-task-priority').value = '4';

    });

    $("#hide-edit-task-button").click(function () {
        document.getElementById("edit-task").style.display = "none";
        document.getElementById("display-add-task-button").style.display = "inline-block";
        document.getElementById("add-project-button").style.display = "inline-block";

        activeTaskId = undefined;
    });

    $("#add-project-button").click(function () {
        document.getElementById("add-project-button").style.display = "none";
        document.getElementById("display-add-task-button").style.display = "none";
        document.getElementById('add-project').style.display = "flex";
    });

    $("#add-project-apply-button").click(function () {

        let name = document.getElementById('add-project-name').value;

        if (name === '' || name === undefined) {
            alert('Невозможно создать проект с пустым именем');
        } else {
            document.getElementById("add-project-button").style.display = "inline-block";
            document.getElementById("display-add-task-button").style.display = "inline-block";
            document.getElementById('add-project').style.display = "none";
            createProject();
            document.getElementById('add-project-name').value = '';
        }
    });

    $("#add-project-cancel-button").click(function () {
        document.getElementById("add-project-button").style.display = "inline-block";
        document.getElementById("display-add-task-button").style.display = "inline-block";
        document.getElementById('add-project').style.display = "none";
    });
});

function getUserData() {

    let user = {
        username: Cookies.get('username'),
        password: Cookies.get('password')
    };

    let json = JSON.stringify(user);

    $.ajax(loginServletUrl, {
        type: 'POST',
        data: json,
        contentType: 'application/json',
        success: function (result) {
            userData = result;
            displayUserData();
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}

function removeTask(taskIndex) {

    let task = {
        projectId: activeProjectId,
        taskId: userData.userProjects[activeProjectIndex].projectTasks[taskIndex].taskId
    };

    let json = JSON.stringify(task);

    $.ajax(removeTaskServetUrl, {
        type: 'POST',
        data: json,
        contentType: 'application/json',
        success: function (result) {
            userData = result;
            displayUserData();
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}

function createTask() {
    let text = document.getElementById('add-task-text').value;
    let dl = document.getElementById('add-task-deadline').value;

    let task = {
        projectId: activeProjectId,
        taskTitle: document.getElementById('add-task-title').value,
        // taskText: document.getElementById('add-task-text').value === '' ? null : document.getElementById('add-task-text').value,
        taskText: text === '' || text === undefined ? null : text,
        // deadline: document.getElementById('add-task-deadline').value === '' ? null : document.getElementById('add-task-deadline').value,
        deadline: dl === '' || dl === undefined ? null : dl,
        priority: document.getElementById('add-task-priority').value
    };

    let json = JSON.stringify(task);

    $.ajax(createTaskServletUrl, {
        type: 'POST',
        data: json,
        contentType: 'application/json',
        success: function (result) {
            userData = result;
            displayUserData();
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}

function displayUserData() {

    document.getElementById('project-list').innerHTML = "";

    let username = document.createElement("div");
    username.id = 'user-name';
    username.className = 'user_name';
    username.innerHTML = userData.username;

    let userId = document.createElement("div");
    userId.id = 'user-id';
    userId.className = 'user_id';
    userId.innerHTML = 'id: ' + userData.id;

    document.getElementById('project-list').appendChild(username);
    document.getElementById('project-list').appendChild(userId);

    if (userData.userProjects.length > 0) {

        document.getElementById('display-add-task-button').disabled = false;

        for (let i = 0; i < userData.userProjects.length; i++) {

            let project = document.createElement("a");
            project.id = 'user-project' + userData.userProjects[i].projectId;
            project.className = 'user_project';
            project.innerHTML = userData.userProjects[i].projectName;

            document.getElementById('project-list').appendChild(project);
        }

        $(".user_project").click(function (event) {
            for (let i = 0; i < userData.userProjects.length; i++) {

                if (userData.userProjects[i].projectId.toString() === (event.target.id.match(/[0-9]+/g) || []).join('')) {
                    displayUserProject(i);
                }
            }
        });
        displayUserProject(activeProjectIndex);
    } else {
        document.getElementById("project-tasks").innerHTML = "";
        document.getElementById('display-add-task-button').disabled = true;
    }
}

function displayUserProject(projectIndex) {

    activeProjectId = userData.userProjects[projectIndex].projectId;
    activeProjectIndex = projectIndex;

    document.getElementById("project-tasks").innerHTML = "";

    let projectName = document.createElement("div");
    projectName.className = 'project_name';
    projectName.id = 'project-name';
    projectName.innerHTML = userData.userProjects[projectIndex].projectName;

    let editProjectButton = document.createElement("button");
    editProjectButton.id = 'edit-project-button';

    $(editProjectButton).click(function () {
        showEditProjectDialog();
    });

    let removeProjectButton = document.createElement("button");
    removeProjectButton.id = 'remove-project-button';

    $(removeProjectButton).click(function () {
        removeProject();
    });

    projectName.appendChild(editProjectButton);
    projectName.appendChild(removeProjectButton);

    document.getElementById("project-tasks").appendChild(projectName);

    for (let i = 0; i < userData.userProjects[projectIndex].projectTasks.length; i++) {

        let task = document.createElement("div");
        task.id = 'task' + userData.userProjects[projectIndex].projectTasks[i].taskId;
        // task.className = 'task';

        let completed = document.createElement("input");
        completed.type = 'checkbox';
        completed.id = "completed" + userData.userProjects[projectIndex].projectTasks[i].taskId;

        let completedLabel = document.createElement("label");
        completedLabel.for = completed.id;
        completedLabel.className = 'task_title';
        completedLabel.innerHTML = userData.userProjects[projectIndex].projectTasks[i].taskTitle;

        let taskText = document.createElement("p");
        taskText.className = 'task_text';

        let text;
        if (userData.userProjects[projectIndex].projectTasks[i].taskText === undefined) {
            text = '';
        } else {
            text = userData.userProjects[projectIndex].projectTasks[i].taskText;
        }
        taskText.innerHTML = text;

        let taskProperties = document.createElement("div");
        taskProperties.className = 'task_properties';

        let deadline = document.createElement("p");
        deadline.id = 'task-deadline' + userData.userProjects[projectIndex].projectTasks[i].taskId;

        let dl;
        if (userData.userProjects[projectIndex].projectTasks[i].deadline === undefined) {
            dl = 'БЕЗ СРОКОВ';
            task.className = 'task';
            deadline.className = "task_deadline";

        } else {
            dl = userData.userProjects[projectIndex].projectTasks[i].deadline.replace('T', ' ').replace("'", "").replace("'", "");

            if (new Date() < new Date(dl)) {
                task.className = 'task';
                deadline.className = "task_deadline";
            } else {
                task.className = 'task';
                deadline.className = "task_overdue_deadline";
            }
        }

        if (userData.userProjects[projectIndex].projectTasks[i].taskStatus === "completed") {
            task.className = 'task_completed';
            deadline.className = 'task_deadline';
        }
        deadline.innerHTML = dl;

        let priority = document.createElement("p");
        priority.className = "task_priority";
        priority.innerHTML = userData.userProjects[projectIndex].projectTasks[i].priority;

        taskProperties.appendChild(deadline);
        taskProperties.appendChild(priority);

        let taskContentLeft = document.createElement("div");
        taskContentLeft.className = 'task_content_left';

        let taskButtonsRight = document.createElement("div");
        taskButtonsRight.className = 'task_buttons_right';

        let taskButtonEdit = document.createElement("button");
        taskButtonEdit.className = 'task_button_edit';
        taskButtonEdit.id = 'task-button-edit' + userData.userProjects[projectIndex].projectTasks[i].taskId;
        taskButtonEdit.innerHTML = 'ИЗМЕНИТЬ';

        let taskButtonRemove = document.createElement("button");
        taskButtonRemove.className = 'task_button_remove';
        taskButtonRemove.id = 'task-button-remove' + userData.userProjects[projectIndex].projectTasks[i].taskId;
        taskButtonRemove.innerHTML = 'УДАЛИТЬ';

        taskContentLeft.appendChild(completed);
        taskContentLeft.appendChild(completedLabel);
        taskContentLeft.appendChild(taskText);
        taskContentLeft.appendChild(taskProperties);

        taskButtonsRight.appendChild(taskButtonEdit);
        taskButtonsRight.appendChild(taskButtonRemove);

        task.appendChild(taskContentLeft);
        task.appendChild(taskButtonsRight);

        $(taskButtonRemove).click(function (event) {
            console.log('EVENT TARGET ' + event.target);
            console.log('EVENT TARGET ID ' + event.target.id);

            for (let i = 0; i < userData.userProjects[projectIndex].projectTasks.length; i++) {

                if (userData.userProjects[projectIndex].projectTasks[i].taskId.toString() === (event.target.id.match(/[0-9]+/g) || []).join('')) {
                    console.log('TASK INDEX ' + i);
                    console.log('TASK ID ' + userData.userProjects[projectIndex].projectTasks[i].taskId);
                    removeTask(i);
                }
            }
        });

        $(taskButtonEdit).click(function (event) {
            displayEditTaskMenu(parseInt((event.target.id.match(/[0-9]+/g) || []).join('')));
        });

        $(completed).click(function (event) {

            let taskId = parseInt((event.target.id.match(/[0-9]+/g) || []).join(''));
            activeTaskId = taskId;

            let taskIndex;
            for (let i = 0; i < userData.userProjects[activeProjectIndex].projectTasks.length; i++) {

                if (userData.userProjects[activeProjectIndex].projectTasks[i].taskId === taskId) {
                    taskIndex = i;
                }
            }
            let task = userData.userProjects[activeProjectIndex].projectTasks[taskIndex];

            document.getElementById('edit-task-title').value = task.taskTitle;
            document.getElementById('edit-task-text').value = task.taskText === undefined ? '' : task.taskText;
            document.getElementById('edit-task-deadline').value = task.deadline === undefined ? '' : task.deadline.replace(' ', 'T');
            document.getElementById('edit-task-priority').value = task.priority;
            editTask();
        });

        document.getElementById("project-tasks").appendChild(task);
        document.querySelector('#' + completed.id).checked
            = userData.userProjects[projectIndex].projectTasks[i].taskStatus === "completed";
    }
}

function displayEditTaskMenu(taskId) {

    activeTaskId = taskId;

    let taskIndex;
    for (let i = 0; i < userData.userProjects[activeProjectIndex].projectTasks.length; i++) {

        if (userData.userProjects[activeProjectIndex].projectTasks[i].taskId === taskId) {
            taskIndex = i;
        }
    }
    let task = userData.userProjects[activeProjectIndex].projectTasks[taskIndex];

    document.getElementById('display-add-task-button').style.display = "none";
    document.getElementById('edit-task').style.display = "flex";
    document.getElementById("display-add-task-button").style.display = "none";

    document.getElementById('edit-task-title').value = task.taskTitle;
    document.getElementById('edit-task-text').value = task.taskText === undefined ? '' : task.taskText;
    document.getElementById('edit-task-deadline').value = task.deadline === undefined ? '' : task.deadline.replace(' ', 'T');
    document.getElementById('edit-task-priority').value = task.priority;

    window.scrollTo(0, document.body.scrollHeight);
}

function editTask() {
    let text = document.getElementById('edit-task-text').value;
    let dl = document.getElementById('edit-task-deadline').value;

    let task = {
        projectId: activeProjectId,
        taskId: activeTaskId,
        taskTitle: document.getElementById('edit-task-title').value,
        taskText: text === '' || text === undefined ? null : text,
        deadline: dl === '' || dl === undefined ? null : dl,
        priority: document.getElementById('edit-task-priority').value,
        taskStatus: document.querySelector('#completed' + activeTaskId).checked ? "completed" : "not_completed"
    };
    let json = JSON.stringify(task);

    $.ajax(editTaskServletUrl, {
        type: 'POST',
        data: json,
        contentType: 'application/json',
        success: function (result) {
            userData = result;
            displayUserData();
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}

function showEditProjectDialog() {

    let inputText = document.createElement('input');
    inputText.id = 'edit-project-text';
    inputText.type = 'text';
    inputText.maxLength = 20;
    inputText.value = userData.userProjects[activeProjectIndex].projectName;

    let applyButton = document.createElement('button');
    applyButton.id = 'apply-button';
    applyButton.innerHTML = 'ПРИМЕНИТЬ';

    let cancelButton = document.createElement('button');
    cancelButton.id = 'cancel-button';
    cancelButton.innerHTML = 'ОТМЕНА';

    document.getElementById('project-name').innerHTML = '';
    document.getElementById('project-name').appendChild(inputText);
    document.getElementById('project-name').appendChild(applyButton);
    document.getElementById('project-name').appendChild(cancelButton);

    $(applyButton).click(function () {
        editProject();
    });

    $(cancelButton).click(function () {
        document.getElementById('project-name').innerHTML = userData.userProjects[activeProjectIndex].projectName

        let editProjectButton = document.createElement("button");
        editProjectButton.id = 'edit-project-button';

        let removeProjectButton = document.createElement("button");
        removeProjectButton.id = 'remove-project-button';

        document.getElementById('project-name').appendChild(editProjectButton);
        document.getElementById('project-name').appendChild(removeProjectButton);

        $(editProjectButton).click(function () {
            showEditProjectDialog();
        });

        $(removeProjectButton).click(function () {
            removeProject();
        });
    });
}

function editProject() {

    let project = {
        projectId: activeProjectId,
        projectName: document.getElementById('edit-project-text').value
    };

    let json = JSON.stringify(project);

    $.ajax(editProjectServletUrl, {
        type: 'POST',
        data: json,
        contentType: 'application/json',
        success: function (result) {
            userData = result;
            displayUserData();
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}

function removeProject() {

    let project = {
        projectId: activeProjectId
    };

    let json = JSON.stringify(project);

    $.ajax(removeProjectServletUrl, {
        type: 'POST',
        data: json,
        contentType: 'application/json',
        success: function (result) {
            userData = result;
            activeProjectIndex = 0;
            displayUserData();
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}

function createProject() {

    let project = {
        userId: userData.id,
        projectName: document.getElementById('add-project-name').value
    };

    let json = JSON.stringify(project);

    $.ajax(createProjectServletUrl, {
        type: 'POST',
        data: json,
        contentType: 'application/json',
        success: function (result) {
            userData = result;
            displayUserData();
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}
