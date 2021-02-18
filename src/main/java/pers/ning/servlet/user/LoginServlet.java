package pers.ning.servlet.user;

import pers.ning.pojo.User;
import pers.ning.service.user.UserService;
import pers.ning.service.user.UserServiceImpl;
import pers.ning.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    // Servlet: 控制层调用业务层
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet-start...");
        // 获取用户名密码
        String userCode = req.getParameter("userCode");
        String userPassword = req.getParameter("userPassword");

        // 和数据库中的密码进行对比，调用业务层
        UserService userService = new UserServiceImpl();
        User user = userService.login(userCode, userPassword);

        if (user != null) { // 查有此人
            // 将用户信息放到Session里
            req.getSession().setAttribute(Constants.USER_SESSION, user);
            // 跳转到登录成功页面
            resp.sendRedirect("jsp/frame.jsp");
        } else { // 查无此人
            // 转发回登录页面，顺带提示用户名或密码错误
            req.setAttribute("error", "用户名或密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
