<?php

include_once "lib/checker.php";

$serial = $_POST['serial'];
$imei = $_POST['imei'];
$package = $_POST['appPackage'];

$checker = new Checker();

echo $checker->checkSerial($serial, $imei, $package);

unset($checker);

?>