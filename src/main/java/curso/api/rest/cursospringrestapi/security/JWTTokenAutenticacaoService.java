package curso.api.rest.cursospringrestapi.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.cursospringrestapi.ApplicationContextLoad;
import curso.api.rest.cursospringrestapi.model.Usuario;
import curso.api.rest.cursospringrestapi.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {

	/* Tempo de expiração do token 2 dias */
	/*
	 * Ou seja, caso usuario faça login, ele vai deslogar automaticamente o usuario
	 */
	private static final long EXPIRATION_TIME = 172800000;

	/* Uma senha unica pra compor a auth e ajudar na segurança */
	private static final String SECRET = "SenhaExtremamenteSecreta";

	/* Prefixo padrão de token */
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	/* Gerando token de autenticacao e adicionando ao cabeçalho e reposta http */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		/* montagem do token */
		String JWT = Jwts.builder() /* chama o gerador de token */
				.setSubject(username) /* add o usuario que ta tentando fazer login */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* tempo de expiração */
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();/* compactação e algoritmo de senha */

		/* Junta token com o prefixo */
		String token = TOKEN_PREFIX + " " + JWT; /* Bearer + base64 caracteres */

		/* adiciona um cabeçalho http */
		response.addHeader(HEADER_STRING, token); /* Authorization: Bearer + base64 caracter */

		/*Liberando resposta para portas diferentes que usam API ou caso clientes WEB*/
		liberacaoCors(response);

		/* Escreve token como resposta no corpo http */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	/* Retorna o usuário validado com token ou caso não seja valido retorna null */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		/* Pega o token enviado no cabeçalho http */
		String token = request.getHeader(HEADER_STRING);

		if (token != null) {
			
			/*Remove o prefixo caso o token seja valido*/
			String tokenLimpo = token.replace(TOKEN_PREFIX, "");

			/* Faz a validação do token do usuário na requisição */
			String user = Jwts.parser().setSigningKey(SECRET) /* Bearer base64 caracter */
					.parseClaimsJws(tokenLimpo) /* remove prefixo Bearer */
					.getBody().getSubject(); /* depois retorna o usuario: ex João Silva */

			if (user != null) {

				Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
						.findUserByLogin(user);

				if (usuario != null) {
					
					if (tokenLimpo.equalsIgnoreCase(usuario.getToken())) {
						
						return new UsernamePasswordAuthenticationToken(
								usuario.getLogin(), 
								usuario.getSenha(),
								usuario.getAuthorities());
					}
					
				}

			}

		}

		liberacaoCors(response);
		return null; /* Não autorizado */
	}

	private void liberacaoCors(HttpServletResponse response) {

		/*Liberando o cliente a ter req e res*/
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Origin-Headers") == null) {
			response.addHeader("Access-Control-Allow-Origin-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Origin-Headers") == null) {
			response.addHeader("Access-Control-Request-Origin-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}

	}

}
