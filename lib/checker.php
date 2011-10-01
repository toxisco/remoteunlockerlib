<?php

include_once "lib/database.php";

class Checker {
	
	private $db;
	
	public function __construct() {
		$this->db = new Database();
	}
	
	public function __destruct() {
		unset($this->db);
	}
	
	public function checkSerial($serial, $imei) {
		$res1 = mysql_query("SELECT imei FROM serial_table WHERE serial = '$serial'", $this->db->getDbLink());
		
		if (!$res1) return Config::SERVER_NO_CONNECTION;
		
		if (mysql_num_rows($res1) > 0) {
			
			$row = mysql_fetch_array($res1);			
			if ($row['imei'] == NULL) {
				if (mysql_query("UPDATE serial_table SET imei = '$imei' WHERE serial = '$serial'", $this->db->getDbLink())) return Config::SERIAL_OK;
			} else return ($row['imei'] == $imei) ? Config::SERIAL_OK : Config::SERIAL_ALREADY_USED;
			
		} else return Config::SERIAL_WRONG;
	}
	
}

?>