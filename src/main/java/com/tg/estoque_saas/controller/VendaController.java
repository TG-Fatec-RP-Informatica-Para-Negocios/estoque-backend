package com.tg.estoque_saas.controller;

import com.tg.estoque_saas.dto.VendaRequest;
import com.tg.estoque_saas.entity.Venda;
import com.tg.estoque_saas.repository.VendaRepository;
import com.tg.estoque_saas.service.ProdutoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@CrossOrigin(origins = "*")
public class VendaController {

    @Autowired
    private ProdutoService produtoService; // Injetando o serviço
    
    @Autowired
    private VendaRepository repository;

    public List<Venda> listarTodas() {
        return repository.findAll();
    }

    @PostMapping
    public Venda registrarVenda(@RequestBody VendaRequest request) {
        // Se a data vier nula no JSON, usamos o 'agora'
        LocalDateTime dataFinal = (request.getDataVenda() == null)
                ? LocalDateTime.now()
                : request.getDataVenda();

        return produtoService.registrarVenda(
                request.getProdutoId(),
                request.getQuantidade(),
                dataFinal,
                request.isVendaAntiga() // Caso queira apenas cadastrar a venda antiga sem alterar o estoque
        );
    }

    // Busca vendas de um produto específico
    // URL: localhost:8080/api/vendas/produto/1
    @GetMapping("/produto/{produtoId}")
    public List<Venda> listarPorProduto(@PathVariable Long produtoId) {
        return produtoService.listarVendasPorProduto(produtoId);
    }

}