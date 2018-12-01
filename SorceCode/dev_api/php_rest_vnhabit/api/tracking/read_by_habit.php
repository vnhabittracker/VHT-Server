<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/Tracking.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$tracker = new Tracking($db);
$tracker->habit_id = isset($_GET['habit_id']) ? $_GET['habit_id'] : die();
$result = $tracker->getTrackByHabit();

// get row count
$num = $result->rowCount();

if ($num > 0) {
    $arr = array();
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        array_push($arr, $row);
    }

    // turn to JSON
    echo json_encode(
        array(
            'result' => '1',
            'data' => $arr
        )
    );

} else {
    echo json_encode(
        array(
            'result' => '0'
        )
    );
    die();
}

?>
