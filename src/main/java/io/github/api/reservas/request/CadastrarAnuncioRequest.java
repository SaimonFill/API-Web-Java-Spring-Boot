package io.github.api.reservas.request;

import io.github.api.reservas.domain.FormaPagamento;
import io.github.api.reservas.domain.Imovel;
import io.github.api.reservas.domain.TipoAnuncio;
import io.github.api.reservas.domain.Usuario;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarAnuncioRequest {

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo idImovel.")
    private Long idImovel;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo idAnunciante.")
    private Long idAnunciante;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo tipoAnuncio.")
    private TipoAnuncio tipoAnuncio;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo valorDiaria.")
    private BigDecimal valorDiaria;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo formasAceitas.")
    private List<FormaPagamento> formasAceitas;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo descricao.")
    private String descricao;

    private Imovel imovel;

    private Usuario anunciante;

}
