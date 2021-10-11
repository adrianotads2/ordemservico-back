package br.edu.ifms.ordemservico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import br.edu.ifms.ordemservico.dto.ServidorDTO;
import br.edu.ifms.ordemservico.entities.Servidor;
import br.edu.ifms.ordemservico.repositories.ServidorRepository;
import br.edu.ifms.ordemservico.services.exceptions.DataBaseException;
import br.edu.ifms.ordemservico.services.exceptions.ResourceNotFoundException;

@Service
public class ServidorService {

	@Autowired
	private ServidorRepository repository;
	
	@Transactional(readOnly = true)
	public List<ServidorDTO> findAll(){
	     List<Servidor> list = repository.findAll();
	     return list.stream().map(servidor -> new ServidorDTO(servidor))
	    		 .collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public ServidorDTO findById(Long id){
		Optional<Servidor> obj = repository.findById(id);
		Servidor servidor = obj.orElseThrow(()-> new ResourceNotFoundException("O servidor solicitado não foi localizado"));
		return new ServidorDTO(servidor);
	}
	
	@Transactional
	public ServidorDTO insert(ServidorDTO dto) {
		Servidor servidor = new Servidor();
		copyDtoToEntity(dto, servidor);
		servidor = repository.save(servidor);
		return new ServidorDTO(servidor);
	}
	
	@Transactional
	public ServidorDTO update(Long id, ServidorDTO dto) {
		try {
			Servidor servidor = repository.getById(id);
			copyDtoToEntity(dto, servidor);
			servidor = repository.save(servidor);
			return new ServidorDTO(servidor);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("O id do Servidor não foi localizado");
		}
		
		
	}

	
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Não foi possível excluir, o id do Servidor não foi localizado");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Não foi possível excluir o servidor, pois o mesmo está em uso");
		}
		
	}
	
	private void copyDtoToEntity(ServidorDTO dto, Servidor servidor) {
		servidor.setNome(dto.getNome());
		servidor.setEmail(dto.getEmail());
		servidor.setTelefone(dto.getTelefone());
		servidor.setSenha(dto.getSenha());
		
	}


	
	
}
