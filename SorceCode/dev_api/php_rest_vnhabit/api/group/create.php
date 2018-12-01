<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/config.php';
include_once '../../models/Group.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$group = new Group($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$group->group_id = $data->group_id;
$group->user_id = $data->user_id;
$group->group_name = $data->group_name;
$group->group_description = $data->group_description;

if ($group->create()) {
    echo json_encode(
        array(
            'result' => '1',
            'id' => $group->group_id
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
