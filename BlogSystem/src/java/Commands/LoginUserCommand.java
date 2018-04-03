/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Commands;

import Business.User;
import DAO.UserDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author d00186050
 */
public class LoginUserCommand implements Command {

    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String forwardToJsp = "";
        String Username = request.getParameter("username");
        String Password = request.getParameter("password");

        if (Username != null || Password != null) {
            try {
                UserDao userdao = new UserDao("myblog");

                User Result = userdao.findUserByUsernamePassword(Username, Password);

                if (Result != null) {
                    HttpSession session = request.getSession();
                    session.setAttribute("login_message", Result.getUsername());
                    session.setAttribute("login_status", 1);
                    forwardToJsp = "myBlog.jsp";
                } else {
                    HttpSession session = request.getSession();
                    session.setAttribute("login_message", "Sorry, Username or password incorrect");
                    session.setAttribute("login_status", null);
                    forwardToJsp = "login.jsp";
                }
            }
            catch (NumberFormatException e) {
                forwardToJsp = "error.jsp";
                HttpSession session = request.getSession();
                session.setAttribute("errorMessage", "Not logged in");
            }
        }
        return forwardToJsp;
    }
}
