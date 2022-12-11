function createList(allTasks) {
    for (var i = 0; i < allTasks.length; i++) {
        console.log(allTasks[i].text)
        const taskDiv = document.createElement("div");
        taskDiv.id = allTasks[i].id
        taskDiv.classList.add("task");
        const taskTextDiv = document.createElement("div");
        taskTextDiv.classList.add("task__text");
        taskTextDiv.appendChild(document.createTextNode(allTasks[i].text));
        const taskControlDiv = document.createElement("div");
        taskControlDiv.classList.add("task__control");
        const taskStatusDiv = document.createElement("div");
        taskStatusDiv.classList.add("task__status");
        if(allTasks[i].bought){
            taskStatusDiv.classList.add("green");
        }
        else{
            taskStatusDiv.classList.add("red");
        }

        const taskChangeStatusBtn = document.createElement("button");
        taskChangeStatusBtn.classList.add("task__change_status");
        taskChangeStatusBtn.appendChild(document.createTextNode('Отметить'));
        taskChangeStatusBtn.onclick = function(){
            console.log(changeStatus(taskDiv.id, function(status) {
                if(status){
                    taskStatusDiv.classList.remove("red");
                    taskStatusDiv.classList.add("green");
                }else{
                    taskStatusDiv.classList.remove("green");
                    taskStatusDiv.classList.add("red");
                }
            }));
        }

        const taskDeleteBtn = document.createElement("button");
        taskDeleteBtn.classList.add("task__delete");
        taskDeleteBtn.appendChild(document.createTextNode('Удалить'));
        taskDeleteBtn.onclick = function(){
            deleteTask(taskDiv.id, function(){
                            let element = document.getElementById("container");
                            while (element.firstChild) {
                                element.removeChild(element.firstChild);
                            }
                            console.log("удаление");
                            getJson();
                    });
        }

        taskControlDiv.appendChild(taskStatusDiv);
        taskControlDiv.appendChild(taskChangeStatusBtn);
        taskControlDiv.appendChild(taskDeleteBtn);

        taskDiv.appendChild(taskTextDiv);
        taskDiv.appendChild(taskControlDiv);

        document.getElementById("container").append(taskDiv);
    }
}

function getJson() {
    var xhr  = new XMLHttpRequest();
    var res;
    xhr.open("GET", "http://localhost:8000/api/tasks", true);
    xhr.onload = function (){
        res = [...JSON.parse(xhr.response).result];
        createList(res);
    }
    xhr.send(null);
}

function changeStatus(id, callback) {
    var xhr  = new XMLHttpRequest();
    xhr.open("PUT", "http://localhost:8000/api/tasks/" + id, false);
    xhr.onload = function (){
        const status = xhr.getResponseHeader("status");
        console.log(status);
        callback(status === "1")
    }
    xhr.send(null);
}

function addTask(text, callback) {
    var xhr  = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8000/api/tasks", false);
    xhr.setRequestHeader("text", text);
    xhr.onerror = function() {
      alert("Ошибка соединения");
    };
    xhr.onload = function (){
        callback();
    }
    xhr.send(null);
}

function deleteTask(id, callback) {
    var xhr  = new XMLHttpRequest();
    xhr.open("DELETE", "http://localhost:8000/api/tasks/" + id, false);
    xhr.onerror = function() {
      alert("Ошибка соединения");
    };
    xhr.onload = function (){
        callback();
    }
    xhr.send(null);
}

window.onload = getJson;