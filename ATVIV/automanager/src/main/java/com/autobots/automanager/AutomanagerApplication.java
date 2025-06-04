package com.autobots.automanager;

import java.util.ArrayList;
import java.util.List;

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
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

    @Autowired
    private RepositorioUsuario usuarioRepositorio;
    
    @Autowired
    private RepositorioEmpresa empresaRepositorio;
    
    @Autowired
    private RepositorioMercadoria mercadoriaRepositorio;
    
    @Autowired
    private RepositorioServico servicoRepositorio;

    public static void main(String[] args) {
        SpringApplication.run(AutomanagerApplication.class, args);
    }


	//Fiz a adição de alguns dados iniciais para que te poupe tempo ao rodar o sistema.
    @Override
    public void run(String... args) throws Exception {
        BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
        
        // Aqui vai criar uma empresa
        if (empresaRepositorio.count() == 0) {
            Empresa gersonMotors = new Empresa();
            gersonMotors.setRazaoSocial("Gerson Motors SA");
            gersonMotors.setNomeFantasia("Gerson Motors Best Show Ever");
            gersonMotors.setCnpj("12.345.678/0001-99");
            
            Endereco enderecoEmpresa = new Endereco();
            enderecoEmpresa.setEstado("SP");
            enderecoEmpresa.setCidade("São Paulo");
            enderecoEmpresa.setBairro("Centro");
            enderecoEmpresa.setRua("Avenida Paulista");
            enderecoEmpresa.setNumero("950");
            enderecoEmpresa.setCodigoPostal("01310-100");
            gersonMotors.setEndereco(enderecoEmpresa);
            
            List<Telefone> telefonesEmpresa = new ArrayList<>();
            Telefone telefone1 = new Telefone();
            telefone1.setDdd("11");
            telefone1.setNumero("987654321");
            telefonesEmpresa.add(telefone1);
            
            gersonMotors.setTelefones(telefonesEmpresa);
            empresaRepositorio.save(gersonMotors);
            
            // Aqui eu ja adiciono algumas mercadorias 
            Mercadoria carro1 = new Mercadoria();
            carro1.setNome("Fusca 1976");
            carro1.setQuantidade(5);
            carro1.setValor(25000.0);
            carro1.setDescricao("Clássico brasileiro em ótimo estado");
            
            Mercadoria carro2 = new Mercadoria();
            carro2.setNome("Chevette 1985");
            carro2.setQuantidade(3);
            carro2.setValor(18000.0);
            carro2.setDescricao("Popular esportivo com motor AP");
            
            mercadoriaRepositorio.save(carro1);
            mercadoriaRepositorio.save(carro2);
            
            // Crio Serviços para a empresa
            Servico servico1 = new Servico();
            servico1.setNome("Troca de óleo");
            servico1.setValor(150.0);
            servico1.setDescricao("Troca de óleo e filtro");
            
            Servico servico2 = new Servico();
            servico2.setNome("Alinhamento e balanceamento");
            servico2.setValor(200.0);
            servico2.setDescricao("Alinhamento e balanceamento completo");
            
            servicoRepositorio.save(servico1);
            servicoRepositorio.save(servico2);
        }
        
        // Crio alguns usuarios, cada um com um nivel de acesso. a senha é sempre a mesma para facilitar
		// Senha: 123456
        criarUsuarioSeNaoExistir("Luis Inacio Lula da Silva", "lula", "123456", Perfil.ROLE_ADMIN);
        criarUsuarioSeNaoExistir("Jair Messias Bolsonaro", "bolsonaro", "123456", Perfil.ROLE_GERENTE);
        criarUsuarioSeNaoExistir("Alexandre de Moraes Big Xandão", "xandao", "123456", Perfil.ROLE_VENDEDOR);
        criarUsuarioSeNaoExistir("Temer", "temer", "123456", Perfil.ROLE_CLIENTE);
    }
		//para logar voce irá colocar o usuario e a senha criados
    
    private void criarUsuarioSeNaoExistir(String nome, String usuario, String senha, Perfil perfil) {
        if (usuarioRepositorio.findByCredencialNomeUsuario(usuario).isEmpty()) {
            Usuario user = new Usuario();
            user.setNome(nome);
            user.getPerfis().add(perfil);
            
            Credencial credencial = new Credencial();
            credencial.setNomeUsuario(usuario);
            credencial.setSenha(new BCryptPasswordEncoder().encode(senha));
            user.setCredencial(credencial);
            
            //Randomizei alguns itens para agilizar, endereço e telefone so muda o numero

            Telefone telefone = new Telefone();
            telefone.setDdd("11");
            telefone.setNumero("98765" + (int)(Math.random() * 10000));
            
            List<Telefone> telefones = new ArrayList<>();
            telefones.add(telefone);
            user.setTelefones(telefones);
            
            Endereco endereco = new Endereco();
            endereco.setEstado("SP");
            endereco.setCidade("São Paulo");
            endereco.setBairro("Centro");
            endereco.setRua("Avenida Paulista");
            endereco.setNumero(String.valueOf(100 + (int)(Math.random() * 900)));
            endereco.setCodigoPostal("01310-100");
            user.setEndereco(endereco);
            
            usuarioRepositorio.save(user);
        }
    }
}