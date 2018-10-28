<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');

include_once '../../config/Database.php';
include_once '../../models/Tracking.php';

// Instantiate DB & connect
$database = new Database();
$db = $database->connect();
$tracker = new User($db);

?>
