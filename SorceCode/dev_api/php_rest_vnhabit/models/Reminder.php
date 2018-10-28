<?php

include_once '../../models/Model.php';

    class Reminder extends Model {
        // db
        private $conn;
        private $table = 'reminder';
        private $cols;
        private $params;

        public $reminder_id;
        public $habit_id;
        public $reminder_time;
        public $repeat_time;
        public $reminder_description;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = $this->get_read_param(NULL, NULL);
        }

        public function read() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' ORDER BY reminder_id ASC';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Execute query
            $stmt->execute();
            return $stmt;
        }

        public function find($habitId, $time) {
            // Create query
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . 
                ' WHERE habit_id = :habit_id AND reminder_time = :reminder_time LIMIT 0,1';

            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind params
            $stmt = $this->bind_param($stmt, array('habit_id' => $habitId, 'reminder_time' => $time));
            // Execute query
            $stmt->execute();
            // get row count
            $num = $stmt->rowCount();
            if ($num == 1) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                $this->reminder_id = $row['reminder_id'];
                return $row;
            } else {
                return NULL;
            }
        }

        // Create
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

        // Update
        public function update() {
            // create query
            $query = 'UPDATE ' . $this->table . ' SET ' . $this->get_query_param(array('reminder_id'))
                        . ' WHERE habit_id = :habit_id AND current_date = :current_date';
            
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

        // Update
        public function updateById($reminderId) {
            // create query
            $query = 'UPDATE ' . $this->table . ' SET ' . $this->get_query_param(array('reminder_id'))
                        . ' WHERE reminder_id = :reminder_id';
            
                // Prepare statement
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param($stmt, array('reminder_id' => $reminderId));
            // Execute query
            if ($stmt->execute()) {
                return true;
            }
            // Print error if something goes wrong
            printf("Error: %s.\n", $stmt->error);
            return false;
        }
    }

?>
