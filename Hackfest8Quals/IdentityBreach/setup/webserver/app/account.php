<?php
# start the session
session_start();
include "../phpconf/config.php";

if ($_SESSION["loggedin"] !== true) {
	header('Location: /login.php');
	die();
}
?>

<!doctype html>
<HTML>
	<head>
	  <title>Command Center Dashboard</title>
		<meta charset="UTF-8">
		<meta name="description" content="">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Lobster" />
		<link rel="stylesheet" href="css/style.css" type="text/css">
	</head>
	<body>

		<!-- Nav -->
		<?php include_once("../phpconf/navbar.php"); ?>
		<!-- NAV -->
		
		<div class="content">

			<div class="bannerleftAccount">
			  <h1 class="voila">
				Command Center Dashboard
			  </h1>
			  <?php
			  if ($_SESSION["user"] === "admin") {
			  	echo "<p>Hopefully you are the right captain. We are on hurry no time to check in, take the flag and let's move to the next operation.</p>";
				echo "<span style='color: red;'>$FLAG</span>";
				echo '<br><br><br><iframe width="500px" height="350px" src="https://www.youtube.com/watch?v=rqTmlyJp_6k&ab_channel=NightmaREE3Z" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>';
			  }
			  else {
				echo "<p>Welcome " . htmlEntities($_SESSION['user'], ENT_QUOTES);
				echo "<p>Noting to see here &macr;\_(&#12484;)_/&macr;</p>";
			  }
			  ?>
			</div>

		</div>


	</body>


</html>
