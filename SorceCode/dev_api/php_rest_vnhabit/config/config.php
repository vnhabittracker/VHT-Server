<?php

    class Database {
        // DB params
        // private $host = 'us-cdbr-iron-east-01.cleardb.net';
        // private $db_name = 'heroku_7fcf4b302659005';
        // private $username = 'b6c50987d4ce83';
        // private $password = '51830534';

        private $host = 'localhost';
        private $db_name = 'vnhabit';
        private $username = 'root';
        private $password = '';
        private $conn;

        public function connect() {
            $this->conn = null;

            // $cleardb_url = parse_url(getenv("CLEARDB_DATABASE_URL"));
            // $cleardb_server   = $cleardb_url["host"];
            // $cleardb_username = $cleardb_url["user"];
            // $cleardb_password = $cleardb_url["pass"];
            // $cleardb_db       = substr($cleardb_url["path"],1);

            // $host = $cleardb_server;
            // $db_name = $cleardb_db;
            // $username = $cleardb_username;
            // $password = $cleardb_password;

            try {
                $this->conn = new PDO('mysql:host=' . $this->host . ';dbname=' . $this->db_name, $this->username, $this->password);
                $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                $this->conn->query('SET NAMES UTF8');
            } catch(PDOException $e) {
                echo 'Connection Error: '. $e->getMessage();
            }
            return $this->conn;
        }
    }

?>
