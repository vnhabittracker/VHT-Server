<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/Group.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$group = new Group($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$group->group_id = $data->group_id;
$group->parent_id = $data->parent_id;
$group->group_name = $data->group_name;
$group->group_icon = $data->group_icon;
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
