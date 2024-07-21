<?php
defined('in') || die("No direct access");
if (isset($_GET['filename'])){
	$filename=$_GET['filename'];
	//  Here we have two filters we cannot : ..
	if ((strpos($filename, ':') !== false) or (strpos($filename, '..') !== false)) {
		die('wow');}
		$file_content=file_get_contents($filename);

	// The evil part, we need to bypass this !
	if (strpos($file_content, '=<?')!=false){
		$msg=file_get_contents('data://text/plain;base64,d293');
		die($msg);
	}
	// Including the filename
	include($filename);
}