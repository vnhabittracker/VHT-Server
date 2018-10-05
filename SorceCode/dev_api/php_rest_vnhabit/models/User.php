<?php

    class User {
        // db
        private $conn;
        private $table = 'user';

        // user
        public $user_id;
        public $username;
        public $password;
        public $email;
        public $date_of_birth;
        public $gender;
        public $user_icon;
        public $avatar;
        public $user_description;

        public function __construct($db) {
            $this->conn = $db;
        }

        // Get all User
        public function read() {
            $query = 'SELECT
                    user_id, 
                    username, 
                    password, 
                    email, 
                    date_of_birth, 
                    gender, 
                    user_icon, 
                    avatar, 
                    user_description 
                FROM
            ' . $this->table . ' ORDER BY user_id ASC';

            // Prepare statement
            $stmt = $this->conn->prepare($query);
            
            // Execute query
            $stmt->execute();

            return $stmt;
        }

        // Get Single User
        public function read_single() {
            // Create query
            $query = 'SELECT 
                    user_id, 
                    username, 
                    password, 
                    email, 
                    date_of_birth, 
                    gender, 
                    user_icon, 
                    avatar, 
                    user_description 
                FROM ' . $this->table . 
                ' WHERE
                    username = :username and password = :password 
                    LIMIT 0,1';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Bind params
            $stmt->bindParam(":username", $this->username);
            $stmt->bindParam(":password", $this->password);

            // Execute query
            $stmt->execute();
            // get row count
            $num = $stmt->rowCount();

            if ($num == 1) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                $this->user_id = $row['user_id'];
                $this->username = $row['username'];
                $this->password = $row['password'];
                $this->email = $row['email'];
                $this->date_of_birth = $row['date_of_birth'];
                $this->gender = $row['gender'];
                $this->user_icon = $row['user_icon'];
                $this->avatar = $row['avatar'];
                $this->user_description = $row['user_description'];
                return $this;
            } else {
                return NULL;
            }
        }

        // Create User
        public function create() {
            // create query
            $query = 'insert into ' . $this->table . ' set 
                username = :username, 
                password = :password, 
                email = :email, 
                date_of_birth = :date_of_birth, 
                gender = :gender, 
                user_icon = :user_icon, 
                avatar = :avatar, 
                user_description = :user_description';
            
            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Clean data
            $this->username = htmlspecialchars(strip_tags($this->username));
            $this->password = htmlspecialchars(strip_tags($this->password));
            $this->email = htmlspecialchars(strip_tags($this->email));
            $this->date_of_birth = htmlspecialchars(strip_tags($this->date_of_birth));
            $this->gender = htmlspecialchars(strip_tags($this->gender));
            $this->user_icon = htmlspecialchars(strip_tags($this->user_icon));
            $this->avatar = htmlspecialchars(strip_tags($this->avatar));
            $this->user_description = htmlspecialchars(strip_tags($this->user_description));

            // Bind data
            $stmt->bindParam(':username', $this->username);
            $stmt->bindParam(':password', $this->password);
            $stmt->bindParam(':email', $this->email);
            $stmt->bindParam(':date_of_birth', $this->date_of_birth);
            $stmt->bindParam(':gender', $this->gender);
            $stmt->bindParam(':user_icon', $this->user_icon);
            $stmt->bindParam(':avatar', $this->avatar);
            $stmt->bindParam(':user_description', $this->user_description);

            // Execute query
            if ($stmt->execute()) {
                return true;
            }

            // Print error if something goes wrong
            printf("Error: %s.\n", $stmt->error);
            return false;
        }

        // Update user
        public function update() {
            // create query
            $query = 'UPDATE ' . $this->table . ' SET 
                        username = :username, 
                        password = :password, 
                        email = :email, 
                        date_of_birth = :date_of_birth, 
                        gender = :gender, 
                        user_icon = :user_icon, 
                        avatar = :avatar, 
                        user_description = :user_description
                    WHERE user_id = :user_id
                    ';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Clean data
            $this->user_id = htmlspecialchars(strip_tags($this->user_id));
            $this->username = htmlspecialchars(strip_tags($this->username));
            $this->password = htmlspecialchars(strip_tags($this->password));
            $this->email = htmlspecialchars(strip_tags($this->email));
            $this->date_of_birth = htmlspecialchars(strip_tags($this->date_of_birth));
            $this->gender = htmlspecialchars(strip_tags($this->gender));
            $this->user_icon = htmlspecialchars(strip_tags($this->user_icon));
            $this->avatar = htmlspecialchars(strip_tags($this->avatar));
            $this->user_description = htmlspecialchars(strip_tags($this->user_description));

            // Bind data
            $stmt->bindParam(':user_id', $this->user_id);
            $stmt->bindParam(':username', $this->username);
            $stmt->bindParam(':password', $this->password);
            $stmt->bindParam(':email', $this->email);
            $stmt->bindParam(':date_of_birth', $this->date_of_birth);
            $stmt->bindParam(':gender', $this->gender);
            $stmt->bindParam(':user_icon', $this->user_icon);
            $stmt->bindParam(':avatar', $this->avatar);
            $stmt->bindParam(':user_description', $this->user_description);

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
