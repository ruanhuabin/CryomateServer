package com.cryomate;

//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		int num = 4096;
		String str = Integer.toBinaryString(num);
		System.out.println(str);
		
		
		HelloWorld hwWorld = new HelloWorld();
		List<String>  resultList = hwWorld.splitStringFromEnd(str, 5);
		
		for (Iterator<String> iterator = resultList.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			int num2 = Integer.parseUnsignedInt(string, 2);
			System.out.println(string + " --> " + num2);
			
		}
		
		//long time = System.currentTimeMillis();
		//String aaString = Long.toBinaryString(time);
		//System.out.println(aaString);
			
        for (int i = 0; i < 100; i++) 
        {
            System.out.println(hwWorld.getNextID2());			
        }
	}

}
