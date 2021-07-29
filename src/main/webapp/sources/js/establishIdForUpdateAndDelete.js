let buttons = document.getElementsByTagName("button");
let idInputForUpdate = document.getElementById("update").getElementsByTagName("input")[0];
let spanWithIdForUpdate = document.getElementsByClassName("span_with_id")[0];
let idInputForDelete = document.getElementById("delete").getElementsByTagName("input")[0];
let spanWithIdForDelete = document.getElementsByClassName("span_with_id")[1];
for (let i = 0; i < buttons.length; i++) {
    let button = buttons[i];
    if (button.getAttribute("name") === "update") {
        button.onclick = function () {
            idInputForUpdate.value = button.value;
            spanWithIdForUpdate.innerText = button.value;
        }
    } else if (button.getAttribute("name") === "delete") {
        button.onclick = function () {
            idInputForDelete.value = button.value;
            spanWithIdForDelete.innerText = button.value;
        }
    }
}
