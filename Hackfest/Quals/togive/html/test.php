    <?php

    $data = $db->querySingle("SELECT * FROM users WHERE username='${username}'and password='$password).'", true);
    // admin' and 1=2 union select 1,2,3,4,5,6,7,8,9,10,11,12,13,14-- -
    $host = str_replace(' ', '', $data['host']);
    $port = (int) $data['port'];
    print_r($port);

    ?>