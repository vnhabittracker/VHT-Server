<?php

// Headers
header('Access-Control-Allow-Origin: *');
header('Content-Type: application/json');
header('Access-Control-Allow-Methods: GET');
header('Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods, Authorization, X-Requested-With');

echo json_encode(
    array(
        'result' => '1',
        'data' => array(
            'app' => 'vnhabit',
            'desciption' => 'help user in building good habit',
            'date' => date("d/m/Y")
        )
    )
);


$msg = "First line of text\nSecond line of text";

// use wordwrap() if lines are longer than 70 characters
$msg = wordwrap($msg,70);

// send email
mail("tvtd995a3@gmail.com","My subject",$msg);

?>
