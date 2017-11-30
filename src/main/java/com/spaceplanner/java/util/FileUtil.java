package com.spaceplanner.java.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 28/07/15
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final int BUFFER_SIZE = 4096;


    public static void write(String filePath, InputStream inputStream) {
        logger.info("Writing file := "+ filePath);
        OutputStream outputStream = null;
        try {
            File file = new File(filePath);
            /*boolean isDirectoryCreated = */file.getParentFile().mkdirs();
            /*if (!file.exists()) {
                file.createNewFile();
            }*/
            outputStream = new FileOutputStream(file);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            logger.info("File has been written.");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Exception while writing file. :="+ e.getMessage());
        } finally {
            if (null != outputStream)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("Exception while closing output stream. :="+ e.getMessage());
                }
        }
    }
    
    public static InputStream read(String filePath) throws FileNotFoundException {
        File downloadFile = new File(filePath);
        return new FileInputStream(downloadFile);
    }
    
    public static void download(String filePath, OutputStream outputStream)throws IOException{
        File downloadFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(downloadFile); try{
            IOUtils.copy(inputStream, outputStream);
        }catch (Exception e){
            logger.error("Exception occure while tryign to download file at "+ filePath);
            logger.error(e.getMessage());
        }finally {
            inputStream.close();
            if(null!=outputStream)
                outputStream.close();
        }
    }
    
    public static void download(String contextPath, String filePath, OutputStream outputStream) throws IOException {
        System.out.println("Context Path = " + contextPath);
        // construct the complete absolute path of the file
        String fullPath = contextPath + filePath;
        File downloadFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(downloadFile);
        // get MIME type of the file
        String mimeType = null;//context.getMimeType(fullPath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        //System.out.println("MIME type: " + mimeType);
        // set content attributes for the response
        //response.setContentType(mimeType);
        //response.setContentLength((int) downloadFile.length());

        // set headers for the response
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                downloadFile.getName());
        //response.setHeader(headerKey, headerValue);

        try{
            IOUtils.copy(inputStream, outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            inputStream.close();
            if(null!=outputStream)
                outputStream.close();
        }

        /*byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

        } catch (IOException e) {
            logger.error("Unable to write file := "+ e.getMessage());
            throw e;
        }finally {
            inputStream.close();
            if(null!=outputStream)
                outputStream.close();
        }*/
    }
}
