package com.autobots.automanager.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.filtros.Autenticador;
import com.autobots.automanager.filtros.Autorizador;
import com.autobots.automanager.jwt.ProvedorJwt;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguranca extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl servico;

    @Autowired
    private ProvedorJwt provedorJwt;

    private static final String[] rotasPublicas = {
            "/auth/login",
            "/cadastrar-usuario",
            "/api/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(rotasPublicas).permitAll()

                .antMatchers("/empresas/**").hasRole("ADMIN")

                .antMatchers("/clientes/listar").hasRole("ADMIN")
                .antMatchers("/usuarios/listar").hasAnyRole("ADMIN", "GERENTE")
                .antMatchers("/gerentes/**").hasRole("ADMIN")
                .antMatchers("/vendedores/listar").hasRole("GERENTE")

                .antMatchers("/servicos/cadastrar").hasAnyRole("ADMIN", "GERENTE")
                .antMatchers("/servicos/listar").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
                .antMatchers("/mercadorias/cadastrar").hasAnyRole("ADMIN", "GERENTE")
                .antMatchers("/mercadorias/listar").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
                .antMatchers("/vendas/cadastrar").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
                .antMatchers("/vendas/minhas-vendas").hasRole("VENDEDOR")
                .antMatchers("/vendas/minhas-compras").hasRole("CLIENTE")
                .antMatchers("/clientes/meus-dados").hasRole("CLIENTE")
                .antMatchers("/api/clientes/**").hasRole("ADMIN")
                .antMatchers("/api/funcionarios/**").hasAnyRole("ADMIN", "GERENTE")
                .antMatchers("/api/produtos/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
                .antMatchers("/api/vendas/**").hasAnyRole("ADMIN", "GERENTE")
                .antMatchers("/api/veiculos/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")

                .anyRequest().authenticated()
                .and()
                .addFilter(new Autenticador(authenticationManager(), provedorJwt))
                .addFilter(new Autorizador(authenticationManager(), provedorJwt, servico));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder autenticador) throws Exception {
        autenticador.userDetailsService(servico).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration().applyPermitDefaultValues();
        configuracao.addAllowedHeader("*");
        configuracao.addAllowedMethod("*");
        configuracao.addAllowedOrigin("*");
        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", configuracao);
        return fonte;
    }
}