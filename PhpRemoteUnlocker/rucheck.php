<?php

include_once "lib/checker.php";

$serial = $_POST['serial'];
$imei = $_POST['imei'];

$checker = new Checker();

echo $checker->checkSerial($serial, $imei);

unset($checker);

?>