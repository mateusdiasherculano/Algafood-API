package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/restaurantes")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;


    @GetMapping
    public List<Restaurante> listarRestaurantes() {
        return restauranteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Restaurante buscar(@PathVariable Long id) {
       return cadastroRestauranteService.tentaBuscar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar(@RequestBody Restaurante restaurante) {

        try {
            return cadastroRestauranteService.salvar(restaurante);
        }catch (EntidadeNaoEncontradaException ex) {
           throw new NegocioException(ex.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Restaurante atualizar(@PathVariable Long id, @RequestBody Restaurante restaurante) {

        Restaurante restauranteAtual = cadastroRestauranteService.tentaBuscar(id);
        BeanUtils.copyProperties(restaurante, restauranteAtual, "id", "formasPagamento", "endereco", "dataCadastro");

        try {
            return cadastroRestauranteService.salvar(restauranteAtual);

        }catch(EntidadeNaoEncontradaException e){
            throw new NegocioException(e.getMessage());
        }
    }

    @PatchMapping("/{restauranteId}")
    public Restaurante atualizarParcial(@PathVariable Long restauranteId, @RequestBody Map<String, Object> campos) {

        Restaurante restauranteAtual = cadastroRestauranteService.tentaBuscar(restauranteId);
        merge(campos, restauranteAtual);

        return atualizar(restauranteId, restauranteAtual);
    }


    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);

            assert field != null;
            field.setAccessible(true);

            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);


            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {

        Restaurante restaurante = cadastroRestauranteService.tentaBuscar(id);

        try{
         cadastroRestauranteService.excluir(restaurante.getId());

        }catch (EntidadeEmUsoException e){
           throw new NegocioException(e.getMessage());
        }

    }
}
