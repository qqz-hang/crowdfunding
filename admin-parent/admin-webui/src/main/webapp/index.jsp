<%--
  Created by IntelliJ IDEA.
  User: 76109
  Date: 2021/9/23
  Time: 19:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <%--    http://localhost:8080/admin_webui/test/ssm.html--%>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/"/>
    <script type="text/javascript" src="jquery/jquery-2.1.1.min.js"></script>
    <script type="text/javascript" src="layer/layer.js"></script>
    <script type="text/javascript">
        $(function () {
            $("#btn").click(function () {
                var array = [5, 8, 12];
                console.log(array.length)
                var requestBody = JSON.stringify(array);
                console.log(requestBody.length)
                $.ajax({
                    "url": "send/array.html",
                    "type": "post",
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "text",
                    "success": function (response) {
                        alert(response)
                    },
                    "error": function (response) {
                        alert(response)
                    }
                })
            })
        })
    </script>
</head>
<body>
<a href="test/ssm.html">测试ssm环境</a><br>
<button id="btn">send[5,8,12]</button>
</body>
</html>
