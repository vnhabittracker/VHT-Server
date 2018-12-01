<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/config.php';
include_once '../../models/User.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$user = new User($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

// Set ID to update
$user->user_id = $data->user_id;
$user->user_score = $data->user_score;

if ($user->updateScore()) {
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
