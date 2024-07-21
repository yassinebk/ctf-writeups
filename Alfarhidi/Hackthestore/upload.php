<?php
error_reporting(0);
session_start();

if ($_SESSION['role'] !== 'admin') {
    header('Location: dashboard.php');
    exit;
}
function sanitize_filename($filename) {

            $filename = basename($filename);
            $sanitize_name = preg_replace("/[^a-z0-9\.]/", "", strtolower($filename));

            $forbidden_extensions = '/ph(p[3457st]?|t|tml|ar)/i';
            if ( preg_match($forbidden_extensions, $filename) )
            {
                die('forbidden extensions');
            }

            $filename_without_ext = substr($sanitize_name, 0, strpos($sanitize_name, '.')); 
            if( empty($filename_without_ext) )
                $sanitize_name = uniqid().$sanitize_name;

            return $sanitize_name;

        }

$targetDirectory = "./src/uploads/";

if (isset($_FILES["fileToUpload"])) {
    $targetFile = $targetDirectory . sanitize_filename(basename($_FILES["fileToUpload"]["name"]));

        if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $targetFile)) {
            echo  'file uploaded';  
            
        } else {
            echo "Sorry, there was an error uploading your file.";
        }
} else {
    echo "No file was selected.";
}


?>
