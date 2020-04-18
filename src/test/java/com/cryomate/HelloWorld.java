package com.cryomate;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cryomate.utils.FileUtils;

public class HelloWorld
{

    private static final Logger logger = LoggerFactory
            .getLogger(HelloWorld.class);
    public void name()
    {

    }

    public static List<String> splitString(String inputString, int step)
    {
        List<String> list   = new ArrayList<String>();
        int          length = inputString.length();
        int          num    = length / step;
        int          start  = 0;
        int          end    = 0;

        for (int i = 0; i < num; i++)
        {
            start = i;
            end   = (i + 1) * step;
            list.add(inputString.substring(start, end));
        }

        if (length % step != 0)
        {
            list.add(inputString.substring(num * step));
        }

        return list;
    }

    public List<String> splitStringFromEnd(String inputString, int step)
    {
        List<String> list   = new ArrayList<String>();
        int          length = inputString.length();
        int          num    = length / step;
        int          start  = 0;
        int          end    = 0;

        int mod = length % step;

        for (int i = length; i > mod; i -= step)
        {
            start = i - step;
            end   = i;
            list.add(inputString.substring(start, end));
        }

        if (mod != 0)
        {
            list.add(inputString.substring(0, mod));
        }

        return list;
    }

    private char       decTo64table[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
                        'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
                        'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                        'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '@', '$'};
    private static int nextValue      = 5;
    public int loadNextValue()
    {
        return nextValue++;
    }
    public synchronized String getNextID()
    {
        StringBuffer nextID    = new StringBuffer();
        int          nextValue = loadNextValue();
        String       strValue  = Integer.toBinaryString(nextValue);
        List<String> valueList = splitStringFromEnd(strValue, 6);
        for (String item : valueList)
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
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        StringBuffer nextID = new StringBuffer();
        // int nextValue = loadNextValue();
        // String strValue = Integer.toBinaryString(nextValue);
        long         nextValue = System.currentTimeMillis();
        String       strValue  = Long.toBinaryString(nextValue);
        List<String> valueList = splitStringFromEnd(strValue, 6);
        for (String item : valueList)
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
        FileReader     fr        = new FileReader(fileName);
        BufferedReader br        = new BufferedReader(fr);
        String         line      = br.readLine();
        int            columnNum = 1;
        if (line != null)
        {
            columnNum = line.split(" +").length;
        }
        Vector[] v = new Vector[columnNum];
        for (int i = 0; i < columnNum; i++)
        {
            v[i] = new Vector();
        }
        while (line != null)
        {
            String columns[] = line.split(" +");
            for (int i = 0; i < columnNum; i++)
            {
                v[i].add(columns[i]);
            }
            line = br.readLine();
        }

        if (br != null)
        {
            br.close();
        }
        if (fr != null)
        {
            fr.close();
        }

        return v;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String foo2(String fileName) throws IOException
    {
        FileReader     fr        = new FileReader(fileName);
        BufferedReader br        = new BufferedReader(fr);
        String         line      = br.readLine();
        int            columnNum = 1;
        if (line != null)
        {
            columnNum = line.split(" +").length;
        }
        StringBuffer[] sb = new StringBuffer[columnNum];
        for (int i = 0; i < columnNum; i++)
        {
            sb[i] = new StringBuffer();
        }
        sb[0].append("x:");
        for (int i = 1; i < columnNum; i++)
        {
            sb[i].append("y" + i + ":");
        }

        while (line != null)
        {
            String columns[] = line.split(" +");
            for (int i = 0; i < columnNum; i++)
            {
                sb[i].append(columns[i] + ",");
            }
            line = br.readLine();
        }

        if (br != null)
        {
            br.close();
        }
        if (fr != null)
        {
            fr.close();
        }

        StringBuffer finalValue = new StringBuffer();
        // Remove the last ',' in the end of each sb[i], and append sb[i] to new string buffer used as final return
        // string
        for (int i = 0; i < columnNum; i++)
        {
            sb[i].setCharAt(sb[i].length() - 1, '\n');
            finalValue.append(sb[i]);
        }
        // Remove the last '\n'
        finalValue.setCharAt(finalValue.length() - 1, '\0');
        return finalValue.toString();
    }

    public void genTextFiles(Vector<String> fileNames, String outputFileNameFullPath) throws IOException
    {
        StringBuffer sb = new StringBuffer();
        for (String fileName : fileNames)
        {
            sb.append(fileName + ":");
            FileReader     fr   = new FileReader(fileName);
            BufferedReader br   = new BufferedReader(fr);
            String         line = br.readLine();
            while (line != null)
            {
                sb.append(line + ";");
                line = br.readLine();
            }
            sb.setCharAt(sb.length() - 1, '\n');
            br.close();
            fr.close();
        }

        FileWriter     fw = new FileWriter(outputFileNameFullPath);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(sb.toString());
        bw.close();
        fw.close();
    }
    
    private String getFilterString(String inputFilterString)
    {
        int a = inputFilterString.indexOf('@') + 1;
        String finalFilterStr = inputFilterString.substring(a);
        
        return finalFilterStr;
    }
    
    @SuppressWarnings("rawtypes")
    public HashMap<String, Vector<String>> listDir2(String pPath, String[] filters)
    {
        File dir = new File(pPath);
        //Vector[] fileLists = new Vector[filters.length];      
        HashMap<String, Vector<String>> filter2Files = new HashMap<String, Vector<String>>();
        for(int i = 0; i < filters.length; i ++)
        {
            Vector<String> fileList = new Vector<String>();
            //fileLists[i] = new Vector();
            String filterString = getFilterString(filters[i]);
            FileFilter fileFilter = new WildcardFileFilter(filterString);
            File[] files = dir.listFiles(fileFilter);
            for (int j = 0; j < files.length; j++) 
            {
                fileList.add(files[j].getName());
            }
            logger.info("InputFilter = [{}], Actual Filter [{}]  -->: {}", filters[i], filterString, fileList);
            fileList.sort(null);
            filter2Files.put(filters[i], fileList);
            
        }
        
        return filter2Files;
    }
    

    public static void main(String[] args) throws IOException
    {

        HelloWorld hw = new HelloWorld();
        String pPath = "/Share/TestData/C2D_001/";
        String[] filters = new String[2];
        filters[0] = "TXT@*.txt";
        filters[1] = "MRC@*.mrcs";
        HashMap<String, Vector<String>> filterFiles = hw.listDir2(pPath, filters);
        //System.out.println(filterFiles.get(filters[0]));
        //System.out.println(filterFiles);
        
        for(Entry<String, Vector<String>> e: filterFiles.entrySet())
        {
            if(e.getKey().startsWith("TXT@"))
            {
                System.out.println("TXT: " + e.getValue());
            }
            else if(e.getKey().startsWith("MRC@"))
            {
                System.out.println("MRC:" + e.getValue());
            }
        }
        
        
        
        //filter
        
        
        
        
        

    }

}
