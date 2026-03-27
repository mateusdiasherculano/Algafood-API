package com.algaworks.algafood.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegocioException extends RuntimeException {

    public NegocioException(String message) {
         super(message);
    }

    public NegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
