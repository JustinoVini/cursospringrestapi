package curso.api.rest.cursospringrestapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.cursospringrestapi.service.ImplementacaoUserDetailsService;

/*mapeia, url, endereços, urls, autoriza ou bloqueia acessos a URL*/
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private ImplementacaoUserDetailsService implementacaoUserDetailsService;
	
	/*Configura as solicitações de acesso por HTTP*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*Ativando a proteção contra usuário que não estão validos por token*/
		/*Proteção contra ataque de usuarios que não estão autenticados*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*Ativando a permissão para acesso a página inicial Ex: sistema.com.br/ ou só barra ou index*/
		.disable().authorizeRequests().antMatchers("/").permitAll()
		.antMatchers("/index").permitAll()

		.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
		/*URL de logout - redireciona após logout*/
		.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
		
		/*mapeia url de logout e invalida o usuário*/
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/*Filtra requisições de login para authenticação*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
							   UsernamePasswordAuthenticationFilter.class)
		/*Filtra demais requisições para verificar a presença do TOKEN JWT no HEADER HTTP*/
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	// provedor de autenticacao
	@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
			/*Service que irá consultar o usuário no banco de dados*/
			auth.userDetailsService(implementacaoUserDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder()); /*Padrão de codificação de senha*/
		}
	
}
