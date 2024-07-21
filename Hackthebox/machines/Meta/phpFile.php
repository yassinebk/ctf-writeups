<?php echo zlib_decode(base64_decode($_POST['data'])); ?>
<html>
<form method="POST">
<textarea name="data"></textarea>
<input type="submit">Submit</input>
</form>

</html>


