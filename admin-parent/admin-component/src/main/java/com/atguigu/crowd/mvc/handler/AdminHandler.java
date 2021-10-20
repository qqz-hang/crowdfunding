package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.AdminEditFailedException;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.entity.Admin;
import com.github.pagehelper.PageInfo;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {
    @Resource
    private AdminService adminService;

    @RequestMapping("/admin/do/logout.html")
    public String logout(HttpSession session) {
        session.invalidate();//使session失效  退出登录
        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd, HttpSession session) {
        // 调用adminService的getAdminByLoginAcct获取到Admin对象
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
        // 将admin对象保存到session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main/page.html";
    }

    private PageInfo<Admin> pageInfo = null;

    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                              ModelMap modelMap) {
        pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword
    ) {
        adminService.remove(adminId);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @PreAuthorize("hasRole('经理')")
    @RequestMapping("/admin/save.html")
    public String saveAdmin(Admin admin) {
        adminService.saveAdmin(admin);

        // 这里使用pageHelper来找到总页数
        return "redirect:/admin/get/page.html?pageNum=" + pageInfo.getPages();
    }

    @RequestMapping("/admin/to/add/edit.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId, ModelMap modelMap) {
        // 根据adminId查询数据
        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }

    @RequestMapping("/admin/update.html")
    public String updatePage(Admin admin,
                             @RequestParam("pageNum") Integer pageNum,
                             @RequestParam("keyword") String keyword) {

        try {
            adminService.update(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException) {
                throw new AdminEditFailedException(CrowdConstant.MESSAGE_UPDATE_ERROR);
            }
            throw e;
        }
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyword=" + keyword;
    }
}
