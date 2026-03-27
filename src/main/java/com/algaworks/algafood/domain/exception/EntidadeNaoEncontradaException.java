package com.algaworks.algafood.domain.exception;



public abstract class EntidadeNaoEncontradaException extends NegocioException {

    public EntidadeNaoEncontradaException(String message) {
        super(message);
    }

    public EntidadeNaoEncontradaException(String message, Throwable cause) {
        super(message, cause);
    }
}
