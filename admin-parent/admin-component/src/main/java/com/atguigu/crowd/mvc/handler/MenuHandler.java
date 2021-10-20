package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import com.atguigu.entity.Menu;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MenuHandler {
    @Resource
    private MenuService menuService;

    //@ResponseBody
    @RequestMapping("/menu/remove.json")
    public ResultEntity<String> removeMenu(@RequestParam("id") Integer id) {
        menuService.removeMenu(id);
        return ResultEntity.successWithoutData();
    }

    //@ResponseBody
    @RequestMapping("/menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu) {
        menuService.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    //@ResponseBody
    @RequestMapping("/menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu) {
        menuService.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }


    //@ResponseBody
    @RequestMapping("/menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
        List<Menu> menus = menuService.getAll();
        // 先给根节点赋空值
        Menu root = null;
        // 存储id和menu对象
        Map<Integer, Menu> menuMap = new HashMap<>();
        for (Menu menu : menus) {
            Integer id = menu.getId();
            menuMap.put(id, menu);
        }
        for (Menu menu : menus) {
            Integer pid = menu.getPid();
            // 如果pid为空 说明为根节点
            if (pid == null) {
                // 把当前正在遍历的Menu赋值给root
                root = menu;
                continue;
            }
            Menu father = menuMap.get(pid);
            father.getChildren().add(menu);
        }
        return ResultEntity.successWithData(root);
    }
}
