package io.github.api.reservas.response;

import io.github.api.reservas.domain.Anuncio;
import io.github.api.reservas.domain.Reserva;
import io.github.api.reservas.domain.Usuario;
import io.github.api.reservas.request.CadastrarReservaRequest;
import io.github.api.reservas.service.AnuncioService;
import io.github.api.reservas.service.ReservaService;
import io.github.api.reservas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservaResponse {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final AnuncioService anuncioService;


    public InformacaoReservaResponse reservaResponse(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        final Usuario nomeSolicitante = usuarioService.buscaUsuarioPorId(cadastrarReservaRequest.getIdSolicitante());
        final Anuncio dadosAnuncio = anuncioService.buscaAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());
        final Reserva dadosReserva = reservaService.buscaReservaResponse(cadastrarReservaRequest);

       return InformacaoReservaResponse.builder()
                .idReserva(dadosReserva.getId())
                .solicitante(DadosSolicitanteResponse.builder()
                        .id(cadastrarReservaRequest.getIdSolicitante())
                        .nome(nomeSolicitante.getNome())
                        .build())
                .quantidadePessoas(dadosReserva.getQuantidadePessoas())
                .anuncio(DadosAnuncioResponse.builder()
                        .id(dadosAnuncio.getId())
                        .imovel(dadosAnuncio.getImovel())
                        .anunciante(dadosAnuncio.getAnunciante())
                        .formasAceitas(dadosAnuncio.getFormasAceitas())
                        .descricao(dadosAnuncio.getDescricao())
                        .build())
                .periodo(dadosReserva.getPeriodo())
                .pagamento(dadosReserva.getPagamento())
                .build();
    }
}
