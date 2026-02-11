package com.tg.estoque_saas.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VendaRequest {
    private Long produtoId;
    private Integer quantidade;
    private LocalDateTime dataVenda;
    private boolean vendaAntiga; 
}