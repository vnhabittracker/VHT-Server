<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/Database.php';
include_once '../../models/Habit.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate Habit object
$habit = new Habit($db);

// User query
$result = $habit->read();

// get row count
$num = $result->rowCount();

// check if any users
if ($num > 0) {
    $habits_arr = array();
    $habits_arr['data'] = array();
    
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $habit_item = array(
            'habit_id' => $habit_id,
            'user_id' => $user_id,
            'catagory_id' => $catagory_id,
            'schedule_id' => $schedule_id,
            'goal_id' => $goal_id,
            'habit_name' => $habit_name,
            'habit_type' => $habit_type,
            'unit' => $unit,
            'count_type' => $count_type,
            'start_date' => $start_date,
            'end_date' => $end_date,
            'created_date' => $created_date,
            'habit_icon' => $habit_icon,
            'habit_description' => $habit_description
        );

        // push to "data"
        array_push($habits_arr['data'], $habit_item);
    }

    // turn to JSON
    echo json_encode($habits_arr);

} else {
    // no users
    echo json_encode(
        array('message' => 'No Habits Found')
    );
    die();
}

?>
