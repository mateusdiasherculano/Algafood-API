package com.algaworks.algafood.domain.exception;
import org.springframework.http.HttpStatusCode;


public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {


    public CidadeNaoEncontradaException(String message) {
        super(message);
    }
}
