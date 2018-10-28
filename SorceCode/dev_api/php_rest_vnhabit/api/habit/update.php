<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/Reminder.php';
include_once '../../models/Habit.php';
include_once '../../models/MonitorDate.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$habit = new Habit($db);
$reminder = new Reminder($db);
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
    $habit->habit_id = $data->habit_id;
    $habit->user_id = $data->user_id;
    $habit->group_id = $data->group_id;
    $habit->monitor_id = $data->monitor_id;
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
}

if (isset($habit->habit_id)) {
    $arr_reminder = $data->reminders;
    for($i = 0; $i < count($arr_reminder); $i++) {
        $item = $arr_reminder[$i];
        $reminder->reminder_id = $item->reminder_id;
        $reminder->habit_id = $habit->habit_id;
        $reminder->reminder_time = $item->reminder_time;
        $reminder->repeat_time = $item->repeat_time;
        if ($reminder->find($habit->habit_id, $item->reminder_time)) {
            $reminder->updateById($reminder->reminder_id);
        } else {
            $reminder->create();
        }
    }
}

if ($habit->update()) {
    echo json_encode(
        array(
            'result' => '1',
            'id' => $habit->habit_id
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
