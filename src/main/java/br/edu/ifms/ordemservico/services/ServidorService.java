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
import br.edu.ifms.ordemservico.services.exceptions.ErroAutenticacaoException;
import br.edu.ifms.ordemservico.services.exceptions.RegraNegocioException;
import br.edu.ifms.ordemservico.services.exceptions.ResourceNotFoundException;

@Service
public class ServidorService {

	@Autowired
	private ServidorRepository repository;
	
	public ServidorDTO autenticar(String email, String senha) {
		Optional<Servidor> servidor = repository.findByEmail(email);
		if(!servidor.isPresent()) {
			throw new ErroAutenticacaoException("O email do servidor não foi encontrado.");
		}
		if(!servidor.get().getSenha().equals(senha)) {
			throw new ErroAutenticacaoException("A senha do servidor não foi localizada.");
		}
		return new ServidorDTO(servidor.get());
	}
	
	@Transactional(readOnly = true)
	public List<ServidorDTO> findAll(){
	     List<Servidor> list = repository.findAll();
	     return list.stream().map(servidor -> new ServidorDTO(servidor))
	    		 .collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public ServidorDTO findById(Long id){
		Optional<Servidor> obj = repository.findById(id);
		Servidor servidor = obj.orElseThrow(()-> new ResourceNotFoundException("O servidor solicitado n�o foi localizado"));
		return new ServidorDTO(servidor);
	}
	
	@Transactional
	public ServidorDTO insert(ServidorDTO dto) {
		Servidor servidor = new Servidor();
		copyDtoToEntity(dto, servidor);
		validarEmail(servidor.getEmail());
		servidor = repository.save(servidor);
		return new ServidorDTO(servidor);
	}
	
	@Transactional
	public ServidorDTO update(Long id, ServidorDTO dto) {
		try {
			Servidor servidor = repository.getById(id);
			copyDtoToEntity(dto, servidor);
			validarEmail(servidor.getEmail());
			servidor = repository.save(servidor);
			return new ServidorDTO(servidor);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("O id do Servidor n�o foi localizado");
		}
		
		
	}

	
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("N�o foi poss�vel excluir, o id do Servidor n�o foi localizado");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("N�o foi poss�vel excluir o servidor, pois o mesmo est� em uso");
		}
		
	}
	
	private void copyDtoToEntity(ServidorDTO dto, Servidor servidor) {
		servidor.setNome(dto.getNome());
		servidor.setEmail(dto.getEmail());
		servidor.setTelefone(dto.getTelefone());
		servidor.setSenha(dto.getSenha());
		
	}

	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um servidor cadastrado com esse email.");
		}
	}
	
}
