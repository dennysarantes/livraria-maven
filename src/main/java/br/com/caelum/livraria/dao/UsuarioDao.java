package br.com.caelum.livraria.dao;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.caelum.livraria.modelo.Usuario;

public class UsuarioDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	private DAO<Usuario> dao;

	@PostConstruct
	void init() {
		this.dao = new DAO<Usuario>(this.em, Usuario.class);
	}

	
	public Usuario buscaPorId(Integer id) {
		Usuario usuario = em.find(Usuario.class, id);
		return usuario;
	}

	public Usuario buscaPorNomeSenha(String email, String senha) {

		try {
			Object singleResult = em
					.createQuery("SELECT u FROM Usuario u WHERE u.email = :pEmail AND u.senha = :pSenha")
					.setParameter("pEmail", email).setParameter("pSenha", senha).getSingleResult();
			Usuario usuario = (Usuario) singleResult;
			return usuario;
		} catch (NoResultException e) {
			System.out.println("Usuário ou senha inválidos");
		}

		Usuario usuario = new Usuario();

		return usuario;
	}

}
