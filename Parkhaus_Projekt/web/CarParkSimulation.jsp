<%--
  Created by IntelliJ IDEA.
  User: marku
  Date: 18.06.2022
  Time: 17:08
  To change this template use File | Settings | File Templates.
--%>
<html>
<head>
    <link rel="stylesheet" href="CarParkSimulation.css">
    <title>Parkhaus Team1</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <meta charset="UTF-8">
</head>

<style>

</style>
<body>



    <input type="submit" id="enter" value="Park Car">
    <input type="submit" id="leave" value="Unpark Car">
    <input type="submit" id="enterN" value="Park Normal Car">
    <input type="submit" id="enterF" value="Park Family Car">
    <input type="submit" id="enterSP" value="Park Special Need Car">
    <input type="submit" class="button-red" id="undo" value="Undo">


    <tr>
        <td>
            <form action="CarParkServlet?cmd=stats" method="GET">
                <div class="container"  style="margin-left: -3px; margin-top: 5px">
                    <div class="row row-cols-5">
                        <div class="col">
                            <input type="submit" class="btn btn-primary form-control" value="Download Stats" name="cmd"/>
                        </div>
                        <div class="col">
                            <input type="submit" class="btn btn-primary form-control" value="Download History" name="cmd"/>
                        </div>

                    </div>
                </div>
            </form>
        </td>
    </tr>

    <tr>
        <td>
            <form action="CarParkServlet" method="POST">
                <div class="container"  style="margin-left: -3px; margin-top: 5px">
                    <div class="row row-cols-5">
                        <div class="col">
                            <input type="submit" class="btn btn-primary form-control" value="Edit Config"  name="cmd"/>
                        </div>
                    </div>
                </div>
            </form>
        </td>
    </tr>






    <divs id="avgParkDurationDivs"></divs>

    <divs id="changedTypeDivs"></divs>

    <divs id="changedTypeOverallDivs"></divs>

<div class="CarParkStatus">
    <h1>Current CarPark Status:</h1>
    <h2 id="freeSlots">Free Slots: </h2>
</div>

<divs id="container">

</divs >

    <table id="stats" class="content-table">
        <thead class="carParkHeader">
        <tr>
            <th>Stats</th>
            <th> </th>
        </tr>
        </thead>

        <tbody id="statsBody">
        <tr>
            <td>Sum of generated fees: </td>
            <td id=sumStat> 0,00 €</td>
        </tr>
        <tr>
            <td>Maximum amount of parking slots: </td>
            <td id=maxCountCarsStat> </td>
        </tr>
        <tr>
            <td>Currently parked cars: </td>
            <td id=countParkedCarsStat> 0 Cars</td>
        </tr>
        <tr>
            <td>Average park duration: </td>
            <td id=avgParkDurationStat> 0 Hours</td>
        </tr>
        <tr>
            <td>Average family cars that are parking on a different slot: </td>
            <td id=changedTypeFamStat> 0 % </td>
        </tr>
        <tr>
            <td>Average special need cars that are parking on a different slot: </td>
            <td id=changedTypeSpnStat> 0 % </td>
        <tr>
            <td>Average family cars that parked on a different slot: </td>
            <td id=changedTypeOverallFamStat> 0 % </td>
        </tr>
        <tr>
            <td>Average special need cars that parked on a different slot: </td>
            <td id=changedTypeOverallSpnStat> 0 % </td>
        </tr>
        </tbody>

    </table>

    <div class="carParkHeader">
    <h1> Carpark History </h1>
    </div>

<table id="leaveCarsTable" class="content-table">

    <thead class="carParkHeader">

        <tr>
            <th>Position</th>
            <th>Parkslot Type</th>
            <th>License Plate</th>
            <th>Park Duration (in hours)</th>
            <th>Paid Fee</th>

        </tr>
    </thead>
    <tbody id="leaveCarsTableBody">

    </tbody>
</table>





