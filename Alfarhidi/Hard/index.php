<?php

require __DIR__.'/vendor/autoload.php';

function sanitize_serialize($var, $default=[])
{
    $var = trim($var);
    echo $var ;
    if (is_string($var) && ! empty($var)) {
        return unserialize($var);
} else {
    return $default;
}
}

if (isset($_GET['source'])) {
    highlight_file(__FILE__);
    exit();
}

if (isset($_POST['data'])) {
    if (strpos($_POST['data'],'a:0') !== false){
        sanitize_serialize($_POST['data']);
    }
    else{
        echo "You are not allowed to do that!";
    }
}

?>
