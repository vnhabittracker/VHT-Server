<?php

    class Model {

        public function make_query_param($arr) {
            $str = '';
            $length = count($arr);
            for ($i = 0; $i < $length; $i++) {
                $str = $str . $arr[$i] . '= :' . $arr[$i];
                if ($i < $length - 1) {
                    $str = $str . ', ';
                }
            }
            return $str;
        }
    }
?>