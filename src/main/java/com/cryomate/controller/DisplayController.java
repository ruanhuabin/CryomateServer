package com.cryomate.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cryomate.entity.Users;
import com.cryomate.pojo.Constant;
import com.cryomate.utils.CommandRunner;
import com.cryomate.utils.FileUtils;
import com.fasterxml.uuid.Generators;

@Controller
@RequestMapping("/")
public class DisplayController
{
    private static final Logger logger  = LoggerFactory.getLogger(DisplayController.class);
    @Value("${cryomate.data.tmpdir}")
    private String tmpDirPrefix;
    
    @RequestMapping("/api/cDisp_Stack")
    @ResponseBody    
    public String cDisplayStack(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return display(request, response);
    }
    
    @RequestMapping("/api/cDisp_Image")
    @ResponseBody    
    public String cDisplayImage(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return display(request, response);
    }
    
    @RequestMapping("/api/cDisp_FSC")
    @ResponseBody    
    public String cDisplayFSC(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String pFilename = request.getParameter("pFilename"); 
        //This parameter is reserved for future use
        String pTextDataFormat = request.getParameter("pTextDataFormat");
        if(pFilename == null || pFilename.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFilename is null or empty";
        }
        
        //Make sure the file exists
        File file = new File(pFilename);
        if(!file.exists())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File [ " + pFilename + " ] does not exist";
        }
        
