package com.lendico.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InstantDateFormatException extends IOException
{
    private static final long serialVersionUID = -8577515888254365386L;

    public InstantDateFormatException(String message)
    {
    	 super(message);
    }

}
