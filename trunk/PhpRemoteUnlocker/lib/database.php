<?php

include_once "lib/config.php";

class Database {
	
	private $link;
	
	const CREATE_TABLE = "CREATE TABLE IF NOT EXISTS serial_table 
							(id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, 
							serial VARCHAR(255) NOT NULL, 
							imei VARCHAR(255),
							appPackage VARCHAR(255))";
	
	const UPDATE_DB = "ALTER TABLE serial_table ADD COLUMN (appPackage VARCHAR(255))";
	
	public function __construct() {
		$this->link = mysql_connect(Config::HOST,
									Config::USERNAME,
									Config::PASSWORD);
		mysql_select_db(Config::DATABASE, $this->link);
	}
	
	public function __destruct() {
		mysql_close($this->link);
	}
	
	public function getDbLink() {
		return $this->link;
	}
	
	public function isInstalled() {
		return mysql_query("SELECT * FROM serial_table", $this->link);
	}
	
	public function updateDb() {
		return mysql_query(Database::UPDATE_DB, $this->link);
	}
	
	public function setupDb() {
		return mysql_query(Database::CREATE_TABLE, $this->link);
	}
	
	public function insertSerial($serial) {
		if ($this->isSerialPresent($serial)) {
			echo "<p>Serial already exists</p>";
			return false;
		}
		else return mysql_query("INSERT INTO serial_table (serial) VALUES ('$serial')", $this->link);
	}
	
	public function createSerialTable() {
		$res = mysql_query("SELECT * FROM serial_table", $this->link);
		while ($row = mysql_fetch_array($res)) {
			echo '<tr><td>'.$row['id'].'</td>
					<td>'.$row['serial'].'</td>
					<td>'.$row['imei'].'</td>
					<td>'.$row['appPackage'].'</td>
					<td><a href="'.$_SERVER['PHP_SELF'].'?edit='.$row['id'].'">Edit</a> | <a href="'.$_SERVER['PHP_SELF'].'?delete='.$row['id'].'">Delete</a></td>
				</tr>';
		}
	}
	
	public function deleteSerial($id) {
		return mysql_query("DELETE FROM serial_table WHERE id = $id", $this->link);
	}
	
	private function isSerialPresent($serial) {
		$res = mysql_query("SELECT id FROM serial_table WHERE serial = '$serial'", $this->link);
		return (mysql_num_rows($res) < 1) ? false : true;
	}
	
}

?>