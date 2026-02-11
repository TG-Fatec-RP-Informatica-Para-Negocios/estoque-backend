package com.tg.estoque_saas.service;

import com.tg.estoque_saas.entity.Produto;
import com.tg.estoque_saas.entity.Venda;
import com.tg.estoque_saas.repository.ProdutoRepository;
import com.tg.estoque_saas.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Transactional
    public Venda registrarVenda(Long produtoId, Integer quantidade, LocalDateTime data, boolean vendaAntiga) {
        // Busca o produto
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Lógica de Estoque: Se NÃO for venda antiga, subtrai do saldo atual
        if (!vendaAntiga) {
            produto.setEstoqueAtual(produto.getEstoqueAtual() - quantidade);
        }

        // Atualiza a data da última venda no cadastro do produto
        produto.setDataUltimaVenda(data.toLocalDate());
        produtoRepository.save(produto);

        // Salva o registro na tabela de vendas
        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setQuantidade(quantidade);
        venda.setDataVenda(data);

        return vendaRepository.save(venda);
    }

    // Buscar todas as vendas de um produto específico
    public List<Venda> listarVendasPorProduto(Long produtoId) {
        return vendaRepository.findByProdutoId(produtoId);
    }
}