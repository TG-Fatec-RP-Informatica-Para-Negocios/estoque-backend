package com.tg.estoque_saas.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; 

    private String descricao;

    private Double precoCusto; 

    private Double precoVenda; 

    private Integer estoqueAtual;

    private Integer loteMinimo;

    private Integer tempoEntregaDias;
     
    private Integer estoqueSeguranca;

    private Integer prontoPedido; 

    private LocalDate dataUltimaVenda; 
}