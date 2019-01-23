package com.dhp.model;


import javax.persistence.*;
import java.util.Set;


@Entity
@Table(name = "user")
public class User {
    private Long id;
    private String nome;
    private String cpf;
    private String celular;
    private String email;
    private String senha;
    private String confirmaSenha;
    private int verificado;
    private String ref;
    private long comprasAprovadas;
    private Set<Role> roles;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

    @Transient
    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    @ManyToMany
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public int getVerificado() {
		return verificado;
	}

	public void setVerificado(int verificado) {
		this.verificado = verificado;
	}

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
    @Transient
    public long getComprasAprovadas() {
        return comprasAprovadas;
    }

    public void setComprasAprovadas(long comprasAprovadas) {
        this.comprasAprovadas = comprasAprovadas;
    }


}
