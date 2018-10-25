<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/MonitorDate.php';
include_once '../../models/Habit.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$habit = new Habit($db);
$date = new MonitorDate($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$date->mon = $data->mon;
$date->tue = $data->tue;
$date->wed = $data->wed;
$date->thu = $data->thu;
$date->fri = $data->fri;
$date->sat = $data->sat;
$date->sun = $data->sun;

// Create user
if ($date->create()) {
    $habit->monitor_id = $date->monitor_id;
}

if (isset($habit->monitor_id)) {
    $habit->user_id = $data->user_id; 
    $habit->group_id = $data->group_id;
    $habit->habit_name = $data->habit_name;
    $habit->habit_target = $data->habit_target;
    $habit->habit_type = $data->habit_type;
    $habit->monitor_type = $data->monitor_type;
    $habit->monitor_unit = $data->monitor_unit;
    $habit->monitor_number = $data->monitor_number; 
    $habit->start_date = $data->start_date;
    $habit->end_date = $data->end_date;
    $habit->created_date = $data->created_date;
    $habit->habit_color = $data->habit_color;
    $habit->habit_description = $data->habit_description;
    $date->habit_id = $data->habit_id;
    $habit->create();
}

// Create user
if (isset($habit->monitor_id)) {
    echo json_encode(
        array(
            'result' => '1',
            'id' => $habit->monitor_id
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
