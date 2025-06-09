package com.autobots.automanager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
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


    //Decidi criar alguns dados estaticos para facilitar no uso das rotas prof, mas recomendo usar e criar alguns dados usando de exemplo o readme

    //no momento so tem um unico usuario, o administrador, e uma unica empresa, a AutoManager Ltda

    //para logar esta o tutorial usando este usuario criado, la no readme
    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();


        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("AutoManager Ltda");
        empresa.setNomeFantasia("AutoManager");
        empresa.setCadastro(new Date());

        Usuario usuario = new Usuario();
        usuario.setNome("administrador");
        usuario.getPerfis().add(Perfil.ADMIN);

        Credencial credencial = new Credencial();
        credencial.setNomeUsuario("admin");
        credencial.setSenha(codificador.encode("admin"));
        usuario.setCredencial(credencial);

        usuario = repositorioUsuario.save(usuario);
        
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

        
        criarMercadoria(empresa, "Pastilha de Freio", 50, 120.50, "Pastilha de freio para carros populares");
        criarMercadoria(empresa, "Filtro de Óleo", 100, 35.90, "Filtro de óleo universal");

        
        criarServico(empresa, "Troca de Óleo", 150.00, "Troca de óleo completa com filtro");
        criarServico(empresa, "Alinhamento e Balanceamento", 200.00, "Alinhamento e balanceamento completo");

        
        repositorioEmpresa.save(empresa);
    }

    private void criarMercadoria(Empresa empresa, String nome, int quantidade, double valor, String descricao) {
        Mercadoria mercadoria = new Mercadoria();
        mercadoria.setNome(nome);
        mercadoria.setQuantidade(quantidade);
        mercadoria.setValor(valor);
        mercadoria.setDescricao(descricao);
        empresa.getMercadorias().add(mercadoria);
    }

    private void criarServico(Empresa empresa, String nome, double valor, String descricao) {
        Servico servico = new Servico();
        servico.setNome(nome);
        servico.setValor(valor);
        servico.setDescricao(descricao);
        empresa.getServicos().add(servico);
    }
}