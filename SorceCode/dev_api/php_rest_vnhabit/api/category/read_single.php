<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/Database.php';
include_once '../../models/User.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate User object
$user = new User($db);

// Get username and password
$user->username = isset($_GET['username']) ? $_GET['username'] : die();
$user->password = isset($_GET['password']) ? $_GET['password'] : die();

// Get user by username and password
$result = $user->read_single();

// found one user
if (isset($result)) {
    // Create array
    $user_arr = array(
        'user_id' => $user->user_id, 
        'username' => $user->username, 
        'password' => $user->password, 
        'email' => $user->email, 
        'date_of_birth' => $user->date_of_birth, 
        'gender' => $user->gender, 
        'user_icon' => $user->user_icon, 
        'avatar' => $user->avatar, 
        'user_description' => $user->user_description
    );

    // convert array object to JSON object
    echo json_encode(
        array(
            'result' => '1',
            'data' => $user_arr
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
