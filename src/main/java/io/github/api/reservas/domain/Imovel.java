package io.github.api.reservas.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identificacao;

    @Enumerated(EnumType.STRING)
    private TipoImovel tipoImovel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "id_proprietario")
    private Usuario proprietario;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_imovel")
    private List<CaracteristicaImovel> caracteristicas;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean ativo = false;

    public Imovel(String identificacao, TipoImovel tipoImovel, Endereco endereco, Usuario proprietario, List<CaracteristicaImovel> caracteristicas) {
        this.identificacao = identificacao;
        this.tipoImovel = tipoImovel;
        this.endereco = endereco;
        this.proprietario = proprietario;
        this.caracteristicas = caracteristicas;
    }
}