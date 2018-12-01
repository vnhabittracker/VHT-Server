<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/MonitorDate.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

$monitorDate = new MonitorDate($db);

$result = $monitorDate->read();

// get row count
$row_count = $result->rowCount();

if ($row_count > 0) {
    $monitorDate_arr = array();
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        array_push($monitorDate_arr, $row);
    }

    // turn to JSON
    echo json_encode(
        array(
            'result' => '1',
            'data' => $monitorDate_arr
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
