<?php
ini_set("session.cookie_httponly", true);
include "../phpconf/config.php";

session_start(); // Moved to the top

try {
    $conn = new PDO("mysql:host=$db_address;dbname=intelDB", $db_username, $db_password);
    // set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    echo "Connection to the database failed." . $e->getMessage();
    exit(); // Terminate the script if the database connection fails
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST["username"]) && isset($_POST["password"])) {
        // Check if user exists
        $username = trim($_POST["username"]); // trim will remove all white spaces
        $password = $_POST["password"];

        $statement = $conn->prepare('SELECT username FROM soldiers WHERE username = ?');
        $statement->bindParam(1, $username, PDO::PARAM_STR);

        $statement->execute();
        $row = $statement->fetch();
        if ($row) {
            header('Location: /register.php?status=exists');
            exit(); // Terminate the script after redirect
        } else {
            $statement= $conn->prepare('INSERT into soldiers (username, password) VALUES (?,?)');
            $statement->execute([$username, $password]);
            header('Location: /register.php?status=success');
            exit(); // Terminate the script after redirect
        }
    }
}
?>
