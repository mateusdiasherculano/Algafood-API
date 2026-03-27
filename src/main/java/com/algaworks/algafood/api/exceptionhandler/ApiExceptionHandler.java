package com.algaworks.algafood.api.exceptionhandler;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ApiExceptionHandler  extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        if (body == null) {
            body = Problem.builder()
                 .title("Erro ao tentar processar os dados")
                 .status(statusCode.value())
                 .build();
        }else if (body instanceof String) {
            body = Problem.builder()
                  .title((String) body)
                  .status(statusCode.value())
                  .build();
        }


        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }


    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<?> handleEntidadeNaoEncontradoException(EntidadeNaoEncontradaException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        Problem problem =  Problem.builder().status(status.value())
                           .type(ProblemType.ENTIDADE_NAO_ENCONTRADA.getUri())
                           .title(ProblemType.ENTIDADE_NAO_ENCONTRADA.getTitle()).detail(ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request) {

        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {

        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    private void createProblemBuilder(HttpStatus status, String title, String detail) {}
}
