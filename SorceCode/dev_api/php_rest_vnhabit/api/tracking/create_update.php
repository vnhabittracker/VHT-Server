<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/Tracking.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$tracker = new Tracking($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$tracker->habit_id = $data->habit_id;
$tracker->current_date = $data->current_date;
$tracker->count = $data->count;
$tracker->tracking_description = $data->tracking_description;

$error = false;
if ($tracker->get_tracking() != NULL) {
    if ($tracker->update() != NULL) {
        echo json_encode(
            array(
                'result' => '1',
                'id' => $tracker->habit_id
            )
        );
    } else {
        $error = true;
    }
} else if ($tracker->create()) {
    echo json_encode(
        array(
            'result' => '1',
            'id' => $tracker->habit_id
        )
    );
} else {
    $error = true;
}

if ($error == true) {
    echo json_encode(
        array(
            'result' => '0'
        )
    );
}

?>
