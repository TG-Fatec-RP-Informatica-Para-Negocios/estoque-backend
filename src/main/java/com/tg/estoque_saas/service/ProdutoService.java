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

    // --- MÉTODOS BÁSICOS DE PRODUTO (QUE FALTAVAM) ---
    
    // Listar todos os produtos
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Buscar produto por ID
    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    // Salvar/Cadastrar Produto
    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    // --- MÉTODOS DE VENDAS E ESTATÍSTICAS ---

    // Listar todas as vendas (geral)
    public List<Venda> listarTodasVendas() {
        return vendaRepository.findAll();
    }

    // Registrar uma venda (com lógica de estoque)
    @Transactional
    public Venda registrarVenda(Long produtoId, Integer quantidade, LocalDateTime data, boolean vendaAntiga) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!vendaAntiga) {
            // Se for venda atual, valida e baixa estoque
            if (produto.getEstoqueAtual() < quantidade) {
                throw new RuntimeException("Estoque insuficiente!");
            }
            produto.setEstoqueAtual(produto.getEstoqueAtual() - quantidade);
        }

        produto.setDataUltimaVenda(data.toLocalDate());
        produtoRepository.save(produto);

        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setQuantidade(quantidade);
        venda.setDataVenda(data);

        return vendaRepository.save(venda);
    }

    // Listar vendas de um produto específico
    public List<Venda> listarVendasPorProduto(Long produtoId) {
        return vendaRepository.findByProdutoId(produtoId);
    }

    // --- MÉTODOS DE EXCLUSÃO E SEGURANÇA ---

    // Deletar Produto (Com trava de segurança)
    @Transactional
    public void deletarProduto(Long id){
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }

        List<Venda> vendas = vendaRepository.findByProdutoId(id);

        if (!vendas.isEmpty()){
            throw new RuntimeException("BLOQUEADO: Não é possível excluir um produto que possui histórico de vendas. Isso afetaria os cálculos de previsão de demanda.");
        }
        
        produtoRepository.deleteById(id);
    }

    // Cancelar Venda (Com opção de estorno)
    @Transactional
    public void cancelarVenda(Long vendaId, boolean reporEstoque) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (reporEstoque) {
            Produto produto = venda.getProduto();
            produto.setEstoqueAtual(produto.getEstoqueAtual() + venda.getQuantidade());
            produtoRepository.save(produto);
        }

        vendaRepository.delete(venda);
    }
}