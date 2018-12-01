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

// Get username and password
$user->username = isset($_POST['username']) ? $_POST['username'] : die();
$user->password = isset($_POST['password']) ? $_POST['password'] : die();

// Get user by username and password
$foundUser = $user->read_single();

// found one user
if (isset($foundUser)) {
    echo json_encode(
        array(
            'result' => '1',
            'data' => $foundUser 
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
