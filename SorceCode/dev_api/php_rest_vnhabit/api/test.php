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

?>
