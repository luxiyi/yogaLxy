//展现学生信息

function  showStudentInfo() {
    $.ajax({
        url:"/user/showStudentInfo",
        type:"post",
        success:function (data) {
            console.log(data);
                // $("#userHeadimg").val(data.data.userHeadimg);
                $("#userEmail").val(data.data.userEmail);
                $("#userPhone").val(data.data.userPhone);
                $("#idcard").val(data.data.idcard);
                $("#realName").val(data.data.realName);
                $("#userNickname").val(data.data.userNickname);
                // $("input:radio[name='userPrivacy'][value="+data.data.userPrivacy+"]").attr('checked','true');
                $("input:radio[name='sex'][value="+data.data.sex+"]").attr('checked','true');
        }
    });
}
showStudentInfo();
//修改学生个人信息
function  updateStudentInfo() {
    $.ajax({
        url:"/user/updateStudentInfo",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userPhone":$("#userPhone").val(),
            "idcard":$("#idcard").val(),
            "realName":$("#realName").val(),
            "userNickname":$("#userNickname").val(),
            "userPrivacy":$("input[name=userPrivacy]:checked").val(),
            "sex":$("input[name=sex]:checked").val()
        },
        success:function (data) {
            if (data.message=="成功完善信息"){
                console.log(data);
                alert(data.message);
                window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}

//修改学生个人头像  $('#userHeadimg')[0].files[0] 后面必须加.name
function  uploadHead() {
    alert($('#userHeadimg')[0].files[0].name);
    var formData = new FormData();
    formData.append("userHeadimg", $('#userHeadimg')[0].files[0].name);
    $.ajax({
        url:"/user/uploadHead",
        type:"post",
        data:formData,
        contentType : false,
        processData : false,
        // enctype:"multipart/form-data",
        // mimeType:"multipart/form-data",
        dataType: "json",
        // async:false,
        // cache:false,
        success:function (data) {
            console.log(data);
                alert(data.message);
        }
    });
}