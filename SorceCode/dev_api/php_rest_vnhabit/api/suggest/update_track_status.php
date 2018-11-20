<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/Database.php';
include_once '../../models/HabitSuggestion.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();
$hbsg = new HabitSuggestion($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));
$hbsg->habit_name_id = $data->habit_name_id;
$hbsg->total_track = $data->total_track;
$hbsg->success_track = $data->success_track;

if ($hbsg->updateTrack()) {
    echo json_encode(
        array(
            'result' => '1',
            'data' => $hbsg->habit_name_id
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
