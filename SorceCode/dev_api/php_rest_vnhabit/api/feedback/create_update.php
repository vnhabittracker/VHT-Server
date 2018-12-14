<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/config.php';
include_once '../../models/Feedback.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$feedback = new Feedback($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$feedback->feedback_id = $data->feedback_id;
$feedback->user_id = $data->user_id;
$feedback->star_num = $data->star_num;
$feedback->feedback_description = $data->feedback_description;

$error = true;

if ($feedback->read_single()) {
    if($feedback->update()) {
        $error = false;
    }
} else {
    if($feedback->create()) {
        $error = false;
    }
}

if (!$error) {
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
