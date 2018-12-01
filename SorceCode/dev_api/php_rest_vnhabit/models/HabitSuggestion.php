<?php

include_once '../../models/Model.php';

class HabitSuggestion extends Model {
    // db
    private $conn;
    private $table = 'habit_suggestion';
    private $cols;
    private $params;

    public $habit_name_id;
    public $group_id;
    public $habit_name_uni;
    public $habit_name_ascii;
    public $habit_name_count;
    public $total_track;
    public $success_track;

    public function __construct($db) {
        $this->conn = $db;
        $this->cols = $this->get_read_param(NULL, NULL);
    }

    public function read() {
        $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' ORDER BY `group_id` ASC';
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function search($searck_key) {
        $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' WHERE habit_name_ascii LIKE "%' . $searck_key . '%"';
        // var_dump($query);
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function create() {
        $query = 'INSERT INTO ' . $this->table . ' SET ' . $this->get_query_param(NULL);
        $stmt = $this->conn->prepare($query);
        $stmt = $this->bind_param_exc($stmt, NULL);
        if ($stmt->execute()) {
            return true;
        }
        return false;
    }

    public function find($habit_name_ascii) {
        $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' WHERE habit_name_ascii = "' . $habit_name_ascii . '"';
        // var_dump($query);
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;
    }

    public function isUpdate() {
        $query = 'SELECT ' . $this->cols . ' FROM ' . $this->table . ' WHERE habit_name_id = "' . $this->habit_name_id . '"';
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt->rowCount() > 0;
    }

    public function updateCount() {
        // create query
        $query = 'UPDATE ' . $this->table . ' SET habit_name_count = habit_name_count + 1 WHERE habit_name_id = :habit_name_id';
        $stmt = $this->conn->prepare($query);
        $stmt = $this->bind_param($stmt, array('habit_name_id' => $this->habit_name_id));
        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }

    public function updateTrack() {
        $query = 'UPDATE ' . $this->table . ' SET total_track = total_track + :total_track, success_track = success_track + :success_track WHERE habit_name_id = :habit_name_id';
        $stmt = $this->conn->prepare($query);
        $stmt = $this->bind_param($stmt, array('total_track' => $this->total_track, 'success_track' => $this->success_track, 'habit_name_id' => $this->habit_name_id));
        if ($stmt->execute()) {
            return true;
        }
        printf("Error: %s.\n", $stmt->error);
        return false;
    }

    public function getRecommendList($limit) {
        $sqlLv1 = 'SELECT ' . $this->cols . ', (success_track / total_track) * 100 AS hbcal FROM `habit_suggestion` HAVING hbcal >= 80 ORDER BY habit_name_count DESC LIMIT '. $limit;
        $sqlLv2 = 'SELECT ' . $this->cols . ', (success_track / total_track) * 100 AS hbcal FROM `habit_suggestion` HAVING hbcal >= 50 AND hbcal < 80 ORDER BY habit_name_count DESC LIMIT ' . $limit;
        $sqlLv3 = 'SELECT ' . $this->cols . ', (success_track / total_track) * 100 AS hbcal FROM `habit_suggestion` HAVING hbcal < 50 ORDER BY habit_name_count DESC LIMIT ' . $limit;
        $arr_lv1 = array();
        $arr_lv2 = array();
        $arr_lv3 = array();
        $stmt = $this->conn->prepare($sqlLv1);
        if ($stmt->execute()) {
            while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                array_push($arr_lv1, $row);
            }
        }
        $stmt = $this->conn->prepare($sqlLv2);
        if ($stmt->execute()) {
            while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                array_push($arr_lv2, $row);
            }
        }
        $stmt = $this->conn->prepare($sqlLv3);
        if ($stmt->execute()) {
            while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                array_push($arr_lv3, $row);
            }
        }
        $arr_res = array(
            $arr_lv1,
            $arr_lv2,
            $arr_lv3
        );
        return $arr_res;
    }
}

?>
