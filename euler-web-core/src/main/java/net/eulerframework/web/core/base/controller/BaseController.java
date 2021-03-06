package net.eulerframework.web.core.base.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.eulerframework.common.util.MIMEUtils;
import net.eulerframework.common.util.MIMEUtils.MIME;
import net.eulerframework.common.util.StringUtils;
import net.eulerframework.common.util.io.file.FileUtils;
import net.eulerframework.common.util.io.file.SimpleFileIOUtils;
import net.eulerframework.web.core.base.WebContextAccessable;

public abstract class BaseController extends WebContextAccessable {
    
    @Resource private ObjectMapper objectMapper;
    
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
    
    protected void writeString(String string) throws IOException{
        this.getResponse().getOutputStream().write(string.getBytes("UTF-8"));
    }
    
    protected void writeFile(String fileName, File file) throws FileNotFoundException, IOException {
        HttpServletResponse response = this.getResponse();
        String extension = FileUtils.extractFileExtension(fileName);
        MIME mime;
        if(StringUtils.hasText(extension)) {
            mime = MIMEUtils.getMIME(extension);
        } else {
            mime = MIMEUtils.getDefaultMIME();
        }
        this.getResponse().setHeader("Content-Type", mime.getContentType());
        response.setHeader("Content-Disposition", mime.getContentDisposition() + 
                ";fileName=\"" + new String(fileName.getBytes("utf-8"), "ISO8859-1") + "\"");
        response.setHeader("Content-Length", String.valueOf(file.length()));
        SimpleFileIOUtils.readFileToOutputStream(file, response.getOutputStream(), 2048);
    }
    
//    protected void setNoCacheHeader() {
//        HttpServletResponse response = this.getResponse();
//        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Date", new Date().getTime());
//        response.setIntHeader("Expires", 0);
//    }
}
