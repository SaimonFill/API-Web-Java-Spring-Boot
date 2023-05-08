package io.github.api.reservas.controller;

import io.github.api.reservas.domain.FormaPagamento;
import io.github.api.reservas.domain.Reserva;
import io.github.api.reservas.request.CadastrarReservaRequest;
import io.github.api.reservas.response.InformacaoReservaResponse;
import io.github.api.reservas.response.ReservaResponse;
import io.github.api.reservas.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    private final ReservaResponse informacaoReservaResponse;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InformacaoReservaResponse criaReserva(@RequestBody @Valid CadastrarReservaRequest cadastrarReservaRequest) throws Exception {
        reservaService.criaReserva(cadastrarReservaRequest);
        return informacaoReservaResponse.reservaResponse(cadastrarReservaRequest);
    }

    @GetMapping(path = "/solicitantes/{idSolicitante}")
    public Page<Reserva> buscaReservaPorIdSolicitante
            (@PathVariable Long idSolicitante,
             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dataHoraFinal,
             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dataHoraInicial,
             @PageableDefault(sort = "periodo", direction = Sort.Direction.DESC) Pageable pageable) {
        return reservaService.buscaReservaPorIdSolicitante(idSolicitante, dataHoraFinal, dataHoraInicial, pageable);
    }

    @GetMapping(path = "/anuncios/anunciantes/{idAnunciante}")
    public Page<Reserva> buscaReservaPorIdAnunciante(
            @PathVariable Long idAnunciante,
            @PageableDefault(sort = "periodo", direction = Sort.Direction.DESC) Pageable pageable) {
        return reservaService.buscaReservaPorIdAnunciante(idAnunciante, pageable);
    }

    @PutMapping(path = "{idReserva}/pagamentos")
    public void pagarReserva(@PathVariable Long idReserva, @RequestBody FormaPagamento formaPagamento) throws Exception {
        reservaService.pagarReserva(idReserva, formaPagamento);
    }

    @PutMapping(path = "{idReserva}/pagamentos/cancelar")
    public void cancelarReserva(@PathVariable Long idReserva) throws Exception {
        reservaService.cancelarReserva(idReserva);
    }

    @PutMapping(path = "{idReserva}/pagamentos/estornar")
    public void estornarReserva(@PathVariable Long idReserva) throws Exception {
        reservaService.estornarReserva(idReserva);
    }
}
