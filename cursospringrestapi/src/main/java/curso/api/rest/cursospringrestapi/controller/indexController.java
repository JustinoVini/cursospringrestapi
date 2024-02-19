package curso.api.rest.cursospringrestapi.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import curso.api.rest.cursospringrestapi.DTO.UsuarioDTO;
import curso.api.rest.cursospringrestapi.model.Usuario;
import curso.api.rest.cursospringrestapi.repository.UsuarioRepository;

// liberar cross
@CrossOrigin(origins = "*")
// mapear a classe.
@RestController /* Definição de arquitetura rest */
@RequestMapping(value = "/usuario")
public class indexController {

	/**
	 * injeção de dependencia
	 * 
	 * o que é:
	 * 
	 * padrão de projeto de software que permite que os objetos de uma classe
	 * obtenham suas dependências de uma fonte externa
	 */

	@Autowired
	private UsuarioRepository usuarioRepository;

	/* Serviço RestFul */
	/**
	 * 
	 * @param nome
	 * @return nome
	 */
	@GetMapping(value = "/{id}", produces = "application/json")
	@Cacheable("cacheuser")
	@CacheEvict(value = "cacheuser", allEntries = true)
	@CachePut("cacheuser")
	public ResponseEntity<UsuarioDTO> init(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()), HttpStatus.OK);
	}

	/* Delete de Usuario sem response entity*/
	/*@DeleteMapping(value = "/{id}", produces = "application/text")
	public String deletar(@PathVariable(value = "id") Long id) {

		usuarioRepository.deleteById(id);

		return "ok";
		
		"errado"
	}*/
	
	/*Delete com ResponseEntity*/
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public ResponseEntity<?> deletar(@PathVariable(value = "id") Long id) {
	    usuarioRepository.deleteById(id);
	    return ResponseEntity.ok("Usuário deletado com sucesso");
	}
	

	/**
	 * criando um exemplo de um método que retorna todos
	 * @throws InterruptedException 
	 */
	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "cacheusuarios", allEntries = true)
	@CachePut("cacheusuarios")
	public ResponseEntity<List<Usuario>> usuario() throws InterruptedException {

		List<Usuario> list = usuarioRepository.findAll();

		Thread.sleep(1000);

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}

	/*Consulta usuário por nome*/
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuarioConsultaNome(@PathVariable("nome") String nome) throws InterruptedException {

		List<Usuario> list = usuarioRepository.findUserByNome(nome);

		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}

	/**
	 * Criando o primeiro método post
	 * 
	 * Em resumo, a classe ResponseEntity é uma ferramenta poderosa para
	 * personalizar e controlar a resposta HTTP retornada por uma aplicação Spring.
	 * @throws IOException 
	 */
	@PostMapping(value = "/", produces = "application/json")
	// RequestBody, pega o json e converte para o objeto, para isso, o json precisa
	// ter
	// todos os atributos e iguais.
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception {

		// correção dos telefones.
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			// ele amarra os telefones aos pais.
			// na hora de persistir ele irá entender.
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		/*Consumindo API do VIACEP*/
		URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
		/*Abrindo a excessao*/
		URLConnection connection = url.openConnection();
		/*Pegar os dados*/
		InputStream is = connection.getInputStream();
		/*Prepara a leitura*/
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		/*Agora obteremos os dados nessa variavel*/
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		/*Faça enquanto */		
		while((cep = br.readLine()) != null) {
			jsonCep.append(cep);
		}
		
		System.out.println(jsonCep.toString());
		
		/*A linha abaixo pega a string e converte para json*/
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		/*Passando os dados do usuario aux para o usuario para gravar*/
		usuario.setCep(userAux.getCep());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setComplemento(userAux.getComplemento());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setUf(userAux.getUf());
		
		/*Passar para json a string aux cep*/
		
		/*Fim do consumo viacep*/
		
		// var temporaria para criptografar a senha
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		// instacia do novo usuario
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	@PutMapping(value = "/{id}", produces = "application/json")
	// RequestBody, pega o json e converte para o objeto, para isso, o json precisa
	// ter
	// todos os atributos e iguais.
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {

		// correção dos telefones.
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			// ele amarra os telefones aos pais.
			// na hora de persistir ele irá entender.
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		/*Atualização de senha do usuario*/
		Usuario userTemporario = usuarioRepository.findUserByLogin(usuario.getLogin());
		
		if (!userTemporario.getSenha().equals(usuario.getSenha())) { /*Se a senha for diferente*/
			// var temporaria para criptografar a senha
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
		} 
		
		
		/* Outras rotinas antes de atualizar */
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	/*
	 * Exemplo com mais de um post
	 * 
	 * @PostMapping(value = "/vendausuario", produces = "application/json") public
	 * ResponseEntity<Usuario> cadastrarvenda(@RequestBody Usuario usuario) {
	 * 
	 * //Aqui seria o processo de venda Usuario usuarioSalvo =
	 * usuarioRepository.save(usuario);
	 * 
	 * return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	 * 
	 * }
	 */

}
