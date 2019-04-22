//发送邮箱密码
function sendRegEmailPwd() {
    $.ajax({
        url:"user/sendRegEmailPwd",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
        },
        success:function (data) {
            if (data=="0"){
                alert("前往邮箱获取密码");
               window.location.href="./regByEmail.html";
            }else if(data=="1"){
                alert("为空");
            }else if(data=="2"){
                alert("已存在");
            }else {
                alert("失败");
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
            "userPwd":$("#userPwd").val()
        },
        success:function (data) {
            if (data=="0"){
                alert("成功");
                window.location.href="./index.html";
            }else if(data=="1"){
                alert("为空");
            }else if(data=="2"){
                alert("已存在");
            }else {
                alert("失败");
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
            if (data=="0"){
                alert("成功");
                window.location.href="./index.html";
            }else if(data=="1"){
                alert("为空");
            }else {
                alert("失败");
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
            if (data=="0"){
                alert("前往邮箱获取密码");
                window.location.href="./login.html";
            }else if(data=="1"){
                alert("为空");
            }else if(data=="2"){
                alert("不存在");
            }else {
                alert("失败");
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
            if (data=="0"){
                alert("请查收密码");
            }else if(data=="1"){
                alert("为空")
            } else if(data=="2"){
                alert("已存在")
            } else {
                alert("发送失败");
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
            if (data=="0"){
                alert("成功");
                window.location.href="./index.html";
            }else if(data=="1"){
                alert("为空");
            }else if(data=="2"){
                alert("已存在");
            }else {
                alert("失败");
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
            if (data=="0"){
                alert("请查收验证码");
            }else if(data=="1"){
                alert("为空")
            } else if(data=="2"){
                alert("已存在")
            } else {
                alert("发送失败");
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
            if (data=="0"){
                alert("成功");
                window.location.href="./index.html";
            }else if(data=="1"){
                alert("为空");
            }else if(data=="2"){
                alert("已存在");
            }else {
                alert("失败");
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
            if (data=="0"){
                alert("请查收密码");
                window.location.href="./login.html";
            }else if(data=="1"){
                alert("为空")
            } else if(data=="2"){
                alert("不存在")
            } else {
                alert("发送失败");
            }
        }
    });
}