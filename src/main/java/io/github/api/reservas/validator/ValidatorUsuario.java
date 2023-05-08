package io.github.api.reservas.validator;

import io.github.api.reservas.exception.CpfJaExisteException;
import io.github.api.reservas.exception.EmailJaExisteException;
import io.github.api.reservas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatorUsuario {

    private final UsuarioRepository usuarioRepository;

    public void validaCadastroUsuario(String email, String cpf) throws CpfJaExisteException, EmailJaExisteException {
        boolean emailDuplicado = usuarioRepository.existsByEmail(email);
        boolean cpfDuplicado = usuarioRepository.existsByCpf(cpf);

        if (emailDuplicado) {
            throw new EmailJaExisteException(email);
        }
        if (cpfDuplicado) {
            throw new CpfJaExisteException(cpf);
        }
    }
}
