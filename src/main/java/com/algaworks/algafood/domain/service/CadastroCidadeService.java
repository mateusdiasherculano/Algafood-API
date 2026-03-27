package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroCidadeService {

    public static final String MSG_CIDADE_NAO_ENCONTRADA = "Cidade com codigo %d não encontrada";
    public static final String MSG_ESTADO_EM_USO = "cidade de id %d não pode ser removida, pois está em uso";

    @Autowired
    private CadastroEstadoService cadastroEstadoService;
    @Autowired
    private CidadeRepository cidadeRepository;
    

    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();

        Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);

        cidade.setEstado(estado);

        return cidadeRepository.save(cidade);
    }

    public void excluir(Long id) {

        Cidade cidade = tentaBuscarCidade(id);

        try {
            cidadeRepository.deleteById(cidade.getId());

        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, id));

        }
    }

    public Cidade tentaBuscarCidade(Long id) {

        return cidadeRepository.findById(id)
                .orElseThrow(() -> new CidadeNaoEncontradaException(String.format(MSG_CIDADE_NAO_ENCONTRADA, id)));
    }

}
