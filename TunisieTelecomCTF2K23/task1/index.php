<?php
define('in', true);
error_reporting(0);

if ($_GET['page']==='upload.php'){ //guess my file extention
	include('upload.php');
}
else if ($_GET['page']==='view.php'){ //guess my file extention
	include('view.php');}
else{
	die('welcome');
}
