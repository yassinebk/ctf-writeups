<?php
class AES_key{
    private $pi = "6.2831853174";


    public function getChars(){
        $CharsList = array();
        for ($i=1; $i<=26; $i++){
            array_push($CharsList,chr(96+$i));

        }


        return $CharsList;
    }


    public function getPiChars(){
        $nPai = $this->pi;
        $CharsList = $this->getChars();
        $nPai = str_replace(".", "", $nPai);
        $nPaiArray = str_split($nPai);
        $PiCharsList = array();


        for ($i=0; $i<=count($nPaiArray); $i++){
            array_push($PiCharsList, $CharsList[$nPaiArray[$i]]);
        }


        return implode("",$PiCharsList);
    }


    public function aaa(int $n){
        return $n % 5;
    }


    public function bbb(int $n){
        $s = ($n+1)*($n-1) % 5;
        $s = $s + $n*4%3;
        $s = $s + $n/4*3;
        $s = $this->aaa(314) / ($s%5*$n+7) + 5;


        return $s;
    }

    public function ccc(int $n){
        $k = 3;
        $CharsList = $this->getChars();
        $PadCharsList = array();
        for ($i=0; $i< $n*$k; $i=$i+$k){
            array_push($PadCharsList, $CharsList[$i]);
        }


        return implode('',$PadCharsList);
    }


}

class AES_enc{
    private $cipher;

    public function __construct(string $data){
        $this->setCipher($this->encAES($data));
    }

    public function encAES(string $data){
        $data=  base64_decode($data);
        $s = new AES_key;
        $key = $s->getPiChars().$s->ccc($s->bbb(2568));
        $iv = substr(md5($key), 0, 16);
        $enc = openssl_decrypt($data, "aes-128-cbc", $key, 1, $iv);
        return  $enc;
    }


    public function getCipher(){
        return $this->cipher;
    }


    public function setCipher($cipher){
        $this->cipher = $cipher;
    }
}


$x=new AES_enc("N0olEIRCNH5iulyAW2kj3g1M3dkNoCyCkmctJyQs72v0Vhn4QzfpbsxE9a4gxi/xgt4/Mj3N76QsJSa7C0TXDw==");
echo $x->encAES("N0olEIRCNH5iulyAW2kj3g1M3dkNoCyCkmctJyQs72v0Vhn4QzfpbsxE9a4gxi/xgt4/Mj3N76QsJSa7C0TXDw==");
