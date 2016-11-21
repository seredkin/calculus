package com.futurice.seredkin.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @NoArgsConstructor
public class CalculusResult {

    private boolean error;
    private String message;
    private BigDecimal result;

    public CalculusResult(BigDecimal result){
        this.result = result;
    }

    public CalculusResult(String errorMessage){
        this.error = true;
        this.message = errorMessage;
    }

}
