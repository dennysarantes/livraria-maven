package br.com.caelum.livraria.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.dao.LivroDao;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;
import br.com.caelum.livraria.modelo.Usuario;
import br.com.caelum.livraria.tx.Transacional;

@Named
@ViewScoped
public class LivroBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private LivroDao dao;
	
	@Inject
	private AutorDao daoAutor;
	
	@Inject
	private FacesContext context;
	
	private Livro livro = new Livro();
	private Livro livroEscolhidoParaExcluir = new Livro();
	private List<Livro> livros;
	// private List<Autor> autores = new DAO<Autor>(Autor.class).listaTodos();
	private Integer autorId;
	private Autor autor = new Autor();
	private Usuario usuarioLogado = new Usuario();
	private List<String> categorias;
	private List<Livro> livrosFiltrados;
	
	
	public List<Livro> getLivrosFiltrados() {
		return livrosFiltrados;
	}

	public void setLivrosFiltrados(List<Livro> livrosFiltrados) {
		this.livrosFiltrados = livrosFiltrados;
	}

	public List<String> getCategorias() {
		List<String> categorias = this.dao.buscaCategorias();		
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public List<Livro> getLivros() {
		//DAO<Livro> dao = new DAO<Livro>(Livro.class);
		if(this.livros == null) {
			this.livros = this.dao.listaTodos();
		}
		
		return livros; 
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	
	
	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Livro getLivro() {
		//livro.setPreco(1.0);
		return livro;
	}

	public List<Autor> getAutores() {

		return this.daoAutor.listaTodos();
	}

	public Livro getLivroEscolhidoParaExcluir() {
		return livroEscolhidoParaExcluir;
	}

	public void setLivroEscolhidoParaExcluir(Livro livroEscolhidoParaExcluir) {
		this.livroEscolhidoParaExcluir = livroEscolhidoParaExcluir;
	}

	@Transacional
	public String gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());
		System.out.println("ISBN e preço do livro a ser gravado: " + this.livro.getIsbn() + " " + this.livro.getPreco());
		
		try {
			usuarioLogado = (Usuario)context.getExternalContext().getSessionMap().get("usuario");
			livro.setResponsavelCadastro(usuarioLogado.getNome());
			livro.setDataCadastro();
		} catch (Exception e) {
			System.out.println("WARN [" + LocalDateTime.now() + "] erro ao obter usuário");
		}
		
		if (livro.getAutores().isEmpty()) {
			//throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			context.addMessage("autor", new FacesMessage("O livro deve conter ao menos um autor"));
			return null;
		}else {
			if (this.livro.getId() == null) {
				try {
					this.dao.adiciona(this.livro);
					this.livro = new Livro();
				} catch (Exception e) {
					System.out.println("Problema ao persistir o livro...");
				}
			}else
					
			try {
				this.dao.atualiza(livro);
				this.livro = new Livro();
			} catch (Exception e) {
				System.out.println("Problema ao persistir o livro...");
			}
		}

		return "livro?faces-redirect=true";
	
	}

	
	public void gravarAutor() {
		System.out.println("Gravando autor...");
		System.out.println("Id do autor selecionado: " + this.autorId);
		
		
		Autor autorSelecionado = this.daoAutor.buscaPorId(this.autorId);

		System.out.println();

		List<Autor> autores = (List<Autor>) livro.getAutores();

		if (autores.isEmpty()) {
			this.livro.adicionaAutor(autorSelecionado);
		} else {
			System.out.println("o livro já possui autores cadastrados...");
			this.livro.adicionaAutor(autorSelecionado);
		}
		
		
		
//		if (!autores.isEmpty()) {
//			
//			for (Autor autor : autores) {
//				if (autor.getId() == autorSelecionado.getId()) {
//					System.out.println("Esse autor já foi selecionado...");
//				} else {
//					System.out.println("Autor adicionado...");
//					this.livro.adicionaAutor(autorSelecionado);
//				}
//			}
//		}

	}

	public List<Autor> getautoresDoLivro() {
		List<Autor> autoresDoLivro = livro.getAutores();
		return autoresDoLivro;
	}

	public void excluirAutor(Autor autor) {
		List<Autor> autoresDoLivro = livro.getAutores();

		for (int i = 0; i < autoresDoLivro.size(); i++) {
			if (autoresDoLivro.get(i).getNome().equals(autor.getNome())) {
				livro.excluirAutor(autoresDoLivro.get(i));
				//System.out.println("Autor " + autoresDoLivro.get(i).getNome() + " excluído com sucesso!");
			}
		}
	}
	
	
	public void excluirAutorOld() {
		
		Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		String nomeAutorParaExcluir = params.get("autorParaExcluir");

		System.out.println("Nome do autor selecionado para exclusão: " + nomeAutorParaExcluir);

		List<Autor> autoresDoLivro = livro.getAutores();

		for (int i = 0; i < autoresDoLivro.size(); i++) {
			if (autoresDoLivro.get(i).getNome().equals(nomeAutorParaExcluir)) {
				livro.excluirAutor(autoresDoLivro.get(i));
				//System.out.println("Autor " + autoresDoLivro.get(i).getNome() + " excluído com sucesso!");
			}
		}

	}

	public void comecaComDigitoUm(FacesContext fc, UIComponent c, Object isbn) throws ValidatorException {
		String isbnASerVerificado = isbn.toString();
		
		if(!isbnASerVerificado.startsWith("1")) {
			throw new ValidatorException(new FacesMessage("O ISBN deve começar com o dígito 1"));
		}
		
	}
	
	public String formAutor() {
		System.out.println("Chamando form autor");
		return "autor?faces-redirect=true";
	}
	
	@Transacional		
	public void excluirLivro() {
		
		System.out.println("Livro a ser removido " + this.livroEscolhidoParaExcluir.getTitulo());
		
		this.dao.remove(this.livroEscolhidoParaExcluir);
		
	}
	public void carregarLivro(Livro livro) {
		this.livro = this.dao.buscaPorId(livro.getId());
		
	}
	@Transacional
	public void excluirLivroPergunta(Livro livroEscolhido) {
		System.out.println("Entrou na pergunta de exclusão");
		System.out.println("Livro escolhido inicialmente: " + livroEscolhido.getTitulo() );
		this.livroEscolhidoParaExcluir = livroEscolhido;
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, "confirmado", "Livro excluído");
		context.addMessage(null, message);
        this.dao.remove(this.livroEscolhidoParaExcluir);
		
		//PrimeFaces.current().executeScript("check()");
	}
	
	public void editarLivro() {
		
	}
}
