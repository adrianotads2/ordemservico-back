package br.edu.ifms.ordemservico.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.ifms.ordemservico.dto.OrdemDeServicoDTO;
import br.edu.ifms.ordemservico.entities.OrdemDeServico;
import br.edu.ifms.ordemservico.repositories.OrdemDeServicoRepository;
import br.edu.ifms.ordemservico.services.exceptions.ResourceNotFoundException;

@Service
public class OrdemDeServicoService {

	@Autowired
	private OrdemDeServicoRepository repository;
	
	@Transactional(readOnly = true)
	public List<OrdemDeServicoDTO> findAll(){
	     List<OrdemDeServico> list = repository.findAll();
	     return list.stream().map(ordem -> new OrdemDeServicoDTO(ordem))
	    		 .collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public OrdemDeServicoDTO findById(Long id){
		Optional<OrdemDeServico> obj = repository.findById(id);
		OrdemDeServico ordem = obj.orElseThrow(()-> new ResourceNotFoundException("A ordem solicitada não foi localizada"));
		return new OrdemDeServicoDTO(ordem);
	}
	
	@Transactional
	public OrdemDeServicoDTO insert(OrdemDeServicoDTO dto) {
		OrdemDeServico ordem = new OrdemDeServico();
		copyDtoToEntity(dto, ordem);
		ordem = repository.save(ordem);
		return new OrdemDeServicoDTO(ordem);
	}
	
	@Transactional
	public OrdemDeServicoDTO update(Long id, OrdemDeServicoDTO dto) {
		try {
			OrdemDeServico ordem = repository.getById(id);
			copyDtoToEntity(dto, ordem);
			ordem = repository.save(ordem);
			return new OrdemDeServicoDTO(ordem);
		}catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("O id da ordem não foi localizado");
		}
		
		
	}

	
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Não foi possível excluir, o id da ordem não foi localizado");
		} 
		
	}
	
	private void copyDtoToEntity(OrdemDeServicoDTO dto, OrdemDeServico ordem) {
		ordem.setEquipamento(dto.getEquipamento());
		ordem.setPatrimonio(dto.getPatrimonio());
		ordem.setSetor(dto.getSetor());
		ordem.setDescricaoProblema(dto.getDescricaoProblema());
		ordem.setDataCadastro(dto.getDataCadastro());
		ordem.setStatus(dto.getStatus());
		ordem.setPrioridade(dto.getPrioridade());
		ordem.setDescricaoSolucao(dto.getDescricaoSolucao());
		ordem.setServidor(dto.getServidor());
	}


	
	
}
