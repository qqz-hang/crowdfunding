<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>控制面板</title>
    <jsp:include page="include-head.jsp"/>
    <link rel="stylesheet" href="css/pagination.css"/>
    <script type="text/javascript" src="jquery/jquery.pagination.js"></script>
    <link rel="stylesheet" href="ztree/zTreeStyle.css"/>
    <script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="crowd/my-role.js"></script>
    <script type="text/javascript">
        $(function () {
            // 初始化数据
            window.pageNum = 1;
            window.pageSize = 5;
            window.keyword = "";
            generatePage()
            // 给查询按钮绑定单击函数
            $("#searchBtn").click(function () {
                // 获取关键词数据 赋值给全局变量
                window.keyword = $("#keywordInput").val()
                generatePage()
            })
            $("#showAddModelBtn").click(function () {
                $('#addModal').modal('show')
            })
            // 给模态框中的新增按钮绑定单击响应函数
            $("#saveRoleBtn").click(function () {
                let roleName = $.trim($("#addModal [name=roleName]").val())

                $.ajax({
                    "url": "role/sava.json",
                    "type": "post",
                    "data": {
                        "name": roleName
                    },
                    "dataType": "json",
                    "success": function (response) {
                        let result = response.result;
                        //  layer.msg(result === "SUCCESS" ? "操作成功" : "操作失败" + response.message);
                        if (result === "SUCCESS") {
                            layer.msg("操作成功");
                            // 跳转到最后一页
                            window.pageNum = 9999;
                            // 重新加载分页
                            generatePage()
                        }
                        if (result === "FAILED") {
                            layer.msg("操作失败" + response.message);
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                // 关闭模态框
                $('#addModal').modal('hide')
                // 清理模态框
                $("#inputSuccess4").val("")
            });
            // 使用jQuery的on函数 给动态生成的按钮 绑定单击函数
            $("#rolePageBody").on("click", ".pencilBtn", function () {
                // 打开模态框
                $("#editModal").modal("show");
                // 获取roleName
                let roleName = $(this).parent().prev().text();
                // 获取roleId
                window.roleId = this.id;
                // 将数据进行回显
                $("#editModal [name=roleName]").val(roleName);
            });
            $("#updateRoleBtn").click(function () {
                let roleName = $("#editModal [name=roleName]").val();

                $.ajax({
                    "url": "role/update.json",
                    "type": "post",
                    "data": {
                        "id": window.roleId,
                        "name": roleName
                    },
                    "dataType": "json",
                    "success": function (response) {
                        let result = response.result;
                        //  layer.msg(result === "SUCCESS" ? "操作成功" : "操作失败" + response.message);
                        if (result === "SUCCESS") {
                            layer.msg("操作成功");
                            // 重新加载分页
                            generatePage()
                        }
                        if (result === "FAILED") {
                            layer.msg("操作失败" + response.message);
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                // 关闭模态框
                $('#editModal').modal('hide')
            });
            $("#removeRoleBtn").click(function () {
                // 把json数组转换成字符串
                let requestBody = JSON.stringify(window.roleIdArray)
                $.ajax({
                    "url": "role/remove/id/array.json",
                    "type": "post",
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "dataType": "json",
                    "success": function (response) {
                        let result = response.result;
                        //  layer.msg(result === "SUCCESS" ? "操作成功" : "操作失败" + response.message);
                        if (result === "SUCCESS") {
                            layer.msg("操作成功");
                            // 重新加载分页
                            generatePage()
                        }
                        if (result === "FAILED") {
                            layer.msg("操作失败" + response.message);
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                // 关闭模态框
                $("#confirmModal").modal("hide");
            });
            // 单条删除
            $("#rolePageBody").on("click", ".removeBtn", function () {
                let roleName = $(this).parent().prev().text()
                let roleArray = [{
                    "roleId": this.id,
                    "roleName": roleName
                }]
                showConfirmModal(roleArray);
            });
            // 给总的按钮绑定响应函数
            $("#summaryBox").click(function () {
                let currentStatus = this.checked;
                $(".itemBox").prop("checked", currentStatus);
            });
            $("rolePageBody").on("click", ".itemBox", function () {
                // 获取当前已经选中的.itemBox的数量
                let checkedBoxCount = $(".itemBox:checked").length;
                // 获取全部的数量
                let totalBoxCount = $(".itemBox").length;
                $("#summaryBox").prop("checked", checkedBoxCount === totalBoxCount);
            });
            $("#batchRemoveBtn").click(function () {
                // 准备一个空的数组接受下面要传入的对象
                let roleArray = [];
                //遍历当前选中的多选框
                $(".itemBox:checked").each(function () {
                    let roleId = this.id;
                    let roleName = $(this).parent().next().text();
                    roleArray.push({
                        "roleId": roleId,
                        "roleName": roleName
                    });
                });
                if (roleArray.length === 0) {
                    layer.msg("请至少选择一个进行删除");
                    return;
                }
                showConfirmModal(roleArray);
                $("#confirmModal").modal("hide");
            });
            // 使用jQuery的on函数 给动态生成的按钮 绑定单击函数
            $("#rolePageBody").on("click", ".checkBtn", function () {
                // 把当前的角色id存入全局变量
                window.roleId = this.id;
                // 打开模态框
                $("#assignModal").modal("show");
                fillAuthTree();
            });
            $("#assignBtn").click(function () {
                let authIdArray = [];
                let zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
                let checkedNodes = zTreeObj.getCheckedNodes();
                for (let i = 0; i < checkedNodes.length; i++) {
                    let checkedNode = checkedNodes[i];
                    let authId = checkedNode.id;
                    authIdArray.push(authId);
                }
                let requestBody = {
                    "authIdArray": authIdArray,
                    "roleId": [window.roleId]
                };
                requestBody = JSON.stringify(requestBody);
                $.ajax({
                    "url": "assign/do/role/assign/auth.json",
                    "type": "post",
                    "dataType": "json",
                    "data": requestBody,
                    "contentType": "application/json;charset=UTF-8",
                    "success": function (response) {
                        let result = response.result;
                        if (result === "SUCCESS") {
                            layer.msg("操作成功！");
                        }
                        if (result === "FAILED") {
                            layer.msg("操作失败！" + response.message);
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " ====" + response.statusText);
                    }
                });
                $("#assignModal").modal("hide");
            });
        });
    </script>
</head>
<body>
<jsp:include page="include-nav.jsp"/>
<div class="container-fluid">
    <div class="row">
        <jsp:include page="include-sidebar.jsp"/>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" id="searchBtn" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button type="button" id="batchRemoveBtn" class="btn btn-danger"
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" id="showAddModelBtn" class="btn btn-primary" style="float:right;"
                    ><i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody">

                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="modal-role-add.jsp"/>
<jsp:include page="modal-role-edit.jsp"/>
<jsp:include page="modal-role-confirm.jsp"/>
<jsp:include page="modal-role-assign-auth.jsp"/>
</body>
</html>
