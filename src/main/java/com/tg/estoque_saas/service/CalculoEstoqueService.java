package com.tg.estoque_saas.service;

import org.springframework.stereotype.Service;
import com.tg.estoque_saas.entity.Produto;
import java.util.List;

@Service
public class CalculoEstoqueService {

    // Fator Z fixado em 1.65 para um nível de serviço de 95%
    private static final double FATOR_Z = 1.65;

    public void calcularParametrosDeRessuprimento(Produto produto, List<Integer> historicoVendasDiarias){

        // Se o produto é novo e não tem vendas, não há como calcular estatística
        if (historicoVendasDiarias == null || historicoVendasDiarias.isEmpty()) {
            return;
        }

        // Passo 1: Descobrir o ritmo de vendas e variação
        double demandaMedia = calcularDemandaMedia(historicoVendasDiarias);
        // CORREÇÃO 1: Chamando o método certo e dando o nome certo para a variável
        double desvioPadrao = calcularDesvioPadrao(historicoVendasDiarias, demandaMedia); 

        // Lead Time (Tempo que o fornecedor leva para entregar o pedido)
        int leadTime = produto.getTempoEntregaDias();

        // Passo 2: A Fórmula de Ballou / Slack para calcular o estoque de segurança
        double calculoES = FATOR_Z * desvioPadrao * Math.sqrt(leadTime);

        // Passo 3: A Fórmula do Ponto de Pedido
        // PP = (Demanda Média Diária x Lead Time) + Estoque de Segurança
        double calculoPP = (demandaMedia * leadTime) + calculoES;

        // Passo 4: Atualizar o objeto Produto (Arredondando para cima)
        produto.setEstoqueSeguranca((int) Math.ceil(calculoES));
        // CORREÇÃO 2: Salvando o Ponto de Pedido que você calculou (verifique se na sua Entity é prontoPedido ou pontoPedido)
        produto.setProntoPedido((int) Math.ceil(calculoPP)); 
    }

    private double calcularDemandaMedia(List<Integer> vendas) {
        return vendas.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    private double calcularDesvioPadrao(List<Integer> vendas, double media) {
        double variancia = vendas.stream()
                .mapToDouble(venda -> Math.pow(venda - media, 2))
                .average()
                .orElse(0.0);
                
        // CORREÇÃO 3: Removido o return duplicado
        // O Desvio Padrão é a raiz quadrada da variância
        return Math.sqrt(variancia);
    }
}