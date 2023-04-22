 <?php

function show_image () {

    if (! isset ($_SESSION['captcha'])) {
        die ('This is not how you are supposed to use it.');
    }

    $image = @imagecreatetruecolor (255, 64) or die ("Cannot Initialize new GD image stream");

    $bg = imagecolorallocate ($image, 255, 255, 255);
    imagefill ($image, 0, 0, $bg);
    imagecolordeallocate ($image, $bg);

    for($i = 0; $i <= 255 / 10.0; $i++) {
        $color = imagecolorallocate ($image, rand (0, 128), rand (0, 128), rand (0, 128));
        imagechar ($image, rand (1, 5), $i * rand (20, 40), rand (10, 64 - 10), $_SESSION['captcha'][$i], $color);// wrong calculations here
        imagecolordeallocate ($image, $color);
    }

    /* Yay, dots! */
    for($i=0; $i < 1024; $i++) {
        $color = imagecolorallocate ($image, rand (0, 255), rand (0, 255), rand (0, 255));
        imagesetthickness ($image, rand (1, 5));
        imagefilledellipse ($image, rand (0, 255), rand (0, 64), 3, 3, $color);
        imagecolordeallocate ($image, $color);
    }

    /* Yay, lines! */
    imagesetthickness ($image, 1);
    for ($i=0; $i < 8; $i++) {
        $color = imagecolorallocate ($image, rand (0, 255), rand (0, 255), rand (0, 255));
        imageline($image, rand (0, 255), 0, rand (0, 255), 64, $color);
        imagecolordeallocate ($image, $color);
    }

    /* Php doesn't offer a method to output images to a variable. */
    ob_start ();
    imagepng ($image);
    $str_image = ob_get_contents ();
    ob_end_clean ();
    imagedestroy ($image);

    return base64_encode ($str_image);
}

?>
 