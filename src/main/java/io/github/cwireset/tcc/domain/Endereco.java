package io.github.cwireset.tcc.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo cep.")
    @Pattern(regexp = "[0-9]{5}-[0-9]{3}", message = "O CEP deve ser informado no formato 99999-999.")
    private String cep;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo logradouro.")
    private String logradouro;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo numero.")
    private String numero;

    private String complemento;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo bairro.")
    private String bairro;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo cidade.")
    private String cidade;

    @NotBlank(message = "Campo obrigatório não informado. Favor informar o campo estado.")
    private String estado;

}
