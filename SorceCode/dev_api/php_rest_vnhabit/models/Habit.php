<?php

include_once '../../models/Model.php';
include_once '../../models/MonitorDate.php';

    class Habit extends Model {
        // db
        private $conn;
        private $table = 'habit h';
        private $cols;
        private $params;

        // habit
        public $habit_id;
        public $user_id;
        public $group_id;
        public $monitor_id;
        public $habit_name;
        public $habit_target;
        public $habit_type;
        public $monitor_type;
        public $monitor_unit;
        public $monitor_number;
        public $start_date;
        public $end_date;
        public $created_date;
        public $habit_color;
        public $habit_description;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = $this->get_read_param(NULL, 'h');
            $this->params = $this->get_query_param(array('habit_id'));
        }

        // Get all Habit
        public function read() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' ORDER BY habit_id ASC';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Execute query
            $stmt->execute();
            return $stmt;
        }

        public function read_by_user() {
            // Create query
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' WHERE user_id = :user_id';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind params
            $stmt->bindParam(":user_id", $this->user_id);
            // Execute query
            $stmt->execute();
            return $stmt;
        }
    
        public function read_join_monitor() {
            $date = new MonitorDate($this->conn);
            $query = 'SELECT ' . $this->cols . ', '
             . $date->get_read_param(array('monitor_id', 'habit_id'), 'd') 
             . ' FROM ' . $this->table 
             . ' LEFT JOIN monitor_date d ON h.monitor_id = d.monitor_id WHERE h.user_id = :user_id';
            
             $stmt = $this->conn->prepare($query);
            $stmt->bindParam(":user_id", $this->user_id);
            $stmt->execute();
            return $stmt;
        }

        // Create Habit
        public function create() {
            // create query
            $query = 'INSERT INTO habit SET ' . $this->get_query_param(array('habit_id'));
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param_exc($stmt, array('habit_id'));
            // Execute query
            if ($stmt->execute()) {
                $this->habit_id = $this->conn->lastInsertId();
                return true;
            }
            return false;
        }

        // Update Habit
        public function update() {
            // create query
            $query = 'UPDATE habit SET ' . $this->get_query_param(array('habit_id')) . ' WHERE habit_id = :habit_id';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Bind data
            $stmt = $this->bind_param_exc($stmt, NULL);

            // Execute query
            if ($stmt->execute()) {
                return true;
            }

            // Print error if something goes wrong
            printf("Error: %s.\n", $stmt->error);
            return false;
        }

        // Detele Habit
        public function delete() {
            // create query
            $query = 'DELETE FROM habit WHERE habit_id = :habit_id';
            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Clean data
            $this->user_id = htmlspecialchars(strip_tags($this->habit_id));

            // Bind data
            $stmt->bindParam(':habit_id', $this->habit_id);
            
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
