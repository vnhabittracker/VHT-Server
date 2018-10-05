<<<<<<< HEAD
<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/Habit.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$habit = new Habit($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

// Set ID to update
$habit->habit_id = $data->habit_id;

$user->username = $data->username;
$user->password = $data->password;
$user->email = $data->email;
$user->date_of_birth = $data->date_of_birth;
$user->gender = $data->gender;
$user->user_icon = $data->user_icon;
$user->avatar = $data->avatar;
$user->user_description = $data->user_description;

if ($habit->update()) {
    echo json_encode(
        array('message' => 'Habit Updated')
    );
} else {
    echo json_encode(
        array('message' => 'Habit Not Updated')
    );
}

?>
=======
<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: PUT');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

include_once '../../config/Database.php';
include_once '../../models/Habit.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();

// Instantiate
$habit = new Habit($db);

// Get raw posted data
$data = json_decode(file_get_contents("php://input"));

// Set ID to update
$habit->habit_id = $data->habit_id;

$habit->user_id = $data->user_id;
$habit->catagory_id = $data->catagory_id;
$habit->schedule_id = $data->schedule_id;
$habit->goal_id = $data->goal_id;
$habit->habit_name = $data->habit_name;
$habit->habit_type = $data->habit_type;
$habit->unit = $data->unit;
$habit->count_type = $data->count_type;
$habit->start_date = $data->start_date;
$habit->end_date = $data->end_date;
$habit->created_date = $data->created_date;
$habit->habit_icon = $data->habit_icon;
$habit->habit_description = $data->habit_description;

if ($habit->update()) {
    echo json_encode(
        array('message' => 'Habit Updated')
    );
} else {
    echo json_encode(
        array('message' => 'Habit Not Updated')
    );
}

?>
>>>>>>> 94ec3212c7b0dbccec027404e7a51aba893218af
