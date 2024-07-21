<?php
error_reporting(0);
session_start();
require_once 'bootstrap.php'; 
$debugMode = False;

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $hashedPassword = hash('sha256', $password);
    $qb = $entityManager->createQueryBuilder();
    $qb->select('u')
        ->from('MyApp\\user', 'u')
        ->where('u.username = :username')
        ->setParameter('username', $username);

    try {
        $user = $qb->getQuery()->getOneOrNullResult();
        if ($user) {
            if ($hashedPassword === $user->getPasswordHash()) {
                $randomString = bin2hex(random_bytes(16)); 
                $_SESSION['authenticated'] = $username . '_' . $randomString;
                if ($user->getUsername() === 'Administrator') {
                    $_SESSION['role'] = 'admin'; 
                    header('Location: admin.php');
                    exit;
                }else{
                    header('Location: dashboard.php');
                    exit;
                }
                
                exit; 
            } else {
                echo 'Invalid password';
            }
        } else {
            echo 'User not found';
        }
    } catch (\Doctrine\ORM\ORMException $e) {
        if ($debugMode) {
            echo 'Database error: ' . $e->getMessage();
        } else {
            echo 'An error occurred while processing your request.';
        }
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
        }

        .container {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin-top: 10px;
        }

        input {
            padding: 10px;
            margin: 5px 0;
            border: 1px solid #ccc;
            border-radius: 3px.
        }

        button {
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
    </style>
    <title>Authentication Form</title>
</head>
<body>
    <div class="container">
        <form method="post">
            <h2>Login</h2>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            <button type="submit">Login</button>
        </form>
    </div>



</body>
</html>
