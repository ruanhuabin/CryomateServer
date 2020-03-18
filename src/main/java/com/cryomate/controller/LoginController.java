package com.cryomate.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import com.cryomate.entity.Users;
import com.cryomate.pojo.Constant;
import com.cryomate.repository.UsersRepository;
import com.cryomate.utils.CommandRunner;

import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/api")
public class LoginController
{
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Value("${cryomate.user.homedir.prefix}")
    private String              userHomeDirPrefix;
    @Value("${cryomate.user.datadir.prefix}")
    private String              dataDirPrefix;
    @Value("${cryomate.user.workdir.prefix}")
    private String              workDirPrefix;

    @Autowired
    UsersRepository userRepos;
    @RequestMapping(value = "/cLogin")
    @ResponseBody
    public String login(HttpServletRequest request)
    {

        String name     = request.getParameter("pUserName");
        String password = request.getParameter("pPassword");
        if (name == null || name.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Parameter pUserName is null or empty";
        }

        if (password == null || password.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Parameter pPassword is null or empty";
        }

        logger.info("User [ {} ] starts to login, request method: {}", name, request.getMethod());

        StringBuffer retMessage = new StringBuffer();
        Users        user       = userRepos.findByUserNameAndPassword(name, password);
        // Add user info into current session and construct return message
        if (user != null)
        {
            request.getSession().setAttribute("userInfo", user);
            logger.info("user [ " + name + " ] is valid");

            retMessage.append("pUserName:" + user.getUserName() + "\n");
            retMessage.append("pUserGroup:" + user.getUserGroup() + "\n");
            retMessage.append("pAuthority:" + user.getAuthority() + "\n");
            retMessage.append("pDataAdd:" + user.getDateAdd() + "\n");
            retMessage.append("pDateExp:" + user.getDateExp() + "\n");
            retMessage.append("pHomeDir:" + user.getHomeDir() + "\n");
            retMessage.append("pWorkDir:" + user.getWorkDir() + "\n");
            retMessage.append("pDataDir:" + user.getDataDir() + "\n");
            retMessage.append("pEmail:" + user.getEmail() + "\n");
            retMessage.append("pPhone:" + user.getPhone() + "\n");
            retMessage.append("pStatus:login success");

            return retMessage.toString();

        } else
        {
            return "pStatus:login failed";
        }

    }

    @RequestMapping(value = "/cLogout")
    @ResponseBody
    public String logout(HttpServletRequest request)
    {
        String      info;
        HttpSession session           = request.getSession();
        String      currLoginUserName = ((Users) session.getAttribute("userInfo")).getUserName();

        session.removeAttribute("userInfo");
        Object userInfo = session.getAttribute("userInfo");
        if (userInfo == null)
        {
            info = "logout success";
        } else
        {
            info = "logout failed";
        }

        logger.info("User [ {} ] logout success", currLoginUserName);

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + info;

    }

    @RequestMapping(value = "/cRegister")
    @ResponseBody
    public String register(HttpServletRequest request)
    {
        String userName  = request.getParameter("pUserName");
        String password  = request.getParameter("pPassword");
        String authority = request.getParameter("pAuthority");
        String userGroup = request.getParameter("pUserGroup");
        String dateAdd   = request.getParameter("PDateAdd");
        String dateExp   = request.getParameter("pDateExp");
        String email     = request.getParameter("pEmail");
        String phone     = request.getParameter("pPhone");

        if (userGroup == null || userGroup.equals(""))
        {
            userGroup = userName;
        }

        String userHomeDirPrefix;
        if (this.userHomeDirPrefix.charAt(this.userHomeDirPrefix.length() - 1) != '/')
        {
            userHomeDirPrefix = this.userHomeDirPrefix + "/";
        } else
        {
            userHomeDirPrefix = this.userHomeDirPrefix;
        }

        String dataDirPrefix;
        if (this.dataDirPrefix.charAt(this.dataDirPrefix.length() - 1) != '/')
        {
            dataDirPrefix = this.dataDirPrefix + "/";
        } else
        {
            dataDirPrefix = this.dataDirPrefix;
        }

        String workDirPrefix;
        if (this.workDirPrefix.charAt(this.workDirPrefix.length() - 1) != '/')
        {
            workDirPrefix = this.workDirPrefix + "/";
        } else
        {
            workDirPrefix = this.workDirPrefix;
        }

        String homeDir = userHomeDirPrefix + userName;
        String workDir = workDirPrefix + userGroup + "/" + userName;
        String dataDir = dataDirPrefix + userGroup + "/" + userName;

        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append("register operation:\n");
        strBuffer.append("\tname:" + userName + "\n");
        strBuffer.append("\tpassword:" + password + "\n");
        strBuffer.append("\tauthority:" + authority + "\n");
        strBuffer.append("\tworkGroup:" + userGroup + "\n");
        strBuffer.append("\thomeDir:" + homeDir + "\n");
        strBuffer.append("\tworkDir:" + workDir + "\n");
        strBuffer.append("\tdataDir:" + dataDir + "\n");
        System.out.println(strBuffer.toString());

        // Check whether user name is already used
        Users user = userRepos.getByUserName(userName);
        if (user != null)
        {
            StringBuffer retMsg = new StringBuffer();
            retMsg.append("pStatus:register failed:user [ ");
            retMsg.append(userName);
            retMsg.append(" ] is already exist");
            System.out.println(retMsg.toString());
            return retMsg.toString();

        }
        
        Users newUser = new Users();
        newUser.setAuthority(authority);
        newUser.setDateAdd(dateAdd);
        newUser.setDateExp(dateExp);
        newUser.setPassword(password);
        newUser.setUserGroup(userGroup);
        newUser.setUserName(userName);
        newUser.setHomeDir(homeDir);
        newUser.setDataDir(dataDir);
        newUser.setWorkDir(workDir);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        userRepos.save(newUser);
        System.out.println("Register: create new user " + userName + " success");

        // invoke script to create system user
        String command[] = new String[7];
        command[0] = "./warehouse/script/register.sh";
        command[1] = userGroup;
        command[2] = userName;
        command[3] = password;
        command[4] = this.userHomeDirPrefix;
        command[5] = workDir;
        command[6] = dataDir;

        String result = CommandRunner.runCommand(command);
        System.out.println(command[0] + ": running result: " + result.toString());

        return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "register success";
    }

    @RequestMapping(value = "/cRemove")
    @ResponseBody
    public String remove(HttpServletRequest request)
    {
        String name = request.getParameter("pUserName");

        if (name == null)
        {
            return "pStatus:Error: user name is null";
        }

        Users user = userRepos.getByUserName(name);
        if (user == null)
        {
            return "pStatus:Error: user [ " + name + " ] is not exist";
        }

        System.out.println("-------->User [ " + name + " ] is exist");

        Users currUser = (Users) (request.getSession().getAttribute("userInfo"));

        // We can not remove ourself
        if (currUser != null && currUser.getUserName().equals(name))
        {
            System.out.println("self delete self");
            return "pStatus:Error: user [ " + currUser.getUserName() + " ] can't not be removed by himself";
        }
        String currUserAuthority = currUser.getAuthority();

        if (currUserAuthority != null && currUserAuthority.equals(Constant.AUTHORITY_SYSTEM_ROOT))
        {
            Users u = new Users();
            u.setUserName(name);
            userRepos.delete(u);
            return "pStatus:remove success";
        } else
        {
            return "pStatus:Error: permission denied";
        }

    }

}
