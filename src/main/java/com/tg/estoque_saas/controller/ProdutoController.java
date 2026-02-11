package com.tg.estoque_saas.controller;

import com.tg.estoque_saas.entity.Produto;
import com.tg.estoque_saas.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repository;

    // Listar (GET) - localhost:8080/api/produtos
    @GetMapping
    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    // Cadastrar (POST) - Aqui você envia o JSON
    @PostMapping
    public Produto cadastrar(@RequestBody Produto produto) {
        // colocar a lógica dos cálculos
        return repository.save(produto);
    }

    // Buscae por ID(GET) - localhost:8080/api/produtos/(numero do ID)
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }
}