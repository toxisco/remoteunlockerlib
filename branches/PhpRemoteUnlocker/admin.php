<?php

session_set_cookie_params(3600);
session_start();

include_once "lib/config.php";
include_once "lib/database.php";

$pwd = $_POST['pwd'];
$newSerial = $_POST['new_serial'];
$delete = $_GET['delete'];
$edit = $_GET['edit'];

if (isset($pwd)) {
	if ($pwd == Config::RU_PASSWORD) $_SESSION['authorized'] = true;
	else $wrongPassword = "<p>Wrong password</p>";
}

$db = new Database();

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 //EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=Cp1252">
<title>RemoteUnlocker Serial Manager</title>
</head>

<body>

<?php if (!isset($_SESSION['authorized']) || $_SESSION['authorized'] == false): ?>

<?php if (isset($wrongPassword)) echo $wrongPassword; ?>
	<form name="Login" action="<?php echo $_SERVER['PHP_SELF']; ?>"
		method="POST">
		Password: <input type="password" name="pwd" /> <input type="submit"
			value="Login" />
	</form>
		
	<?php else: ?>
	
	<?php
	
	if (isset($newSerial)) {
		if ($db->insertSerial($newSerial)) echo "<p>New serial correctly added</p>";
		else echo "<p>Serial not added because of DB fault</p>";
	}
	
	if (isset($delete)) {
		if ($db->deleteSerial($delete)) echo "Serial correctly deleted";
		else echo "Error deleting serial";
	}
	
	?>
	
	<form name="newserial" action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
	New serial: <input type="text" name="new_serial"/>
	<input type="submit" value="Add" />
	</form>
	
	<table border="1">
		<tr>
			<th>ID</th>
			<th>Serial</th>
			<th>Device ID</th>
			<th>Package</th>
			<th>Options</th>
		</tr>
		
			<?php $db->createSerialTable(); ?>
			
	</table>
	
	<?php 
	endif;
	unset($db); 
	?>

	</body>
</html>
