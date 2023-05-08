package io.github.api.reservas.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.api.reservas.domain.*;
import io.github.api.reservas.exception.ConsultaIdInvalidoException;
import io.github.api.reservas.exception.FormaPagamentoInvalidaException;
import io.github.api.reservas.exception.PagamentoInvalidoException;
import io.github.api.reservas.repository.ReservaRepository;
import io.github.api.reservas.request.CadastrarReservaRequest;
import io.github.api.reservas.validator.ValidatorReserva;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final UsuarioService usuarioService;
    private final AnuncioService anuncioService;
    private final ReservaRepository reservaRepository;

    public void criaReserva(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        final Usuario solicitante = usuarioService.buscaUsuarioPorId(cadastrarReservaRequest.getIdSolicitante());
        final Anuncio anuncio = anuncioService.buscaAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());

        ValidatorReserva validatorReserva = new ValidatorReserva(anuncioService, reservaRepository);

        validatorReserva.validaAnuncioAtivo(cadastrarReservaRequest);
        validatorReserva.validaSolicitante(cadastrarReservaRequest);
        validatorReserva.validaHoraReserva(cadastrarReservaRequest);
        validatorReserva.validaPeriodo(cadastrarReservaRequest);
        validatorReserva.validaReservaHotel(cadastrarReservaRequest);
        validatorReserva.validaReservaPousada(cadastrarReservaRequest);
        validatorReserva.validaSobreposicaoDatas(cadastrarReservaRequest);

        final Pagamento pagamento = new Pagamento(
                BigDecimal.valueOf(calculaValorTotal(cadastrarReservaRequest)),
                null,
                StatusPagamento.PENDENTE
        );

        final Reserva reserva = Reserva.builder()
                .solicitante(solicitante)
                .anuncio(anuncio)
                .periodo(cadastrarReservaRequest.getPeriodo())
                .quantidadePessoas(cadastrarReservaRequest.getQuantidadePessoas())
                .dataHoraReserva(LocalDateTime.now())
                .pagamento(pagamento)
                .build();

        reservaRepository.save(reserva);
    }

    public double calculaValorTotal(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        Integer diarias = cadastrarReservaRequest.getPeriodo().getDataHoraFinal().compareTo(cadastrarReservaRequest.getPeriodo().getDataHoraInicial());
        Double valorDiaria = anuncioService.buscaAnuncioPorId(cadastrarReservaRequest.getIdAnuncio()).getValorDiaria().doubleValue();

        return diarias * valorDiaria;
    }

    public Page<Reserva> buscaReservaPorIdSolicitante
            (Long idSolicitante,
             @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dataHoraFinal,
             @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dataHoraInicial,
             Pageable pageable) {

        if (dataHoraFinal != null && dataHoraInicial != null) {
            return reservaRepository.findByPeriodo_DataHoraInicialGreaterThanEqualAndPeriodo_DataHoraFinalLessThanEqualAndSolicitante_Id
                    (dataHoraInicial, dataHoraFinal, idSolicitante, pageable);
        }
        return reservaRepository.findBySolicitante_Id(idSolicitante, pageable);
    }

    public Page<Reserva> buscaReservaPorIdAnunciante(Long idAnunciante, Pageable pageable) {
        return reservaRepository.findByAnuncio_Anunciante_Id(idAnunciante, pageable);
    }

    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws Exception{
        boolean formaPagamentoAceita = reservaRepository.existsByIdAndAnuncio_FormasAceitas(idReserva, formaPagamento);
        final Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ConsultaIdInvalidoException("Reserva", idReserva));

        if (!formaPagamentoAceita) {
            throw new FormaPagamentoInvalidaException(formaPagamento, reserva.getAnuncio().getFormasAceitas());
        }
        if (!reserva.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)) {
            throw new PagamentoInvalidoException("pagamento", StatusPagamento.PENDENTE);
        }

        reserva.getPagamento().setFormaEscolhida(formaPagamento);
        reserva.getPagamento().setStatus(StatusPagamento.PAGO);
        reservaRepository.save(reserva);
    }

    public void cancelarReserva(Long idReserva) throws Exception{
        final Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ConsultaIdInvalidoException("Reserva", idReserva));

        if (!reserva.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)) {
            throw new PagamentoInvalidoException("cancelamento", StatusPagamento.PENDENTE);
        }

        reserva.getPagamento().setStatus(StatusPagamento.CANCELADO);
        reservaRepository.save(reserva);
    }

    public void estornarReserva(Long idReserva) throws Exception{
        final Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ConsultaIdInvalidoException("Reserva", idReserva));

        if (!reserva.getPagamento().getStatus().equals(StatusPagamento.PAGO)) {
            throw new PagamentoInvalidoException("estorno", StatusPagamento.PAGO);
        }

        reserva.getPagamento().setStatus(StatusPagamento.ESTORNADO);
        reserva.getPagamento().setFormaEscolhida(null);
        reservaRepository.save(reserva);
    }

    public Reserva buscaReservaResponse(CadastrarReservaRequest cadastrarReservaRequest) {
        return reservaRepository.findByPeriodo_DataHoraInicialEqualsAndPeriodo_DataHoraFinalEqualsAndAnuncio_Id
                (cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal(), cadastrarReservaRequest.getIdAnuncio());
    }
}
