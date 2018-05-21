function loadDocument() {
    var xhr = new XMLHttpRequest();

    xhr.onreadystatechange = function (event) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                document.getElementById('documentTextarea').value = event.target.responseText;
            } else {
                alert(event.target.responseText);
            }
        }
    };

    xhr.open('GET',
           '/load-document?id=' + document.getElementById('documentIdText').value,
           true);
    xhr.send();
}

function uploadDocument() {
    var xhr = new XMLHttpRequest();
    var fd  = new FormData();

    fd.append('content', document.getElementById('documentTextarea').value);

    xhr.onreadystatechange = function (event) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                document.getElementById('documentIdText').value = event.target.responseText;
            } else {
                alert(event.target.responseText);
            }
        }
    };

    xhr.open('POST',
             '/upload-document',
             true);
    xhr.send(fd);
}

function search() {
    var xhr = new XMLHttpRequest();
    var fd  = new FormData();

    fd.append('tokens', document.getElementById('tokensTextarea').value);

    xhr.onreadystatechange = function (event) {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                var searchResult = JSON.parse(event.target.responseText);
                document.getElementById('resultTextarea').value = searchResult.result;
                if (searchResult.failedTasks > 0) {
                    document.getElementById("failedTaskCountSpan").innerHTML = 'Failed to process ' + searchResult.failedTasks + ' documents';
                }
            } else {
                alert(event.target.responseText);
            }
        }
    };

    xhr.open('POST',
             '/search',
             true);
    xhr.send(fd);
}