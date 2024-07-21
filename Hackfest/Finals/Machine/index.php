        input[type="text"] {
            padding: 5px;
            width: 300px;
        }
        input[type="submit"] {
            padding: 5px 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        pre {
            background-color: #f1f1f1;
            padding: 10px;
        }
        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <h1>Team Members Search</h1>
    <form method="post">
        <label for="command">Enter the team member you want to check:</label>
        <input type="text" name="command" id="command">
        <input type="submit" value="Search">
    </form>

    <?php
    $blacklist = ['python', 'python2', 'python3', 'python3.10', 'ls', 'exec', 'rm', 'cat', 'touch', 'whoami', 'sh', 'bash', 'dev', 'tcp', 'rm', 'mkfifo', 'nc', 'tmp', 'system', 'cat', 'less', 'tail', 'head', 'more', '@', '%', '^', '/', ',', '<', '&'];
    if(isset($_POST['command'])){
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        $command = $_POST['command'];

        foreach ($blacklist as $malicious_command) {
            if (strpos($command, $malicious_command) !== false) {
                echo '<p class="error">WTF! Are you trying to hack me?!</p>';
                exit();
            }
        }
        if(!empty($_POST['command'])){
        $output = shell_exec($command);
        echo '<pre>' . htmlspecialchars($output) . '</pre>';
    }}}
    ?>
</body>
</html>