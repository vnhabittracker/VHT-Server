<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/config.php';
include_once '../../models/MonitorDate.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$date = new MonitorDate($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));
$date->monitor_id = $data->monitor_id;
$date->habit_id = $data->habit_id;
$date->mon = $data->mon;
$date->tue = $data->tue;
$date->wed = $data->wed;
$date->thu = $data->thu;
$date->fri = $data->fri;
$date->sat = $data->sat;
$date->sun = $data->sun;

if ($date->update()) {
    echo json_encode(
        array(
            'result' => '1',
            'id' => $date->monitor_id
        )
    );
} else {
    echo json_encode(
        array(
            'result' => '0'
        )
    );
}

?>
