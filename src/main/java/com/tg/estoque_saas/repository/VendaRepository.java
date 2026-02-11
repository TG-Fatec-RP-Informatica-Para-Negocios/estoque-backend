package com.tg.estoque_saas.repository;

import com.tg.estoque_saas.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long>{
    // Busca todas as vendas de um produto para calcular a média/desvio
    List<Venda> findByProdutoId(Long produtoId);
    
}
