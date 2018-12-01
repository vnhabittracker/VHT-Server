<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/Habit.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate Habit object
$habit = new Habit($db);

// User query
$result = $habit->read();

// get row count
$row_count = $result->rowCount();

// check if any users
if ($row_count > 0) {

    $habits_arr = array();
    
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        array_push($habits_arr, $row);
    }

    // turn to JSON
    echo json_encode(
        array(
            'result' => '1',
            'data' => $habits_arr
        )
    );

} else {
    // no users
    echo json_encode(
        array(
            'result' => '0'
        )
    );
    die();
}

?>
