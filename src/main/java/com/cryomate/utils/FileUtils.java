package com.cryomate.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cryomate.pojo.Constant;
import com.cryomate.pojo.CryomateFileAttribute;

public class FileUtils
{
	private static final Logger logger = LoggerFactory
            .getLogger(FileUtils.class);
	private static final int SUCCESS = 0;
	private static final int FAILED = 1;
	public String getFileOwner(String fileNameFullPath) throws IOException
	{
		Path path = Paths.get(fileNameFullPath);
		// create object of file attribute.
		FileOwnerAttributeView view = Files.getFileAttributeView(path,
		        FileOwnerAttributeView.class);
		// this will get the owner information.
		UserPrincipal userPrincipal = view.getOwner();

		String fileOwner = userPrincipal.getName();

		return fileOwner;
	}
	
	public CryomateFileAttribute getFileAttr(String fileNameFullPath) throws IOException
	{
		Path file = Paths.get(fileNameFullPath);;
		PosixFileAttributes attr = Files.readAttributes(file,
		        PosixFileAttributes.class);		
		CryomateFileAttribute cfa = new CryomateFileAttribute();
		cfa.setOwner(attr.owner().getName());
		cfa.setGroup(attr.group().getName());
		
		return cfa;
		
		
	}
	
	@SuppressWarnings("rawtypes")
    public Vector[] listDir(String pPath, String[] filters)
	{
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
            logger.info("Filter [{}]  -->: {}", filters[i], fileLists[i]);
            fileLists[i].sort(null);
        }
        
        return fileLists;
	}
	
	public int changeFileOwnerGroup(String fileFullName, String owner, String group) throws IOException
	{
//		Path path = Paths.get(fileFullName);	    
//	    UserPrincipalLookupService lookupService = FileSystems.getDefault()
//	        .getUserPrincipalLookupService();
//	    UserPrincipal userPrincipal = lookupService.lookupPrincipalByName(currLoginUserName);
//
//	    Files.setOwner(path, userPrincipal);
		
		Path file = Paths.get(fileFullName);		
		UserPrincipal userPrincipal = file.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByName(owner);
		GroupPrincipal groupPrincipal =  file.getFileSystem().getUserPrincipalLookupService().lookupPrincipalByGroupName(group);
		PosixFileAttributeView fileAttributeView = Files.getFileAttributeView(file, PosixFileAttributeView.class);
		fileAttributeView.setOwner(userPrincipal);
		fileAttributeView.setGroup(groupPrincipal);		
		return SUCCESS;
	}
	
	public int mergeFiles(String outputFileNameFullPath, String[] srcFileNames) throws IOException
	{
		File ofile = new File(outputFileNameFullPath);
		if(ofile.exists())
		{
			ofile.delete();
			ofile.createNewFile();
		}
		
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		List<File> list = new ArrayList<File>();
		for(String fileName: srcFileNames)
		{
			list.add(new File(fileName));
		}
		
		try {
		    fos = new FileOutputStream(ofile,true);
		    
		    for (File file : list) {
		        fis = new FileInputStream(file);
		        fileBytes = new byte[(int) file.length()];
		        bytesRead = fis.read(fileBytes, 0,(int)  file.length());
		        assert(bytesRead == fileBytes.length);
		        assert(bytesRead == (int) file.length());
		        fos.write(fileBytes);
		        fos.flush();
		        fileBytes = null;
		        fis.close();
		        fis = null;
		    }
		    fos.close();
		    fos = null;
		}catch (Exception exception){
			exception.printStackTrace();
			return FAILED;
		}
		return SUCCESS;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
    public String genDataFromFSCFile(String fileName) throws IOException
    {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        int columnNum = 1;
        if(line != null)
        {
            columnNum = line.split(" +").length;
        }        
        StringBuffer[] sb = new StringBuffer[columnNum];
        for(int i = 0; i < columnNum; i ++)
        {
            sb[i] = new StringBuffer();
        }
        
        while(line != null)
        {
            String columns[] = line.split(" +");            
            for(int i = 0; i < columnNum; i ++)
            {
                sb[i].append(columns[i] + "\t");
            }
            line = br.readLine();
        }
        
        if(br != null)
        {
            br.close();
        }
        if(fr != null)
        {
            fr.close();
        }
        
        StringBuffer finalValue = new StringBuffer(); 
        //Remove the last ',' in the end of each sb[i], and append sb[i] to new string buffer used as final return string
        for(int i = 0; i < columnNum; i ++)
        {
            sb[i].setCharAt(sb[i].length() - 1, ';');
            finalValue.append(sb[i]);
        }
        //Remove the last '\n'
        finalValue.setCharAt(finalValue.length() - 1, '\n');
        return finalValue.toString();
    }

    public String genDataFromTextFile(String fileFullName) throws IOException
    {
        FileReader fr = new FileReader(fileFullName);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();              
        StringBuffer sb = new StringBuffer();        
        
        while(line != null)
        {
            sb.append(line + ";");
            line = br.readLine();
        }
        sb.setCharAt(sb.length() - 1, '\n');
        
        if(br != null)
        {
            br.close();
        }
        if(fr != null)
        {
            fr.close();
        }
        
        return sb.toString();
    }

}
