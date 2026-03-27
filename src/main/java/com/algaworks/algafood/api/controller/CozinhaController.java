package com.algaworks.algafood.api.controller;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cozinhas")
public class CozinhaController {

    private final CozinhaRepository cozinhaRepository;
    private final CadastroCozinhaService cadastroCozinhaService;

    @Autowired
    public CozinhaController(CozinhaRepository cozinhaRepository, CadastroCozinhaService cadastroCozinhaService) {
        this.cozinhaRepository = cozinhaRepository;
        this.cadastroCozinhaService = cadastroCozinhaService;
    }

    @GetMapping
    public ResponseEntity<List<Cozinha>> listar(){
       List<Cozinha> lista = cozinhaRepository.findAll();

        return ResponseEntity.ok().body(lista);
    }

    @GetMapping("/{id}")
    public Cozinha buscar(@PathVariable Long id){
        return cadastroCozinhaService.buscarOuFalhar(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody Cozinha cozinha) {
        return cadastroCozinhaService.salvar(cozinha);
    }

    @PutMapping("/{id}")
    public Cozinha atualizar(@PathVariable Long id, @RequestBody Cozinha cozinha){
        Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(id);

        BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

        return cadastroCozinhaService.salvar(cozinhaAtual);


    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id){
        cadastroCozinhaService.excluir(id);
    }
}
