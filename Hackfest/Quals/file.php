<?php
// I wanted to make shopping in my drug store even faster so I wrote a compiled php extension especially for that specific thing.

error_reporting(0);
session_start();

if ((isset($_GET["res"]) && ($_GET["res"] === "1")) || !isset($_SESSION["cash"])) {
	$_SESSION["cash"] = 100;

	if (isset($_SESSION["b"]))
		unset($_SESSION["b"]);

	$_SESSION["b"]["Chocolate"] = 0;
	$_SESSION["b"]["Camel"] = 0;
	$_SESSION["b"]["Coca"] = 0;
	header("Location: index.php");
	die();
}

if ($_SERVER["REQUEST_METHOD"] === "POST") {
	if (isset($_POST["product"]) && !empty($_POST["product"])) {
		$error = "";
		$res = shop($_POST["product"], $_SESSION["cash"]);
		if ($res === -2) {
			$error = "Insufficient Cash!";
		}
		else if (substr((string)$res, 0, 9) === "Hackfest{") {
			$_SESSION["b"]["Flag"] = $res;
		}
		else {
			$_SESSION["cash"] = $res;
			$_SESSION["b"][$_POST["product"]] += 1;
		}
	}
}
?>
<!DOCTYPE html>
<html>
<head>
  <title>Drug Store</title>
  <link rel="stylesheet" href="style.css">
</head>
<body>
  <header>
    <h1>Drug Store</h1>
    <!--<h4><a href="phpinfo.php">Config</a></h4>-->
    <h3><a class="reset" href="index.php?res=1">Reset</a></h3>
  </header>
  <main>
  <div id="balance">Current Balance: <?php echo (isset($_SESSION["cash"]) ? $_SESSION["cash"] : "100") . " $"; ?></div>
  <div style="text-align: center; color: red; font-weight: bold;"> <?php echo ($error ? $error : ""); ?></div>
    <div class="product">
      <h2>Chocolate: 2 $</h2>
      <div>
         <img src="img.php?img=chocolate.png" alt="Chocolate" />
      </div>
      <form method="POST" action="index.php">
         <input type="hidden" name="product" value="Chocolate">
         <button type="submit">Order Now</button>
      </form>
    </div>
    <div class="product">
      <h2>Coca Cola: 3 $</h2>
      <div>
         <img src="img.php?img=coca.png" alt="Coca Cola" />
      </div>
      <form method="POST" action="index.php">
         <input type="hidden" name="product" value="Coca">
         <button type="submit">Order Now</button>
      </form>
    </div>
    <div class="product">
      <h2>Camel Blue: 10 $</h2>
      <div>
         <img src="img.php?img=camel.png" alt="Camel Blue" />
      </div>
      <form method="POST" action="index.php">
         <input type="hidden" name="product" value="Camel">
         <button type="submit">Order Now</button>
      </form>
    </div>
    <div class="product">
      <h2>Flag: 1337 $</h2>
      <form method="POST" action="index.php">
         <input type="hidden" name="product" value="Flag">
	 <button type="submit">Order Now</button>
      </form>
    </div>
  <?php
	if (isset($_SESSION["b"])) {
	    echo '<div id="myCart">Owned</div><br />';
	    echo '<ul id="cart">';
		foreach ($_SESSION["b"] as $k => $v) {
			echo "<li>{$k}: {$v}</li>";
		}
	    echo "</ul>\n</div>";
	}
  ?>
  </main>
</body>
</html>

