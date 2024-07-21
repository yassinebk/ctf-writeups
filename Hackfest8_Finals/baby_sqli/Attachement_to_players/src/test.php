<?php

$input =$_GET['user'];
$forbid = "0x|0b|limit|glob|php|load|inject|month|day|now|collationlike|regexp|limit|columns|char|sin|cos|asin|procedure|trim|pad|make|mid";
$forbid .= "substr|compress|code|replace|conv|insert|right|left|cast|ascii|x|hex|version|data|load_file|out|gcc|locate|count|reverse|b|y|z|--";

$val=preg_match("/$forbid/i", $input,$matches);

if ($val) { 
	print_r($matches);
} else if(preg_match('/\s/', $input)){
	echo "whitespace";
} 
else if(preg_match('/[\/\\\\]/', $input) ) {
	echo "slash";
}

else if( preg_match('/(--|#|\/\*)/', $input)){
	echo "got comments";
}

else { echo "passed";}
