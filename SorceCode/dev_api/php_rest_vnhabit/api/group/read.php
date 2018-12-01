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

$group->user_id = isset($_GET['user_id']) ? $_GET['user_id'] : die();

$result = $group->readByUser();

if ($result->rowCount() > 0) {

    $group_array = array();
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        array_push($group_array, $row);
    }

    // turn to JSON
    echo json_encode(
        array(
            'result' => '1',
            'data' => $group_array
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
