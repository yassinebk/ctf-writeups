<?php
error_reporting(0);
session_start();

// if (!isset($_SESSION['authenticated'])) {
//     header('Location: index.php'); 
//     exit;
// }
// if (isset($_POST['logout'])) {
//     session_unset();
//     session_destroy();
//     header('Location: index.php'); 
//     exit;
// }
require_once 'bootstrap.php';
use MyApp\Product;


if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['productId'])) {
    $searchedProductId = $_GET['productId'];


    $qb = $entityManager->createQueryBuilder();
    $qb->select('p')
        ->from('MyApp\Product', 'p')
        ->where('p.productID = ' . $searchedProductId);




    $query = $qb->getQuery();
    var_dump($query->getDQL());
    $product = $query->getOneOrNullResult();

    echo '<div class="container">';
    echo '<h3>Results</h3>';
    echo '<table border="1">';
    echo '<tr><th>Product ID</th><th>Product Name</th></tr>';

    if ($product) {
        echo '<tr>';
        echo '<td>' . htmlspecialchars($product->getProductID()) . '</td>';
        echo '<td>' . htmlspecialchars($product->getproduct_name()) . '</td>';
        echo '</tr>';
    } else {
        echo '<tr><td colspan="2">No product found for Product ID: ' . htmlspecialchars($searchedProductId) . '</td></tr>';
    }

    echo '</table>';
    echo '</div>';
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

        .search-results {
            margin-top: 20px;
        }
    </style>
    <title>Dashboard</title>
</head>

<body>
    <div class="container">
        <h2>Dashboard</h2>
        <form method="get" action="dashboard.php">
            <label for="productId">Search Product ID:</label>
            <input type="text" id="productId" name="productId" required>
            <button type="submit">Search</button>
        </form>
    </div>
    <form method="post">
        <button type="submit" name="logout">Logout</button>
    </form>


</body>

</html>