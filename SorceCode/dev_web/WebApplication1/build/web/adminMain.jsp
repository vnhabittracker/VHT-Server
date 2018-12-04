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
    
    <title>JSP Page</title>
</head>
<body>
    <div id="piechart" style="width: 900px; height: 500px;"></div>
</body>
<script>      google.charts.load('current', {'packages': ['corechart']});
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {

        var data = google.visualization.arrayToDataTable([
            ['', 'Hours per Day'],
            ['Đọc sách', 11],
            ['Ăn sáng', 2],
            ['Giao tiếp', 2],
            ['Tập thể dục', 2],
            ['Ngủ sớm', 7]
        ]);

        var options = {
            title: 'My Daily Activities'
        };

        var chart = new google.visualization.PieChart(document.getElementById('piechart'));

        chart.draw(data, options);
    }</script>
</html>
