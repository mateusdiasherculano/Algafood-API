package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    public static final String MSG_ESTADO_NAO_ENCONTRADO = "Não existe um cadastro de estado com esse id: %s";

    public static final String MSG_ESTADO_EM_USO = "Estado de id %d não pode ser removida, pois está em uso";

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado){
        return estadoRepository.save(estado);
    }

    public void excluir(Long id){

        Estado estado = buscarOuFalhar(id);

        try {
            estadoRepository.deleteById(estado.getId());

        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, id));
        }
    }

    public Estado buscarOuFalhar(Long id){
        return estadoRepository.findById(id)
                .orElseThrow(() -> new EstadoNaoEncontradoException(String.format(MSG_ESTADO_NAO_ENCONTRADO, id)));
    }
}
