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

    // Conectando o nosso motor matemático!
    @Autowired
    private CalculoEstoqueService calculoEstoqueService;

    // --- MÉTODOS BÁSICOS DE PRODUTO ---
    
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    // --- MÉTODOS DE VENDAS E ESTATÍSTICAS ---

    public List<Venda> listarTodasVendas() {
        return vendaRepository.findAll();
    }

    @Transactional
    public Venda registrarVenda(Long produtoId, Integer quantidade, LocalDateTime data, boolean vendaAntiga) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!vendaAntiga) {
            if (produto.getEstoqueAtual() < quantidade) {
                throw new RuntimeException("Estoque insuficiente!");
            }
            produto.setEstoqueAtual(produto.getEstoqueAtual() - quantidade);
        }

        produto.setDataUltimaVenda(data.toLocalDate());
        produtoRepository.save(produto);

        Venda venda = new Venda();
        venda.setProduto(produto);
        venda.setQuantidadeVendida(quantidade); 
        venda.setDataVenda(data.toLocalDate()); 

        // Salva a venda no banco de dados
        vendaRepository.save(venda);
        
        // 1. Busca todo o histórico de vendas desse produto
        List<Venda> historicoCompleto = vendaRepository.findByProdutoId(produtoId);
        
        // 2. Extrai só os números (as quantidades vendidas) para a matemática
        List<Integer> quantidadesVendidas = historicoCompleto.stream()
                .map(Venda::getQuantidadeVendida)
                .toList();
                
        // 3. Roda a fórmula de Slack e Ballou para atualizar o Ponto de Pedido e Estoque de Segurança
        calculoEstoqueService.calcularParametrosDeRessuprimento(produto, quantidadesVendidas);
        
        // 4. Salva o produto de novo com os indicadores matemáticos
        produtoRepository.save(produto);

        return venda;
    }

    public List<Venda> listarVendasPorProduto(Long produtoId) {
        return vendaRepository.findByProdutoId(produtoId);
    }

    // --- MÉTODOS DE EXCLUSÃO E SEGURANÇA ---

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

    @Transactional
    public void cancelarVenda(Long vendaId, boolean reporEstoque) {
        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (reporEstoque) {
            Produto produto = venda.getProduto();
            produto.setEstoqueAtual(produto.getEstoqueAtual() + venda.getQuantidadeVendida());
            produtoRepository.save(produto);
        }

        vendaRepository.delete(venda);
    }
}