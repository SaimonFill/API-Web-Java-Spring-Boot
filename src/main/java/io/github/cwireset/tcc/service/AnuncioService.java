package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.Anuncio;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;
import io.github.cwireset.tcc.exception.ConsultaIdInvalidoException;
import io.github.cwireset.tcc.exception.ImovelJaContemAnuncioException;
import io.github.cwireset.tcc.repository.AnuncioRepository;
import io.github.cwireset.tcc.request.CadastrarAnuncioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnuncioService {

    private UsuarioService usuarioService;
    private ImovelService imovelService;
    private AnuncioRepository anuncioRepository;

    @Autowired
    public AnuncioService(UsuarioService usuarioService, ImovelService imovelService, AnuncioRepository anuncioRepository) {
        this.usuarioService = usuarioService;
        this.imovelService = imovelService;
        this.anuncioRepository = anuncioRepository;
    }

    public Anuncio cadastraAnuncio(CadastrarAnuncioRequest cadastrarAnuncioRequest) throws Exception {
        final Usuario anunciante = usuarioService.buscaUsuarioPorId(cadastrarAnuncioRequest.getIdAnunciante());
        final Imovel imovel = imovelService.buscaImovelPorId(cadastrarAnuncioRequest.getIdImovel());
        final Long idImovel = cadastrarAnuncioRequest.getIdImovel();

        if (validaImovelComAnuncio(idImovel)) {
            throw new ImovelJaContemAnuncioException(idImovel);
        }

        final Anuncio anuncio = new Anuncio(
                cadastrarAnuncioRequest.getTipoAnuncio(),
                imovel,
                anunciante,
                cadastrarAnuncioRequest.getValorDiaria(),
                cadastrarAnuncioRequest.getFormasAceitas(),
                cadastrarAnuncioRequest.getDescricao()
        );

        anuncio.setAtivo(true);
        anuncioRepository.save(anuncio);
        return anuncio;
    }

    public Page<Anuncio> listarAnuncios(Pageable pageable) {
        return anuncioRepository.findByAtivoTrue(pageable);
    }

    public Page<Anuncio> buscaAnuncioPorIdAnunciante(Pageable pageable, Long idAnunciante) {
        return anuncioRepository.findByAnunciante_IdAndAtivoTrue(pageable, idAnunciante);
    }

    public boolean validaImovelComAnuncio(Long idImovel) {
        return anuncioRepository.existsByImovel_Id(idImovel);
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
