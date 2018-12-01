<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: DELETE');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/config.php';
include_once '../../models/Habit.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$habit = new Habit($db);

$habit->habit_id = isset($_GET['habit_id']) ? $_GET['habit_id'] : die();

if ($habit->delete()->rowCount() > 0) {
    echo json_encode(
        array(
            'result' => '1'
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
