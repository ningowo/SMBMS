package pers.ning.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import pers.ning.pojo.Role;
import pers.ning.pojo.User;
import pers.ning.service.role.RoleServiceImpl;
import pers.ning.service.user.UserService;
import pers.ning.service.user.UserServiceImpl;
import pers.ning.utils.Constants;
import pers.ning.utils.PageSupport;

import javax.print.attribute.standard.PagesPerMinute;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 实习Servlet复用
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");

        if (method != null) {
            if (method.equals("savepwd")) {
                this.updatePwd(req, resp);
            } else if (method.equals("pwdmodify")) {
                this.pwdModify(req, resp);
            } else if (method.equals("query")) {
                this.query(req, resp);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) {
        // 查询用户列表
        String queryUserName = req.getParameter("queryName");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;

        // 获取用户列表
        UserService userService = new UserServiceImpl();

        // 设置第一次访问时的设置
        // 可以写到配置文件里，方便修改
        int pageSize = 7;
        int currentPageNo = 1;

        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);
        }
        if (pageIndex != null) {
            currentPageNo  = Integer.parseInt(pageIndex);
        }
        // 获取用户的总数
        int totalUserCount = userService.getUserCount(queryUserName, queryUserRole);
        // 总页数支持
        PageSupport support = new PageSupport();
        support.setCurrentPageNo(currentPageNo);
        support.setPageSize(pageSize);
        support.setTotalCount(totalUserCount);

        int totalPageCount = support.getTotalPageCount();
        // 控制首页和尾页
        if (totalPageCount < 1) { // 不满一页显示第一页
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) { // 在最后一页往后翻
            currentPageNo = totalPageCount;
        }

        //获取用户列表展示
        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        List<Role> roleList = new RoleServiceImpl().getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalCount", totalUserCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        // 返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;

        if (o != null && !StringUtils.isNullOrEmpty(newpassword)) {
            UserService userService = new UserServiceImpl();
            flag = userService.updatePwd(((User)o).getId(), newpassword);
            if (flag) {
                req.setAttribute("message", "密码修改成功， 请退出后使用新密码登录");
                // 密码修改成功后要移除当前session
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            req.setAttribute("message", "新密码有问题");
        }

        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

    // 验证旧密码
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String oldpassword = req.getParameter("oldpassword");

        // 万能的map
        Map<String, String> resultMap = new HashMap<String, String>();
        if (o == null) { // session失效了
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result", "error");
        } else {
            String userPassword = ((User) o).getUserPassword();// session中用户密码
            if (oldpassword.equals(userPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }

        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            // 用于把hashMap转换为Json格式
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



