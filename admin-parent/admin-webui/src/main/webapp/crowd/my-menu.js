function generateTree() {
        // 发起ajax请求 准备数据
        $.ajax({
            "url": "menu/get/whole/tree.json",
            "type": "post",
            "dataType": "json",
            "success": function (response) {
                let result = response.result;
                if (result === "SUCCESS") {
                    // 创建json对象 用于存储zTree所做的设置
                    let setting = {
                        "view": {
                            "addDiyDom": myAddDiyDom,
                            "addHoverDom": myAddHoverDom,
                            "removeHoverDom": myRemoveHoverDom
                        },
                        "data": {
                            "key": {
                                "url": "mao"
                            }
                        }
                    };
                    let zNodes = response.data;
                    // 初始化树形结构
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                }
            },
            "error": function (response) {
                layer(response.message);
            }
        });

}
function myRemoveHoverDom(treeId, treeNode) {
    let btnGroupId = treeNode.tId + "_btnGroup";
    $("#" + btnGroupId).remove();
}

function myAddHoverDom(treeId, treeNode) {
    let btnGroupId = treeNode.tId + "_btnGroup";
    if ($("#" + btnGroupId).length > 0) {
        return;
    }
    let addBtn = "<a id='" + treeNode.id + "' class='addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    let removeBtn = "<a id='" + treeNode.id + "' class='removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title=' 删 除 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    let editBtn = "<a id='" + treeNode.id + "' class='editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title=' 修 改 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
    let level = treeNode.level;
    let btnHtml = "";
    if (level === 0) {
        btnHtml = addBtn;
    } else if (level === 1) {
        btnHtml = addBtn + " " + editBtn;
        // 当前节点的子节点的数量
        let length = treeNode.children.length;
        // 没有子节点才可以删除
        if (length === 0) {
            btnHtml = btnHtml + " " + removeBtn;
        }
    } else if (level === 2) {
        btnHtml = editBtn + " " + removeBtn;
    }
    // 找到附着按钮组的超链接
    let anchorId = treeNode.tId + "_a";
    // 追加span
    $("#" + anchorId).after("<span id='" + btnGroupId + "'>" + btnHtml + "</span>");


}

function myAddDiyDom(treeId, treeNode) {
    // 根据id的生成规则拼接处spanId
    let spanId = treeNode.tId + "_ico";
    // 根据控制图标的span标签的id找到这个span标签
    // 删除旧的class
    $("#" + spanId).removeClass().addClass(
        treeNode.icon
    )
    // 加上新的class
}