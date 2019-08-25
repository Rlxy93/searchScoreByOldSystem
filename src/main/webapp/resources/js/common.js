var id;
$(function () {
    $('#loginNew').click(function () {
        var userInfo = {};
        userInfo.username = $('#username').val();
        userInfo.password = $('#password').val();
        if (userInfo.username != null) {
            id = userInfo.username;
        }
        var loginUrl = "./loginNew?id="+userInfo.username;
        var formData = new FormData();
        formData.append('userInfo', JSON.stringify(userInfo));
        $.ajax({
            url: loginUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    alert("登录成功！");
                    window.location = "./old.html?id=" + userInfo.username;
                } else {
                    alert("登录失败！请检查您输入的账号密码是否正确！");
                    window.location.reload();
                }
            }
        });
    });
});


$(function () {
    $('#loginOld').click(function () {
        var loginUrl = "./login";
        var userInfo = {};
        userInfo.username = $('#username').val();
        userInfo.password = $('#password').val();
        var verifyCode = $('#code').val();
        var formData = new FormData();
        formData.append('userInfo', JSON.stringify(userInfo));
        formData.append('verifyCode', JSON.stringify(verifyCode));
        $.ajax({
            url: loginUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success) {
                    alert("查询成功！");
                    document.write("<img src='./resources/score/" + userInfo.username + ".jpg'><a href='./new.html'>继续查询</a>")
                } else {
                    alert("查询失败！请检查您输入的账号密码是否正确！");
                    window.location.reload();
                }
            }
        });
    });
});

$(function () {
    var currentUrl = document.location.toString();
    var Url = currentUrl.split("?");
    var para = Url[1].substring(3);
    var searchUrl = "./getverifyCode?"+Url[1];
    $.ajax({
        url: searchUrl,
        type: 'POST',
        contentType: false,
        processData: false,
        cache: false,
        success: function (data) {
            if (data.success) {
                $('#verifyCode').attr("src", "./resources/verifyCode/" + para + ".jpeg");
            }
        }
    });
});