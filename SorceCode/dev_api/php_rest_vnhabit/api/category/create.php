<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: POST');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/User.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$user = new User($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

$user->username = $data->username;
$user->password = $data->password;
$user->email = $data->email;
$user->phone = $data->phone;
// $user->date_of_birth = $data->date_of_birth;
// $user->gender = $data->gender;
// $user->user_icon = $data->user_icon;
// $user->avatar = $data->avatar;
// $user->user_description = $data->user_description;

// Create user
if ($user->create()) {
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
