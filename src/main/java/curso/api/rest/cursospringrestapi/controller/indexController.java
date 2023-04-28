package curso.api.rest.cursospringrestapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.cursospringrestapi.model.Usuario;
import curso.api.rest.cursospringrestapi.repository.UsuarioRepository;

// mapear a classe.
@RestController /*Definição de arquitetura rest*/
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
	
	/*Serviço RestFul*/
	/**
	 * 
	 * @param nome
	 * @return nome
	 */
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		// retorno do objeto em formato json
		/*Usuario usuario = new Usuario();
		usuario.setId(50L);
		usuario.setLogin("vinicius@edamatec.com.br");
		usuario.setNome("Vinicius Justino");
		usuario.setSenha("1234");
		
		// formato de lista]
		Usuario usuario2 = new Usuario();
		usuario2.setId(8L);
		usuario2.setLogin("vinicius@edamatec.com.br");
		usuario2.setNome("Vinicius Justino");
		usuario2.setSenha("1234");
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario);
		usuarios.add(usuario2);
		/*
		 Retorno de json.
		 */ //return ResponseEntity.ok(usuario); */
		
		// retorno em lista.
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	/*Delete de Usuario*/
	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String deletar(@PathVariable(value = "id") Long id) {
		
		usuarioRepository.deleteById(id);
		
		return "ok";
	}
	
	/**
	 * criando um exemplo de um método que retorna todos
	 */
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario () {
		
		List<Usuario> list = usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	/**
	 * Criando o primeiro método post
	 * 
	 * Em resumo, a classe ResponseEntity é uma ferramenta poderosa 
	 * para personalizar e controlar a resposta HTTP retornada por uma aplicação Spring.
	 */
	@PostMapping(value = "/", produces = "application/json")
	// RequestBody, pega o json e converte para o objeto, para isso, o json precisa ter
	// todos os atributos e iguais.
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
		
		// instacia do novo usuario
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}
	
	@PutMapping(value = "/{id}", produces = "application/json")
	// RequestBody, pega o json e converte para o objeto, para isso, o json precisa ter
	// todos os atributos e iguais.
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {
		
		/*Outras rotinas antes de atualizar*/
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}
	
	/*
	 * Exemplo com mais de um post
	@PostMapping(value = "/vendausuario", produces = "application/json")
	public ResponseEntity<Usuario> cadastrarvenda(@RequestBody Usuario usuario) {
		
		//Aqui seria o processo de venda
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
		
	}*/
	
}
