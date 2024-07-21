<?php 
// %[argnum$][flags][width][.precision]specifier. 

// admin"%1$2c union select 1,2 -- &password=aahOdB

function mysql_fquery($query,$params){
    echo vsprintf($query, $params);
}

if (isset($_POST['username']) && isset($_POST['password'])) {
   $username = strtr($_POST['username'], ['"' => '\\"', '\\' => '\\\\']);
   $password=sha1($_POST['password']);

   echo "\n<br>";
   echo 'password is '. $_POST['password'].' '.$password;
   echo "\n<br>";
   mysql_fquery('SELECT * FROM users WHERE username = "%s"', [$username]);
   echo "\n<br>";
   echo $password;
   echo "\n<br>";
   echo sprintf("%2c",$password);
   echo "\n<br>";

  echo "second password query: ";

    mysql_fquery('SELECT * FROM users WHERE username = "'.$username.'" AND password = "%s"', [$password]);

  $htmlsafe_username = htmlspecialchars($username, ENT_COMPAT | ENT_SUBSTITUTE);
  echo "\n<br>"; 
  echo "Safe_username: ".$htmlsafe_username;
  echo "\n<br>"; 
  $greeting = $username === "admin" 
      ? "Hello $htmlsafe_username, the server time is %s and the flag is %s"
      : "Hello $htmlsafe_username, the server time is %s";

  $message = vsprintf($greeting, [date('Y-m-d H:i:s'), "flag{x}"]);
   echo "Hello $htmlsafe_username, the server time is %s and the flag is %s";
  echo "\n<br>"; 
   echo $message;
}


?>

