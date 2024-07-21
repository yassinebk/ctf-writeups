<?php
defined('in') || die("No direct access");
if ((isset($_GET['content'])) and (isset($_GET['dir'])) ) {
	$content=base64_decode($_GET['content']);
	$dir=$_GET['dir'];
	// No filters , we can only add slashes to absolute path and maybe overwriting files
	if ((strpos($dir, ':') !== false) or (strpos($dir, '.') !== false)) {
		die('wow');
	}

	// .jpg
	$filename=bin2hex(random_bytes(6)).'.jpg';

	
	//
	$dir=$dir.'/'.$filename;

	file_put_contents($dir, $content);
	echo 'file uploaded => '.$dir;
}