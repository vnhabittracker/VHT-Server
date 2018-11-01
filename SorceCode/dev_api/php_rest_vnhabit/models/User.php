<?php

include_once '../../models/Model.php';

    class User extends Model {
        // db
        private $conn;
        private $table = 'user';
        private $cols;
        private $params;

        // user
        public $user_id;
        public $username;
        public $phone;
        public $password;
        public $email;
        public $date_of_birth;
        public $gender;
        public $user_icon;
        public $avatar;
        public $user_description;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = $this->get_read_param(NULL, NULL);
            $this->params = $this->get_query_param(array('user_id'));
        }

        // Get all User
        public function read() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' ORDER BY user_id ASC';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Execute query
            $stmt->execute();
            return $stmt;
        }

        // Get Single User
        public function read_single() {
            // Create query
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . 
                ' WHERE username = :username and password = :password LIMIT 0,1';

            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind params
            $stmt = $this->bind_param($stmt, array('username' => $this->username, 'password' => $this->password));
            // Execute query
            $stmt->execute();
            // get row count
            $num = $stmt->rowCount();
            if ($num == 1) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                return $row;
            } else {
                return NULL;
            }
        }

        public function find_by_username() {
            // Create query
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . 
                ' WHERE
                    username = :username 
                    LIMIT 0,1';

            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind params
            $stmt = $this->bind_param($stmt, array('username' => $this->username));
            // Execute query
            $stmt->execute();
            // get row count
            $num = $stmt->rowCount();
            if ($num == 1) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                return $row;
            } else {
                return NULL;
            }
        }

        // Create User
        public function create() {
            // create query
            $query = 'INSERT INTO ' . $this->table . ' SET ' . $this->get_query_param(NULL);
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind data
            $stmt = $this->bind_param_exc($stmt, NULL);
            // Execute query
            if ($stmt->execute()) {
                return true;
            }
            printf("Error: %s.\n", $stmt->error);
            return false;
        }

        // Update user
        public function update() {
            // create query
            $query = 'UPDATE ' . $this->table . ' SET ' . $this->get_query_param(array('user_id')) . ' WHERE user_id = :user_id';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param_exc($stmt, NULL);
            // Execute query
            if ($stmt->execute()) {
                return true;
            }
            // Print error if something goes wrong
            printf("Error: %s.\n", $stmt->error);
            return false;
        }

        // Detele user
        public function delete() {
            // create query
            $query = 'DELETE FROM ' . $this->table . ' WHERE user_id = :user_id';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Clean data
            $this->user_id = htmlspecialchars(strip_tags($this->user_id));

            // Bind data
            $stmt->bindParam(':user_id', $this->user_id);

            // Execute query
            if($stmt->execute()) {
                return true;
            }
            // Print error if something goes wrong
            printf("Error: %s.\n", $stmt->error);
            return false;
        }
    }

?>
