//发送邮箱密码
function sendRegEmailCode() {
    $.ajax({
        url:"user/sendRegEmailCode",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
        },
        success:function (data) {
            if (data=="前往邮箱获取密码"){
                alert(data);
               window.location.href="./regByEmail.html";
            }else if(data=="不能为空"){
                alert(data);
            }else if(data=="邮箱格式不匹配"){
                alert(data);
            }else if(data=="已存在"){
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
            "userPwd":$("#userPwd").val()
        },
        success:function (data) {
            if (data=="注册成功"){
                alert(data);
                window.location.href="./index.html";
            }else if(data=="不能为空"){
                alert(data);
            }else if(data=="邮箱格式不匹配"){
                alert(data);
            }else if(data=="密码格式不匹配"){
                alert(data);
            }else if(data=="注册失败"){
                alert(data);
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
            }else if(data=="不能为空"){
                alert(data);
            }else {
                alert(data);
                window.location.reload();
            }
        }
    });
}
//邮箱找回密码
function getOldEmailPwd() {
    $.ajax({
        url:"user/getOldEmailPwd",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
        },
        success:function (data) {
            if (data=="前往邮箱获取密码"){
                alert(data);
                window.location.href="./login.html";
            }else if(data=="不能为空"){
                alert(data);
            }else{
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
            }else if(data=="不能为空"){
                alert(data)
            } else if(data=="手机号已存在"){
                alert(data)
            } else {
                alert(data);
            }
        }
    });
}
//注册手机
function regByPhone() {
    $.ajax({
        url:"user/regByPhone",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
            "userPwd":$("#userPwd").val()
        },
        success:function (data) {
            if (data=="注册成功"){
                alert(data);
                window.location.href="./index.html";
            }else if(data=="不能为空"){
                alert(data);
            }else if(data=="已存在"){
                alert(data);
            }else if(data=="手机格式不匹配"){
                alert(data);
            }else {
                alert("注册失败");
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
            if (data=="请在手机上查收验证码"){
                alert(data);
            }else if(data=="手机格式不匹配"){
                alert(data);
            } else if(data=="手机号不存在"){
                alert(data);
            } else {
                alert(data);
            }
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
            }else if(data=="不能为空"){
                alert(data);
            }else if(data=="手机格式不匹配"){
                alert(data);
            }else if(data=="手机号不存在"){
                alert(data);
            }else {
                alert(data);
            }
        }
    });
}
//手机找回密码
function  getOldPhonePwd() {
    $.ajax({
        url:"user/getOldPhonePwd",
        type:"post",
        data:{
            "userPhone":$("#userPhone").val(),
        },
        success:function (data) {
            if (data=="请在手机上查收密码"){
                alert(data);
                window.location.href="./login.html";
            }else if(data=="不能为空"){
                alert(data);
            } else if(data=="手机号不存在"){
                alert(data);
            } else {
                alert(data);
            }
        }
    });
}