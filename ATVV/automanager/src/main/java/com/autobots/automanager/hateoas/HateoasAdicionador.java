package com.autobots.automanager.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteApiController;
import com.autobots.automanager.controles.ControleCliente;
import com.autobots.automanager.controles.ControleEmpresa;
import com.autobots.automanager.controles.ControleMercadoria;
import com.autobots.automanager.controles.ControleServico;
import com.autobots.automanager.controles.ControleUsuario;
import com.autobots.automanager.controles.ControleVenda;
import com.autobots.automanager.controles.FuncionarioApiController;
import com.autobots.automanager.controles.ProdutoApiController;
import com.autobots.automanager.controles.VendaApiController;
import com.autobots.automanager.controles.VeiculoApiController;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.entidades.Veiculo;

import java.util.List;
import java.util.Map;

@Component
public class HateoasAdicionador {

    
    private static final Map<Class<?>, String> RECURSO_MAPPING = Map.ofEntries(
        Map.entry(ControleEmpresa.class, "empresas"),
        Map.entry(ControleServico.class, "servicos"),
        Map.entry(ControleMercadoria.class, "mercadorias"),
        Map.entry(ControleUsuario.class, "usuarios"),
        Map.entry(ControleVenda.class, "vendas"),
        Map.entry(ControleCliente.class, "clientes"),
        Map.entry(ProdutoApiController.class, "api/produtos"),
        Map.entry(ClienteApiController.class, "api/clientes"),
        Map.entry(FuncionarioApiController.class, "api/funcionarios"),
        Map.entry(VendaApiController.class, "api/vendas"),
        Map.entry(VeiculoApiController.class, "api/veiculos")
    );

    
    private static final Map<Class<?>, Class<?>> ENTIDADE_CONTROLADOR_MAPPING = Map.of(
        Empresa.class, ControleEmpresa.class,
        Servico.class, ControleServico.class,
        Mercadoria.class, ControleMercadoria.class,
        Usuario.class, ControleUsuario.class,
        Cliente.class, ControleCliente.class,
        Venda.class, ControleVenda.class,
        Veiculo.class, ControleCliente.class 
    );

    public void adicionarLink(Object objeto, Class<?> controlador, String rel) {
        if (objeto instanceof List<?>) {
            ((List<?>) objeto).forEach(item -> adicionarLinkIndividual(item, controlador, rel));
        } else {
            adicionarLinkIndividual(objeto, controlador, rel);
        }
    }

    private void adicionarLinkIndividual(Object objeto, Class<?> controlador, String rel) {
        try {
            Long id = (Long) objeto.getClass().getMethod("getId").invoke(objeto);
            String nomeRecurso = RECURSO_MAPPING.getOrDefault(controlador, controlador.getSimpleName().replace("Controle", "").toLowerCase());

            
            Class<?> controladorEntidade = ENTIDADE_CONTROLADOR_MAPPING.getOrDefault(objeto.getClass(), controlador);

           
            Link linkSelf;
            if (controladorEntidade == ControleEmpresa.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ControleEmpresa.class).buscarEmpresa(id)).withRel(rel);
            } else if (controladorEntidade == ControleServico.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ControleServico.class).buscarServico(id)).withRel(rel);
            } else if (controladorEntidade == ControleMercadoria.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ControleMercadoria.class).buscarMercadoria(id)).withRel(rel);
            } else if (controladorEntidade == ControleUsuario.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ControleUsuario.class).buscarUsuario(id)).withRel(rel);
            } else if (controladorEntidade == ControleVenda.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ControleVenda.class).listarTodasVendas()).slash(id).withRel(rel);
            } else if (controladorEntidade == ControleCliente.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ControleCliente.class).buscarCliente(id)).withRel(rel);
            } else if (controlador == ProdutoApiController.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ProdutoApiController.class).obterProdutosPorEmpresa(id)).withRel(rel);
            } else if (controlador == ClienteApiController.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClienteApiController.class).obterClientesPorEmpresa(id)).withRel(rel);
            } else if (controlador == FuncionarioApiController.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(FuncionarioApiController.class).obterFuncionariosPorEmpresa(id)).withRel(rel);
            } else if (controlador == VendaApiController.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaApiController.class).obterVendasPorPeriodo(id, null, null)).withRel(rel);
            } else if (controlador == VeiculoApiController.class) {
                linkSelf = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoApiController.class).obterVeiculosPorEmpresa(id)).withRel(rel);
            } else {
                linkSelf = Link.of(String.format("http://localhost:8080/%s/%d", nomeRecurso, id)).withRel(rel);
            }

            objeto.getClass().getMethod("add", Link.class).invoke(objeto, linkSelf);

            
            Link linkTodos = Link.of(String.format("http://localhost:8080/%s", nomeRecurso)).withRel("todos_" + nomeRecurso);
            objeto.getClass().getMethod("add", Link.class).invoke(objeto, linkTodos);

            
            adicionarLinksAninhados(objeto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void adicionarLinksAninhados(Object objeto) {
        try {
            if (objeto instanceof Empresa empresa) {
                empresa.getServicos().forEach(servico -> adicionarLinkIndividual(servico, ControleServico.class, "self"));
                empresa.getMercadorias().forEach(mercadoria -> adicionarLinkIndividual(mercadoria, ControleMercadoria.class, "self"));
                empresa.getUsuarios().forEach(usuario -> adicionarLinkIndividual(usuario, usuario instanceof Cliente ? ControleCliente.class : ControleUsuario.class, "self"));
                empresa.getVendas().forEach(venda -> adicionarLinkIndividual(venda, ControleVenda.class, "self"));
            } else if (objeto instanceof Venda venda) {
                venda.getServicos().forEach(servico -> adicionarLinkIndividual(servico, ControleServico.class, "self"));
                venda.getMercadorias().forEach(mercadoria -> adicionarLinkIndividual(mercadoria, ControleMercadoria.class, "self"));
                if (venda.getVeiculo() != null) {
                    adicionarLinkIndividual(venda.getVeiculo(), ControleCliente.class, "self"); 
                }
                if (venda.getCliente() != null) {
                    adicionarLinkIndividual(venda.getCliente(), ControleCliente.class, "self");
                }
                if (venda.getFuncionario() != null) {
                    adicionarLinkIndividual(venda.getFuncionario(), ControleUsuario.class, "self");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}