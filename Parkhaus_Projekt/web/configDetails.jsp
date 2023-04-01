<%@ page import="parkhaus.CarPark" %>
<%@ page import="parkhaus.CarParkServlet" %>
<%@ page import="parkhaus.Car" %>
<%@ page import="parkhaus.JSON" %>
<%@ page import="parkhaus.JSON" %><%--

  Created by IntelliJ IDEA.
  User: marku
  Date: 18.06.2022
  Time: 20:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Parkhaus Team1</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <style>
        * {
            font-family: sans-serif, Helvetica, Arial;
        }
        .grey-background {
            background-color: lightgrey;
        }
        .center {
            text-align: center;
        }
        .box {
            border: thin solid black;
            margin: 0.5rem 0;
            padding: 1rem;
        }
        .lightblue {
            background-color: #d0ebf6;
        }
        .lightyellow {
            background-color: lightgoldenrodyellow;
        }
        .lightgreen {
            background-color: #ccfdcc;
        }
    </style>
</head>


<body style="background-color: ghostwhite">
<div class="box center grey-background">
    <h1>Parkhaus Team1</h1>
    <p>Tomcat Version : <%= application.getServerInfo() %></p>
</div>


 <tr>
      <td>
        <form action="CarParkServlet" method="POST">
            <div class="container">
                <div class="row row-cols-5" id="levels">
          <%
    for(int i = 0; i < JSON.getConfigJson().getJSONArray("levels").length(); i++){
%>
                <div class="col level-<%=(i + 1)%>">
                    <label class="form-label">Nummer</label>
                    <span class="input-group-text" id="basic-addon1"><%=(i + 1)%></span>
                </div>
                <div class="col level-<%=(i + 1)%>">

                    <label for="levelName<%=(i + 1)%>" class="form-label">Level Name</label>
                    <input type="text" class="form-control" id="levelName<%=(i + 1)%>" name="levelName<%=(i + 1)%>">
                </div>
                <div class="col level-<%=(i + 1)%>">
                    <label for="normalAmount<%=(i + 1)%>" class="form-label">Normal Amount</label>
                    <input type="number" class="form-control" id="normalAmount<%=(i + 1)%>" min="0" name="normalAmount<%=(i + 1)%>">
                </div>
                <div class="col level-<%=(i + 1)%>">
                    <label for="familyAmount<%=(i + 1)%>" class="form-label">Family Amount</label>
                    <input type="number" class="form-control" id="familyAmount<%=(i + 1)%>" min="0" name="familyAmount<%=(i + 1)%>">
                </div>
                <div class="col level-<%=(i + 1)%>">
                    <label for="disabledAmount<%=(i + 1)%>" class="form-label">Disabled Amount</label>
                    <input type="number" class="form-control" id="disabledAmount<%=(i + 1)%>" min="0" name="disabledAmount<%=(i + 1)%>">
                </div>

<%
    }

%>

                </div>
                <div class="row row-cols-5">
                    <div class="col">
                        <label for="fees" class="form-label">Fees</label>
                        <input type="number" class="form-control" id="fees" min="0" name="fees" step="0.01">
                    </div>
                    <div class="col">
                        <label  class="form-label">Button</label>
                        <input type="submit" class="btn btn-primary form-control" name="cmd" value="Generate"/>
                    </div>
                    <div class="col">
                        <label class="form-label">Add Row</label>
                        <input type="button" class="btn btn-primary form-control" id="addRow" value="+">
                    </div>
                    <div class="col">
                        <label class="form-label">Remove Row</label>
                        <input type="button" class="btn btn-primary form-control" id="removeRow" value="-">
                    </div>
                    <div class="col">

                        <input hidden type="number" class="btn btn-primary form-control" id="levelAmount" name="levelAmount" value="<%=(JSON.getConfigJson().getJSONArray("levels").length())%>"/>
                    </div>
                </div>
        </div>
      </form>
      </td>
    </tr>

<script
        src="https://code.jquery.com/jquery-3.6.0.min.js"
        integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4="
        crossorigin="anonymous"></script>
<script>

    $(document).on('click','#addRow', function() {
        let level = 0;
        try{
            level = parseInt($("#levels div:last-child").attr('class').slice(-1));
        }catch (err){
        }
        let pointer = $('#levels')
        level+=1;
        $('#levelAmount').attr("value",level.toString());
        pointer.append('                <div class=\"col level-{level}\">\n                    <label class=\"form-label\">Nummer</label>\n                    <span class=\"input-group-text\" id=\"basic-addon1\">{level}</span>\n                </div>\n                <div class=\"col level-{level}\">\n\n                    <label for=\"levelName{level}\" class=\"form-label\">Level Name</label>\n                    <input type=\"text\" class=\"form-control\" id=\"levelName{level}\" name=\"levelName{level}\">\n                </div>\n                <div class=\"col level-{level}\">\n                    <label for=\"normalAmount{level}\" class=\"form-label\">Normal Amount</label>\n                    <input type=\"number\" class=\"form-control\" id=\"normalAmount{level}\" min=\"1\" name=\"normalAmount{level}\">\n                </div>\n                <div class=\"col level-{level}\">\n                    <label for=\"familyAmount{level}\" class=\"form-label\">Family Amount</label>\n                    <input type=\"number\" class=\"form-control\" id=\"familyAmount{level}\" min=\"1\" name=\"familyAmount{level}\">\n                </div>\n                <div class=\"col level-{level}\">\n                    <label for=\"disabledAmount{level}\" class=\"form-label\">Disabled Amount</label>\n                    <input type=\"number\" class=\"form-control\" id=\"disabledAmount{level}\" min=\"1\" name=\"disabledAmount{level}\">\n                </div>'.replaceAll('{level}',level));
    });

    $(document).on('click','#removeRow', function() {
        let level = 0;
        try{
            level = parseInt($("#levels div:last-child").attr('class').slice(-1));
        }catch (err){
        }
        let pointer = $('#levels')
        if(level>0){
            pointer.find('.level-{level}'.replace('{level}',level)).remove();
            level--;
            $('#levelAmount').attr("value",level.toString());
        }

    });
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>
</body>
</html>
