package com.cryomate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.uuid.Generators;
import com.cryomate.entity.Tables;
import com.cryomate.entity.Users;
import com.cryomate.pojo.Constant;
import com.cryomate.repository.TablesRepository;
import com.cryomate.utils.CommandRunner;
import com.cryomate.utils.KeyGenerator;

@Controller
@RequestMapping("/")
public class QueryController
{
    private static final Logger logger                            = LoggerFactory.getLogger(QueryController.class);
    private static final String ERROR_DB_TABLE_INDEX_IS_NOT_FOUND = "not_found";
    @Autowired
    private TablesRepository    tablesRepository;

    @RequestMapping("/api/cSys_Command")
    @ResponseBody
    public String executeSystemCommandV2(HttpServletRequest request, HttpServletResponse response)
    {
        String cmdString = request.getParameter("pParaString");
        if (cmdString == null || cmdString.equals(""))
        {
            return "Error: pParaString is null or empty";
        }
        Users currUser = (Users) request.getSession().getAttribute("userInfo");

        if (currUser == null)
        {
            return "Error: no login user is found.";
        }
        String[] command = new String[4];
        command[0] = "./warehouse/script/execSysCommand.sh";
        command[1] = currUser.getUserName();
        command[2] = currUser.getPassword();
        command[3] = cmdString;

        String result = CommandRunner.runCommand(command);

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + result;

    }

    private File gen2DClassTarFile(String outputTarFileName)
    {

        String filePath = "./warehouse/";
        File   file     = new File(filePath, outputTarFileName);

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
            String[] command = {"tar", "cvf", outputTarFileName, "2DClass"};
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

        return file;
    }

    @RequestMapping("/api/cDisp_2DClass")
    @ResponseBody
    public String cDisp2DClass(HttpServletRequest request, HttpServletResponse response)
    {
        String pID         = request.getParameter("pID");
        String pRound      = request.getParameter("pRound");
        String sNormalized = request.getParameter("sNormalized");
        String pMean       = request.getParameter("pMean");
        String pStd        = request.getParameter("pStd");
        String sRAW        = request.getParameter("sRAW");
        String sMRC        = request.getParameter("sMRC");
        String sJPEG       = request.getParameter("sJPEG");

        if (sNormalized == null)
        {
            System.out.println("sNormalized is null");
        }
        if (sNormalized != null && sNormalized.length() == 0)
        {
            System.out.println("sNormalized is an empty string");
        }

        System.out.printf(
                "pID = %s, pRound = %s, sNormalized = %s, sRaw = %s, sMRC = %s, sJPEG = %s, pMean = %s, pStd = %s\n",
                pID, pRound, sNormalized, sRAW, sMRC, sJPEG, pMean, pStd);

        if (pID == "" || pID == null)
        {
            return "Error: pID is not set";
        }

        if (pRound == "" || pRound == null)
        {
            pRound = "Final";
        }

        if (sNormalized != null && sNormalized.length() == 0 && sJPEG != null && sJPEG.length() == 0)
        {
            return "Error: Parameter sNormalized and sJPEG should not set simultaneously from client.";
        }

        if (sRAW != null && sMRC != null)
        {

            return "Error: Parameter sRAW and sMRC should not set simultaneously from client.";
        }

        if (sJPEG != null && sMRC != null)
        {

            return "Error: Parameter sJPEG and sMRC should not set simultaneously from client.";
        }

        if (sJPEG != null && sRAW != null)
        {

            return "Error: Parameter sJPEG and sRAW should not set simultaneously from client.";
        }

        String mrcsFileName = "Reference_Round_" + pRound + ".mrcs";
        System.out.printf("2D Class file name: %s\n", mrcsFileName);

        String outputDir = Generators.timeBasedGenerator().generate().toString();
        System.out.printf("output dir: %s\n", outputDir);

        String argumentString = "--input ./warehouse/" + mrcsFileName + " --output ./warehouse/imageDir/" + outputDir;

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

        System.out.printf("Argument String = %s\n", argumentString);

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

        String result = convertToImage(command);
        if (result.length() != 0)
        {
            return "Error: Failed to generate Image [ detail:  " + result + " ]";
        }

        File tarFile = compressImageDir("./warehouse/imageDir/", outputDir, outputDir + ".tar");

        transferToClient(response, tarFile);

        return null;

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

    private File compressImageDir(String resultDir, String dirToBeCompressed, String outputTarFileName)
    {
        // String dirName = "./warehouse/";
        File file = new File(resultDir, outputTarFileName);

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

    private String convertToImage(String[] command)
    {

        StringBuffer   result    = new StringBuffer();
        Process        process   = null;
        BufferedReader bufrIn    = null;
        BufferedReader bufrError = null;
        try
        {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(command, null, null);
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
                e.printStackTrace();
            }

            // 销毁子进程
            if (process != null)
            {
                process.destroy();
            }

        }

        System.out.println("result is: " + result);

        return result.toString();

    }

    @RequestMapping("/api/cSQL_Command")
    @ResponseBody
    public String execSQLCommand(HttpServletRequest request, HttpServletResponse response)
    {
        String sql     = request.getParameter("pParaString");
        String dbTable = request.getParameter("pDBTable");

        if (sql == null || sql == "")
        {
            return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: sql statement is empty";
        }

        String finalSQL = sql;
        if (dbTable != null && dbTable.length() > 0)
        {
            finalSQL = modifySQL(sql, dbTable);
        }
        if (finalSQL.equals(ERROR_DB_TABLE_INDEX_IS_NOT_FOUND))
        {
            logger.error("The corresponding TableIndex is not found when dbTable = {}", dbTable);
            return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: TableIndex is not found when pDBTable = " + dbTable;
        }
        logger.info("final sql to execute: {}", finalSQL);

        String command[] = new String[2];
        command[0] = "./warehouse/script/execSQL.sh";
        command[1] = finalSQL;

        StringBuffer   result    = new StringBuffer();
        Process        process   = null;
        BufferedReader bufrIn    = null;
        BufferedReader bufrError = null;
        try
        {
            // String[] command = { command};
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
                // System.out.println("error message add: " + line + "\n");
                result.append(line).append('\n');
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("====================>execute failed1");
            return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "command executes failed";
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
                System.out.println("====================>execute failed2");
                return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "close input buffer or error buffer failed";
            }

            // 销毁子进程
            if (process != null)
            {
                process.destroy();
            }

        }

        // return success message for modify operation like insert, update, delete
        // Commented by suggestion of shenbo and lixueming
        // if(result.length() == 0)
        // {
        // result.append("execute success");
        // }
        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + result.toString();
    }

