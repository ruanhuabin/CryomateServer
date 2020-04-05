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
                argumentString = argumentString + " --pStd 5";
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
    
    private String checkMultiStackParameter(HttpServletRequest request, 
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

        if (!file.isDirectory())
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pPath [ " + dirOrFile + " ] does not a directory";
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
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/api/cDisp_MultiStack")
    @ResponseBody   
    public String cDispMultiStack(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String pPath       = request.getParameter("pPath");
        String pFileFilter = request.getParameter("pFileFilter");
        String pAll        = request.getParameter("pAll");
        String sNormalized = request.getParameter("sNormalized");
        String pMean       = request.getParameter("pMean");
        String pStd        = request.getParameter("pSTD");
        String sRAW        = request.getParameter("sRAW");
        String sMRC        = request.getParameter("sMRC");
        String sJPEG       = request.getParameter("sJPEG");
        
        String ret = checkMultiStackParameter(request, pPath, sNormalized, pMean, pStd, sRAW, sMRC, sJPEG, pFileFilter);
        if(ret != null)
        {
            return ret;
        } 
        if(pAll == null || pAll == "")
        {
            pAll = "-1";
        }
        
        if(!pAll.equals("-1") && !pAll.equals("1") && ! pAll.equals("1"))
        {
            return Constant.HTTP_RTN_STATUS_RESULT_PREFIX + "Error: pAll value should be -1, 0, or 1";
        }
        
        //Generate unique output directory
        String argumentString = "";
        String loginUserName = getLoginUserName(request);
        String outputDir = Generators.timeBasedGenerator().generate().toString();
        String outputDirFullPath = this.tmpDirPrefix + File.separatorChar + loginUserName + File.separatorChar + outputDir;
        if(!pAll.contentEquals("0"))
        {   
            argumentString = genArgumentString(sNormalized, pMean, pStd, sRAW, sMRC, sJPEG, outputDirFullPath);            
        }
        //List directory
        String[] filters = pFileFilter.split(";");
        for(int i = 0; i < filters.length; i ++)
        {
            logger.debug("filter string [{}] : {}", i, filters[i]);
        }
        
        File dir = new File(pPath);
        Vector[] fileLists = new Vector[filters.length];        
        //Save file name list in fileLists, format: filterString:filename1;filename2;filename3;filenameN;
        for(int i = 0; i < filters.length; i ++)
        {
            fileLists[i] = new Vector();
            FileFilter fileFilter = new WildcardFileFilter(filters[i]);
            File[] files = dir.listFiles(fileFilter);
            for (int j = 0; j < files.length; j++) 
            {
               fileLists[i].add(files[j].getName());
            }
            logger.debug("Filter [{}]  -->: {}", filters[i], fileLists[i]);
            fileLists[i].sort(null);
        }
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
                String result = genBinaryData(mrcFiles, argumentString);
                logger.info("Gen jpeg|mrc|raw complete, return msg: {}", result);
            }
            
            String outputFileNameFullPath = outputDirFullPath + File.separatorChar + "rtn_string.txt";
            writeFilterFileList(outputFileNameFullPath, filters, fileLists);
            //Generate text return file
            for(int i = 1; i < fileLists.length; i ++)
            {
                logger.info("Text fileLists[{}] = {}", i, fileLists[i]);
                if(fileLists[i].size() > 0)
                {
                    Vector<String> textFiles = getTextFiles(pPath, fileLists[i]);
                    logger.info("Those text files will be used to generate return text message: {}", textFiles);
                    genTextDataToFiles(textFiles, outputFileNameFullPath);
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
                String result = genBinaryData(mrcFiles, argumentString);
                logger.info("Gen jpeg|mrc|raw complete, return msg: {}", result);
                
            }
            
            String outputFileNameFullPath = outputDirFullPath + File.separatorChar + "rtn_string.txt";
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
                    genTextDataToFiles(textFiles, outputFileNameFullPath);
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
            //transferNoImageFile();
        }
        return null;
    }

    private String genArgumentString(String sNormalized, String pMean, String pStd, String sRAW, String sMRC,
            String sJPEG, String outputDirFullPath)
    {
        String argumentString;
        argumentString = "--output " + outputDirFullPath;

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
                argumentString = argumentString + " --pStd 5";
            }
        }
        return argumentString;
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
    private String genBinaryData(Vector<String> fileToBeGenData, String argumentString)
    {
        // TODO Auto-generated method stub
        String result = "";
        for(String f: fileToBeGenData)
        {
            int a = f.lastIndexOf('/') + 1;
            int b = f.lastIndexOf(".mrc");
            String prefix = f.substring(a,b);
            argumentString = argumentString + " --input " + f + " --prefix " + prefix;            
            String paraItems[] = argumentString.split(" ");
            String[] command = new String[paraItems.length + 1];
            command[0] = "./warehouse/bin/mrcs2jpeg";
            logger.info("Program: {}, Argument String : {}", command[0], argumentString);
            for (int i = 0; i < paraItems.length; i++)
            {
                command[i + 1] = paraItems[i].trim();
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
            for(int j = 0; j < fileLists[i].size(); j ++)
            {
                strFileList.append(fileLists[i].get(j) + ";");                
            }
            strFileList.append("\n");
        }
        bw.write(strFileList.toString());
        bw.close();
        fw.close();
        
    }

    @SuppressWarnings("rawtypes")
    public void genTextDataToFiles(Vector<String> fileNames, String outputFileNameFullPath) throws IOException
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
                argumentString = argumentString + " --pStd 5";
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
