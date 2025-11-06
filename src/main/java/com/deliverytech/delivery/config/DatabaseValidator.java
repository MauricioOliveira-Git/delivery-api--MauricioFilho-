package com.deliverytech.delivery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseValidator implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n🔍 ===== VALIDAÇÃO DA ESTRUTURA DO BANCO =====\n");
        
        validarTabelas();
        validarRelacionamentos();
        validarConstraints();
        
        System.out.println("\n✅ ===== VALIDAÇÃO CONCLUÍDA COM SUCESSO! =====\n");
    }

    private void validarTabelas() {
        System.out.println("📊 VALIDANDO TABELAS:");
        
        String[] tabelasEsperadas = {
            "CLIENTES", "RESTAURANTES", "PRODUTOS", 
            "PEDIDOS", "ITENS_PEDIDO"
        };
        
        for (String tabela : tabelasEsperadas) {
            try {
                jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + tabela, Integer.class
                );
                System.out.println("   ✅ Tabela " + tabela + " - OK");
            } catch (Exception e) {
                System.out.println("   ❌ Tabela " + tabela + " - FALHA: " + e.getMessage());
            }
        }
    }

    private void validarRelacionamentos() {
        System.out.println("\n🔗 VALIDANDO RELACIONAMENTOS:");
        
        // Verificar se as FK estão funcionando
        String[] consultasRelacionamentos = {
            "SELECT COUNT(*) FROM PEDIDOS p JOIN CLIENTES c ON p.client_id = c.id",
            "SELECT COUNT(*) FROM PEDIDOS p JOIN RESTAURANTES r ON p.restaurant_id = r.id",
            "SELECT COUNT(*) FROM ITENS_PEDIDO ip JOIN PEDIDOS ped ON ip.order_id = ped.id",
            "SELECT COUNT(*) FROM ITENS_PEDIDO ip JOIN PRODUTOS prod ON ip.product_id = prod.id",
            "SELECT COUNT(*) FROM PRODUTOS p JOIN RESTAURANTES r ON p.restaurant_id = r.id"
        };
        
        for (int i = 0; i < consultasRelacionamentos.length; i++) {
            try {
                int count = jdbcTemplate.queryForObject(consultasRelacionamentos[i], Integer.class);
                System.out.println("   ✅ Relacionamento " + (i+1) + " - " + count + " registros vinculados");
            } catch (Exception e) {
                System.out.println("   ❌ Relacionamento " + (i+1) + " - FALHA: " + e.getMessage());
            }
        }
    }

    
    private void validarConstraints() {
        System.out.println("\n⚡ VALIDANDO CONSTRAINTS:");
        
        // Array corrigido - separando tabela e coluna manualmente
        String[][] constraints = {
            {"CLIENTES", "email"},
            {"CLIENTES", "name"}, 
            {"RESTAURANTES", "name"},
            {"PRODUTOS", "name"},
            {"PEDIDOS", "status"}
        };
        
        for (String[] constraint : constraints) {
            try {
                String tabela = constraint[0];
                String coluna = constraint[1];
                
                Integer nulos = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + tabela + " WHERE " + coluna + " IS NULL", 
                    Integer.class
                );
                
                if (nulos == 0) {
                    System.out.println("   ✅ Constraint " + tabela + "." + coluna + " - OK");
                } else {
                    System.out.println("   ⚠️ Constraint " + tabela + "." + coluna + " - " + nulos + " valores nulos");
                }
            } catch (Exception e) {
                System.out.println("   ❌ Erro validando " + constraint[0] + "." + constraint[1] + ": " + e.getMessage());
            }
        }
    }
}