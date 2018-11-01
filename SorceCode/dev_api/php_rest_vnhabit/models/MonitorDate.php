<?php

include_once '../../models/Model.php';

class MonitorDate extends Model {
    // db
    private $conn;
    private $table = 'monitor_date d';
    public $cols;
    private $params;

    public $monitor_id;
    public $habit_id;
    public $mon;
    public $tue;
    public $wed;
    public $thu;
    public $fri;
    public $sat;
    public $sun;

    public function __construct($db) {
        $this->conn = $db;
        $this->cols = $this->get_read_param(NULL, 'd');
        $this->params = $this->get_query_param(NULL);
    }

    // GET
    public function read() {
        $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' ORDER BY monitor_id ASC';
        // Prepare statement
        $stmt = $this->conn->prepare($query);
        // Execute query
        $stmt->execute();
        return $stmt;
    }

    public function create() {
        // create query
        $query = 'INSERT INTO monitor_date SET ' . $this->get_query_param(NULL);
        
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

    public function update() {
        // create query
        $query = 'UPDATE ' . $this->table . ' SET ' . $this->get_query_param(array('monitor_id')) . ' WHERE monitor_id = :monitor_id';

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
}

?>
