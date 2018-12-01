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
        public $password;
        public $email;
        public $date_of_birth;
        public $gender;
        public $real_name;
        public $user_description;
        public $created_date;
        public $last_login_time;
        public $continue_using_count;
        public $current_continue_using_count;
        public $best_continue_using_count;
        public $user_score;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = $this->get_read_param(NULL, NULL);
            $this->params = $this->get_query_param(array('user_id'));
        }

        private function copy($user) {
            $this->user_id = $user['user_id'];
            $this->username = $user['username'];
            $this->password = $user['password'];
            $this->email = $user['email'];
            $this->date_of_birth = $user['date_of_birth'];
            $this->gender = $user['gender'];
            $this->real_name = $user['real_name'];
            $this->user_description = $user['user_description'];
            $this->created_date = $user['created_date'];
            $this->last_login_time = $user['last_login_time'];
            $this->continue_using_count = $user['continue_using_count'];
            $this->current_continue_using_count = $user['current_continue_using_count'];
            $this->best_continue_using_count = $user['best_continue_using_count'];
            $this->user_score = $user['user_score'];
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
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' WHERE username = :username and password = :password LIMIT 1';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind params
            $stmt = $this->bind_param($stmt, array('username' => $this->username, 'password' => $this->password));
            // Execute query
            $stmt->execute();
            // get row count
            $rowCount = $stmt->rowCount();
            if ($rowCount == 1) {
                $user = $stmt->fetch(PDO::FETCH_ASSOC);
                $today = new DateTime();
                $lastLogin = new DateTime($user['last_login_time']);
                $diff = $lastLogin ->diff($today)->format("%a");
                if ($diff == 1) {
                    $user['current_continue_using_count'] += 1;
                    if ($user['current_continue_using_count'] > $user['best_continue_using_count']) {
                        $user['best_continue_using_count'] = $user['current_continue_using_count'];
                    }
                    $user['last_login_time'] = date('Y-m-d');
                    $cur_continue = $user['current_continue_using_count'];
                    if($cur_continue <= 7) {
                        $user['user_score'] += 2;
                    } else if ($cur_continue <= 30) {
                        $user['user_score'] += 4;
                    } else if ($cur_continue < 90) {
                        $user['user_score'] += 8;
                    } else if ($cur_continue < 180) {
                        $user['user_score'] += 18;
                    } else if ($cur_continue < 360) {
                        $user['user_score'] += 32;
                    }
                    $user['continue_using_count'] += 1;

                    $this->copy($user);
                    $this->update();
                } else if ($diff > 1) {
                    $user['last_login_time'] = date('Y-m-d');
                    $user['current_continue_using_count'] = 1;
                    $this->copy($user);
                    $this->update();
                }
                
                return $user;
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
            $excludeArr = array('user_id', 'created_date', 'last_login_time', 'continue_using_count', 'current_continue_using_count', 'best_continue_using_count', 'user_score');
            $excludeArr2 = array('created_date', 'last_login_time', 'continue_using_count', 'current_continue_using_count', 'best_continue_using_count', 'user_score');
            $query = 'UPDATE ' . $this->table . ' SET ' . $this->get_query_param($excludeArr) . ' WHERE user_id = :user_id';
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param_exc($stmt, $excludeArr2);
            if ($stmt->execute()) {
                return true;
            }
            printf("Error: %s.\n", $stmt->error);
            return false;
        }

        public function updateScore() {
            $query = 'UPDATE ' . $this->table . ' SET user_score = user_score + :user_score WHERE user_id = :user_id';
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param($stmt, array('user_score' => $this->user_score, 'user_id' => $this->user_id));
            if ($stmt->execute()) {
                return true;
            }
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
