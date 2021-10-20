package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.AccessForbiddenException;
import com.atguigu.crowd.exception.AdminEditFailedException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常解析类
 */
@ControllerAdvice
public class CrowdExceptionResolver {
    /**
     * 处理登录异常类
     *
     * @return
     */
    @ExceptionHandler(LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(LoginFailedException failedException,
                                                    HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName, failedException, request, response);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView resolveException(Exception exception,
                                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    @ExceptionHandler(LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(LoginAcctAlreadyInUseException loginAcctAlreadyInUseException,
                                                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        return commonResolve(viewName, loginAcctAlreadyInUseException, request, response);
    }

    @ExceptionHandler(AdminEditFailedException.class)
    public ModelAndView resolveAdminEditFailedException(AdminEditFailedException adminEditFailedException, HttpServletRequest request,
                                                        HttpServletResponse response) throws IOException {
        String viewName = "admin-edit";
        return commonResolve(viewName, adminEditFailedException, request, response);
    }

    /**
     * 公共方法
     *
     * @param viewName
     * @param exception
     * @param request
     * @return
     */
    private ModelAndView commonResolve(String viewName, Exception exception,
                                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (CrowdUtil.judgeRequestType(request)) {
            response.getWriter().write(new Gson().toJson(ResultEntity.failed(exception.getMessage())));

            return null;
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
