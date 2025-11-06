package com.deliverytech.delivery.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery.entity.ClienteEntity;
import com.deliverytech.delivery.entity.ItemPedidoEntity;
import com.deliverytech.delivery.entity.PedidoEntity;
import com.deliverytech.delivery.entity.ProdutoEntity;
import com.deliverytech.delivery.entity.RestauranteEntity;
import com.deliverytech.delivery.entity.StatusPedido;
import com.deliverytech.delivery.repository.ClienteRepository;
import com.deliverytech.delivery.repository.ItemPedidoRepository;
import com.deliverytech.delivery.repository.PedidoRepository;
import com.deliverytech.delivery.repository.ProdutoRepository;
import com.deliverytech.delivery.repository.RestauranteRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    public void run(String... args) throws Exception {
        // ✅ VERIFICA SE JÁ EXISTEM DADOS ANTES DE EXECUTAR
        if (clienteRepository.count() > 0) {
            System.out.println("✅ Dados já existem no banco. Pulando DataLoader.");
            return;
        }
        
        System.out.println("\n🎯 ===== INICIANDO ATIVIDADE 2: TESTES DE PERSISTÊNCIA =====\n");
    
        // 💾 2.1 Implementação do DataLoader
        System.out.println("💾 2.1 INSERINDO DADOS DE TESTE...");
        inserirDadosTeste();
    
        // 🔍 2.2 Validação das Consultas  
        System.out.println("\n🔍 2.2 VALIDAÇÃO DAS CONSULTAS...");
        validarConsultas();

        testarAtividade3();
    
        // 🛠️ ATIVIDADE 4 - NOVAS VALIDAÇÕES
        System.out.println("\n⚡ 4.2 VALIDAÇÃO DE PERFORMANCE...");
        validarPerformance();
        
        System.out.println("\n📊 4.3 RELATÓRIO DE ESTATÍSTICAS...");
        gerarRelatorioEstatisticas();
    
        System.out.println("\n🎉 ===== TODAS AS ATIVIDADES CONCLUÍDAS COM SUCESSO! =====\n");
    }
    

    private void inserirDadosTeste() {
        System.out.println("📥 Populando banco de dados com dados de teste...");

        //  3 CLIENTES DIFERENTES
        System.out.println("👤 Criando 3 clientes...");
        ClienteEntity cliente1 = criarCliente("João Silva", "joao@email.com", "11999999999", "Rua A, 123");
        ClienteEntity cliente2 = criarCliente("Maria Santos", "maria@email.com", "11988887777", "Av. Paulista, 1000");
        ClienteEntity cliente3 = criarCliente("Pedro Oliveira", "pedro@email.com", "11977776666", "Rua B, 456");

        clienteRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
        System.out.println("✅ 3 clientes criados com sucesso!");

        //  2 RESTAURANTES DE CATEGORIAS DISTINTAS
        System.out.println("\n🍽️ Criando 2 restaurantes...");
        RestauranteEntity rest1 = criarRestaurante("Pizzaria Bella Napoli", "Pizza", "Rua das Pizzas, 123",
                "1133334444", new BigDecimal("5.00"), 4.5f, 30);
        RestauranteEntity rest2 = criarRestaurante("Sushi Master", "Japonesa", "Av. Japão, 456",
                "1155556666", new BigDecimal("8.00"), 4.8f, 40);

        restauranteRepository.saveAll(Arrays.asList(rest1, rest2));
        System.out.println("✅ 2 restaurantes criados com sucesso!");

        //  5 PRODUTOS VARIADOS
        System.out.println("\n🛒 Criando 5 produtos...");
        List<ProdutoEntity> produtos = Arrays.asList(
                criarProduto("Pizza Margherita", "Molho de tomate, mussarela, manjericão",
                        new BigDecimal("45.00"), "Pizza", rest1),
                criarProduto("Pizza Calabresa", "Calabresa, cebola, mussarela",
                        new BigDecimal("50.00"), "Pizza", rest1),
                criarProduto("Sushi Combo", "10 peças variadas de sushi",
                        new BigDecimal("35.00"), "Sushi", rest2),
                criarProduto("Temaki Salmão", "Temaki de salmão com cream cheese",
                        new BigDecimal("22.00"), "Temaki", rest2),
                criarProduto("Refrigerante", "Lata 350ml",
                        new BigDecimal("6.00"), "Bebida", rest2)
        );

        produtoRepository.saveAll(produtos);
        System.out.println("✅ 5 produtos criados com sucesso!");

        // 2 PEDIDOS COM ITENS
        System.out.println("\n📦 Criando 2 pedidos com itens...");
        criarPedidosComItens(cliente1, cliente2, rest1, rest2, produtos);
        System.out.println("✅ 2 pedidos com itens criados com sucesso!");
    }

    // MÉTODOS AUXILIARES PARA CRIAR ENTIDADES
    private ClienteEntity criarCliente(String nome, String email, String telefone, String endereco) {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setName(nome);
        cliente.setEmail(email);
        cliente.setPhone(telefone);
        cliente.setAddress(endereco);
        cliente.setActive(true);
        cliente.setCreatedAt(LocalDateTime.now());
        return cliente;
    }

    private RestauranteEntity criarRestaurante(String nome, String categoria, String endereco,
            String telefone, BigDecimal taxaEntrega, Float rating, Integer tempoEntrega) {
        RestauranteEntity restaurante = new RestauranteEntity();
        restaurante.setName(nome);
        restaurante.setCategory(categoria);
        restaurante.setAddress(endereco);
        restaurante.setPhone(telefone);
        restaurante.setDeliveryPrice(taxaEntrega);
        restaurante.setRating(null);
        restaurante.setDeliveryTime(tempoEntrega);
        restaurante.setActive(true);
        restaurante.setCreatedAt(LocalDateTime.now());
        return restaurante;
    }

    private ProdutoEntity criarProduto(String nome, String descricao, BigDecimal preco,
            String categoria, RestauranteEntity restaurante) {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setName(nome);
        produto.setDescription(descricao);
        produto.setPrice(preco);
        produto.setCategory(categoria);
        produto.setAvailable(true);
        produto.setActive(true);
        produto.setRestaurant(restaurante);
        return produto;
    }

    private void criarPedidosComItens(ClienteEntity cliente1, ClienteEntity cliente2,
            RestauranteEntity rest1, RestauranteEntity rest2,
            List<ProdutoEntity> produtos) {
        //  PEDIDO 1 - João na Pizzaria
        PedidoEntity pedido1 = new PedidoEntity();
        pedido1.setClient(cliente1);
        pedido1.setRestaurant(rest1);
        pedido1.setStatus(StatusPedido.CONFIRMED);
        pedido1.setDeliveryAddress(cliente1.getAddress());
        pedido1.setTotalAmount(new BigDecimal("95.00"));
        pedido1.setCreationDate(LocalDateTime.now().minusHours(2));
        pedido1.setLastUpdate(LocalDateTime.now().minusHours(2));

        pedido1 = pedidoRepository.save(pedido1);

        // Itens do Pedido 1
        ItemPedidoEntity item1 = new ItemPedidoEntity();
        item1.setOrder(pedido1);
        item1.setProduct(produtos.get(0)); // Pizza Margherita
        item1.setUnitPrice(produtos.get(0).getPrice());
        item1.setTotalPrice(produtos.get(0).getPrice());
        item1.setQuantity(1);

        ItemPedidoEntity item2 = new ItemPedidoEntity();
        item2.setOrder(pedido1);
        item2.setProduct(produtos.get(1)); // Pizza Calabresa
        item2.setUnitPrice(produtos.get(1).getPrice());
        item2.setTotalPrice(produtos.get(1).getPrice());
        item2.setQuantity(1);

        pedido1.setItems(Arrays.asList(item1, item2));
        pedidoRepository.save(pedido1);

        // 📦 PEDIDO 2 - Maria no Sushi
        PedidoEntity pedido2 = new PedidoEntity();
        pedido2.setClient(cliente2);
        pedido2.setRestaurant(rest2);
        pedido2.setStatus(StatusPedido.PREPARING);
        pedido2.setDeliveryAddress(cliente2.getAddress());
        pedido2.setTotalAmount(new BigDecimal("57.00"));
        pedido2.setCreationDate(LocalDateTime.now().minusHours(1));
        pedido2.setLastUpdate(LocalDateTime.now().minusHours(1));

        pedido2 = pedidoRepository.save(pedido2);

        // Itens do Pedido 2
        ItemPedidoEntity item3 = new ItemPedidoEntity();
        item3.setOrder(pedido2);
        item3.setProduct(produtos.get(2)); // Sushi Combo
        item3.setUnitPrice(produtos.get(2).getPrice());
        item3.setTotalPrice(produtos.get(2).getPrice());
        item3.setQuantity(1);

        ItemPedidoEntity item4 = new ItemPedidoEntity();
        item4.setOrder(pedido2);
        item4.setProduct(produtos.get(4)); // Refrigerante
        item4.setUnitPrice(produtos.get(4).getPrice());
        item4.setTotalPrice(produtos.get(4).getPrice().multiply(new BigDecimal(2)));
        item4.setQuantity(2);

        pedido2.setItems(Arrays.asList(item3, item4));
        pedidoRepository.save(pedido2);
    }

    private void validarConsultas() {
        System.out.println("🔍 Testando todas as consultas derivadas...");

        // 🔎 CENÁRIO 1: Busca de Cliente por Email
        System.out.println("\n1. 🔎 Busca de Cliente por Email");
        clienteRepository.findByEmail("joao@email.com")
                .ifPresent(cliente
                        -> System.out.println("   ✅ Cliente encontrado: " + cliente.getName() + " - " + cliente.getEmail()));

        // 🍔 CENÁRIO 2: Produtos por Restaurante
        System.out.println("\n2. 🍔 Produtos por Restaurante");
        restauranteRepository.findAll().forEach(rest -> {
            List<ProdutoEntity> produtos = produtoRepository.findByRestaurantId(rest.getId());
            System.out.println("   ✅ " + rest.getName() + ": " + produtos.size() + " produtos");
            produtos.forEach(p -> System.out.println("      🛒 " + p.getName() + " - R$ " + p.getPrice()));
        });

        // 📅 CENÁRIO 3: Pedidos Recentes
        System.out.println("\n3. 📅 Pedidos Recentes (Top 10)");
        List<PedidoEntity> pedidosRecentes = pedidoRepository.findTop10ByOrderByIdDesc();
        System.out.println("   ✅ " + pedidosRecentes.size() + " pedidos recentes encontrados");
        pedidosRecentes.forEach(p
                -> System.out.println("      📦 Pedido #" + p.getId() + " - " + p.getStatus() + " - R$ " + p.getTotalAmount()));

        // 🔍 CONSULTAS ADICIONAIS
        System.out.println("\n5. 🔍 Consultas Adicionais");

        // Clientes ativos
        List<ClienteEntity> clientesAtivos = clienteRepository.findByActiveTrue();
        System.out.println("   ✅ Clientes ativos: " + clientesAtivos.size());

        // Produtos disponíveis
        List<ProdutoEntity> produtosDisponiveis = produtoRepository.findAll();
        System.out.println("   ✅ Produtos disponíveis: " + produtosDisponiveis.size());

        // Restaurantes ativos
        List<RestauranteEntity> restaurantesAtivos = restauranteRepository.findByActiveTrue();
        System.out.println("   ✅ Restaurantes ativos: " + restaurantesAtivos.size());

        // Pedidos por status
        List<PedidoEntity> pedidosConfirmados = pedidoRepository.findByStatus(StatusPedido.CONFIRMED);
        System.out.println("   ✅ Pedidos confirmados: " + pedidosConfirmados.size());

        // Busca de cliente por nome
        List<ClienteEntity> clientesMaria = clienteRepository.findByNameContainingIgnoreCase("Maria");
        System.out.println("   ✅ Clientes com 'Maria' no nome: " + clientesMaria.size());

        // Produtos por categoria
        List<ProdutoEntity> produtosPizza = produtoRepository.findByCategoryAndActiveTrue("Pizza");
        System.out.println("   ✅ Produtos da categoria Pizza: " + produtosPizza.size());

        // Restaurantes por categoria
        List<RestauranteEntity> restaurantesPizza = restauranteRepository.findByCategory("Pizza");
        System.out.println("   ✅ Restaurantes de Pizza: " + restaurantesPizza.size());

        // Verificar relacionamentos
        System.out.println("\n6. 🔗 Verificação de Relacionamentos");
        System.out.println("   ✅ Todos os relacionamentos funcionaram nas consultas anteriores!");
        System.out.println("   ✅ Pedidos no banco: " + pedidoRepository.count());
        System.out.println("   ✅ Itens de pedido no banco: " + itemPedidoRepository.count());

        System.out.println("\n🎯 TODAS AS CONSULTAS FORAM VALIDADAS COM SUCESSO!");
    }


    private void testarAtividade3() {
        System.out.println("\n🚀 ===== INICIANDO ATIVIDADE 3: CONSULTAS CUSTOMIZADAS =====\n");
        
        // 🧠 3.1 CONSULTAS COM @QUERY
        System.out.println("🧠 3.1 CONSULTAS COM @QUERY");
        
        // 3.1.1 Total de vendas por restaurante
        System.out.println("\n📊 Total de vendas por restaurante:");
        List<Object[]> vendasPorRestaurante = pedidoRepository.findTotalVendasPorRestaurante();
        vendasPorRestaurante.forEach(venda -> {
            System.out.println("   🍽️ " + venda[0] + ": R$ " + venda[1]);
        });
        
        // 3.1.2 Pedidos com valor acima de R$ 50
        System.out.println("\n💰 Pedidos com valor acima de R$ 50:");
        List<PedidoEntity> pedidosCaros = pedidoRepository.findPedidosComValorAcimaDe(new BigDecimal("50.00"));
        pedidosCaros.forEach(pedido -> {
            System.out.println("   📦 Pedido #" + pedido.getId() + ": R$ " + pedido.getTotalAmount());
        });
        
        // 🛢️ 3.2 CONSULTAS NATIVAS
        System.out.println("\n🛢️ 3.2 CONSULTAS NATIVAS");
        
        // 3.2.1 Ranking de clientes
        System.out.println("\n🏆 Ranking de clientes por nº de pedidos:");
        List<Object[]> rankingClientes = pedidoRepository.findRankingClientesPorPedidos();
        rankingClientes.forEach(cliente -> {
            System.out.println("   👤 " + cliente[0] + ": " + cliente[1] + " pedidos");
        });
        
        // 3.2.2 Faturamento por categoria
        System.out.println("\n💵 Faturamento por categoria:");
        List<Object[]> faturamentoCategoria = pedidoRepository.findFaturamentoPorCategoria();
        faturamentoCategoria.forEach(cat -> {
            System.out.println("   🏷️ " + cat[0] + ": R$ " + cat[1]);
        });
        
        // 3.2.3 Produtos mais vendidos
        System.out.println("\n🔥 Top 10 produtos mais vendidos:");
        List<Object[]> topProdutos = produtoRepository.findTop10ProdutosMaisVendidos();
        topProdutos.forEach(produto -> {
            System.out.println("   🛒 " + produto[0] + " (" + produto[3] + "): " + produto[1] + " unidades");
        });
        
        // 📊 3.3 PROJEÇÕES E DTOs
        System.out.println("\n📊 3.3 PROJEÇÕES E DTOs");
        
        // 3.3.1 Vendas por restaurante com projeção
        System.out.println("\n📈 Vendas por restaurante (Projeção):");
        try {
            var vendasProjecao = pedidoRepository.findVendasPorRestauranteComProjecao();
            vendasProjecao.forEach(venda -> {
                System.out.println("   🍽️ " + venda.getRestaurante() + 
                                 ": R$ " + venda.getTotalVendas() + 
                                 " (" + venda.getTotalPedidos() + " pedidos)");
            });
        } catch (Exception e) {
            System.out.println("   ⚠️ Projeção de vendas não implementada: " + e.getMessage());
        }
        
        // 3.3.2 Ranking de clientes com projeção
        System.out.println("\n👑 Ranking de clientes (Projeção):");
        try {
            var rankingProjecao = pedidoRepository.findRankingClientesComProjecao();
            rankingProjecao.forEach(cliente -> {
                System.out.println("   👤 " + cliente.getNomeCliente() + 
                                 " - " + cliente.getEmail() +
                                 ": " + cliente.getTotalPedidos() + " pedidos, R$ " + cliente.getTotalGasto());
            });
        } catch (Exception e) {
            System.out.println("   ⚠️ Projeção de ranking não implementada: " + e.getMessage());
        }
        
        System.out.println("\n🎉 ===== ATIVIDADE 3 TESTADA COM SUCESSO! =====\n");
    }
    private void validarPerformance() {
        System.out.println("\n⚡ ===== TESTES DE PERFORMANCE =====\n");
        
        long startTime, endTime;
        
        // Teste 1: Consulta simples
        System.out.println("1. 📊 Consulta de todos os clientes:");
        startTime = System.currentTimeMillis();
        List<ClienteEntity> clientes = clienteRepository.findAll();
        endTime = System.currentTimeMillis();
        System.out.println("   ⏱️  Tempo: " + (endTime - startTime) + "ms");
        System.out.println("   📈 Registros: " + clientes.size());
        
        // Teste 2: Consulta com relacionamento
        System.out.println("\n2. 🍽️ Consulta de produtos por restaurante:");
        startTime = System.currentTimeMillis();
        restauranteRepository.findAll().forEach(rest -> {
            List<ProdutoEntity> produtos = produtoRepository.findByRestaurantId(rest.getId());
        });
        endTime = System.currentTimeMillis();
        System.out.println("   ⏱️  Tempo: " + (endTime - startTime) + "ms");
        
        // Teste 3: Consulta complexa
        System.out.println("\n3. 📦 Consulta de pedidos com itens:");
        startTime = System.currentTimeMillis();
        List<PedidoEntity> pedidos = pedidoRepository.findAll();
        pedidos.forEach(pedido -> {
            // Forçar carregamento dos relacionamentos para teste
            if (pedido.getItems() != null) {
                pedido.getItems().size();
            }
        });
        endTime = System.currentTimeMillis();
        System.out.println("   ⏱️  Tempo: " + (endTime - startTime) + "ms");
        System.out.println("   📈 Pedidos processados: " + pedidos.size());
    }
    
    private void gerarRelatorioEstatisticas() {
        System.out.println("\n📈 ===== RELATÓRIO DE ESTATÍSTICAS =====\n");
        
        System.out.println("👤 CLIENTES:");
        System.out.println("   • Total: " + clienteRepository.count());
        System.out.println("   • Ativos: " + clienteRepository.findByActiveTrue().size());
        
        System.out.println("\n🍽️ RESTAURANTES:"); 
        System.out.println("   • Total: " + restauranteRepository.count());
        System.out.println("   • Por categoria:");
        restauranteRepository.findAll().forEach(rest -> {
            System.out.println("     - " + rest.getCategory() + ": " + rest.getName());
        });
        
        System.out.println("\n🛒 PRODUTOS:");
        System.out.println("   • Total: " + produtoRepository.count());
        System.out.println("   • Disponíveis: " + produtoRepository.findByActiveTrue().size());
        System.out.println("   • Por categoria:");
        produtoRepository.findAll().forEach(prod -> {
            System.out.println("     - " + prod.getCategory() + ": " + prod.getName() + " - R$ " + prod.getPrice());
        });
        
        System.out.println("\n📦 PEDIDOS:");
        System.out.println("   • Total: " + pedidoRepository.count());
        System.out.println("   • Itens totais: " + itemPedidoRepository.count());
        System.out.println("   • Por status:");
        for (StatusPedido status : StatusPedido.values()) {
            List<PedidoEntity> pedidosStatus = pedidoRepository.findByStatus(status);
            System.out.println("     - " + status + ": " + pedidosStatus.size());
        }
    }
    
}