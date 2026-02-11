package com.tg.estoque_saas.repository;

import com.tg.estoque_saas.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long>{

    // Criar buscas
    
}
