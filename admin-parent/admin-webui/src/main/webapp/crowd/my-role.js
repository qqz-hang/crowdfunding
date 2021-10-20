// 用于在Auth模态框中填充树形结构数据
function fillAuthTree() {
    var ajaxReturn = $.ajax({
        "url": "assign/get/all/auth.json",
        "type": "post",
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status != 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + " 说明是" + ajaxReturn.statusText);
        return;
    }
    var authList = ajaxReturn.responseJSON.data;
    var setting = {
        "data": {
            "simpleData": {
                "enable": true,
                "pIdKey": "categoryId"
            },
            "key": {
                "name": "title"
            }
        },
        "check": {
            "enable": true
        }
    };
    $.fn.zTree.init($("#authTreeDemo"), setting, authList);
    var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
    zTreeObj.expandAll(true);
    ajaxReturn = $.ajax({
        "url": "assign/get/assigned/auth/id/by/role/id.json",
        "type": "post",
        "data": {
            "roleId": window.roleId
        },
        "dataType": "json",
        "async": false
    });
    if (ajaxReturn.status !== 200) {
        layer.msg("请求处理出错！响应状态码是：" + ajaxReturn.status + " 说明是" + ajaxReturn.statusText);
        return;
    }
    var authIdArray = ajaxReturn.responseJSON.data;
    for (var i = 0; i < authIdArray.length; i++) {
        var authId = authIdArray[i];
        var treeNode = zTreeObj.getNodeByParam("id", authId);
        var checked = true;
        var checkTypeFlag = false;
        zTreeObj.checkNode(treeNode, checked, checkTypeFlag);
    }
}

function showConfirmModal(roleArray) {
    // 打开模态框
    $("#confirmModal").modal("show");
    // 清除数据
    $("#roleNameDiv").empty();
    window.roleIdArray = [];
    for (let i = 0; i < roleArray.length; i++) {
        let role = roleArray[i];
        let roleName = role.roleName;
        $("#roleNameDiv").append(roleName + "<br/>");
        let roleId = role.roleId;
        window.roleIdArray.push(roleId)
    }
}

function generatePage() {
    // 获取分页数据
    let pageInfo = getPageInfoRemote();

    // 填充表格
    fillTableBody(pageInfo);
}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {
    let ajaxResult = $.ajax({
        "url": "role/get/page/info.json",
        "type": "post",
        "data": {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        "async": false,
        "dataType": "json"
    })
    console.log(ajaxResult)
    let statusCode = ajaxResult.status
    if (statusCode !== 200) {
        layer.msg("失败！响应状态码为:" + statusCode + "说明信息=" + ajaxResult.statusText)
        return null
    }
    let resultEntity = ajaxResult.responseJSON
    let result = resultEntity.result
    if (result === "FAILED") {
        layer.msg(resultEntity.message);
        return null;
    }

    return resultEntity.data;

}

// 填充表格
function fillTableBody(pageInfo) {
    $("#rolePageBody").empty();
    $("#Pagination").empty();
    // 判断pageInfo是否有效
    if (pageInfo == null || pageInfo.list == null || pageInfo.list.length === 0) {
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！ 没有查询到您搜索的数据！ </td></tr>");
        return;
    }
    // 对pageInfo的list属性 进行遍历
    for (let i = 0; i < pageInfo.list.length; i++) {
        let role = pageInfo.list[i];
        let roleId = role.id;
        let roleName = role.name;
        let numberTd = "<td>" + (i + 1) + "</td>";
        let checkboxTd = "<td><input id='" + roleId + "' class='itemBox' type='checkbox'></td>";
        let roleNameTd = "<td>" + roleName + "</td>";
        let checkBtn = "<button id='" + roleId + "' type='button' class='btn btn-success btn-xs checkBtn'><i class='glyphicon glyphicon-check'></i></button>";
        let pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
        let removeBtn = "<button id='" + roleId + "' type='button' class='btn btn-danger btn-xs removeBtn'><i class='glyphicon glyphicon-remove'></i></button>";
        let buttonTd = "<td>" + checkBtn + " " + pencilBtn + " " + removeBtn + "</td>";
        let tr = "<tr>" + numberTd + checkboxTd + roleNameTd + buttonTd + "</tr>";
        $("#rolePageBody").append(tr);
    }
    generateNavigator(pageInfo)
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {

    // 声明总记录数
    let totalRecord = pageInfo.total
    // 声明相关属性
    let properties = {
        "num_edge_entries": 3,
        "num_display_entries": 5,
        "callback": paginationCallBack,
        "items_per_page": pageInfo.pageSize,
        "current_page": pageInfo.pageNum - 1,
        "prev_text": "上一页",
        "next_text": "下一页"
    }
    $("#Pagination").pagination(totalRecord, properties);
}

// 翻页的回调函数
function paginationCallBack(pageIndex, jQuery) {
    // 修改window对象的pageNum属性
    window.pageNum = pageIndex + 1;
    // 调用分页函数
    generatePage()
    // 取消页码超链接的默认行为
    return false

}