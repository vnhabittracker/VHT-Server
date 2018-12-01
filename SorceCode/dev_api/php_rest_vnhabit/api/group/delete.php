<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/Group.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

$group = new Group($db);

$group->group_id = isset($_GET['group_id']) ? $_GET['group_id'] : die();

$result = $group->deleteById();

if ($result->rowCount() > 0) {
    
    // turn to JSON
    echo json_encode(
        array(
            'result' => $result->rowCount()
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
