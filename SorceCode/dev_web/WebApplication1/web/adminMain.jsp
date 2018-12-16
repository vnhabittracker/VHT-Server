<%-- 
    Document   : adminMain
    Created on : Dec 4, 2018, 9:46:51 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

        <title>Top habit used</title>
    </head>
    <body>
        <div id="piechart" style="width: 900px; height: 500px;"></div>
    </body>
    <script>
        google.charts.load('current', {'packages': ['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart() {

            var data = google.visualization.arrayToDataTable([
                ['', 'Hours per Day'],
                ['Đọc sách', 101],
                ['Ăn sáng', 40],
                ['Giao tiếp', 34],
                ['Tập thể dục', 22],
                ['Ngủ sớm', 11]
            ]);

            var options = {
                title: 'Thống kê các thói quen được sử dụng nhiều'
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart'));

            chart.draw(data, options);
        }</script>
</html>
