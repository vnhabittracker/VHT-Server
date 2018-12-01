<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/HabitSuggestion.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

$hbsg = new HabitSuggestion($db);

$result = $hbsg->getRecommendList(5);

if ($result) {
    echo json_encode(
        array(
            'result' => '1',
            'data' => $result
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
