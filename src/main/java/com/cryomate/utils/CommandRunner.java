package com.cryomate.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cryomate.controller.QueryController;
import com.cryomate.pojo.Constant;

public class CommandRunner
{
    private static final Logger logger = LoggerFactory.getLogger(CommandRunner.class);
    public static String runCommand(String[] command)
    {
        StringBuffer   result    = new StringBuffer();
        Process        process   = null;
        BufferedReader bufrIn    = null;
        BufferedReader bufrError = null;
        try
        {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(command, null, new File("./"));
            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn    = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            // 读取输出
            String line;
            while ((line = bufrIn.readLine()) != null)
            {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null)
            {
                result.append(line).append('\n');
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + " Error:Command executes failed";
        }
        finally
        {
            try
            {
                bufrIn.close();
                bufrError.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Close bufrIn and bufrError failed";
            }

            // 销毁子进程
            if (process != null)
            {
                process.destroy();
            }
        }
        if(result.toString().length() == 0)
        {
            logger.info("Command [{}] runs success", command[0]);            
        }
        else
        {
            logger.error("Command [{}] runs failed: {}", command[0], result.toString());
        }
        return result.toString();

    }

}
