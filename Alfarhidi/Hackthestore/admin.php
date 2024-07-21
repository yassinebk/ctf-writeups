<?php
session_start();

if (@$_SESSION['role'] !== 'admin') {
    header('Location: dashboard.php');
    exit;
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
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }

        .container {
            max-width: 800px;
            padding: 20px;
            background-color: #fff;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        input {
            padding: 10px;
            margin: 5px 0;
            border: 1px solid #ccc;
            border-radius: 3px;
        }

        button {
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }
    </style>
    <title>Admin Dashboard</title>
</head>

<body>
    <h2>Admin Dashboard</h2>
    <div class="container">
        
        <h2>upload files</h2>
        <form action="upload.php" method="post" enctype="multipart/form-data">
            <input type="file" name="fileToUpload" id="fileToUpload">
            <button type="submit" name="submit">Upload File</button>
        </form>
    </div>

    <div class="container">
        <h2>Entity Search</h2>
        <form action="entity_search.php" method="post">
            <label for="entityName">Enter Entity Name:</label>
            <select id="entityName" name="entityName">
                <option value="user">User</option>
                <option value="product">Product</option>
            </select>
            <button type="submit" name="search">Search</button>
        </form>

        
    </div>

</body>

</html>
