//展现隐私功能
function  showPrivacy() {
    $.ajax({
        url:"/userApp/showPrivacy",
        type:"post",
        success:function (data) {
            console.log(data);
            $("input:radio[name='userPrivacy'][value="+data.data.userPrivacy+"]").attr('checked','true');
        }
    });
}
showPrivacy();
//修改隐私
function  updatePrivacy() {
    $.ajax({
        url:"/userApp/updatePrivacy",
        type:"post",
        data:{
            "userPrivacy":$("input[name=userPrivacy]:checked").val(),
        },
        success:function (data) {
            console.log(data);
            if (data.message=="修改成功"){
                alert(data.message);
                window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}


//展现账号
function  showAccount() {
    $.ajax({
        url:"/userApp/showAccount",
        type:"post",
        success:function (data) {
            $("#userEmail").val(data.data.userEmail);
            $("#userPhone").val(data.data.userPhone);
        }
    });
}
showAccount();

//展现设置密码
function  showModifyUserPwd() {
    $.ajax({
        url:"/userApp/showModifyUserPwd",
        type:"post",
        success:function (data) {
            $("#userEmail").val(data.data.userEmail);
            $("#userPhone").val(data.data.userPhone);
        }
    });
}
showModifyUserPwd();
//修改隐私
function  modifyUserPwd() {
    $.ajax({
        url:"/userApp/modifyUserPwd",
        type:"post",
        data:{
            "userOldPwd":$("#userOldPwd").val(),
            "userNewPwd":$("#userNewPwd").val(),
            "confirmPwd":$("#confirmPwd").val(),
        },
        success:function (data) {
            console.log(data);
            if (data.message=="修改成功"){
                alert(data.message);
                // window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}