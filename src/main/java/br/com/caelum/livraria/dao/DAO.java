package br.com.caelum.livraria.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import br.com.caelum.livraria.modelo.Usuario;
import br.com.caelum.livraria.tx.Transacional;


public class DAO<T> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Class<T> classe;

//	@Inject
//	private JPAUtil jpaUtil;
//	
	//private EntityManager em = jpaUtil.getEntityManager();
	
	private EntityManager em;
	
	public DAO(EntityManager em, Class<T> classe) {
		this.classe = classe;
		this.em = em;
	}

	@Transacional
	public void adiciona(T t) {
		// persiste o objeto
		em.persist(t);
	}

	@Transacional
	public void remove(T t) {

		em.remove(em.merge(t));

	}
	
	@Transacional
	public void atualiza(T t) {

		em.merge(t);

	}

	public List<T> listaTodos() {
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).getResultList();

		return lista;
	}

	public T buscaPorId(Integer id) {
		T instancia = em.find(classe, id);
		return instancia;
	}

	public List<String> buscaCategorias (){
		List<String> categorias;
		
		Query query = em.createQuery("SELECT DISTINCT l.categoria FROM Livro l");
		categorias = query.getResultList();
		
		return categorias;
	}
	
	public Usuario buscaPorNomeSenha(String email, String senha) {
		
		try {
			Object singleResult = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :pEmail AND u.senha = :pSenha")
					.setParameter("pEmail", email)
					.setParameter("pSenha", senha)
					.getSingleResult();
			Usuario usuario = (Usuario) singleResult;
			return usuario;
		} catch (NoResultException e) {
			System.out.println("Usuário ou senha inválidos");
		}
		
		Usuario usuario = new Usuario();
		
		return usuario;
	}
	
	public int contaTodos() {
		long result = (Long) em.createQuery("select count(n) from livro n")
				.getSingleResult();

		return (int) result;
	}

	public List<T> listaTodosPaginada(int firstResult, int maxResults) {
		CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
		query.select(query.from(classe));

		List<T> lista = em.createQuery(query).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();

		return lista;
	}

}
