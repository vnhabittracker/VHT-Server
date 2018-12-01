<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/HabitSuggestion.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

$hbsg = new HabitSuggestion($db);

$result = $hbsg->read();

// get row count
$row_count = $result->rowCount();

if ($row_count > 0) {
    $hbsg_arr = array();
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        array_push($hbsg_arr, $row);
    }
    echo json_encode(
        array(
            'result' => '1',
            'data' => $hbsg_arr
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
