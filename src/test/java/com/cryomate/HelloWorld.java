package com.cryomate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.cryomate.pojo.CryomateFileAttribute;
import com.cryomate.utils.FileUtils;

//import org.apache.logging.log4j.message.StringFormatterMessageFactory;
//import org.springframework.context.support.StaticApplicationContext;

public class HelloWorld {
	
	
	public void name() {
		
	}
	
	public static List<String> splitString(String inputString, int step)
	{
		List<String> list = new ArrayList<String>();
		int length = inputString.length();
		int num = length / step;
		int start = 0;
		int end = 0;		
		
		for (int i = 0; i < num; i ++)
		{
			start = i;
			end = (i + 1) * step;			
			list.add(inputString.substring(start, end));						
		}
		
		
		
		if(length % step != 0)
		{
			list.add(inputString.substring(num * step));
		}
		
		return list;
	}
	
	public List<String> splitStringFromEnd(String inputString, int step)
	{
		List<String> list = new ArrayList<String>();
		int length = inputString.length();
		int num = length / step;
		int start = 0;
		int end = 0;	
		
		int mod = length % step;
		
		for (int i = length; i > mod; i -= step)
		{
			start = i - step;
			end = i ;			
			list.add(inputString.substring(start, end));						
		}
		
		if(mod != 0)
		{
			list.add(inputString.substring(0, mod));
		}
		
		return list;
	}
	
	private char decTo64table[] = {
			'0','1','2','3','4','5','6','7','8','9',
			'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			'@','$'
			                       };
	private static int nextValue = 5;
	public int loadNextValue()
	{
		return nextValue ++;
	}
	public synchronized String getNextID()
	{
		StringBuffer nextID = new StringBuffer();
		int nextValue = loadNextValue();
		String strValue = Integer.toBinaryString(nextValue);
		List<String> valueList = splitStringFromEnd(strValue, 6);
		for (String item: valueList)
		{
			int num = Integer.parseUnsignedInt(item, 2);
			nextID.append(decTo64table[num]);
			
		}
		
		
		String formatString = String.format("%8s", nextID.reverse().toString());
		
		String finalString = formatString.replace(' ', '0');
		return finalString;
		
	}

	public synchronized String getNextID2()
	{
        try
        {
            Thread.sleep(1);
        }
        catch(InterruptedException e)
        { 
            e.printStackTrace(); 
        } 

		StringBuffer nextID = new StringBuffer();
		//int nextValue = loadNextValue();
		//String strValue = Integer.toBinaryString(nextValue);
        long nextValue = System.currentTimeMillis();
        String strValue = Long.toBinaryString(nextValue);
		List<String> valueList = splitStringFromEnd(strValue, 6);
		for (String item: valueList)
		{
			int num = Integer.parseUnsignedInt(item, 2);
			nextID.append(decTo64table[num]);
			
		}
		
		
		String formatString = String.format("%8s", nextID.reverse().toString());
		
		String finalString = formatString.replace(' ', '0');
		return finalString;
		
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
    public static Vector[] foo(String fileName) throws IOException
	{
	    FileReader fr = new FileReader(fileName);
	    BufferedReader br = new BufferedReader(fr);
	    String line = br.readLine();
	    int columnNum = 1;
	    if(line != null)
	    {
	        columnNum = line.split(" +").length;
	    }
	    Vector[] v = new Vector[columnNum];
	    for(int i = 0; i < columnNum; i ++)
	    {
	        v[i] = new Vector();
	    }
	    while(line != null)
	    {
	        String columns[] = line.split(" +");	        
	        for(int i = 0; i < columnNum; i ++)
	        {
	            v[i].add(columns[i]);
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
	    
	    return v;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
    public static String foo2(String fileName) throws IOException
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

	public static void main(String[] args) throws IOException 
	{
	
//	    Vector[] v = foo("/root/file/fsc-files/FSC_Final.txt");
//	    for(int i = 0; i < v.length; i ++)
//	    {
//	        System.out.printf("v[%d]: %d:", i, v[i].size());
////	        for(int j = 0; j < v[i].size(); j ++)
////	        {
////	            System.out.print(" " + v[i].get(j));
////	        }
//	        System.out.print(v[i]);
//	        System.out.println();
//	    }
	    
	    String sb = foo2("/root/file/fsc-files/FSC_Final.txt");
	    System.out.println(sb);
		
	}

}
