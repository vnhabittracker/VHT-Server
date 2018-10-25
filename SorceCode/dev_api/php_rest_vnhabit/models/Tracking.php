<?php

include_once '../../models/Model.php';

    class Tracking extends Model {
        // db
        private $conn;
        private $table = 'tracking';
        private $cols;
        private $params;

        // tracking columns
        public $tracking_id;
        public $habit_id;
        public $current_date;
        public $count;
        public $tracking_description;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = $this->get_read_param(NULL, NULL);
        }

        public function read() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' ORDER BY tracking_id ASC';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Execute query
            $stmt->execute();
            return $stmt;
        }

        public function get_tracking() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . 
                ' WHERE 
                    habit_id = :habit_id and current_date = :current_date 
                    LIMIT 0,1';
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param($stmt, array('habit_id' => $this->habit_id, 'current_date' => $this->current_date));
            $stmt->execute();
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
            $query = 'INSERT INTO ' . $this->table . ' SET ' . $this->get_query_param(array('tracking_id'));
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind data
            $stmt = $this->bind_param_exc($stmt, array('tracking_id'));
            // Execute query
            if ($stmt->execute()) {
                $this->tracking_id = $this->conn->lastInsertId();
                return true;
            }
            printf("Error: %s.\n", $stmt->error);
            return false;
        }

        // Update user
        public function update() {
            // create query
            $query = 'UPDATE ' . $this->table . ' SET count = :count, tracking_description = :tracking_description '
                        . ' WHERE habit_id = :habit_id AND current_date = :current_date';
            
                // Prepare statement
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param_exc($stmt, array('tracking_id'));
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
