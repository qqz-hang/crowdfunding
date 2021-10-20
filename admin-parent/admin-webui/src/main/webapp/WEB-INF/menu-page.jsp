<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>控制面板</title>
    <jsp:include page="include-head.jsp"/>
    <link rel="stylesheet" href="ztree/zTreeStyle.css"/>
    <script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
    <script type="text/javascript" src="crowd/my-menu.js"></script>
    <script type="text/javascript">
        $(function () {
            // 调用生成树形结构的函数
            generateTree();
            $("#treeDemo").on("click", ".addBtn", function () {
                // 把当前id作为新节点的pid
                window.pid = this.id;
                // 打开模态框
                $("#menuAddModal").modal("show");
                return false;
            });
            // 给添加的子节点增加单击响应函数
            $("#menuSaveBtn").click(function () {
                // 获取表单中的数据
                // 收集表单项中用户输入的数据
                let name = $.trim($("#menuAddModal [name=name]").val());
                let url = $.trim($("#menuAddModal [name=url]").val());
                // 单选按钮要定位到“被选中”的那一个
                let icon = $("#menuAddModal [name=icon]:checked").val();
                // 发起Ajax请求
                $.ajax({
                    "url": "menu/save.json",
                    "type": "post",
                    "data": {
                        "pid": window.pid,
                        "name": name,
                        "url": url,
                        "icon": icon
                    },

                    "dataType": "json",
                    "success": function (response) {
                        let result = response.result;
                        if (result === "SUCCESS") {
                            layer.msg("操作成功！");
                            generateTree();
                        }
                        if (result === "FAILED") {
                            layer.msg("操作失败！" + response.message);
                            generateTree();
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                $("#menuAddModal").modal("hide");
                // 清空表单
                $("#menuResetBtn").click();
            });
            // 给编辑按钮绑定单击函数
            $("#treeDemo").on("click", ".editBtn", function () {
                // 把当前id作为新节点的pid
                window.id = this.id;
                // 打开模态框
                $("#menuEditModal").modal("show");
                // 获取zTreeObj对象
                let zTreeObj = $.fn.zTree.getZTreeObj("treeDemo")
                // 根据id属性查询节点
                let key = "id";
                let value = window.id;
                let currentNode = zTreeObj.getNodeByParam(key, value);
                // 回显数据
                $("#menuEditModal [name=name]").val(currentNode.name);
                $("#menuEditModal [name=url]").val(currentNode.url);
                $("#menuEditModal [name=icon]").val([currentNode.icon]);
                return false;
            });
            // 给编辑的子节点增加单击响应函数
            $("#menuEditBtn").click(function () {
                // 获取表单中的数据
                // 收集表单项中用户输入的数据
                let name = $("#menuEditModal [name=name]").val();
                let url = $("#menuEditModal [name=url]").val();
                // 单选按钮要定位到“被选中”的那一个
                let icon = $("#menuEditModal [name=icon]:checked").val();
                // 发起Ajax请求
                $.ajax({
                    "url": "menu/update.json",
                    "type": "post",
                    "data": {
                        "id": window.id,
                        "name": name,
                        "url": url,
                        "icon": icon
                    },

                    "dataType": "json",
                    "success": function (response) {
                        let result = response.result;
                        if (result === "SUCCESS") {
                            layer.msg("操作成功！");
                            generateTree();
                        }
                        if (result === "FAILED") {
                            layer.msg("操作失败！" + response.message);
                            generateTree();
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                $("#menuEditModal").modal("hide");
            });
            $("#treeDemo").on("click", ".removeBtn", function () {
                // 把当前id作为新节点的pid
                window.id = this.id;
                // 打开模态框
                $("#menuConfirmModal").modal("show");
                // 获取zTreeObj对象
                let zTreeObj = $.fn.zTree.getZTreeObj("treeDemo")
                // 根据id属性查询节点
                let key = "id";
                let value = window.id;
                let currentNode = zTreeObj.getNodeByParam(key, value);
                // 回显数据
                $("#removeNodeSpan").html("【<i class='" + currentNode.icon + "'> " + currentNode.name + " </i> 】");
                return false;
            });
            // 给删除的子节点增加单击响应函数
            $("#menuRemoveBtn").click(function () {
                // 发起Ajax请求
                $.ajax({
                    "url": "menu/remove.json",
                    "type": "post",
                    "data": {
                        "id": window.id
                    },
                    "dataType": "json",
                    "success": function (response) {
                        let result = response.result;
                        if (result === "SUCCESS") {
                            layer.msg("操作成功！");
                            generateTree();
                        }
                        if (result === "FAILED") {
                            layer.msg("操作失败！" + response.message);
                            generateTree();
                        }
                    },
                    "error": function (response) {
                        layer.msg(response.status + " " + response.statusText);
                    }
                });
                $("#menuConfirmModal").modal("hide");
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
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <ul id="treeDemo" class="ztree">
                        <%--  动态生成--%>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="modal-menu-add.jsp"/>
<jsp:include page="modal-menu-confirm.jsp"/>
<jsp:include page="modal-menu-edit.jsp"/>
</body>
</html>
