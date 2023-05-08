package io.github.api.reservas.request;

import io.github.api.reservas.domain.Periodo;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarReservaRequest {

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo idSolicitante.")
    private Long idSolicitante;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo idAnuncio.")
    private Long idAnuncio;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo periodo.")
    private Periodo periodo;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo quantidadePessoas.")
    private Integer quantidadePessoas;

}
