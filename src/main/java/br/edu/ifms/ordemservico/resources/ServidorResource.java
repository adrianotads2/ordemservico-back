package br.edu.ifms.ordemservico.resources;


import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import br.edu.ifms.ordemservico.dto.ServidorDTO;
import br.edu.ifms.ordemservico.entities.Servidor;
import br.edu.ifms.ordemservico.services.ServidorService;
import br.edu.ifms.ordemservico.services.exceptions.ErroAutenticacaoException;

@RestController
@RequestMapping(value = "/servidores")
public class ServidorResource {

	@Autowired
	private ServidorService service;
	
	@GetMapping
	public ResponseEntity<List<ServidorDTO>> findAll(){
		List<ServidorDTO> lista = service.findAll();
		return ResponseEntity.ok().body(lista);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ServidorDTO> findById(@PathVariable Long id){
		ServidorDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody ServidorDTO dto) {
		try {
			Servidor servidorAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(servidorAutenticado);
		} catch (ErroAutenticacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping
	public ResponseEntity<ServidorDTO> insert(@Valid @RequestBody ServidorDTO dto){
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
												.path("/{id}")
												.buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ServidorDTO> update(@PathVariable Long id, @Valid @RequestBody ServidorDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ServidorDTO> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}

