package net.eulerframework.web.core.base.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import net.eulerframework.web.core.base.RequestContextAccessor;
import net.eulerframework.web.core.base.response.HttpStatusResponse;
import net.eulerframework.web.core.base.response.Status;
import net.eulerframework.web.core.exception.BadRequestException;
import net.eulerframework.web.core.exception.IllegalParamException;
import net.eulerframework.web.core.exception.ResourceExistException;
import net.eulerframework.web.core.exception.ResourceNotFoundException;

public abstract class BaseController extends RequestContextAccessor {
    
    protected final Logger logger = LogManager.getLogger(this.getClass());
    
    protected void writeString(HttpServletResponse httpServletResponse, String str) throws IOException{
        httpServletResponse.getOutputStream().write(str.getBytes("UTF-8"));
    }
    
    /**  
     * 用于在程序发生{@link BadRequestException}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})   
    public Object badRequestException(BadRequestException e) {
        this.logger.error(e.getMessage(), e);
        return new HttpStatusResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }
    
    /**  
     * 用于在程序发生{@link ResourceExistException}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ResourceExistException.class})   
    public Object exception(ResourceExistException e) {
        e.printStackTrace();
        return new HttpStatusResponse(Status.RESOURCE_EXIST, e.getLocalizedMessage());
    }
    
    /**  
     * 用于在程序发生{@link IllegalParamException}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalParamException.class})   
    public Object illegalParamException(IllegalParamException e) {
        this.logger.error(e.getMessage(), e);
        return new HttpStatusResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }
    
    /**  
     * 用于在程序发生{@link IllegalArgumentException}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})   
    public Object illegalArgumentException(IllegalArgumentException e) {
        this.logger.error(e.getMessage(), e);
        return new HttpStatusResponse(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
    }
    
    /**  
     * 用于在程序发生{@link ResourceNotFoundException}异常时统一返回错误信息
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})   
    public Object resourceNotFoundException(ResourceNotFoundException e) {
        e.printStackTrace();
        return new HttpStatusResponse(HttpStatus.NOT_FOUND);
    }
    
    /**  
     * 用于在程序发生{@link AccessDeniedException}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})   
    public Object accessDeniedException(AccessDeniedException e) {
        return new HttpStatusResponse(HttpStatus.FORBIDDEN);
    }
    
    /**  
     * 用于在程序发生{@link BindException}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BindException.class})   
    public Object bindException(BindException e) {
        e.printStackTrace();
        List<ObjectError> errors = e.getAllErrors();
        List<String> errMsg = new ArrayList<>();
        for(ObjectError err : errors){
            if(FieldError.class.isAssignableFrom(err.getClass()))
                errMsg.add(((FieldError)err).getField()+ ": " + err.getDefaultMessage());
            else
                errMsg.add(err.getDefaultMessage());
        }
        return new HttpStatusResponse(Status.FIELD_VALID_FAILED, errMsg.toString());
    }
    
    /**  
     * 用于在程序发生{@link MissingServletRequestParameterException}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})   
    public Object missingServletRequestParameterException(MissingServletRequestParameterException e) {
        e.printStackTrace();
        return  new HttpStatusResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }
    
    /**  
     * 用于在程序发生{@link Exception}异常时统一返回错误信息 
     * @return  
     */  
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})   
    public Object exception(Exception e) {
        e.printStackTrace();
        return new HttpStatusResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
    }
}
