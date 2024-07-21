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
<!DOCTYPE html>
<html>
<head>
  <title>🎷 Smooth Jazz</title>
  <style>
    body {
      background-color: #f8f8f8;
      font-family: Arial, sans-serif;
    }

    .container {
      max-width: 400px;
      margin: 100px auto;
      padding: 20px;
      background-color: #fff;
      border-radius: 5px;
      box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
      text-align: center;
    }

    h1 {
      color: #333;
    }

    form {
      margin-top: 20px;
    }

    label, input {
      display: block;
      margin-bottom: 10px;
    }

    input[type="text"],
    input[type="password"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
      box-sizing: border-box;
    }

    input[type="submit"] {
      width: 100%;
      padding: 10px;
      background-color: #4287f5;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }

    .music-player {
      margin-top: 20px;
    }

    h2 {
      color: #333;
    }

    audio {
      width: 100%;
      margin-top: 10px;
    }

    .message {
      margin-top: 10px;
    }
  </style>
</head>
<body>
  <div class="container">
    <h1>Smooth Jazz</h1>
    <form method="post" action="/">
      <label for="username">Username:</label>
      <input type="text" id="username" name="username" placeholder="Enter your username">

      <label for="password">Password:</label>
      <input type="password" id="password" name="password" placeholder="Enter your password">

      <input type="submit" value="Login">
    </form>
    <div class="music-player">
      <audio src="/offering-larry-stephens.mp3" id="audio"></audio>
      If you are stuck, you can <a href="javascript:document.getElementById('audio').play()">listen to some smooth jazz</a>.
    </div>
    <div id="message" class="message">
      <p><?= $message ?? '' ?></p>
    </div>
  </div>
</body>
</html>
