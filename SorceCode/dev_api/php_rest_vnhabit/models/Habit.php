<?php

include_once '../../models/Model.php';

    class Habit extends Model {
        // db
        private $conn;
        private $table = 'habit';
        private $cols;
        private $params;
        private $colsArr = array(
            'habit_id', 
            'user_id', 
            'category_id', 
            'schedule_id', 
            'goal_id', 
            'habit_name', 
            'habit_type', 
            'unit', 
            'count_type', 
            'start_date', 
            'end_date', 
            'created_date', 
            'habit_icon', 
            'habit_description'
        );

        // habit
        public $habit_id;
        public $user_id;
        public $category_id;
        public $schedule_id;
        public $goal_id;
        public $habit_name;
        public $habit_type;
        public $unit;
        public $count_type;
        public $start_date;
        public $end_date;
        public $created_date;
        public $habit_icon;
        public $habit_description;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = implode(", ", $this->colsArr);
            $this->params = $this->make_query_param($this->colsArr);
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
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . 
                ' WHERE
                    user_id = :user_id 
                    LIMIT 0,1';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Bind params
            $stmt->bindParam(":user_id", $this->user_id);

            // Execute query
            $stmt->execute();
            // get row count
            $num = $stmt->rowCount();

            if ($num == 1) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                $this->habit_id = $row['habit_id'];
                $this->user_id = $row['user_id'];
                $this->category_id = $row['category_id'];
                $this->schedule_id = $row['schedule_id'];
                $this->goal_id = $row['goal_id'];
                $this->habit_name = $row['habit_name'];
                $this->habit_type = $row['habit_type'];
                $this->unit = $row['unit'];
                $this->count_type = $row['count_type'];
                $this->start_date = $row['start_date'];
                $this->end_date = $row['end_date'];
                $this->created_date = $row['created_date'];
                $this->habit_icon = $row['habit_icon'];
                $this->habit_description = $row['habit_description'];
                return $this;
            } else {
                return NULL;
            }
        }

        // Create Habit
        public function create() {
            // create query
            $query = 'INSERT INTO ' . $this->table . ' SET ' . $this->params;
            
            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Bind data
            $stmt->bindParam(':user_id', $this->user_id);
            $stmt->bindParam(':category_id', $this->category_id);
            $stmt->bindParam(':schedule_id', $this->schedule_id);
            $stmt->bindParam(':goal_id', $this->goal_id);
            $stmt->bindParam(':habit_name', $this->habit_name);
            $stmt->bindParam(':habit_type', $this->habit_type);
            $stmt->bindParam(':unit', $this->unit);
            $stmt->bindParam(':count_type', $this->count_type);
            $stmt->bindParam(':start_date', $this->start_date);
            $stmt->bindParam(':end_date', $this->end_date);
            $stmt->bindParam(':created_date', $this->created_date);
            $stmt->bindParam(':habit_icon', $this->habit_icon);
            $stmt->bindParam(':habit_description', $this->habit_description);

            // Execute query
            if ($stmt->execute()) {
                return true;
            }

            // Print error if something goes wrong
            printf("Error: %s.\n", $stmt->error);
            return false;
        }

        // Update Habit
        public function update() {
            // create query
            $query = 'UPDATE ' . $this->table . ' SET ' . $this->params . ' WHERE habit_id = :habit_id';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Bind data
            $stmt->bindParam(':habit_id', $this->habit_id);
            $stmt->bindParam(':user_id', $this->user_id);
            $stmt->bindParam(':category_id', $this->category_id);
            $stmt->bindParam(':schedule_id', $this->schedule_id);
            $stmt->bindParam(':goal_id', $this->goal_id);
            $stmt->bindParam(':habit_name', $this->habit_name);
            $stmt->bindParam(':habit_type', $this->habit_type);
            $stmt->bindParam(':unit', $this->unit);
            $stmt->bindParam(':count_type', $this->count_type);
            $stmt->bindParam(':start_date', $this->start_date);
            $stmt->bindParam(':end_date', $this->end_date);
            $stmt->bindParam(':created_date', $this->created_date);
            $stmt->bindParam(':habit_icon', $this->habit_icon);
            $stmt->bindParam(':habit_description', $this->habit_description);

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
            $query = 'DELETE FROM ' . $this->table . ' WHERE habit_id = :habit_id';

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
