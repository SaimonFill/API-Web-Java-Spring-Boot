package io.github.api.reservas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConsultaIdInvalidoException extends Exception {
    public ConsultaIdInvalidoException(String entidade, Long id) {
        super(String.format("Nenhum(a) %s com Id com o valor '%d' foi encontrado.", entidade, id));
    }
}