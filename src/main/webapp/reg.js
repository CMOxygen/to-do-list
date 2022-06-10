const regServletUrl = 'reg';

jQuery(document).ready(function () {

    $("#reg-sign-up-button").click(function () {
        signUp();
    });
});

function signUp() {

    let user = {
        username: document.getElementById("reg-input-username").value,
        password: document.getElementById("reg-input-password").value
    };

    let json = JSON.stringify(user);

    $.ajax(regServletUrl, {
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