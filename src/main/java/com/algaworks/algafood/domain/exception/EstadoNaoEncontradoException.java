package com.algaworks.algafood.domain.exception;
import org.springframework.http.HttpStatusCode;


public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public EstadoNaoEncontradoException(String message) {
        super(message);
    }
}
