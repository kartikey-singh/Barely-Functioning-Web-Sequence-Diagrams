let defaultJson = {
    "sequence": [
        {
            "from": "A",
            "to": "B",
            "text": "From A to B"
        },
        {
            "from": "B",
            "to": "D",
            "text": "From B to D"
        },
        {
            "from": "C",
            "to": "D",
            "text": "From C to D"
        },
        {
            "from": "D",
            "to": "D",
            "text": "From D to D"
        },
        {
            "from": "D",
            "to": "A",
            "text": "From D to A"
        }
    ]
};

let stringified = JSON.stringify(defaultJson, null, 2);
let textarea = document.getElementById('code');
let url = "http://localhost:8081/diagram";
textarea.value = stringified;

let editor = CodeMirror.fromTextArea(textarea, {
    lineNumbers: true,
    border: true,
    theme: "eclipse",
    mode: "application/json",
    gutters: ["CodeMirror-lint-markers"],
    styleActiveLine: true,
    lint: true
});


$(document).ready(function () {
    $("#btnSubmit").click(function() {
        let json;
        try {
            json = JSON.stringify(JSON.parse(editor.getValue()));
            fetch(url, {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: json
            }).then(res => {
                if(!res.ok) {
                    $('#diagram').attr({'style': "display:none"});
                    $('#errorBox').attr({'style': "block"});
                    if(res.status === 429) {
                        $('#errorBox').text("Too many requests!!");
                    } else if(res.status === 400) {
                        res.json().then(errorText => {
                            $('#errorBox').text(JSON.stringify(errorText, null, 2));
                        });
                    } else {
                        $('#errorBox').text("Internal Server Error");
                    }
                } else {
                    res.blob().then(blob => {
                        $('#diagram').attr({
                            'src': URL.createObjectURL(blob),
                            'style': "block"
                        });
                        $('#errorBox').attr({'style': "display:none"});
                    });
                }
            });
        } catch (err) {
            $('#diagram').attr({'style': "display:none"});
            $('#errorBox').attr({'style': "block"});
            $('#errorBox').text(err.message);
        }
    });
});