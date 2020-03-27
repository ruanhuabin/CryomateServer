package com.cryomate.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
//		System.out.format("%s %s %s\n", attr.owner().getName(),
//		        attr.group().getName(),
//		        PosixFilePermissions.toString(attr.permissions()));
//		logger.info("File [{}] attribute: {} {} {}", fileNameFullPath,attr.owner().getName(),
//		        attr.group().getName(),
//		        PosixFilePermissions.toString(attr.permissions()) );
		CryomateFileAttribute cfa = new CryomateFileAttribute();
		cfa.setOwner(attr.owner().getName());
		cfa.setGroup(attr.group().getName());
		
		return cfa;
		
		
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
    public static String genDataFromFSCFile(String fileName) throws IOException
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
        sb[0].append("x:");
        for(int i = 1; i < columnNum; i ++)
        {
            sb[i].append("y" + i + ":");
        }
        
        while(line != null)
        {
            String columns[] = line.split(" +");            
            for(int i = 0; i < columnNum; i ++)
            {
                sb[i].append(columns[i] + ",");
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
            sb[i].setCharAt(sb[i].length() - 1, '\n');
            finalValue.append(sb[i]);
        }
        //Remove the last '\n'
        finalValue.setCharAt(finalValue.length() - 1, '\0');
        return finalValue.toString();
    }
	
	

}
