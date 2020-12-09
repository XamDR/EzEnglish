package drm.ezenglish.util

const val template = """
<html>
<head>
    <meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script>
        function setCorrect(id){document.getElementById(id).className = 'editable correct';}
        function setWrong(id){document.getElementById(id).className = 'editable wrong';}
    </script>
    <style>
        div {
            line-height: 1.5;
            font-family: sans-serif;
            font-size: 1rem;
            color: #fff;
        }
        .editable {
            border-width: 0px;
            border-bottom: 1px solid #cccccc;
            font-family: monospace;
            display: inline-block;
            outline: 0;
            color: #0000ff;
            font-size: 105%%;
        }
        .editable.correct {
            color: #00ff00;
            border-bottom: 1px solid #00ff00;
        }
        .editable.wrong {
            color: #ff0000;
            border-bottom: 1px solid #ff0000;
        }
        .editable::-ms-clear {
            width: 0;
            height: 0;
        }
    </style>
</head>
<body>
<div>
    %s
</div>
</body>
</html>
"""