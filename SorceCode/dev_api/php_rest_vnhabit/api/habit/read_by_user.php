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

// Get username and password
$habit->user_id = isset($_GET['user_id']) ? $_GET['user_id'] : die();

// get habits by user_id
$result = $habit->read_by_user();

// found one user
if (isset($result)) {
    // Create array
    $habit_arr = array(
        'habit_id' => $user->habit_id, 
        'user_id' => $user->user_id, 
        'catagory_id' => $user->catagory_id, 
        'schedule_id' => $user->schedule_id, 
        'goal_id' => $user->goal_id, 
        'habit_name' => $user->habit_name, 
        'habit_type' => $user->habit_type, 
        'unit' => $user->unit, 
        'count_type' => $user->count_type, 
        'start_date' => $user->start_date, 
        'end_date' => $user->end_date, 
        'created_date' => $user->created_date, 
        'habit_icon' => $user->habit_icon, 
        'habit_description' => $user->habit_description
    );

    // convert array object to JSON object
    print_r(json_encode($habit_arr));
} else {
    // no users
    echo json_encode(
        array('message' => 'No Habits Found')
    );
    die();
}
  
?>
