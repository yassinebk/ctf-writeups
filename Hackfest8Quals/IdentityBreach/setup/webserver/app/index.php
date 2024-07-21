<?php
include "../phpconf/config.php";

session_start();
?>

<!doctype html>
<HTML>
	<head>
	  <title>Shipment Command Center</title>
		<meta charset="UTF-8">
		<meta name="description" content="">
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="stylesheet" type="text/css" href="//fonts.googleapis.com/css?family=Lobster" />
		<link rel="stylesheet" href="css/style.css" type="text/css">
	</head>
	<body>

		<!-- Nav -->
		<?php include_once("../phpconf/navbar.php"); ?>
		<!-- Nav -->
		<div class="content">

			<div class="bannerleft">
			  <h1>
				On Shipment, every move is a gamble. Make yours count!
			  </h1>
				<p>- Kyle Garrick</p>
			</div>
			<div class="Map">
				<img src='./static/shippement.jpg'>
			</div>

		</div>


	</body>


</html>
