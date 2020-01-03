package com.cryomate.utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KeyGenerator{

    private static char decTo64table[] = {
        '0','1','2','3','4','5','6','7','8','9',
        'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
        'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
        '@','_'
    };

    private static List<String> splitString(String inputString, int step)
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


    public static synchronized String getNextID()
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
        long nextValue = System.currentTimeMillis();
        String strValue = Long.toBinaryString(nextValue);
        List<String> valueList = splitString(strValue, 6);
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
        long num = System.currentTimeMillis();
        String str = Long.toBinaryString(num);
        System.out.println(str);


        KeyGenerator hwWorld = new KeyGenerator();
        List<String>  resultList = hwWorld.splitString(str, 5);

        for (Iterator<String> iterator = resultList.iterator(); iterator.hasNext();) {
            String string = (String) iterator.next();
            int num2 = Integer.parseUnsignedInt(string, 2);
            System.out.println(string + " --> " + num2);

        }

        for (int i = 0; i < 10; i++) 
        {
            System.out.println(hwWorld.getNextID());			
        }
    }

}
