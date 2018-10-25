<?php

    class Model {

        public $defExcludeArr = array('conn', 'table', 'cols', 'params', 'defExcludeArr');

        public function excludeParam($arr1, $arr2) {
            if (isset($arr2)){
                for ($i=0; $i < count($arr2); $i++) {
                    unset($arr1[$arr2[$i]]);
                }
            }
            return $arr1;
        }

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

        public function get_read_param($excludeArr, $pre) {
            $array = get_object_vars($this);
            $array = $this->excludeParam($array, $this->defExcludeArr);
            $array = $this->excludeParam($array, $excludeArr);
            $array = array_keys($array);
            if (isset($pre)) {
                for ($i = 0; $i < count($array); $i++) {
                    $array[$i] = $pre . '.' . $array[$i];
                }
            }
            return implode(", ", $array);
        }

        public function get_query_param($excludeArr) {
            $str = '';
            $array = get_object_vars($this);
            $array = $this->excludeParam($array, $this->defExcludeArr);
            $array = $this->excludeParam($array, $excludeArr);
            $array = array_keys($array);
            $length = count($array);
            for ($i = 0; $i < $length; $i++) {
                $str = $str . '`' . $array[$i] . '`' . '= :' . $array[$i];
                if ($i < $length - 1) {
                    $str = $str . ', ';
                }
            }
            return $str;
        }

        public function bind_param_exc($stmt, $excludeArr) {
            $array = get_object_vars($this);
            $array = $this->excludeParam($array, $this->defExcludeArr);
            $array = $this->excludeParam($array, $excludeArr);
            foreach($array as $key => $value) {
                $stmt->bindValue(':' . $key, $value);
            }
            return $stmt;
        }

        public function bind_param($stmt, $array) {
            foreach($array as $key => $value) {
                $stmt->bindValue(':' . $key, $value);
            }
            return $stmt;
        }
    }
?>
