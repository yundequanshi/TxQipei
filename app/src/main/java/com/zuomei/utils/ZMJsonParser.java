package com.zuomei.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zuomei.exception.ZMParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ZMJsonParser {

    public static String getStringFromInputStream(InputStream is) throws ZMParserException {
        if (is == null) {
            return null;
        }
        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (IOException e1) {
            throw new ZMParserException("从流进行解析失败！");
        }

        String str = sb.toString();
        return str;

    }


    public static <T> T fromJsonStream(Class<T> cls, InputStream is) throws ZMParserException {
        String jsonString = getStringFromInputStream(is);
        return fromJsonString(cls, jsonString);
    }


    public static <T> T fromJsonString(Class<T> cls, String jsonString) throws ZMParserException {
    	
    	try {
    		
    		 T entity = null;
    	     Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    	     entity = gson.fromJson(jsonString, cls);
    	     return entity;
		} catch (Exception e) {
			throw new ZMParserException(String.format("%s(%s:%s)",e.getMessage(),
				"解析的字符串为"
					,jsonString));
		}
       
    }
}
