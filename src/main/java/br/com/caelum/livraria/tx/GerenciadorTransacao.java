package br.com.caelum.livraria.tx;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;

@SuppressWarnings("serial")
@Transacional
@Interceptor
public class GerenciadorTransacao implements Serializable {

	@Inject
	private EntityManager em;
	
	@AroundInvoke
	public Object executaTx(InvocationContext ic) throws Exception {
		
		// abre transacao
		em.getTransaction().begin();
		
		//chama os Daos respectivos
		Object resultadoTransacao = ic.proceed();
		
		// commita a transacao
		em.getTransaction().commit();
		
		return resultadoTransacao;
	}
	
}
