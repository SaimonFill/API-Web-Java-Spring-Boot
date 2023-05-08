package io.github.api.reservas.response;

import io.github.api.reservas.domain.FormaPagamento;
import io.github.api.reservas.domain.Imovel;
import io.github.api.reservas.domain.Usuario;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DadosAnuncioResponse {

    private Long id;
    private Imovel imovel;
    private Usuario anunciante;
    private List<FormaPagamento> formasAceitas;
    private String descricao;

}
