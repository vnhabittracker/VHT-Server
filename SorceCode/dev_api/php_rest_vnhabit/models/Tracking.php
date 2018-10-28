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
            $this->cols = $this->get_read_param(NULL, 't');
        }

        public function read() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' t ORDER BY tracking_id ASC';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Execute query
            $stmt->execute();
            return $stmt;
        }

        public function getTrackByHabit() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' t WHERE habit_id = :habit_id';
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param($stmt, array('habit_id' => $this->habit_id));
            $stmt->execute();
            $num = $stmt->rowCount();
            if ($num > 0) {
                return $stmt;
            } else {
                return NULL;
            }
        }

        public function get_tracking() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' t ' . 
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

        // get track data by habit id and date
        public function getTrackWithParam($track) {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . " t ". 
                ' WHERE 
                    t.habit_id = :habit_id AND t.current_date = :current_date';
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param($stmt, array('habit_id' => $track['habit_id'], 'current_date' => $track['current_date']));
            $stmt->execute();
            // $stmt->debugDumpParams();

            $num = $stmt->rowCount();
            if ($num == 1) {
                return $stmt->fetch(PDO::FETCH_ASSOC);
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

        // Create User
        public function createWithParam($track) {
            // create query
            $query = 'INSERT INTO ' . $this->table . ' SET ' . $this->get_query_param(NULL);
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind data
            $stmt = $this->bind_param($stmt, array('tracking_id' => $track['tracking_id'], 
                                    'habit_id' => $track['habit_id'],
                                    'current_date' => $track['current_date'],
                                    'count' => $track['count'],
                                    'tracking_description' => $track['tracking_description']));
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
            $query = 'UPDATE ' . $this->table . ' t ' . ' SET t.count = :count, t.tracking_description = :tracking_description '
                        . ' WHERE t.habit_id = :habit_id AND t.current_date = :current_date';
            
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

        // Update user
        public function updateWithParam($track) {
            $habit_id = $track['habit_id'];
            $count = $track['count'];
            $tracking_description = $track['tracking_description'];
            $current_date = $track['current_date'];
            // create query
            $query = 'UPDATE ' . $this->table . ' t ' . ' SET count = :count, tracking_description = :tracking_description '
                        . ' WHERE habit_id = :habit_id AND t.current_date = :current_date';
            
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param($stmt, array('habit_id' => $habit_id,
                                    'current_date' => $current_date,
                                    'count' => $count, 
                                    'tracking_description' => $tracking_description));

            // $stmt->debugDumpParams();
            
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
