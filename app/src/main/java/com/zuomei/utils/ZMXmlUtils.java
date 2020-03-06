package com.zuomei.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.zuomei.exception.ZMParserException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;


/**
 * @author Omi
 */
public class ZMXmlUtils {


    public static String getStringFromInputStream(InputStream is) {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4 * 1024];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
        } catch (Exception e) {
        }
        return baos.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXmlString(Class<T> cls, String responseStr) {
        return fromXmlString(cls, responseStr, null);
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromXmlString(Class<T> cls, String responseStr, DateConverter converter) {
        T entity = null;
        XStream xStream = new XStream();
        if (converter != null) {
            xStream.registerConverter(converter);
        }
        xStream.processAnnotations(cls);
        Object object = xStream.fromXML(responseStr);
        entity = (T) object;
        return entity;
    }


    @SuppressWarnings("unchecked")
    public static <T> List<T> fromXml(Class<T> cls, InputStream is, String listAlias) {

        List<T> list = null;
        XStream xStream = new XStream();
        xStream.alias(listAlias, List.class);
        xStream.processAnnotations(cls);
        list = (List<T>) xStream.fromXML(is, list);
        return list;
    }

    public static <T> T fromXml(Class<T> cls, InputStream is, DateConverter converter) {
        String response = getStringFromInputStream(is);
        return fromXmlString(cls, response, converter);
    }

    public static <T> T fromXml(Class<T> cls, InputStream is) {
        String response = getStringFromInputStream(is);
        return fromXmlString(cls, response);
    }

    public static <T> String toXml(Object obj) {
        XStream xStream = new XStream();
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);
    }

    public static File toSignXmlFile(Object obj, String fileName) throws ZMParserException {
        String ret = toXml(obj);
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("\n");
        sb.append(ret);
        try {
            File file = new File(fileName);
            BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
            out.write(sb.toString());
            out.newLine();
            out.close();
            out = null;
            return file;
        } catch (Exception ex) {
            throw new ZMParserException(ex!=null?ex.getMessage():"未知错误");
        }
    }
}