<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>
    <script src='https://cdn.plot.ly/plotly-2.12.1.min.js'></script>
    <script
            src="https://code.jquery.com/jquery-3.6.0.min.js"
            integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4="
            crossorigin="anonymous"></script>



    <script>
        let occupiedIds = [];
    $(document).on('click','#enter', function() {
        $.ajax({
            type: "POST",
            url: window.location.href,
            data: {
                'cmd': 'enter'
            },
        });
    });
    $(document).on('click','#leave', function() {
        let item = occupiedIds[Math.floor(Math.random()*occupiedIds.length)];
        $.ajax({
            type: "POST",
            url: window.location.href,
            data: {
                'cmd': 'leave',
                "parkingSlot":item.parkingSlot
            },
            success:function(data){
                let json = JSON.parse(data);
                var rowCarCount = document.getElementById("leaveCarsTable").rows.length;
                $('#leaveCarsTable').find('tbody').append("<tr id='unparkedCars{data}'></tr>".replace("{data}", rowCarCount));
                var row = $("#unparkedCars data".replace(" data", rowCarCount))
                row.append("<td id='ID'> data </td>".replace("data", json["ID"]));
                row.append("<td id='type'> data </td>".replace("data", json["type"]));
                row.append("<td id='licensePlate'> data </td>".replace("data", json["licensePlate"]));
                row.append("<td id='duration'> data </td>".replace("data", json["duration"]));
                row.append("<td id='fee'> data €</td>".replace("data", json["fee"]).replace("€", "&euro;"));

            }
        });
    });

    $(document).on('click','#enterN', function() {
        $.ajax({
            type: "POST",
            url: window.location.href,
            data: {
                'cmd': 'enterNormal'
            },
        });
    });

    $(document).on('click','#enterF', function() {
        $.ajax({
            type: "POST",
            url: window.location.href,
            data: {
                'cmd': 'enterFamily'
            },
        });
    });

    $(document).on('click','#enterSP', function() {
        $.ajax({
            type: "POST",
            url: window.location.href,
            data: {
                'cmd': 'enterSpecialNeed'
            },
        });
    });

        $(document).on('click','#undo', function() {
            $.ajax({
                type: "POST",
                url: window.location.href,
                data: {
                    'cmd': 'undo'
                },
                success:function(){
                    document.getElementById("leaveCarsTableBody").deleteRow(-1);
                }

            });
        });

    var eventSource = new EventSource(window.location.href+"?cmd=status");


    eventSource.onmessage=function(message){
        var slots = 0;
        var freeSlots = 0;

        occupiedIds = []
        let json = JSON.parse(message.data);
        let container = $('#container');
        container.empty();
        let sum = 0;
        for (const element of json['levels']){
            container.append("<div id=\"parkingSlots-{level}\"></div>".replace('{level}',element.name));
            let parkingSlots = $("#parkingSlots-{level}".replace('{level}',element.name));
            parkingSlots.append("<div class=\"levelHeader\" >\r\n            <h2>{level}  <\/h2>\r\n        <\/div>".replace('{level}',element.name));
            for (const parkingSlot of element.parkSlots){
                let status = "visible";
                slots ++;

                if(parkingSlot.freeStatus === 'true'){
                    status = "invisible";
                    freeSlots ++;
                }
                else{

                    occupiedIds.push({"parkingSlot":sum});
                }
                sum+=1;
                if(parkingSlot.type === 'NORMAL'){
                    parkingSlots.append("        <div class=\"parkingSlotN\" id=\"{normal}\">\r\n            <div class=\"car\" id=\"car{normal}\" ><\/div>\r\n            <style class=\"car\">\r\n                #car{normal}{\r\n                    background-color: {color};\r\n                    visibility: {freeStatus};\r\n                }\r\n            <\/style>\r\n        <\/div>".replaceAll('{normal}',element.name+parkingSlot.id).replace('{color}',parkingSlot.color).replace('{freeStatus}',status));
                }
                if(parkingSlot.type === 'SPECIAL_NEED'){
                    parkingSlots.append("        <div class=\"parkingSlotSP\" id=\"{disabled}\">\r\n            <div class=\"car\" id=\"car{disabled}\" ><\/div>\r\n            <style class=\"car\">\r\n                #car{disabled}{\r\n                    background-color: {color};\r\n                    visibility: {freeStatus};\r\n                }\r\n            <\/style>\r\n        <\/div>".replaceAll('{disabled}',element.name+parkingSlot.id).replace('{color}',parkingSlot.color).replace('{freeStatus}',status));
                }

                if(parkingSlot.type === 'FAMILY'){
                    parkingSlots.append("        <div class=\"parkingSlotF\" id=\"{family}\">\r\n            <div class=\"car\" id=\"car{family}\" ><\/div>\r\n            <style class=\"car\">\r\n                #car{family}{\r\n                    background-color: {color};\r\n                    visibility: {freeStatus};\r\n                }\r\n            <\/style>\r\n        <\/div>".replaceAll('{family}',element.name+parkingSlot.id).replace('{color}',parkingSlot.color).replace('{freeStatus}',status));
                }
            }

        }
        document.getElementById('maxCountCarsStat').innerHTML = "data Cars".replace('data',slots);
        document.getElementById('freeSlots').innerHTML = "Free Slots: data".replace('data',freeSlots);
        document.getElementById('sumStat').innerHTML = "data €".replace('data',Number(json['sumFees']).toFixed(2)).replace("€", "&euro;");
        document.getElementById('changedTypeFamStat').innerHTML = "data %".replace('data',Number(JSON.parse(json['changedType'])[0]).toFixed(2));
        document.getElementById('changedTypeSpnStat').innerHTML = "data %".replace('data',Number(JSON.parse(json['changedType'])[1]).toFixed(2));
        document.getElementById('changedTypeOverallFamStat').innerHTML = "data %".replace('data',Number(JSON.parse(json['changedTypeOverall'])[0]).toFixed(2));
        document.getElementById('changedTypeOverallSpnStat').innerHTML = "data %".replace('data',Number(JSON.parse(json['changedTypeOverall'])[1]).toFixed(2));
        document.getElementById('avgParkDurationStat').innerHTML = "data Hours".replace('data',Number(json['avgParkDuration']).toFixed(2));
        document.getElementById('countParkedCarsStat').innerHTML = "data Cars".replace('data',json['countParkedCars']);
    };

</script>

    <div class="box lightblue">
        <h2>Free and taken parkslots</h2>
        <div id='myDiv'></div>
        <div id='myDiv2'></div>
        <script>
            var eventSource = new EventSource(window.location.href+"?cmd=chartCount");
            eventSource.onmessage=function(message){
                Plotly.newPlot('myDiv', JSON.parse(message.data));
            };



        </script>
    </div>

    <div class="box lightblue">
        <h2>Capacity per parkslot type per level</h2>
        <div id='myDivCapacity'></div>
        <script>
            var eventSource = new EventSource(window.location.href+"?cmd=capacity");
            eventSource.onmessage=function(data){
                let json = JSON.parse(data.data);
                var yData = []
                var xData = []
                for (const el of json["y"]){
                    yData.push(el)
                }
                for (const el of json["x"]){
                    xData.push(el + " Normal")
                    xData.push(el + " Family")
                    xData.push(el + " Special Need")
                }

                var barData = [{
                    x: xData,
                    y: yData,
                    type: 'bar'
                }];
                Plotly.newPlot('myDivCapacity', barData);
            };



        </script>
    </div>

</body>

</html>
