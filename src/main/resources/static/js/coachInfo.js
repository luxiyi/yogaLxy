//展现教练信息
function showCoachInfo() {
    $.ajax({
        url:"/userApp/showCoachInfo",
        type:"post",
        success:function (data) {
            console.log(data);
                $("#userEmail").val(data.currentUser.userEmail);
                $("#userPhone").val(data.currentUser.userPhone);
                $("#idcard").val(data.currentUser.idcard);
                $("#realName").val(data.currentUser.realName);
                $("#userNickname").val(data.currentUser.userNickname);
                $("input:radio[name='userPrivacy'][value="+data.currentUser.userPrivacy+"]").attr('checked','true');
                $("input:radio[name='sex'][value="+data.currentUser.sex+"]").attr('checked','true');
                $("#startTime").val(data.currentCoach.startTime);
                $("#endTime").val(data.currentCoach.endTime);
                $("#coachStyle").val(data.currentCoach.coachStyle);
                 $("input:radio[name='coachType'][value="+data.currentCoach.coachType+"]").attr('checked','true');
                 $("input:radio[name='coachStatus'][value="+data.currentCoach.coachStatus+"]").attr('checked','true');
                 $("input:radio[name='fullClass'][value="+data.currentCoach.fullClass+"]").attr('checked','true');
                 $("input:radio[name='authentication'][value="+data.currentCoach.authentication+"]").attr('checked','true');
                $("#expectedSalary").val(data.currentCoach.expectedSalary);
                $("#coachDetail").val(data.currentCoach.coachDetail);
        }
    });
}
showCoachInfo();
//修改教练个人信息
function  updateCoachInfo() {
    $.ajax({
        url:"/userApp/updateCoachInfo",
        type:"post",
        data:{
            "userEmail":$("#userEmail").val(),
            "userPhone":$("#userPhone").val(),
            "idcard":$("#idcard").val(),
            "realName":$("#realName").val(),
            "userNickname":$("#userNickname").val(),
            "userPrivacy":$("input[name=userPrivacy]:checked").val(),
            "sex":$("input[name=sex]:checked").val(),
            "coachType":$("input[name=coachType]:checked").val(),
            "coachStatus":$("input[name=coachStatus]:checked").val(),
            "fullClass":$("input[name=fullClass]:checked").val(),
            "authentication":$("input[name=authentication]:checked").val(),
            "startTime":$("#startTime").val(),
            "endTime":$("#endTime").val(),
            "coachStyle":$("#coachStyle").val(),
            "expectedSalary":$("#expectedSalary").val(),
            "coachDetail":$("#coachDetail").val(),
        },
        success:function (data) {
            console.log(data);
            if (data.message=="成功完善信息"){
                alert(data.message);
                window.location.href="./index.html";
            }else {
                alert(data.message);
            }
        }
    });
}