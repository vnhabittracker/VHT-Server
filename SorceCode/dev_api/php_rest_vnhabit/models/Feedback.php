<?php

include_once '../../models/Model.php';

    class Feedback extends Model{
        private $conn;
        private $table = 'feedback';
        private $cols;
        private $params;

        public $feedback_id;
        public $user_id;
        public $star_num;
        public $feedback_description;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = $this->get_read_param(NULL, NULL);
            $this->params = $this->get_query_param(NULL);
        }

        public function create() {
            // create query
            $query = "INSERT INTO `" .  $this->table . "` SET " . $this->params;
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            $stmt = $this->bind_param_exc($stmt, NULL);
            // Execute query
            if ($stmt->execute()) {
                return true;
            }
            return false;
        }

        public function read_single() {
            // Create query
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' WHERE feedback_id = :feedback_id';
            // Prepare statement
            $stmt = $this->conn->prepare($query);
            // Bind params
            $stmt->bindParam(":feedback_id", $this->feedback_id);
            // Execute query
            $stmt->execute();
            // get row count
            $row_count = $stmt->rowCount();
            if ($row_count == 1) {
                return $stmt->fetch(PDO::FETCH_ASSOC);
            } else {
                return NULL;
            }
        }

        public function update() {
            // create query
            $query = 'UPDATE ' . $this->table . ' SET ' . $this->get_query_param(array('feedback_id')) . ' WHERE feedback_id = :feedback_id';
    
            // Prepare statement
            $stmt = $this->conn->prepare($query);
    
            // Bind data
            $stmt = $this->bind_param_exc($stmt, NULL);
    
            // Execute query
            if ($stmt->execute()) {
                return true;
            }
            
            return false;
        }
    }

?>
