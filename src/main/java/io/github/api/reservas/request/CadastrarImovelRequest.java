package io.github.api.reservas.request;

import io.github.api.reservas.domain.CaracteristicaImovel;
import io.github.api.reservas.domain.Endereco;
import io.github.api.reservas.domain.TipoImovel;
import io.github.api.reservas.domain.Usuario;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastrarImovelRequest {

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo identificacao.")
    private String identificacao;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo tipoImovel.")
    private TipoImovel tipoImovel;

    @Valid
    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo endereco.")
    private Endereco endereco;

    private Usuario proprietario;

    private List<CaracteristicaImovel> caracteristicas;

    @NotNull(message = "Campo obrigatório não informado. Favor informar o campo idProprietario.")
    private Long idProprietario;

}
