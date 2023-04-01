<%--
  Created by IntelliJ IDEA.
  User: marku
  Date: 18.06.2022
  Time: 17:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="parkhaus.CarParkServlet" %>
<%@ page import="parkhaus.JSON" %>
<%@ page import="java.io.File" %>
<%@ page import="java.nio.file.Files" %>
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
    <div class="container"  style="margin-left: 10px">
        <form class="row row-cols-5" action="CarParkServlet" method="POST">
          <div class="col">
            <input type="submit" class="btn btn-primary form-control" value="Edit Config"  name="cmd"/>
          </div>
          <div class="col">
            <input type="submit" class="btn btn-primary form-control" value="Load from Config"  name="cmd"/>
          </div>
        </form>
        <form class="row row-cols-5" action="CarParkServlet" method="GET">
          <div class="col">
            <input type="submit" class="btn btn-primary form-control" value="Download Config"  name="cmd"/>
          </div>
        </form>
    </div>
  </td>
</tr>
</body>
</html>

