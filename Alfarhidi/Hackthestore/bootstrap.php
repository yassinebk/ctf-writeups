<?php

require_once 'vendor/autoload.php';

use Doctrine\ORM\Tools\Setup;
use Doctrine\ORM\EntityManager;

$connectionParams = [
    'driver' => 'pdo_sqlite',
    'path' => './db.sql',
];
$config = Setup::createAnnotationMetadataConfiguration([__DIR__ . '/src'], true, null, null, false);
$entityManager = EntityManager::create($connectionParams, $config);

return $entityManager;
?>