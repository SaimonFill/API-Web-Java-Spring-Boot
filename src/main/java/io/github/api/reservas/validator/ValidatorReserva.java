package io.github.api.reservas.validator;

import io.github.api.reservas.domain.Anuncio;
import io.github.api.reservas.domain.StatusPagamento;
import io.github.api.reservas.domain.TipoImovel;
import io.github.api.reservas.exception.*;
import io.github.api.reservas.repository.ReservaRepository;
import io.github.api.reservas.request.CadastrarReservaRequest;
import io.github.api.reservas.service.AnuncioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidatorReserva {

    private final AnuncioService anuncioService;
    private final ReservaRepository reservaRepository;

    public void validaHoraReserva(CadastrarReservaRequest cadastrarReservaRequest) {
        LocalDateTime dataHoraInicial = cadastrarReservaRequest.getPeriodo().getDataHoraInicial();
        LocalDateTime dataHoraFinal = cadastrarReservaRequest.getPeriodo().getDataHoraFinal();

        if (dataHoraInicial.getHour() != 14 || dataHoraInicial.getMinute() != 00) {
            cadastrarReservaRequest.getPeriodo().setDataHoraInicial(LocalDateTime.of(
                    dataHoraInicial.getYear(),
                    dataHoraInicial.getMonth(),
                    dataHoraInicial.getDayOfMonth(),
                    14,
                    00
            ));
        }
        if (dataHoraFinal.getHour() != 12 || dataHoraFinal.getMinute() != 00) {
            cadastrarReservaRequest.getPeriodo().setDataHoraFinal(LocalDateTime.of(
                    dataHoraFinal.getYear(),
                    dataHoraFinal.getMonth(),
                    dataHoraFinal.getDayOfMonth(),
                    12,
                    00
            ));
        }
    }

    public void validaPeriodo(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        Integer diferencaPeriodo = cadastrarReservaRequest.getPeriodo().getDataHoraFinal().compareTo(cadastrarReservaRequest.getPeriodo().getDataHoraInicial());
        LocalDateTime dataHoraInicial = cadastrarReservaRequest.getPeriodo().getDataHoraInicial();
        LocalDateTime dataHoraFinal = cadastrarReservaRequest.getPeriodo().getDataHoraFinal();

        if (dataHoraFinal.getDayOfMonth() == dataHoraInicial.getDayOfMonth()) {
            throw new MinimoDiariasException();
        }
        if (diferencaPeriodo < 0) {
            throw new DataFinalMenorQueInicialException();
        }
    }

    public void validaSolicitante(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        Long idSolicitante = cadastrarReservaRequest.getIdSolicitante();
        final Anuncio anuncio = anuncioService.buscaAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());
        Long idAnunciante = anuncio.getAnunciante().getId() ;

        if (idAnunciante == idSolicitante) {
            throw new SolicitanteIgualAnuncianteException();
        }
    }

    public void validaReservaHotel(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        final Anuncio tipoImovel = anuncioService.buscaAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());
        boolean isHotel = tipoImovel.getImovel().getTipoImovel().equals(TipoImovel.HOTEL);
        Integer quantidadePessoaas = cadastrarReservaRequest.getQuantidadePessoas();

        if (isHotel && quantidadePessoaas < 2) {
            throw new MinimoPessoasHotelException();
        }
    }

    public void validaReservaPousada(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        final Anuncio tipoImovel = anuncioService.buscaAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());
        boolean isPousada = tipoImovel.getImovel().getTipoImovel().equals(TipoImovel.POUSADA);
        Integer diarias = cadastrarReservaRequest.getPeriodo().getDataHoraFinal().compareTo(cadastrarReservaRequest.getPeriodo().getDataHoraInicial());

        if (isPousada && diarias < 5) {
            throw new MinimoDiariasPousadaException();
        }
    }

    public void validaSobreposicaoDatas(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        boolean reservasAtivas = reservaRepository.existsByPagamento_StatusEqualsOrPagamento_StatusEquals
                (StatusPagamento.PAGO, StatusPagamento.PENDENTE);

        boolean sobreposicaoDataInicio = reservaRepository.existsByPeriodo_DataHoraFinalGreaterThanAndAnuncio_Id
                (cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getIdAnuncio());

        boolean sobreposicaoDataFinal = reservaRepository.existsByPeriodo_DataHoraInicialLessThanAndAnuncio_Id
                (cadastrarReservaRequest.getPeriodo().getDataHoraFinal(), cadastrarReservaRequest.getIdAnuncio());

        if (reservasAtivas) {
            if (sobreposicaoDataInicio && sobreposicaoDataFinal) {
                throw new SobreposicaoDatasException();
            }
        }
    }

    public void validaAnuncioAtivo(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        final Anuncio anuncio = anuncioService.buscaAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());

        if (!anuncio.isAtivo()) {
            throw new ConsultaIdInvalidoException("Anuncio", cadastrarReservaRequest.getIdAnuncio());
        }
    }
}