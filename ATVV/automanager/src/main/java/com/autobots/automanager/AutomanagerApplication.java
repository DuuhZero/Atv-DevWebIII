package com.autobots.automanager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    @Autowired
    private RepositorioEmpresa repositorioEmpresa;

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }

    // Decidi criar alguns dados estaticos para facilitar no uso das rotas prof, mas
    // recomendo usar e criar alguns dados usando de exemplo o readme

    // no momento so tem um unico usuario, o administrador, e uma unica empresa, a
    // AutoManager Ltda

    // para logar esta o tutorial usando este usuario criado, la no readme
    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

        Date dataCadastro = new Date();

        
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("AutoManager Ltda");
        empresa.setNomeFantasia("AutoManager");
        empresa.setCadastro(new Date());

       
        Endereco endereco = new Endereco();
        endereco.setEstado("SP");
        endereco.setCidade("São Paulo");
        endereco.setBairro("Centro");
        endereco.setRua("Av. Paulista");
        endereco.setNumero("1000");
        endereco.setCodigoPostal("01310-100");
        empresa.setEndereco(endereco);

        
        Telefone telefone = new Telefone();
        telefone.setDdd("11");
        telefone.setNumero("999999999");
        empresa.getTelefones().add(telefone);

        
        empresa = repositorioEmpresa.save(empresa);

        
        Usuario admin = new Usuario();
        admin.setNome("administrador");
        admin.getPerfis().add(Perfil.ADMIN);
        Credencial credencialAdmin = new Credencial();
        credencialAdmin.setNomeUsuario("admin");
        credencialAdmin.setSenha(codificador.encode("admin"));
        admin.setCredencial(credencialAdmin);
        admin = repositorioUsuario.save(admin);

        
        Cliente cliente = new Cliente();
        cliente.setNome("Gerson Penha");
        cliente.getPerfis().add(Perfil.CLIENTE);
        Credencial credencialCliente = new Credencial();
        credencialCliente.setNomeUsuario("gerson");
        credencialCliente.setSenha(codificador.encode("gerson"));
        cliente.setCredencial(credencialCliente);
        cliente = repositorioUsuario.save(cliente);

        
        Usuario vendedor = new Usuario();
        vendedor.setNome("Eduardo Sakaue");
        vendedor.getPerfis().add(Perfil.VENDEDOR);
        Credencial credencialVendedor = new Credencial();
        credencialVendedor.setNomeUsuario("sakaue");
        credencialVendedor.setSenha(codificador.encode("sakaue"));
        vendedor.setCredencial(credencialVendedor);
        vendedor = repositorioUsuario.save(vendedor);

        
        empresa.getUsuarios().add(admin);
        empresa.getUsuarios().add(cliente);
        empresa.getUsuarios().add(vendedor);

        
        Mercadoria mercadoria1 = new Mercadoria();
        mercadoria1.setNome("Pastilha de Freio");
        mercadoria1.setQuantidade(50);
        mercadoria1.setValor(120.50);
        mercadoria1.setDescricao("Pastilha de freio para carros populares");
        mercadoria1.setCadastro(dataCadastro);
        empresa.getMercadorias().add(mercadoria1);

        Mercadoria mercadoria2 = new Mercadoria();
        mercadoria2.setNome("Filtro de Óleo");
        mercadoria2.setQuantidade(100);
        mercadoria2.setValor(35.90);
        mercadoria2.setDescricao("Filtro de óleo universal");
        mercadoria2.setCadastro(dataCadastro);
        empresa.getMercadorias().add(mercadoria2);

        
        Servico servico1 = new Servico();
        servico1.setNome("Troca de Óleo");
        servico1.setValor(150.00);
        servico1.setDescricao("Troca de óleo completa com filtro");
        servico1.setCadastro(dataCadastro);
        empresa.getServicos().add(servico1);

        Servico servico2 = new Servico();
        servico2.setNome("Alinhamento e Balanceamento");
        servico2.setValor(200.00);
        servico2.setDescricao("Alinhamento e balanceamento completo");
        servico2.setCadastro(dataCadastro);
        empresa.getServicos().add(servico2);

        
        Veiculo veiculo1 = new Veiculo();
        veiculo1.setModelo("Gol");
        veiculo1.setPlaca("ABC-1234");
        veiculo1.setAno(2020);
        veiculo1.setProprietario("João Silva");

        Veiculo veiculo2 = new Veiculo();
        veiculo2.setModelo("Corolla");
        veiculo2.setPlaca("XYZ-5678");
        veiculo2.setAno(2021);
        veiculo2.setProprietario("Maria Souza");

        
        Venda venda1 = new Venda();
        venda1.setCadastro(new Date());
        venda1.setCliente(cliente);
        venda1.setFuncionario(vendedor);
        venda1.setVeiculo(veiculo1);
        venda1.getServicos().add(servico1);
        venda1.getMercadorias().add(mercadoria1);

        Venda venda2 = new Venda();
        venda2.setCadastro(new Date());
        venda2.setCliente(cliente);
        venda2.setFuncionario(vendedor);
        venda2.setVeiculo(veiculo2);
        venda2.getServicos().add(servico2);
        venda2.getMercadorias().add(mercadoria2);

        empresa.getVendas().add(venda1);
        empresa.getVendas().add(venda2);

        
        repositorioEmpresa.save(empresa);
    }

}