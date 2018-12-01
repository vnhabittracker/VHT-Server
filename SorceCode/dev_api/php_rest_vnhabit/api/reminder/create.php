<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/config.php';
include_once '../../models/Reminder.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$reminder = new Reminder($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));
$reminder->habit_id = $data->habit_id;
$reminder->reminder_time = $data->reminder_time;
$reminder->repeat_time = $data->repeat_time;
$reminder->reminder_description = $data->reminder_description;

// Create user
if ($reminder->create()) {
    echo json_encode(
        array(
            'result' => '1',
            'id' => $reminder->reminder_id
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
