package io.github.api.reservas.service;

import io.github.api.reservas.domain.Anuncio;
import io.github.api.reservas.domain.Imovel;
import io.github.api.reservas.domain.Usuario;
import io.github.api.reservas.exception.ConsultaIdInvalidoException;
import io.github.api.reservas.exception.ImovelJaContemAnuncioException;
import io.github.api.reservas.repository.AnuncioRepository;
import io.github.api.reservas.request.CadastrarAnuncioRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnuncioService {

    private final UsuarioService usuarioService;
    private final ImovelService imovelService;
    private final AnuncioRepository anuncioRepository;

    public Anuncio cadastraAnuncio(CadastrarAnuncioRequest cadastrarAnuncioRequest) throws Exception {
        final Usuario anunciante = usuarioService.buscaUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante());
        final Imovel imovel = imovelService.buscaImovelPorId(cadastrarAnuncioRequest.getIdImovel());
        final Long idImovel = cadastrarAnuncioRequest.getIdImovel();

        validaImovelComAnuncio(idImovel);

        Anuncio anuncio = Anuncio.builder()
                .tipoAnuncio(cadastrarAnuncioRequest.getTipoAnuncio())
                .imovel(imovel)
                .anunciante(anunciante)
                .valorDiaria(cadastrarAnuncioRequest.getValorDiaria())
                .formasAceitas(cadastrarAnuncioRequest.getFormasAceitas())
                .descricao(cadastrarAnuncioRequest.getDescricao())
                .build();

        anuncio.setAtivo(true);
        return anuncioRepository.save(anuncio);
    }

    public Page<Anuncio> listarAnuncios(Pageable pageable) {
        return anuncioRepository.findByAtivoTrue(pageable);
    }

    public Page<Anuncio> buscaAnuncioPorIdAnunciante(Pageable pageable, Long idAnunciante) {
        return anuncioRepository.findByAnunciante_IdAndAtivoTrue(pageable, idAnunciante);
    }

    public boolean validaImovelComAnuncio(Long idImovel) throws Exception {
       boolean imovelContemAnuncioTrue = anuncioRepository.existsByAtivoTrueAndImovel_Id(idImovel);

       if(imovelContemAnuncioTrue) {
           throw new ImovelJaContemAnuncioException(idImovel);
       }

       return true;
    }

    public void excluirAnuncio(Long idAnuncio) throws Exception {

        Anuncio anuncio = anuncioRepository.findById(idAnuncio)
                .orElseThrow(() -> new ConsultaIdInvalidoException("Anuncio", idAnuncio));
        anuncio.setAtivo(false);
        anuncioRepository.save(anuncio);
    }

    public Anuncio buscaAnuncioPorId(Long idAnuncio) throws Exception {
        return anuncioRepository.findById(idAnuncio)
                .orElseThrow(() -> new ConsultaIdInvalidoException("Anuncio", idAnuncio));
    }
}
