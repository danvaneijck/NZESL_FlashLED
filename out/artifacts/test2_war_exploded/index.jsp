<%--
  Created by IntelliJ IDEA.
  User: Dan
  Date: 13/02/2020
  Time: 11:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>NZESL FlashLED</title>
    <script src="https://code.jquery.com/jquery-1.10.2.js" type="text/javascript"></script>
    <script src="ajax.js" type="text/javascript"></script>
  </head>
  <body>
  <form>
    SCAN item:<input type="text" id="item_ID">
    <button type="button" id="button1" value="flashON">Flash LED</button>
    <button type="button" id="button2" value="flashOFF">Flash OFF</button>
  </form>
  <strong>Server Response</strong>:
  <div id="ajaxResponse"></div>
  </body>
</html>
