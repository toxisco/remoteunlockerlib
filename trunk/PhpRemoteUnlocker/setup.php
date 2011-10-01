<?php

include_once "lib/database.php";

$db = new Database();
if (!$db->isInstalled()) {
	if ($db->setupDb()) echo "Db installed correctly";
	else echo "ERROR INSTALLING DB";
} elseif ($db->updateDb()) echo "Db updated correctly";
else echo "ERROR UPDATING DB";

?>