package com.tg.estoque_saas.controller;

import com.tg.estoque_saas.entity.Produto;
import com.tg.estoque_saas.repository.ProdutoRepository;
import com.tg.estoque_saas.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    // Listar (GET) - localhost:8080/api/produtos
    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

    // Cadastrar (POST) - Aqui você envia o JSON
    @PostMapping
    public Produto cadastrar(@RequestBody Produto produto) {
        // colocar a lógica dos cálculos
        return produtoService.salvar(produto);
    }

    // Buscar por ID GET) - localhost:8080/api/produtos/(numero do ID)
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    // Deletar (DELETE) - localhost:8080/api/produtos/(numero do ID)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Se tiver venda bloqueia
        produtoService.deletarProduto(id);

        // Retorna 204 No Content se der certo
        return ResponseEntity.noContent().build();
    }
}