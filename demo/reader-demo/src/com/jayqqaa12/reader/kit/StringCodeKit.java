package com.jayqqaa12.reader.kit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class StringCodeKit
{

	/** 
     * 获取文件的编码格式 
     * @param path 
     * @return 
     * @throws IOException 
     */  
    public  static String  getTextCharset(String path) throws IOException{  
  
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(path));      
        int p = (bin.read() << 8) +bin.read();  
        String code = null;  
        switch (p) {  
            case 0xefbb:  
                code = "UTF-8";  
                break;  
            case 0xfffe:  
                code = "Unicode";   
                break;  
            case 0xfeff:  
                code = "UTF-16BE";  
                break;  
            default:  
                code = "GBK";  
        }  
        return code;  
    }  
  
}
