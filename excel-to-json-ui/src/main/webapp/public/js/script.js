async function postFile()  {

    const form = document.getElementById("form");
    form.style.display = "none";
    const formData = new FormData();

    formData.append('file', document.getElementById('file-to-upload').files[0]);

    const request = new XMLHttpRequest();


    const file1Size = document.getElementById('file-to-upload').files[0].size;
    console.log(file1Size);

    request.upload.addEventListener('progress', function (e) {
        if (e.loaded <= file1Size) {
            const percent = Math.round(e.loaded / file1Size * 100);
            document.getElementById('progress-bar-file').style.width = percent + '%';
            document.getElementById('progress-bar-file').innerHTML = percent + '%';
        }

        if (e.loaded == e.total) {
            document.getElementById('progress-bar-file').style.width = '100%';
            document.getElementById('progress-bar-file').innerHTML = '100%';
        }
    });

    request.onreadystatechange = function() {
        if (request.readyState == XMLHttpRequest.DONE) {
            document.getElementById('progress-bar-file').style.display = "none"
            // document.getElementsByTagName("html")[0].innerText = request.responseText
            // document.getElementById("response-text").style.display = "block"
            // alert();
            window.open(window.location.href +"/fileuploadservlet","_self");
        }
    }

    request.open('post', 'fileuploadservlet');
    request.timeout = 45000;
    request.send(formData);
}