    private String modifySQL(String sql, String dbTable)
    {
        // TODO Auto-generated method stub
        Tables t = tablesRepository.getByName(dbTable);
        if (t == null)
        {
            return ERROR_DB_TABLE_INDEX_IS_NOT_FOUND;
        }
        String tableIndex = t.getTableIndex();
        String ID         = KeyGenerator.getNextID();
        String dbID       = tableIndex + ID;

        logger.info("TableIndex + ID = {}", dbID);
        String finalSQL = sql.replace("@@KeyID@@", dbID);
        return finalSQL;
    }

    @RequestMapping("/api/cSQL_Tables")
    @ResponseBody
    public String getDatabaseTables(HttpServletRequest request, HttpServletResponse response)
    {

        String command[] = new String[1];
        command[0] = "./warehouse/script/getTables.sh";

        StringBuffer   result    = new StringBuffer();
        Process        process   = null;
        BufferedReader bufrIn    = null;
        BufferedReader bufrError = null;
        try
        {
            // String[] command = { command};
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
                // System.out.println("error message add: " + line + "\n");
                result.append(line).append('\n');
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: command executes failed";
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
                return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: close input/error buffer failed";
            }

            // 销毁子进程
            if (process != null)
            {
                process.destroy();
            }

        }

        // return success message for modify operation like insert, update, delete
        // Commented, suggested by shenbo and lixueming
        // if(result.length() == 0)
        // {
        // result.append("execute success");
        // }
        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + result.toString();
    }

    @RequestMapping("/api/cSQL_Columns")
    @ResponseBody
    public String getTableColumns(HttpServletRequest request, HttpServletResponse response)
    {
        String tableName = request.getParameter("pDBTable");
        if (tableName == null || tableName == "")
        {
            return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: Table name is not specified by key pDBTable";
        }
        String command[] = new String[2];
        command[0] = "./warehouse/script/getTableColumns.sh";
        command[1] = tableName;

        StringBuffer   result    = new StringBuffer();
        Process        process   = null;
        BufferedReader bufrIn    = null;
        BufferedReader bufrError = null;
        try
        {
            // String[] command = { command};
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
                // System.out.println("error message add: " + line + "\n");
                result.append(line).append('\n');
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: command executes failed";
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
                return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: close input/error buffer failed";
            }

            // 销毁子进程
            if (process != null)
            {
                process.destroy();
            }

        }

        // return success message for modify operation like insert, update, delete
        // Commented ,suggested by shenbo and lixueming
        // if(result.length() == 0)
        // {
        // result.append("execute success");
        // }
        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + result.toString();
    }

    @RequestMapping("/api/cGen_ID")
    @ResponseBody
    public String genID(HttpServletRequest request, HttpServletResponse response)
    {
        String ID = KeyGenerator.getNextID();

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + ID;
    }

    @RequestMapping("/api/cGen_DBID")
    @ResponseBody
    public String genDBID(HttpServletRequest request, HttpServletResponse response)
    {
        String tableName = request.getParameter("pDBTable");

        if (tableName == null || tableName.equals(""))
        {
            logger.info("Error: Parameter [pDBTable] is null or empty");
            return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Error: Parameter [pDBTable] is null or empty";
        }

        Tables t = tablesRepository.getByName(tableName);
        if (t == null)
        {
            System.out.println("Error: Table : [ " + tableName + " ] is not exist");
            return "Error: Table : [" + tableName + " ] is not exist";
        }
        String tableIndex = t.getTableIndex();
        String ID         = KeyGenerator.getNextID();
        String dbID       = tableIndex + ID;

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + dbID;
    }

    @RequestMapping("/api/cGet_Time")
    @ResponseBody
    public String getTime(HttpServletRequest request, HttpServletResponse response)
    {
        DateTimeFormatter dtf      = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime     now      = LocalDateTime.now();
        String            currTime = dtf.format(now);

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + "Time=\"" + currTime + "\"";

    }

}