        //Make sure the owner of the being processed file is the login user.
        Users loginUser = (Users)request.getSession().getAttribute("userInfo");
        String loginUserName = loginUser.getUserName();
        FileUtils fu = new FileUtils();
        String fileOwner = fu.getFileOwner(pFilename);
        if(!loginUserName.equals(fileOwner))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File " + pFilename + " ] does not belong to current login user!";
        }  
        
        String fscInfo = fu.genDataFromFSCFile(pFilename);
        
        return fscInfo;      
        
    }
    
    @RequestMapping("/api/cDisp_3DMap")
    @ResponseBody   
    public String cDisplay3DMap(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String pFilename   = request.getParameter("pFilename");
        String sNormalized = request.getParameter("sNormalized");
        String pMean       = request.getParameter("pMean");
        String pStd        = request.getParameter("pSTD");
        String sRAW        = request.getParameter("sRAW");
        String sMRC        = request.getParameter("sMRC");
        String sJPEG       = request.getParameter("sJPEG");
        String pAxis       = request.getParameter("pAxis");
        String pSlice      = request.getParameter("pSlice");
        
        logger.info("pFilename = {}, sNormalized = {}, sRaw = {}, sMRC = {}, sJPEG = {}, pMean = {}, pStd = {}, pAxis = {}, pSlice = {}\n",
                pFilename, sNormalized, sRAW, sMRC, sJPEG, pMean, pStd, pAxis, pSlice);
        
        if (pFilename == "" || pFilename == null)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFilename is null or empty";
        }
        
        //Make sure the file exists
        File file = new File(pFilename);
        if(!file.exists())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File [ " + pFilename + " ] does not exist";
        }
        
        //Make sure the owner of the being processed file is the login user.
        Users loginUser = (Users)request.getSession().getAttribute("userInfo");
        String loginUserName = loginUser.getUserName();
        FileUtils fu = new FileUtils();
        String fileOwner = fu.getFileOwner(pFilename);
        if(!loginUserName.equals(fileOwner))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File " + pFilename + " ] does not belong to current login user!";
        }        

        if (sNormalized != null && sNormalized.length() == 0 && sJPEG != null && sJPEG.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sNormalized and sJPEG should not set simultaneously from client.";
        }

        if (sRAW != null && sMRC != null)
        {

            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sRAW and sMRC should not set simultaneously from client.";
        }

        if (sJPEG != null && sMRC != null)
        {

            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sJPEG and sMRC should not set simultaneously from client.";
        }      
        

        if (sJPEG != null && sRAW != null)
        {

            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sJPEG and sRAW should not set simultaneously from client.";
        } 
        
        if(sJPEG == null && sRAW == null && sMRC == null)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sJPEG, sRAW, sMRC should not set null simultaneously";
        }
        
        //sMRC or sRAW should come with sNormalized != null
        if(sNormalized == null && (sMRC != null || sRAW != null))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: sMRC or sRAW should go with sNormalized";
        }
        
        
        
        logger.info("stack file name: {}\n", pFilename);

        String outputDir = Generators.timeBasedGenerator().generate().toString();
        String outputDirFullPath = this.tmpDirPrefix + 
                           File.separatorChar + 
                           loginUserName + 
                           File.separatorChar + 
                           outputDir;
        
        logger.info("output dir: {}\n", outputDirFullPath);

        String argumentString = "--input " + pFilename + " --output " + outputDirFullPath;

        if (sNormalized != null)
        {
            argumentString = argumentString + " --sNormalized";
        }

        if (sRAW != null)
        {
            argumentString = argumentString + " --sRAW";
        }

        if (sMRC != null)
        {
            argumentString = argumentString + " --sMRC";
        }

        if (sJPEG != null)
        {
            argumentString = argumentString + "  --sJPEG";
        }

        if (pMean != null)
        {
            argumentString = argumentString + " --pMean " + pMean;
        } else
        {
            argumentString = argumentString + " --pMean 0";
        }

        if (pStd != null)
        {
            argumentString = argumentString + " --pStd " + pStd;
        } else
        {
            if (sNormalized == null)
            {
                argumentString = argumentString + " --pStd 1";
            } else
            {
                argumentString = argumentString + " --pStd 3";
            }
        }

        if(pAxis == null)
        {
            pAxis = "z";            
        }
        if(pSlice == null)
        {
            pSlice = "0";
        }
        argumentString = argumentString + " --pAxis " + pAxis + " --pSlice " + pSlice;
        logger.info("Argument String = {}\n", argumentString);

        String paraItems[] = argumentString.split(" ");
        System.out.println("length of paraItems: " + paraItems.length);

        String command[] = new String[paraItems.length + 1];
        command[0] = "warehouse/bin/cDisp_3DMap";

        for (int i = 0; i < paraItems.length; i++)
        {
            command[i + 1] = paraItems[i].trim();
        }

        System.out.println("-->command to be executed:");
        for (String item : command)
        {
            System.out.print(item + " ");
        }
        System.out.println();

        String result = CommandRunner.runCommand(command);
        if (result.length() != 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Failed to generate Image [ detail:  " + result + " ]";
        }

        File tarFile = compressImageDir(outputDirFullPath, outputDir, outputDirFullPath + ".tar");

        logger.info("Compressed file full path: {}", tarFile.getAbsoluteFile());
        transferToClient(response, tarFile);

        return null;
        
        
    }
    
    
    private String display(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String pFilename = request.getParameter("pFilename");        
        String sNormalized = request.getParameter("sNormalized");
        String pMean       = request.getParameter("pMean");
        String pStd        = request.getParameter("pSTD");
        String sRAW        = request.getParameter("sRAW");
        String sMRC        = request.getParameter("sMRC");
        String sJPEG       = request.getParameter("sJPEG");

        logger.info("pFilename = {}, sNormalized = {}, sRaw = {}, sMRC = {}, sJPEG = {}, pMean = {}, pStd = {}\n",
                pFilename, sNormalized, sRAW, sMRC, sJPEG, pMean, pStd);

        if (pFilename == "" || pFilename == null)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFilename is null or empty";
        }
        
        //Make sure the file exists
        File file = new File(pFilename);
        if(!file.exists())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File [ " + pFilename + " ] does not exist";
        }
        
        //Make sure the owner of the being processed file is the login user.
        Users loginUser = (Users)request.getSession().getAttribute("userInfo");
        String loginUserName = loginUser.getUserName();
        FileUtils fu = new FileUtils();
        String fileOwner = fu.getFileOwner(pFilename);
        if(!loginUserName.equals(fileOwner))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File " + pFilename + " ] does not belong to current login user!";
        }        

        if (sNormalized != null && sNormalized.length() == 0 && sJPEG != null && sJPEG.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sNormalized and sJPEG should not set simultaneously from client.";
        }

        if (sRAW != null && sMRC != null)
        {

            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sRAW and sMRC should not set simultaneously from client.";
        }

        if (sJPEG != null && sMRC != null)
        {

            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sJPEG and sMRC should not set simultaneously from client.";
        }      
        

        if (sJPEG != null && sRAW != null)
        {

            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sJPEG and sRAW should not set simultaneously from client.";
        } 
        
        if(sJPEG == null && sRAW == null && sMRC == null)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Parameter sJPEG, sRAW, sMRC should not set null simultaneously";
        }
        
        //sMRC or sRAW should come with sNormalized != null
        if(sNormalized == null && (sMRC != null || sRAW != null))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: sMRC or sRAW should go with sNormalized";
        }
        
        logger.info("stack file name: {}\n", pFilename);

        String outputDir = Generators.timeBasedGenerator().generate().toString();
        String outputDirFullPath = this.tmpDirPrefix + 
                           File.separatorChar + 
                           loginUserName + 
                           File.separatorChar + 
                           outputDir;
        
        logger.info("output dir: {}\n", outputDirFullPath);

        String argumentString = "--input " + pFilename + " --output " + outputDirFullPath;

        if (sNormalized != null)
        {
            argumentString = argumentString + " --sNormalized";
        }

        if (sRAW != null)
        {
            argumentString = argumentString + " --sRAW";
        }

        if (sMRC != null)
        {
            argumentString = argumentString + " --sMRC";
        }

        if (sJPEG != null)
        {
            argumentString = argumentString + "  --sJPEG";
        }

        if (pMean != null)
        {
            argumentString = argumentString + " --pMean " + pMean;
        } else
        {
            argumentString = argumentString + " --pMean 0";
        }

        if (pStd != null)
        {
            argumentString = argumentString + " --pStd " + pStd;
        } else
        {
            if (sNormalized == null)
            {
                argumentString = argumentString + " --pStd 1";
            } else
            {
                argumentString = argumentString + " --pStd 3";
            }
        }

        logger.info("Argument String = {}\n", argumentString);

        String paraItems[] = argumentString.split(" ");
        System.out.println("length of paraItems: " + paraItems.length);

        String command[] = new String[paraItems.length + 1];
        command[0] = "warehouse/bin/mrcs2jpeg";

        for (int i = 0; i < paraItems.length; i++)
        {
            command[i + 1] = paraItems[i].trim();
        }

        System.out.println("-->command to be executed:");
        for (String item : command)
        {
            System.out.print(item + " ");
        }
        System.out.println();

        String result = CommandRunner.runCommand(command);
        if (result.length() != 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Failed to generate Image [ detail:  " + result + " ]";
        }

        File tarFile = compressImageDir(outputDirFullPath, outputDir, outputDirFullPath + ".tar");

        logger.info("Compressed file full path: {}", tarFile.getAbsoluteFile());
        transferToClient(response, tarFile);

        return null;

    }
    
    private File compressImageDir(String dirFullPathToBeCompressed, String dirToBeCompressed, String outputTarFileName)
    {        
        File file = new File(outputTarFileName);
        
        if (!file.getParentFile().exists())
        {
            file.getParentFile().mkdirs();
        }
        System.out.println("File full path = " + file.getAbsolutePath());

        StringBuffer   result    = new StringBuffer();
        Process        process   = null;
        BufferedReader bufrIn    = null;
        BufferedReader bufrError = null;
        try
        {
            String[] command = {"tar", "cvf", outputTarFileName, dirToBeCompressed};
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(command, null, new File(file.getParentFile().getAbsolutePath()));            
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
                // System.out.println("error message add: " + line + "\n");
                result.append(line).append('\n');
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // 销毁子进程
            if (process != null)
            {
                process.destroy();
            }

        }

        // System.out.println("result is: " + result);

        return file;

    }

    private void transferToClient(HttpServletResponse response, File tarFile)
    {

        if (tarFile.exists())
        {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + tarFile.getName());// 设置文件名
            byte[]              buffer = new byte[1024];
            FileInputStream     fis    = null;
            BufferedInputStream bis    = null;
            try
            {
                fis = new FileInputStream(tarFile);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int          i  = bis.read(buffer);
                while (i != -1)
                {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (bis != null)
                {
                    try
                    {
                        bis.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                if (fis != null)
                {
                    try
                    {
                        fis.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
