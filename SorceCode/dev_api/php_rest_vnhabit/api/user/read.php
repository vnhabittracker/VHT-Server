<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/config.php';
include_once '../../models/User.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$user = new User($db);

// User query
$result = $user->read();

// get row count
$row_count = $result->rowCount();

// check if any users
if ($row_count > 0) {
    $users_arr = array();
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        // push to "data"
        array_push($users_arr, $row);
    }

    // turn to JSON
    echo json_encode(
        array(
            'result' => '1',
            'data' => $users_arr
        )
    );

} else {
    // no users
    echo json_encode(
        array(
            'result' => '0'
        )
    );
    die();
}

?>
