<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/HabitSuggestion.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$habit = new HabitSuggestion($db);

$search_key = isset($_GET['search']) ? $_GET['search'] : die();

$result = $habit->search($search_key);

$row_count = $result->rowCount();

if ($row_count > 0) {
    $arr = array();
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        array_push($arr, $row);
    }

    // turn to JSON
    echo json_encode(
        array(
            'result' => '1',
            'data' => $arr
        )
    );

} else {
    echo json_encode(
        array(
            'result' => '0'
        )
    );
    die();
}

?>
