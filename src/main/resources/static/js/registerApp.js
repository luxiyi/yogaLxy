//发送邮箱密码
function sendRegEmailCode() {
    $.ajax({
        url:"/userApp/sendRegEmailCode",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
        },
        success:function (data) {
            if (data.message=="前往邮箱获取验证码"){
                console.log(data);
                alert(data.message);
            }else {
                console.log(data);
                alert(data.message);
            }
        }
    });
}

//注册邮箱
function regByEmail() {
    $.ajax({
        url:"/userApp/regByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userVerifyCode":$("#userVerifyCode").val(),
            "userPwd":$("#userPwd").val(),
            "roleName":$("input[name=roleName]:checked").val()
        },
        success:function (data) {
            if (data.message=="注册成功"){
                console.log(data);
                alert(data.message);
                alert(data.data.roleId);
                window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}
//登录邮箱
function loginByEmailAndPwd() {
    $.ajax({
        url:"/userApp/loginByEmailAndPwd",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userPwd":$("#userPwd").val()
        },
        success:function (data) {
            if (data.message=="登录成功"){
                console.log(data);
                alert(data.message);
                alert(data.data.roleId);
                window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}
//邮箱找回密码,发送验证码
function getCodeByEmail() {
    $.ajax({
        url:"/userApp/getCodeByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
        },
        success:function (data) {
            console.log(data);
                alert(data.message);
        }
    });
}
//邮箱找回密码，验证验证码和邮箱
function getPwdByEmail() {
    $.ajax({
        url:"/userApp/getPwdByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userVerifyCode":$("#emailCode").val()
        },
        success:function (data) {
            if (data.message=="验证成功，跳转下一步"){
                console.log(data);
                alert(data.message);
                window.location.href="./updateUserPwd.html";
            }else {
                alert(data.message);
            }
        }
    });
}
//邮箱找回密码，密码重置，返回登录页面
function  updateUserPwdByEmail() {
    $.ajax({
        url:"/userApp/ updateUserPwdByEmail",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userPwd":$("#userPwd").val(),
            "confirmPwd":$("#confirmPwd").val()
        },
        success:function (data) {
            $("#userEmail").val(data.data.userEmail);
            if (data.message=="重置密码成功"){
                console.log(data);
                alert(data.message);
                window.location.href="./loginApp.html";
            }else {
                alert(data.message);
            }
        }
    });
}


//发送注册手机密码
function sendRegPhonePwd() {
    $.ajax({
        url:"/userApp/sendRegPhonePwd",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
            if (data.message=="请在手机上查收密码"){
                console.log(data);
                alert(data.message);
            } else {
                alert(data.message);
            }
        }
    });
}
//注册手机
function regByPhone() {
    $.ajax({
        url:"/userApp/regByPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userPwd":$("#userPwd").val(),
            "roleName":$("input[name=roleName]:checked").val()
        },
        success:function (data) {
            if (data.message=="注册成功"){
                console.log(data);
                alert(data.message);
                window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}
//发送登录手机验证码
function sendLoginPhonePwd() {
    $.ajax({
        url:"/userApp/sendLoginPhonePwd",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
            console.log(data);
                alert(data.message);
        }
    });
}
//登录手机
function loginByPhoneAndCode() {
    $.ajax({
        url:"/userApp/loginByPhoneAndCode",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userVerifyCode":$("#userVerifyCode").val()
        },
        success:function (data) {
            if (data.message=="登录成功"){
                console.log(data);
                alert(data.message);
                window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}
//手机找回密码
function  getCodeByPhone() {
    $.ajax({
        url:"/userApp/getCodeByPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
            console.log(data);
                alert(data.message);
        }
    });
}

//手机找回密码，验证验证码和手机
function getPwdByPhone() {
    $.ajax({
        url:"/userApp/getPwdByPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userVerifyCode":$("#phoneCode").val()
        },
        success:function (data) {
            $("#userPhone").val(data.data.userPhone);
            if (data.message=="验证成功，跳转下一步"){
                console.log(data);
                alert(data.message);
                window.location.href="./updateUserPwd.html";
            }else {
                alert(data.message);
            }
        }
    });
}
//手机找回密码，密码重置，返回登录页面
function  updateUserPwdByPhone() {
    $.ajax({
        url:"/userApp/updateUserPwdByPhone",
        type:"post",
        data:{
            "userPwd":$("#userPwdPhone").val(),
            "confirmPwd":$("#confirmPwdPhone").val()
        },
        success:function (data) {
            if (data.message=="重置密码成功"){
                console.log(data);
                alert(data.message);
                window.location.href="./loginApp.html";
            }else {
                alert(data.message);
            }
        }
    });
}

//手机绑定，发送验证码
function  sendBindPhoneCode() {
    $.ajax({
        url:"/userApp/sendBindPhoneCode",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
            console.log(data);
            alert(data.message);
        }
    });
}

//手机绑定，验证验证码和手机
function bindPhone() {
    $.ajax({
        url:"/userApp/bindPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userVerifyCode":$("#userVerifyCode").val()
        },
        success:function (data) {
            if (data.message=="绑定手机成功"){
                console.log(data);
                alert(data.message);
                window.location.href="./studentInfo.html";
            }else {
                alert(data.message);
            }
        }
    });
}
