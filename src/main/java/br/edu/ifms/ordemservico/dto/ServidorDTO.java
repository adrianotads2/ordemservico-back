package br.edu.ifms.ordemservico.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import br.edu.ifms.ordemservico.entities.Servidor;

public class ServidorDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	@NotBlank(message= "O campo nome é obrigatório")
	private String nome;
	@NotBlank(message= "O telefone telefone é obrigatório")
	private String telefone;
	@Email
	private String email;
	@NotBlank(message= "O campo senha é obrigatório")
	@Size(min = 5, max = 20, message = "Deve ter entre 5 e 20 caracteres")
	private String senha;
	
	public ServidorDTO() {}

	public ServidorDTO(Long id, String nome, String telefone, String email, String senha) {
		//super();
		this.id = id;
		this.nome = nome;
		this.telefone = telefone;
		this.email = email;
		this.senha = senha;
	}
	
	public ServidorDTO(Servidor servidor) {
		//super();
		this.id = servidor.getId();
		this.nome = servidor.getNome();
		this.telefone = servidor.getTelefone();
		this.email = servidor.getEmail();
		this.senha = servidor.getSenha();
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	

}
