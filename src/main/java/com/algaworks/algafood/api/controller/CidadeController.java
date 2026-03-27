package com.algaworks.algafood.api.controller;


import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private CadastroCidadeService cadastroCidadeService;

    @GetMapping
    public ResponseEntity<List<Cidade>> listar(){
        List<Cidade> cidades = cidadeRepository.findAll();

        return ResponseEntity.status(HttpStatus.CREATED).body(cidades);
    }

    @GetMapping("/{id}")
    public Cidade buscar(@PathVariable Long id){
        return cadastroCidadeService.tentaBuscarCidade(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade adicionar(@RequestBody Cidade cidade) {

        try {
          return cadastroCidadeService.salvar(cidade);
        }catch (EstadoNaoEncontradoException ex) {
            throw new NegocioException(ex.getMessage());
        }

    }

    @PutMapping("/{id}")
    public Cidade atualizar(@PathVariable Long id, @RequestBody Cidade cidade) {

        try {
            Cidade cidadeAtual = cadastroCidadeService.tentaBuscarCidade(id);
            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            return cadastroCidadeService.salvar(cidadeAtual);
        }catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){
        cadastroCidadeService.excluir(id);
    }
}
