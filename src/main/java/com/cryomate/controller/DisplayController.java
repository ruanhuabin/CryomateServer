package com.cryomate.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.filefilter.WildcardFileFilter;
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
    private FileUtils fu = new FileUtils();
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

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + fscInfo;      
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping("/api/cDisp_MultiFSC")
    @ResponseBody    
    public String cDisplayMultiFSC(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String pPath = request.getParameter("pPath"); 
        //This parameter is reserved for future use
        String pTextDataFormat = request.getParameter("pTextDataFormat");
        String pFileFilter = request.getParameter("pFileFilter");
        String pAll = request.getParameter("pAll");
        if(pPath == null || pPath.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFilename is null or empty";
        }
        
        //Make sure the file exists
        File file = new File(pPath);
        if(!file.exists())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File [ " + pPath + " ] does not exist";
        } 
        //Make sure the owner of the being processed file is the login user.
        Users loginUser = (Users)request.getSession().getAttribute("userInfo");
        String loginUserName = loginUser.getUserName();
        String fileOwner = fu.getFileOwner(pPath);
        if(!loginUserName.equals(fileOwner))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File " + pPath + " ] does not belong to current login user!";
        }
        
        if(pFileFilter == null || pFileFilter == "")
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFileFilter is null or empty";
        }
        
        if(pAll == null || pAll == "")
        {
            pAll = "-1";
        }
        
        if(!pAll.contentEquals("-1") && !pAll.contentEquals("1") && !pAll.contentEquals("0"))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pAll should be -1,0, or 1";
        }
        String filters[] = pFileFilter.split(";");
        Vector[] fileLists = fu.listDir(pPath, filters);
        
        StringBuffer rtnString= new StringBuffer();
        
        if(pAll.contentEquals("-1"))
        {
            for(int i = 0; i < fileLists.length; i ++)
            {
                rtnString.append(filters[i] + ":");
                for(int k = 0; k < fileLists[i].size(); k ++)
                {
                    rtnString.append(fileLists[i].get(k) + ";");
                }
                rtnString.setCharAt(rtnString.length() - 1, '\n');
            }
            
            for(int i = 0; i < fileLists.length; i ++)
            {                
                Vector<String> textFiles = getTextFiles(pPath, fileLists[i]);
                for(int j = 0; j < textFiles.size(); j ++)
                {
                    String fileFullName = textFiles.get(j);
                    int a = fileFullName.lastIndexOf("/") + 1;
                    String fileName = fileFullName.substring(a);
                    rtnString.append(fileName + ":");
                    rtnString.append(fu.genDataFromFSCFile(fileFullName));
                }
                
            }
            
        }
        else if(pAll.contentEquals("1"))
        {
            for(int i = 0; i < fileLists.length; i ++)
            {
                rtnString.append(filters[i] + ":");
                for(int k = 0; k < fileLists[i].size(); k ++)
                {
                    rtnString.append(fileLists[i].get(k) + ";");
                }
                rtnString.setCharAt(rtnString.length() - 1, '\n');
            }
            for(int i = 0; i < fileLists.length; i ++)
            {
                Vector<String> textFiles = new Vector<String>();
                for(int m = 0; m < fileLists[i].size(); m ++)
                {
                    textFiles.add(pPath + File.separatorChar + fileLists[i].get(m));
                }
                for(int j = 0; j < textFiles.size(); j ++)
                {
                    String fileFullName = textFiles.get(j);
                    int a = fileFullName.lastIndexOf("/") + 1;
                    String fileName = fileFullName.substring(a);
                    rtnString.append(fileName + ":");
                    rtnString.append(fu.genDataFromFSCFile(fileFullName));
                }
                
            }
            
        }
        else if(pAll.contentEquals("0"))
        {
            for(int i = 0; i < fileLists.length; i ++)
            {
                rtnString.append(filters[i] + ":");
                for(int k = 0; k < fileLists[i].size(); k ++)
                {
                    rtnString.append(fileLists[i].get(k) + ";");
                }
                rtnString.setCharAt(rtnString.length() - 1, '\n');
           }   
        }

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + rtnString.toString();      
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping("/api/cDisp_MultiText")
    @ResponseBody    
    public String cDisplayMultiText(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String pPath = request.getParameter("pPath"); 
        //This parameter is reserved for future use
        String pFileFilter = request.getParameter("pFileFilter");
        String pAll = request.getParameter("pAll");
        if(pPath == null || pPath.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFilename is null or empty";
        }
        
        //Make sure the file exists
        File file = new File(pPath);
        if(!file.exists())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File [ " + pPath + " ] does not exist";
        } 
        //Make sure the owner of the being processed file is the login user.
        Users loginUser = (Users)request.getSession().getAttribute("userInfo");
        String loginUserName = loginUser.getUserName();
        String fileOwner = fu.getFileOwner(pPath);
        if(!loginUserName.equals(fileOwner))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File " + pPath + " ] does not belong to current login user!";
        }
        
        if(pFileFilter == null || pFileFilter == "")
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFileFilter is null or empty";
        }
        
        if(pAll == null || pAll == "")
        {
            pAll = "-1";
        }
        
        if(!pAll.contentEquals("-1") && !pAll.contentEquals("1") && !pAll.contentEquals("0"))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pAll should be -1,0, or 1";
        }
        String filters[] = pFileFilter.split(";");
        Vector[] fileLists = fu.listDir(pPath, filters);
        
        StringBuffer rtnString= new StringBuffer();
        
        if(pAll.contentEquals("-1"))
        {
            for(int i = 0; i < fileLists.length; i ++)
            {
                rtnString.append(filters[i] + ":");
                for(int k = 0; k < fileLists[i].size(); k ++)
                {
                    rtnString.append(fileLists[i].get(k) + ";");
                }
                rtnString.setCharAt(rtnString.length() - 1, '\n');
            }
            
            for(int i = 0; i < fileLists.length; i ++)
            {                
                Vector<String> textFiles = getTextFiles(pPath, fileLists[i]);
                for(int j = 0; j < textFiles.size(); j ++)
                {
                    String fileFullName = textFiles.get(j);
                    int a = fileFullName.lastIndexOf("/") + 1;
                    String fileName = fileFullName.substring(a);
                    rtnString.append(fileName + ":");
                    rtnString.append(fu.genDataFromTextFile(fileFullName));
                }                
            }
            
        }
        else if(pAll.contentEquals("1"))
        {
            for(int i = 0; i < fileLists.length; i ++)
            {
                rtnString.append(filters[i] + ":");
                for(int k = 0; k < fileLists[i].size(); k ++)
                {
                    rtnString.append(fileLists[i].get(k) + ";");
                }
                rtnString.setCharAt(rtnString.length() - 1, '\n');
            }
            for(int i = 0; i < fileLists.length; i ++)
            {
                Vector<String> textFiles = new Vector<String>();
                for(int m = 0; m < fileLists[i].size(); m ++)
                {
                    textFiles.add(pPath + File.separatorChar + fileLists[i].get(m));
                }
                for(int j = 0; j < textFiles.size(); j ++)
                {
                    String fileFullName = textFiles.get(j);
                    int a = fileFullName.lastIndexOf("/") + 1;
                    String fileName = fileFullName.substring(a);
                    rtnString.append(fileName + ":");
                    rtnString.append(fu.genDataFromTextFile(fileFullName));
                }
                
            }
            
        }
        else if(pAll.contentEquals("0"))
        {
            for(int i = 0; i < fileLists.length; i ++)
            {
                rtnString.append(filters[i] + ":");
                for(int k = 0; k < fileLists[i].size(); k ++)
                {
                    rtnString.append(fileLists[i].get(k) + ";");
                }
                rtnString.setCharAt(rtnString.length() - 1, '\n');
           }   
        }

        return Constant.HTTP_RTN_TEXT_RESULT_PREFIX + rtnString.toString(); 
        
    }

    private String getLoginUserName(HttpServletRequest request)
    {
        Users loginUser = (Users)request.getSession().getAttribute("userInfo");
        String loginUserName = loginUser.getUserName();
        
        return loginUserName;
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
        
        String ret = checkCommonParameter(request, pFilename, sNormalized, sRAW, sMRC, sJPEG);
        if(ret != null)
        {
            return ret;
        }

        String outputDir = Generators.timeBasedGenerator().generate().toString();
        String loginUserName = getLoginUserName(request);
        String outputDirFullPath = this.tmpDirPrefix + 
                           File.separatorChar + 
                           loginUserName + 
                           File.separatorChar + 
                           outputDir;
        
        String argumentString = "--input " + pFilename;
        argumentString = genArgumentString(argumentString, sNormalized, pMean, pStd, sRAW, sMRC, sJPEG, outputDirFullPath);
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

        //1:program name
        //2:prefix and prefix value
        String command[] = new String[paraItems.length + 1 + 2];
        command[0] = "warehouse/bin/cDisp_3DMap";
        int beginIndex = pFilename.lastIndexOf('/') + 1;
        String prefixStr = pFilename.substring(beginIndex);
        command[1] = "--prefix";
        command[2] = prefixStr + "@";
        for (int i = 0; i < paraItems.length; i++)
        {
            command[i + 3] = paraItems[i].trim();
        }

        System.out.println("====>Command to be executed:");
        for (String item : command)
        {
            System.out.print(item + " ");
        }
        System.out.println("");

        //run command
        String result = CommandRunner.runCommand(command);
        if (result.length() != 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Failed to generate Image [ detail:  " + result + " ]";
        }
        //compress directory
        File tarFile = compressImageDir(outputDirFullPath, outputDir, outputDirFullPath + ".tar");
        logger.info("Tar File Full Path: {}", tarFile.getAbsoluteFile());
        //transfer tar file to client
        transferToClient(response, tarFile);

        return null;
    }
    
    
    private String checkMultiDisplyParameter(HttpServletRequest request, 
                                            String dirOrFile, 
                                            String sNormalized, 
                                            String pMean, 
                                            String pSTD, 
                                            String sRAW, 
                                            String sMRC, 
                                            String sJPEG, 
                                            String pFileFilter) throws IOException
    {
        String ret = checkCommonParameter(request, dirOrFile, sNormalized, sRAW, sMRC, sJPEG);  
        if(ret != null)
        {
            return ret;
        }
        
        if(pFileFilter == null || pFileFilter == "")
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pFileFilter is null or empty";
        }
        
        return null;
    }

    private String checkCommonParameter(HttpServletRequest request, String dirOrFile, String sNormalized, String sRAW, String sMRC,
            String sJPEG) throws IOException
    {
        if (dirOrFile == "" || dirOrFile == null)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pPath/pFilename is null or empty";
        }

        // Make sure the file exists
        File file = new File(dirOrFile);
        if (!file.exists())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: Input [ " + dirOrFile + " ] does not exist";
        }

        // Make sure the owner of the being processed file is the login user.
        Users     loginUser     = (Users) request.getSession().getAttribute("userInfo");
        String    loginUserName = loginUser.getUserName();
        FileUtils fu            = new FileUtils();
        String    fileOwner     = fu.getFileOwner(dirOrFile);
        if (!loginUserName.equals(fileOwner))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: File or directory " + dirOrFile
                    + " ] does not belong to current login user!";
        }

        if (sNormalized != null && sNormalized.length() == 0 && sJPEG != null && sJPEG.length() == 0)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX
                    + "Error: Parameter sNormalized and sJPEG should not set simultaneously from client.";
        }

        if (sRAW != null && sMRC != null)
        { 
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX
                    + "Error: Parameter sRAW and sMRC should not set simultaneously from client.";
        }

        if (sJPEG != null && sMRC != null)
        { 
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX
                    + "Error: Parameter sJPEG and sMRC should not set simultaneously from client.";
        }

        if (sJPEG != null && sRAW != null)
        { 
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX
                    + "Error: Parameter sJPEG and sRAW should not set simultaneously from client.";
        }

        if (sJPEG == null && sRAW == null && sMRC == null)
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX
                    + "Error: Parameter sJPEG, sRAW, sMRC should not set null simultaneously";
        }

        // sMRC or sRAW should come with sNormalized != null
        if (sNormalized == null && (sMRC != null || sRAW != null))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: sMRC or sRAW should go with sNormalized";
        }

        return null;
    }
    
    @RequestMapping("/api/cDisp_MultiStack")    
    @ResponseBody   
    public String cDispMultiStack(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return multiDisplay(request, response, Constant.STACK_PROGRAM);
        
    }
    
    @RequestMapping("/api/cDisp_MultiImage")    
    @ResponseBody   
    public String cDispMultiImage(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return multiDisplay(request, response, Constant.IMAGE_PROGRAM);
        
    }
    
    @RequestMapping("/api/cDisp_Multi3DMap")    
    @ResponseBody   
    public String cDispMulti3DMap(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return multiDisplay(request, response, Constant.MAP_PROGRAM);
        
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    private String multiDisplay(HttpServletRequest request, HttpServletResponse response, String program) throws IOException
    {
        /*
         * Process Logic:
         * 1. Valid parameter
         * 2. Construct input arguments for program mrcs2jpeg based on input parameter
         * 3. List directory pPath with filter string in parameter pFileFilter
         * 4. pAll = -1: generate jpeg | raw | mrc file from *_Final.mrc or last mrc file
         *               Write following message to FilterMatch.txt
         *                  filter string1: file list
         *                  filename1:file content line by line separated by;
         *    pAll = 1: generate jpeg | raw | mrc file from all listed mrc file
         *              Write following message to FilterMatch.txt
         *                  filter string1: file list
         *                  filename1:file content line by line separated by;
         *    pAll = 0: only gen FilterMatch.txt with content:
         *                  filter string1: file list
         *                  filter string2: file list
         * 
         *    
         */
        String pPath       = request.getParameter("pPath");
        String pFileFilter = request.getParameter("pFileFilter");
        String pAll        = request.getParameter("pAll");
        String sNormalized = request.getParameter("sNormalized");
        String pMean       = request.getParameter("pMean");
        String pStd        = request.getParameter("pSTD");
        String sRAW        = request.getParameter("sRAW");
        String sMRC        = request.getParameter("sMRC");
        String sJPEG       = request.getParameter("sJPEG");
        String pSlice      = request.getParameter("pSlice");
        String pAxis       = request.getParameter("pAxis");
        
        String ret = checkMultiDisplyParameter(request, pPath, sNormalized, pMean, pStd, sRAW, sMRC, sJPEG, pFileFilter);
        if(ret != null)
        {
            return ret;
        } 
        if(pAll == null || pAll == "")
        {
            pAll = "-1";
        }
        
        if(!pAll.equals("-1") && !pAll.equals("1") && ! pAll.equals("0"))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pAll value should be -1, 0, or 1";
        }
        
        if(pAxis == null || pAxis == "")
        {
            pAxis = "z";
        }
        if(!pAxis.equals("z") && !pAxis.equals("y") && ! pAxis.equals("x"))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pAxis value should be x, y, or z";
        }
        
        if(pSlice == null || pSlice == "")
        {
            pSlice = "0";
        }
        
        
        String argumentString = "";
        if(program.contentEquals(Constant.MAP_PROGRAM))
        {
            argumentString="--pAxis " + pAxis + " --pSlice " + pSlice;
        }
        
        //Generate unique output directory
        String loginUserName = getLoginUserName(request);
        String outputDir = Generators.timeBasedGenerator().generate().toString();
        String outputDirFullPath = this.tmpDirPrefix + File.separatorChar + loginUserName + File.separatorChar + outputDir;
        if(!pAll.contentEquals("0"))
        {   
            argumentString = genArgumentString(argumentString, sNormalized, pMean, pStd, sRAW, sMRC, sJPEG, outputDirFullPath);            
        }
        //List directory
        String[] filters = pFileFilter.split(";");
        for(int i = 0; i < filters.length; i ++)
        {
            logger.debug("filter string [{}] : {}", i, filters[i]);
        }
        Vector[] fileLists = fu.listDir(pPath, filters);
        //Construct file list as string
        StringBuffer sbFileList = new StringBuffer();        
        for(int i = 0; i < fileLists.length; i ++)
        {
            sbFileList.append(filters[i] + ":");
            for(int j = 0; j < fileLists[i].size() - 1; j ++)
            {
                sbFileList.append((String)fileLists[i].get(j) + ";");
            }
            if(fileLists[i].size() > 0)
            {
                sbFileList.append((String)fileLists[i].get(fileLists[i].size() - 1) + "\n");
            }
        }
        if(pAll.contentEquals("-1"))
        {
            //We suppose mrc files are in fileList[0], other text file are in fileLists[1...N]
            if(fileLists[0].size() > 0)
            {
                Vector<String> mrcFiles = new Vector<String>();
                getMRCFiles(mrcFiles, pPath, fileLists[0]);
                logger.info("Those mrc files will be used to generate jpeg/mrc/raw: {}", mrcFiles);
                String result = genBinaryData(mrcFiles, argumentString, program);
                logger.info("Gen jpeg|mrc|raw complete, return msg: {}", result);
            }
            
            String outputFileNameFullPath = outputDirFullPath + File.separatorChar + "FilterMatch.txt";
            writeFilterFileList(outputFileNameFullPath, filters, fileLists);
            //Generate text return file
            for(int i = 1; i < fileLists.length; i ++)
            {
                logger.info("Text fileLists[{}] = {}", i, fileLists[i]);
                if(fileLists[i].size() > 0)
                {
                    Vector<String> textFiles = getTextFiles(pPath, fileLists[i]);
                    logger.info("Those text files will be used to generate return text message: {}", textFiles);
                    genTextDataToFiles(textFiles, outputDirFullPath);
                }                
            }
            //Compress output dir
            String outputTarFileName = outputDirFullPath + ".tar";
            File tarFile = compressImageDir(outputDirFullPath, outputDir, outputTarFileName);
            //Transfer compressed output dir to client
            transferToClient(response, tarFile);
        }
        else if(pAll.contentEquals("1"))
        {
            if(fileLists[0].size() > 0)
            { 
                Vector<String> mrcFiles = new Vector<String>();
                for(Object f: fileLists[0])
                {
                    String fileName = (String)f;                    
                    fileName = pPath + File.separatorChar + fileName;
                    mrcFiles.add(fileName);                   
                }
                logger.info("Those mrc files will be used to generate jpeg/mrc/raw: {}", mrcFiles);
                String result = genBinaryData(mrcFiles, argumentString, program);
                logger.info("Gen jpeg|mrc|raw complete, return msg: {}", result);
                
            }
            
            String outputFileNameFullPath = outputDirFullPath + File.separatorChar + "FilterMatch.txt";
            writeFilterFileList(outputFileNameFullPath, filters, fileLists);
            //Generate text return file
            for(int i = 1; i < fileLists.length; i ++)
            {
                logger.info("Text fileLists[{}] = {}", i, fileLists[i]);
                if(fileLists[i].size() > 0)
                {
                    Vector<String> textFiles = new Vector<String>();
                    //getTextFiles(textFiles, pPath, fileLists[i]);
                    String parentPath = pPath + File.separatorChar;
                    for(int j = 0; j < fileLists[i].size(); j ++)
                    {
                        textFiles.add(parentPath + (String)fileLists[i].get(j));
                    }
                    logger.info("Those text files will be used to generate return text message: {}", textFiles);
                    genTextDataToFiles(textFiles, outputDirFullPath);
                }                
            }
            //Compress output dir
            String outputTarFileName = outputDirFullPath + ".tar";
            File tarFile = compressImageDir(outputDirFullPath, outputDir, outputTarFileName);
            //Transfer compressed output dir to client
            transferToClient(response, tarFile);
        }
        else if(pAll.contentEquals("0"))
        {
            String outputFileNameFullPath = outputDirFullPath + File.separatorChar + "FilterMatch.txt";
            File f = new File(outputDirFullPath);
            if(!f.exists())
            {
                boolean isSuccess = f.mkdirs();
                if(!isSuccess)
                {
                    return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + " Error: Create dir [ " + outputDir + " ] failed, maybe permission denied.";
                }
            }
            writeFilterFileList(outputFileNameFullPath, filters, fileLists);
            //Compress output dir
            String outputTarFileName = outputDirFullPath + ".tar";
            File tarFile = compressImageDir(outputDirFullPath, outputDir, outputTarFileName);
            //Transfer compressed output dir to client
            transferToClient(response, tarFile);
        }
        return null;
    }

    private String genArgumentString(String currArgumentString, String sNormalized, String pMean, String pStd, String sRAW, String sMRC,
            String sJPEG, String outputDirFullPath)
    {
        
        currArgumentString = currArgumentString + " --output " + outputDirFullPath;

        if (sNormalized != null)
        {
            currArgumentString = currArgumentString + " --sNormalized";
        }

        if (sRAW != null)
        {
            currArgumentString = currArgumentString + " --sRAW";
            if(pStd == null)
            {
                currArgumentString = currArgumentString + " --pStd 1";
            }
            else
            {
                currArgumentString = currArgumentString + " --pStd " + pStd;
            }
        }

        if (sMRC != null)
        {
            currArgumentString = currArgumentString + " --sMRC";
            if(pStd == null)
            {
                currArgumentString = currArgumentString + " --pStd 1";
            }
            else
            {
                currArgumentString = currArgumentString + " --pStd " + pStd;
            }
        }

        if (sJPEG != null)
        {
            currArgumentString = currArgumentString + "  --sJPEG";
            if(pStd == null)
            {
                currArgumentString = currArgumentString + " --pStd 5";
            }
            else
            {
                currArgumentString = currArgumentString + " --pStd " + pStd;
            }
        }

        if (pMean != null)
        {
            currArgumentString = currArgumentString + " --pMean " + pMean;
        } else
        {
            currArgumentString = currArgumentString + " --pMean 0";
        }

        return currArgumentString;
    }

    @SuppressWarnings("rawtypes")
    private void getMRCFiles(Vector<String> targetMRCFiles, String pPath, Vector files)
    {
        // TODO Auto-generated method stub
        String pathPrefix = pPath + File.separatorChar;
        boolean isFinalExist = false;
        for(Object f: files)
        {
            String fileName = (String)f;
            if(fileName.contains("_Final.mrc"))
            {
                targetMRCFiles.add(pathPrefix + File.separatorChar + fileName);
                isFinalExist = true;
                break;
            }
        }
        
        if(!isFinalExist)
        {
            targetMRCFiles.add(pathPrefix + (String)files.get(files.size() - 1));
        }   
    }

    @SuppressWarnings("rawtypes")
    private Vector<String> getTextFiles( String pPath, Vector files)
    {
        Vector<String> targetTextFiles = new Vector<String>();
        String pathPrefix = pPath + File.separatorChar;
        boolean isFinalExist = false;
        for(Object f: files)
        {
            String fileName = (String)f;
            if(fileName.contains("_Final"))
            {
                targetTextFiles.add(pathPrefix + File.separatorChar + fileName);
                isFinalExist = true;
                break;
            }
        }
        
        if(!isFinalExist)
        {
            targetTextFiles.add(pathPrefix + (String)files.get(files.size() - 1));
        } 
        
        return targetTextFiles;
    }
    private String genBinaryData(Vector<String> fileToBeGenData, String argumentString, String program)
    {
        logger.info("Full argument string: {}", argumentString);
        int cnt = 0;
        String result = "";
        for(String f: fileToBeGenData)
        {
            int beginIndex = f.lastIndexOf('/') + 1;
            String prefixValue = f.substring(beginIndex) + "@";
            String paraItems[] = argumentString.split(" ");
            //1:program name
            //2: --input value
            //2: --genMeta value
            //2: --prefix value
            String[] command = new String[paraItems.length + 1 + 2 + 2 + 2];
            command[0] = program;
            command[1] = "--input";
            command[2] = f;
            command[3] = "--genMeta";
            if(cnt == 0)
            {
                command[4] = "1";
            }
            else
            {
                command[4] = "0";
            }
            cnt ++;
            
            command[5] = "--prefix";
            command[6] = prefixValue;
            logger.info("Program: {}, Argument String : {} {} {} {} {} {} {} ", command[0], command[1], command[2], command[3], command[4], command[5], command[6], argumentString);
            for (int i = 0; i < paraItems.length; i++)
            {
                command[i + 7] = paraItems[i].trim();
            }
            result = result + CommandRunner.runCommand(command);
        }
        
        return result;
    }
    
    public void writeFilterFileList(String outputFileNameFullPath, String[] filters, Vector[] fileLists) throws IOException
    {
        FileWriter fw = new FileWriter(outputFileNameFullPath, true);
        BufferedWriter bw = new BufferedWriter(fw);
        StringBuffer strFileList = new StringBuffer();
        for(int i = 0; i < filters.length; i ++)
        {            
            strFileList.append(filters[i] + ":");
            int fileNum = fileLists[i].size();
            for(int j = 0; j < fileNum - 1; j ++)
            {
                strFileList.append(fileLists[i].get(j) + ";");                
            }
            //append last file without adding separator ";"
            if(fileNum > 0)
            {
                strFileList.append(fileLists[i].get(fileNum - 1));
            }
            strFileList.append("\n");
        }
        
        int endIndex = strFileList.length() - 1;
        if(strFileList.charAt(endIndex) == '\n')
        {
            strFileList.deleteCharAt(endIndex);
        }
        bw.write(strFileList.toString());
        bw.close();
        fw.close();
    }

    @SuppressWarnings("rawtypes")
    public void genTextDataToFiles(Vector<String> fileNames, String outputDir) throws IOException
    {
        for(String fileName: fileNames)
        {
            StringBuffer sb = new StringBuffer();
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while(line != null)
            {               
                sb.append(line + ";");
                line = br.readLine();
            }
            
            br.close();
            fr.close();
            
            //remove last ';'
            int endIndex = sb.length() - 1;
            if(endIndex >= 0 && sb.charAt(endIndex) == ';')
            {
                sb.deleteCharAt(endIndex);
            }
            
            int beginIndex = fileName.lastIndexOf('/') + 1;
            String outputFileNameFullPath = outputDir + File.separatorChar + fileName.substring(beginIndex);
            FileWriter fw = new FileWriter(outputFileNameFullPath);
            BufferedWriter bw = new BufferedWriter(fw);        
            bw.write(sb.toString());
            bw.close();
            fw.close();            
        }
        
    
    }
    @SuppressWarnings("rawtypes")
    public void genTextDataToFilesPrevious(Vector<String> fileNames, String outputFileNameFullPath) throws IOException
    {
        StringBuffer sb = new StringBuffer();
        for(String fileName: fileNames)
        {
            int a = fileName.lastIndexOf("/") + 1;            
            sb.append(fileName.substring(a) + ":");
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while(line != null)
            {               
                sb.append(line + ";");
                line = br.readLine();
            }
            
            int end = sb.length() - 1;            
            if(sb.charAt(end) == ';')
            {
                sb.setCharAt(end, '\n'); 
            }
            
            if(sb.charAt(end) == ':')
            {
                sb.append('\n');
            }
            
            br.close();
            fr.close();         
        }

        FileWriter fw = new FileWriter(outputFileNameFullPath, true);
        BufferedWriter bw = new BufferedWriter(fw);        
        bw.write(sb.toString());
        bw.close();
        fw.close();
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

        String ret = checkCommonParameter(request, pFilename, sNormalized, sRAW, sMRC, sJPEG);  
        if(ret != null)
        {
            return ret;
        }
        String loginUserName = getLoginUserName(request);
        String outputDir = Generators.timeBasedGenerator().generate().toString();
        String outputDirFullPath = this.tmpDirPrefix + File.separatorChar + 
                                   loginUserName + File.separatorChar + outputDir;

        String argumentString = "--input " + pFilename;
        argumentString = genArgumentString(argumentString, sNormalized, pMean, pStd, sRAW, sMRC, sJPEG, outputDirFullPath);
        logger.info("Return value of genArgumentString() = {}", argumentString);

        String paraItems[] = argumentString.split(" ");
        //1:store program name
        //2:store prefix argument, prefix value is based on input file name
        String command[] = new String[paraItems.length + 1 + 2];
        command[0] = "warehouse/bin/mrcs2jpeg";
        //construct prefix value 'filename@'
        int a = pFilename.lastIndexOf('/') + 1;
        String prefixStr = pFilename.substring(a);
        command[1] = "--prefix";
        command[2] = prefixStr + "@";
        
        for (int i = 0; i < paraItems.length; i++)
        {
            command[i + 3] = paraItems[i].trim();
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
        logger.info("Output Tar File Full Path: {}", file.getAbsoluteFile());

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

    private void transferToClient(HttpServletResponse response, File tarFile)
    {

        if (tarFile.exists())
        {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + tarFile.getName());// 设置文件名
            int contentLength = (int)tarFile.length();
            logger.info("Length of Tar File [{}]: {}", tarFile.getName(), contentLength);
            response.setContentLength(contentLength);
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
