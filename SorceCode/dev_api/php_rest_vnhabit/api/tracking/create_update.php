<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/Tracking.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$tracker = new Tracking($db);

// Get raw posted data
$raw = json_decode(file_get_contents("php://input"));
$data = $raw->data;

$arrTrack = array();
for($i = 0; $i < count($data); $i++) {
    array_push($arrTrack, get_object_vars($data[$i]));
}

var_dump($arrTrack);

for($i = 0; $i < count($arrTrack); $i++) {
    $row = $tracker->getTrackWithParam($arrTrack[$i]);
    if($row) {
        $tracker->updateWithParam($arrTrack[$i]);
    } else {
        $tracker->createWithParam($arrTrack[$i]);
    }
}

echo json_encode(
    array(
        'result' => '1',
        'id' => count($data)
    )
);

?>
