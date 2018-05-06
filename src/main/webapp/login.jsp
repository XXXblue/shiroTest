<%--
  Created by IntelliJ IDEA.
  User: xiaojianyu
  Date: 2018/5/6
  Time: 15:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/asset/jquery-1.11.3.min.js"></script>
</head>
<body>

    名<input id ="0" type="text" name="userName">
    吗<input id ="1" type="text" name="password">
    <button onclick="submit();">fdaf</button>
    <script type="text/javascript">
        function submit() {
            var userName = $('#0').val();
            var password = $('#1').val();
            $.ajax({
                url:"/subLogin.json",
                type:"post",
                dataType:"json",
                data:{"userName":userName,"password":password},
                success:function (data) {
                   console.log(data);
                }
            })
        }
    </script>
</body>
</html>
