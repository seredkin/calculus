package com.futurice.seredkin.controller;

import com.futurice.seredkin.api.CalculusException;
import com.futurice.seredkin.api.CalculusResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice @Slf4j
class ErrorHandler{

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus//default 500
    public void handleRuntimeEx(Exception ex) {
        log.warn(ex.getMessage());
    }

    @ExceptionHandler(CalculusException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleNotFoundEx(Exception ex) {
        log.debug(ex.getMessage());
        return new CalculusResult(ex.getMessage());
    }


}
