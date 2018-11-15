<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/Database.php';
include_once '../../models/HabitSuggestion.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$habit = new HabitSuggestion($db);
$habit->habit_name_id = isset($_GET['search_id']) ? $_GET['search_id'] : die();
$result = $habit->updateCount();
?>
