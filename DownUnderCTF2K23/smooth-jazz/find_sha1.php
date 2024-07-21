<?php

function find_sha1_leading_34() {
    $chars = array_merge(range('a', 'z'), range('0', '9'));  // alphanumeric characters
    
    for ($length = 1; ; $length++) {
        $combinations = get_combinations($chars, $length);
        foreach ($combinations as $combination) {
            $potential_string = implode('', $combination);
            $result = sha1($potential_string);

            if (strpos($result, '34') === 0) {
                echo $potential_string."\t".$result."\n";
            }
        }
    }
}

function get_combinations($chars, $length) {
    if ($length === 1) return array_map(function($char) { return [$char]; }, $chars);
    $result = [];

    foreach ($chars as $char) {
        foreach (get_combinations($chars, $length - 1) as $sub_combination) {
            $result[] = array_merge([$char], $sub_combination);
        }
    }

    return $result;
}


echo find_sha1_leading_34();

