package com.deliverytech.delivery.dto;

import java.math.BigDecimal;

public class RelatorioProjection {

    public interface VendasPorRestaurante {

        String getRestaurante();

        BigDecimal getTotalVendas();

        Long getTotalPedidos();
    }

    public interface RankingClientes {

        String getNomeCliente();

        String getEmail();

        Long getTotalPedidos();

        BigDecimal getTotalGasto();
    }

    public interface ProdutosMaisVendidos {

        String getNomeProduto();

        String getCategoria();

        Long getTotalVendido();

        BigDecimal getFaturamentoTotal();
    }
}
