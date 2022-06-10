let loginServletUrl = 'login';

jQuery(document).ready(function () {
    // alert('JQUERY WORKS');

    $("#login-sign-in-button").click(function () {
        signIn();
    });
});

function signIn() {

    let user = {
        username: document.getElementById("login-input-username").value,
        password: document.getElementById("login-input-password").value
    };

    let json = JSON.stringify(user);

    $.ajax(loginServletUrl, {
        data: json,
        contentType: 'application/json',
        type: 'POST',
        success: function (result) {
            document.location.replace("home.jsp");
        },
        error: function (e) {
            alert('NOT WORKS');
            alert(e);
        }
    });
}

function replace(url) {
    document.location.replace(url);
}