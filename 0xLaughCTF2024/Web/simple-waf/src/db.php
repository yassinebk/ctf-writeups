<?php


$servername = "127.0.0.1";
$username = "ctf";
$password = "ctf123";
$dbname = "simplewaf";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}

?>