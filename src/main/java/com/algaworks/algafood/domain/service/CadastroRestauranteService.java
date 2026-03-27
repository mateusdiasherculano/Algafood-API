package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteService {

    public static final String MSG_RESTAURANTE_EM_USO = "Restaurante de id %d não pode ser removido, pois está em uso";
    public static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe cadastro de Restaurante com código %d";


    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante){
        Long cozinhaId = restaurante.getCozinha().getId();

        Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);


        restaurante.setCozinha(cozinha);

        return restauranteRepository.save(restaurante);
    }

    public void excluir(Long id){

        Restaurante restaurante = tentaBuscar(id);

        try{
            restauranteRepository.deleteById(restaurante.getId());

        }catch (DataIntegrityViolationException e){
            throw new EntidadeEmUsoException(String.format(MSG_RESTAURANTE_EM_USO, id));
        }
    }

    public Restaurante tentaBuscar(Long id){
        return restauranteRepository.findById(id).orElseThrow(()-> new RestauranteNaoEncontradoException(String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id)));
    }



}
