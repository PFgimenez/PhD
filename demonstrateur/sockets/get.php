<?php
    $time_start = microtime(true);
    include("functions_socket.php");

    init("lextree","renault_small_header");

    $data3 = get_data();
    echo "data3[]:<br>\n";
    print_r($data3);

    byebye();
    $time_end = microtime(true);
    $time = $time_end - $time_start;
?>

