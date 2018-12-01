<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/Habit.php';
include_once '../../models/Tracking.php';
include_once '../../models/Reminder.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate Habit object
$habit = new Habit($db);
$tracker = new Tracking($db);
$reminder = new Reminder($db);

$habit->user_id = isset($_GET['user_id']) ? $_GET['user_id'] : die();

// get habits by user_id
// $result = $habit->read_by_user();
$result = $habit->read_join_monitor();

// get row count
$row_count = $result->rowCount();

if ($row_count > 0) {

    $habits_arr = array();
    
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        extract($row);

        $tracksArr = array();
        $tracker->habit_id = $habit_id;
        $trackRes = $tracker->getTrackByHabit();
        if ($trackRes) {
            while($row2 = $trackRes->fetch(PDO::FETCH_ASSOC)) {
                array_push($tracksArr, $row2);
            }
        }

        $remindersArr = array();
        $reminder->habit_id = $habit_id;
        $reminderRes = $reminder->getRemindersByHabit();
        if ($reminderRes) {
            while($row3 = $reminderRes->fetch(PDO::FETCH_ASSOC)) {
                array_push($remindersArr, $row3);
            }
        }

        $habit_item = array(
            'habit_id' => $habit_id, 
            'user_id' => $user_id, 
            'group_id' => $group_id, 
            'monitor_id' => $monitor_id, 
            'habit_name' => $habit_name, 
            'habit_target' => $habit_target, 
            'habit_type' => $habit_type, 
            'monitor_type' => $monitor_type, 
            'monitor_unit' => $monitor_unit, 
            'monitor_number' => $monitor_number,
            'start_date' => $start_date, 
            'end_date' => $end_date, 
            'created_date' => $created_date, 
            'habit_color' => $habit_color, 
            'habit_description' => $habit_description,
            'mon' => $mon,
            'tue' => $tue,
            'wed' => $wed,
            'thu' => $thu,
            'fri' => $fri,
            'sat' => $sat,
            'sun' => $sun,
            'tracking_list' => $tracksArr,
            'reminder_list' => $remindersArr
        );

        // push to "data"
        array_push($habits_arr, $habit_item);
    }
        // turn to JSON
        echo json_encode(
            array(
                'result' => '1',
                'data' => $habits_arr
            )
        );

    }  else {
        // no users
        echo json_encode(
            array(
                'result' => '0'
            )
        );
        die();
    }
  
?>
