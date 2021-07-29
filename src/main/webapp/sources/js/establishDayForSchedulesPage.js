let buttons = document.getElementsByTagName("button");
let forms = document.getElementsByTagName("form");

for (let i = 0; i < buttons.length; i++) {
    let button = buttons[i];
    if (button.getAttribute("name") === "day_teacher") {
        button.onclick = function () {
            let dayTeacherForm = forms[0];
            let day = button.value;
            document.getElementsByClassName("modal-title")[0]
                .getElementsByTagName("span")[0].innerText = day;
            dayTeacherForm.getElementsByTagName("input")[0].value = day;
        }
    } else if (button.getAttribute("name") === "day_student") {
        button.onclick = function () {
            let dayStudentForm = forms[1];
            let day = button.value;
            document.getElementsByClassName("modal-title")[1]
                .getElementsByTagName("span")[0].innerText = day;
            dayStudentForm.getElementsByTagName("input")[0].value = day;
        }
    } else if (button.getAttribute("name") === "period_teacher") {
        button.onclick = function () {
            let periodTeacherForm = forms[2];
            let beginDay = button.value;
            document.getElementsByClassName("modal-title")[2]
                .getElementsByTagName("span")[0].innerText = beginDay;
            periodTeacherForm.getElementsByTagName("input")[0].value = beginDay;
        }
    } else if (button.getAttribute("name") === "period_student") {
        button.onclick = function () {
            let periodStudentForm = forms[3];
            let beginDay = button.value;
            document.getElementsByClassName("modal-title")[3]
                .getElementsByTagName("span")[0].innerText = beginDay;
            periodStudentForm.getElementsByTagName("input")[0].value = beginDay;
        }
    }
}
