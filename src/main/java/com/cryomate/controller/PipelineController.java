package com.cryomate.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cryomate.entity.Users;
import com.cryomate.pojo.Constant;
import com.cryomate.utils.CommandRunner;
import com.cryomate.utils.FileUtils;
import com.cryomate.utils.KeyGenerator;
import com.fasterxml.uuid.Generators;

import org.json.*;
@Controller
@RequestMapping("/")
public class PipelineController
{
    private static final Logger logger  = LoggerFactory.getLogger(PipelineController.class);
    private static final int    SUCCESS = 0;
    @Value("${cryomate.data.tmpdir}")
    private String  tmpDirPrefix;

    @RequestMapping("/api/cRun_Flow")
    @ResponseBody
    public String runFlow(@RequestParam("pFlowFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
    {
        Users currUser = (Users) request.getSession().getAttribute("userInfo");
        if (currUser == null)
        {
            logger.info("Error: No login user is found");
            return "Error: No login user is found";
        }
        
        if (file.isEmpty())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File is empty";
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.debug("upload file name：" + fileName);

        // 文件上传后的路径
        String ID = KeyGenerator.getNextID();
        String uploadBaseDir = this.tmpDirPrefix + File.separatorChar;
        String filePath     = uploadBaseDir + currUser.getUserName() + File.separatorChar;
        String fileFullName = filePath + ID + "_"+ fileName;
        File   dest         = new File(fileFullName);
        logger.debug("File upload absolute path: {}", dest.getAbsolutePath());
        // 检测是否存在目录
        if (!dest.getParentFile().exists())
        {
            dest.getParentFile().mkdirs();
            //logger.info("Make dir [{}] success.", dest.getParentFile().getAbsolutePath());
        }
        
        try
        {            
            byte[] flowContent = file.getBytes();            
            FileOutputStream fos = new FileOutputStream(dest);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(flowContent);            
            bos.close();
            fos.close();
            
            // Change the owner of uploaded file to the login user
            String    currLoginUserName  = currUser.getUserName();
            String    currLoginUserGroup = currUser.getUserGroup();
            FileUtils fu                 = new FileUtils();
            int       ret                = fu.changeFileOwnerGroup(fileFullName, currLoginUserName, currLoginUserGroup);
            if (ret == SUCCESS)
            {
                logger.info("Success: Change owner of file {} to {} success", fileFullName, currLoginUserName);
            } else
            {
                logger.info("Failed: Change owner of file {} to {} failed", fileFullName, currLoginUserName);
            }
            
            //Start to parse the json data in flow file
            String flowContentStr = new String(flowContent);
            logger.debug("flow file content: {}", flowContentStr);
           /*
            JSONObject obj1 = new JSONObject(flowContentStr);
            Iterator<String> keys = obj1.keys();
            JSONObject obj1_1 = (JSONObject) obj1.get("2");
            Iterator<String> keys1_1 = obj1_1.keys();
            while(keys.hasNext())
            {
                System.out.println("key = " + keys.next());
            }
            
            while(keys1_1.hasNext())
            {
                System.out.println("key1_1 = " + keys1_1.next());
            }
            
            String script2_2 = ((JSONObject)obj1_1.get("2")).getString("Script");
            System.out.println("script2_2 = " + script2_2);
            */
            return "upload success";
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "upload failed";
    }
}
