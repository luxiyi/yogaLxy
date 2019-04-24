//发送邮箱密码
function sendRegEmailCode() {
    $.ajax({
        url:"user/sendRegEmailCode",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
        },
        success:function (data) {
            if (data=="前往邮箱获取验证码"){
                alert(data);
            }else {
                alert(data);
            }
        }
    });
}

//注册邮箱
function regByEmail() {
    $.ajax({
        url:"user/regByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userVerifyCode":$("#userVerifyCode").val(),
            "userPwd":$("#userPwd").val(),
        },
        success:function (data) {
            if (data=="注册成功"){
                alert(data);
                window.location.href="./index.html";
            }else {
                alert(data);
            }
        }
    });
}
//登录邮箱
function loginByEmailAndPwd() {
    $.ajax({
        url:"user/loginByEmailAndPwd",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userPwd":$("#userPwd").val()
        },
        success:function (data) {
            if (data=="登录成功"){
                alert(data);
                window.location.href="./index.html";
            }else {
                alert(data);
            }
        }
    });
}
//邮箱找回密码,发送验证码
function getCodeByEmail() {
    $.ajax({
        url:"user/getCodeByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
        },
        success:function (data) {
                alert(data);
        }
    });
}
//邮箱找回密码，验证验证码和邮箱
function getPwdByEmail() {
    $.ajax({
        url:"user/getPwdByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userVerifyCode":$("#emailCode").val()
        },
        success:function (data) {
            if (data=="请重置密码"){
                alert(data);
                window.location.href="./updateUserPwd.html";
            }else {
                alert(data);
            }
        }
    });
}
//邮箱找回密码，密码重置，返回登录页面
function  updateUserPwdByEmail() {
    $.ajax({
        url:"user/ updateUserPwdByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userPwd":$("#userPwd").val(),
            "confirmPwd":$("#confirmPwd").val()
        },
        success:function (data) {
            if (data=="重置密码成功"){
                alert(data);
                window.location.href="./login.html";
            }else {
                alert(data);
            }
        }
    });
}


//发送注册手机密码
function sendRegPhonePwd() {
    $.ajax({
        url:"user/sendRegPhonePwd",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
            if (data=="请在手机上查收密码"){
                alert(data);
            } else {
                alert(data);
            }
        }
    });
}
//注册手机
function regByPhone() {
    alert($("input[name=roleId]:checked").val());
    $.ajax({
        url:"user/regByPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userPwd":$("#userPwd").val(),
            "roleId":$("input[name=roleId]:checked").val()
        },
        success:function (data) {
            if (data=="注册成功"){
                alert(data);
                window.location.href="./index.html";
            }else {
                alert(data);
            }
        }
    });
}
//发送登录手机验证码
function sendLoginPhonePwd() {
    $.ajax({
        url:"user/sendLoginPhonePwd",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
                alert(data);
        }
    });
}
//登录手机
function loginByPhoneAndCode() {
    $.ajax({
        url:"user/loginByPhoneAndCode",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userVerifyCode":$("#userVerifyCode").val()
        },
        success:function (data) {
            if (data=="登录成功"){
                alert(data);
                window.location.href="./index.html";
            }else {
                alert(data);
            }
        }
    });
}
//手机找回密码
function  getCodeByPhone() {
    $.ajax({
        url:"user/getCodeByPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
                alert(data);
        }
    });
}

//手机找回密码，验证验证码和手机
function getPwdByPhone() {
    $.ajax({
        url:"user/getPwdByPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userVerifyCode":$("#phoneCode").val()
        },
        success:function (data) {
            if (data=="请重置密码"){
                alert(data);
                window.location.href="./updateUserPwd.html";
            }else {
                alert(data);
            }
        }
    });
}
//手机找回密码，密码重置，返回登录页面
function  updateUserPwdByPhone() {
    $.ajax({
        url:"user/updateUserPwdByPhone",
        type:"post",
        data:{
            "userPwd":$("#userPwdPhone").val(),
            "confirmPwd":$("#confirmPwdPhone").val()
        },
        success:function (data) {
            if (data=="重置密码成功"){
                alert(data);
                window.location.href="./login.html";
            }else {
                alert(data);
            }
        }
    });
}