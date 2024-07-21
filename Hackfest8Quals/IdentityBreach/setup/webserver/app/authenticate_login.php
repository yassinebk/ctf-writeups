<?php
ini_set("session.cookie_httponly", true); // Set HTTP only cookie for session
include "../phpconf/config.php";

session_start();

try {
    // Establish a connection to the MySQL database using PDO
    $conn = new PDO("mysql:host=$db_address;dbname=intelDB", $db_username, $db_password);
    // Set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch(PDOException $e) {
    // If the connection fails, display an error message and stop execution
    echo "Connection to the database failed: " . $e->getMessage();
    die();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST["username"]) && isset($_POST["password"])) {
        $username = $_POST["username"];
        $password = $_POST["password"];

        // For debugging purposes
        error_log("Username: $username Password: $password");

        try {
            // Prepare and execute the SQL statement to retrieve user credentials
            $statement = $conn->prepare('SELECT username, password FROM soldiers WHERE username = ? and password = ?');
            $statement->bindParam(1, $username, PDO::PARAM_STR);
            $statement->bindParam(2, $password, PDO::PARAM_STR);
            $statement->execute();

            // Fetch the result
            $row = $statement->fetch();

            // If user credentials are found, set session variables and redirect
            if ($row) {
                $_SESSION["loggedin"] = true;
                $_SESSION["user"] = $username;
                header('Location: /account.php');
                die();
            } else {
                // If user credentials are not found, redirect to login page with an error
                header('Location: /login.php?error=wrong');
                die();
            }
        } catch (PDOException $e) {
            // If there's an error executing the query, display an error message
            echo "Error executing SQL query: " . $e->getMessage();
        }
    }
}
?>
