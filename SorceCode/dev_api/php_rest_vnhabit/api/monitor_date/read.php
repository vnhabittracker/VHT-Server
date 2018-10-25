<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/Database.php';
include_once '../../models/MonitorDate.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$date = new MonitorDate($db);

// User query
$result = $date->read();

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
    // no users
    echo json_encode(
        array(
            'result' => '0'
        )
    );
    die();
}

?>
