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
    }

?>