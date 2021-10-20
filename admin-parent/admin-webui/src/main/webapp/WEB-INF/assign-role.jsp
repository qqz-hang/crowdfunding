<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>控制面板</title>
    <jsp:include page="include-head.jsp"/>
    <script type="text/javascript">
        $(function () {
            // 给右边的箭头绑定单击响应函数
            $("#toRightBtn").click(function () {
                // 让左边选中的option追加到右边的select
                $("select:eq(0)>option:selected").appendTo("select:eq(1)");
            });
            $("#toLeftBtn").click(function () {
                // 让左边选中的option追加到右边的select
                $("select:eq(1)>option:selected").appendTo("select:eq(0)");
            });
            $("#submitBtn").click(function () {
                $("select:eq(1)>option").prop("selected", "selected");

            });
            layer.msg("操作成功")
        });
    </script>
</head>
<body>
<jsp:include page="include-nav.jsp"/>
<div class="container-fluid">
    <div class="row">
        <jsp:include page="include-sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="admin/do/assign/role.html" method="post" role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label for="unAssignedRoleList">未分配角色列表</label><br>
                            <select id="unAssignedRoleList" class="form-control" multiple="multiple" size="10"
                                    style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left"
                                    style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label for="assignedRoleList">已分配角色列表</label><br>
                            <select id="assignedRoleList" name="roleIdList" class="form-control" multiple="multiple"
                                    size="10"
                                    style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name}</option>
                                </c:forEach>

                            </select>
                        </div>
                        <button id="submitBtn" style="width: 200px; margin: 50px auto 0 auto;"
                                class="btn btn-lg btn-success btn-block" type="submit">保存
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
