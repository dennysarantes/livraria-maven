package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.tx.Transacional;

//@ManagedBean
@Named
@ViewScoped // import javax.faces.view.ViewScoped;
public class AutorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AutorDao dao;

	private Autor autor = new Autor();
	private List<Autor> autores;

	public Autor getAutor() {
		return autor;
	}

	private Integer autorId;

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public void carregarAutorPelaId() {
//		if (!(this.autorId == null)) {
		this.autor = this.dao.buscaPorId(this.autorId);
//		}
	}

	public List<Autor> getAutores() {

		return this.dao.listaTodos();
	}

	public void setAutores(List<Autor> autores) {
		this.autores = autores;
	}
	
	@Transacional
	public String gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());
		boolean empty = this.autor.getNome().isEmpty();

		if (this.autor.getId() == null) {
			this.dao.adiciona(this.autor);
		} else {
			this.dao.atualiza(this.autor);
		}

//		if (!empty) {
//		new DAO<Autor>(Autor.class).adiciona(this.autor);
//		this.autor = new Autor();
//		} else {
//				System.out.println("Valor nulo");
//		}
//		

		return "autor?faces-redirect=true";

		// this.autor.setNome(null);
	}

	@Transacional
	public void excluir(Autor autor) {
		System.out.println("Excluindo autor");
		try {
			this.dao.remove(autor);
		} catch (Exception e) {
			System.out.println("Erro ao tentar remover autor");
		}

	}

	public void carregaAutor(Autor autor) {
		System.out.println("Editando autor");

		this.autor = autor;

	}
}
