package io.github.api.reservas.response;

import io.github.api.reservas.domain.Pagamento;
import io.github.api.reservas.domain.Periodo;
import io.github.api.reservas.domain.*;
import lombok.*;

@Getter
@Setter
@Data
@Builder
public class InformacaoReservaResponse {

    private Long idReserva;
    private DadosSolicitanteResponse solicitante;
    private Integer quantidadePessoas;
    private DadosAnuncioResponse anuncio;
    private Periodo periodo;
    private Pagamento pagamento;

    public InformacaoReservaResponse(Long idReserva, DadosSolicitanteResponse solicitante, Integer quantidadePessoas, DadosAnuncioResponse anuncio, Periodo periodo, Pagamento pagamento) {
        this.idReserva = idReserva;
        this.solicitante = solicitante;
        this.quantidadePessoas = quantidadePessoas;
        this.anuncio = anuncio;
        this.periodo = periodo;
        this.pagamento = pagamento;
    }
}