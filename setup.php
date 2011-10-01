<?php

include_once "lib/database.php";

$db = new Database();
if ($db->setupDb()) echo "Db installed correctly";
else echo "ERROR INSTALLING DB";

?>