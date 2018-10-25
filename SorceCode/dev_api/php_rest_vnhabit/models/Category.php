<?php

include_once '../../models/Model.php';

    class Category {
        private $conn;
        private $table = 'category';
        private $cols;
        private $params;
        private $colsArr = array(
            'category_id', 
            'parent_id', 
            'category_name', 
            'category_icon', 
            'category_description'
        );

        public $category_id;
        public $parent_id;
        public $category_name;
        public $category_icon;
        public $category_description;

        public function __construct($db) {
            $this->conn = $db;
            $this->cols = implode(", ", $this->colsArr);
            $this->params = $this->make_query_param($this->colsArr);
        }

        // Get all User
        public function read() {
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' ORDER BY category_id ASC';

            // Prepare statement
            $stmt = $this->conn->prepare($query);
            
            // Execute query
            $stmt->execute();

            return $stmt;
        }

        // Get Single category by category_id
        public function read_single() {
            // Create query
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . 
                ' WHERE
                    category_id = :category_id 
                    LIMIT 0,1';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Bind params
            $stmt->bindParam(":category_id", $this->category_id);

            // Execute query
            $stmt->execute();
            // get row count
            $num = $stmt->rowCount();

            if ($num == 1) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                $this->user_id = $row['category_id'];
                $this->username = $row['parent_id'];
                $this->password = $row['category_name'];
                $this->email = $row['category_icon'];
                $this->date_of_birth = $row['category_description'];
                return $this;
            } else {
                return NULL;
            }
        }

        // Get Single category group by parrent
        public function read_childs() {
            // Create query
            $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . 
                ' WHERE
                    parent_id = :parent_id 
                    LIMIT 0,1';

            // Prepare statement
            $stmt = $this->conn->prepare($query);

            // Bind params
            $stmt->bindParam(":parent_id", $this->category_id);

            // Execute query
            $stmt->execute();
            // get row count
            $num = $stmt->rowCount();

            if ($num == 1) {
                $row = $stmt->fetch(PDO::FETCH_ASSOC);
                $this->user_id = $row['category_id'];
                $this->username = $row['parent_id'];
                $this->password = $row['category_name'];
                $this->email = $row['category_icon'];
                $this->date_of_birth = $row['category_description'];
                return $this;
            } else {
                return NULL;
            }
        }


    }
?>