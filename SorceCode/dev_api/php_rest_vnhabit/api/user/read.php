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

// User query
$result = $user->read();

// get row count
$num = $result->rowCount();

// check if any users
if ($num > 0) {
    $users_arr = array();
    $users_arr['data'] = array();
    
    while($row = $result->fetch(PDO::FETCH_ASSOC)) {
        extract($row);
        $user_item = array(
            'id' => $user_id,
            'username' => $username,
            'password' => $password,
            'email' => $email,
            'gender' => $gender,
            'date_of_birth' => $date_of_birth,
            'user_icon' => $user_icon,
            'avatar' => $avatar,
            'user_description' => $user_description
        );

        // push to "data"
        array_push($users_arr['data'], $user_item);
    }

    // turn to JSON
    echo json_encode($users_arr);

} else {
    // no users
    echo json_encode(
        array('message' => 'No Users Found')
    );
    die();
}

?>
