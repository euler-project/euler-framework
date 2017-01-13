package net.eulerframework.web.module.authentication.exception;

@SuppressWarnings("serial")
public class IncorrectPasswordException extends UserCheckException {

    @Override
    public String getViewInfo() {
        return "INCORRECT_PASSWORD";
    }


}