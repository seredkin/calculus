package com.futurice.seredkin.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by Anton on 17.11.2016.
 */
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
