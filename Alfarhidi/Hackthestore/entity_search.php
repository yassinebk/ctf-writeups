<?php
error_reporting(0);
session_start();

if ($_SESSION['role'] !== 'admin') {
    header('Location: dashboard.php');
    exit;
}

function getEntityData($entityName) {
    require_once 'bootstrap.php';
     if ($entityName === 'user'){
        $entityName = 'MyApp\\user';
     }
     else if ($entityName === 'product'){
        $entityName = 'MyApp\\Product';
     }
     $qb = $entityManager->createQueryBuilder();
     $qb->select('u')
        ->from($entityName, 'u')
        ->setMaxResults(1);

    try {
        $id = $qb->getQuery()->getOneOrNullResult();
        if ($id) {
        return $id->getId();
}
else {
    return false;
}

}
catch (\Doctrine\ORM\ORMException $e) {
    return false;
}
}
        if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['search'])) {
            $entityName = $_POST['entityName'];
            $resultData = getEntityData($entityName);

            if ($resultData) {
                echo '<table>';
                echo '<tr><th>Entity ID</th><th>Entity Name</th></tr>';
                    echo '<tr>';
                    echo '<td>' . htmlspecialchars($resultData) . '</td>';
                    echo '<td>' . htmlspecialchars($entityName) . '</td>';
                    echo '</tr>';
                echo '</table>';
            } else {
                echo '<p>No results found for entity: ' . htmlspecialchars($entityName) . '</p>';
            }
        }
    
        ?>