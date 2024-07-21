<?php
$db = @mysqli_connect('db', 'random_random', 'random_random');
@mysqli_select_db($db,"task");
@mysqli_query($db,"UPDATE Redacted_TABLENAME SET Redacted_COLUMN = 'hackfest{flag}';");
?>
