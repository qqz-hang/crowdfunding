<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>控制面板</title>
    <jsp:include page="include-head.jsp"/>
</head>
<body>
<jsp:include page="include-nav.jsp"/>
<div class="container-fluid">
    <div class="row">
        <jsp:include page="include-sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="admin/to/main/page.html">首页</a></li>
                <li><a href="admin/get/page.html">数据列表</a></li>
                <li class="active">修改</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-heading">表单数据
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <form role="form" action="admin/update.html" method="post">
                        <input type="hidden" id="id" name="id" value="${requestScope.admin.id}"/>
                        <input type="hidden" id="pageNum" name="pageNum" value="${param.pageNum}"/>
                        <input type="hidden" id="keyword" name="keyword" value="${param.keyword}"/>
                        <p>${requestScope.exception.message}</p>
                        <div class="form-group">
                            <label for="logAcct">登录账号</label>
                            <input type="text" class="form-control" name="loginAcct" id="logAcct"
                                   value="${requestScope.admin.loginAcct}">
                        </div>
                        <div class="form-group">
                            <label for="userName">用户昵称</label>
                            <input type="text" class="form-control" name="userName" id="userName"
                                   value="${requestScope.admin.userName}">
                        </div>
                        <div class="form-group">
                            <label for="email">邮箱地址</label>
                            <input type="email" name="email" class="form-control" id="email"
                                   value="${requestScope.admin.email}">
                            <p class="help-block label label-warning">请输入合法的邮箱地址, 格式为： xxxx@xxxx.com</p>
                        </div>
                        <button type="submit" class="btn btn-success"><i class="glyphicon glyphicon-edit"></i> 修改
                        </button>
                        <button type="reset" class="btn btn-danger"><i class="glyphicon glyphicon-refresh"></i> 重置
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
