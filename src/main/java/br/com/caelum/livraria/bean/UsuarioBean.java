package br.com.caelum.livraria.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.livraria.dao.AutorDao;
import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.dao.UsuarioDao;
import br.com.caelum.livraria.modelo.Usuario;

//@ManagedBean
@Named
@ViewScoped
public class UsuarioBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioDao dao;
	
	@Inject
	private FacesContext context;
	
	private Usuario usuario = new Usuario();
	private Integer usuarioId;
		
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Usuario carregarUsuarioPelaId(Integer id){
//		if (!(this.autorId == null)) {
			return this.dao.buscaPorId(this.usuarioId);	
//		}
	}

	 
	public void teste() {
		
	}
	public String login() {
		
		Usuario usuarioLogin = verificaCredenciaisInformadas();
		
		System.out.println("Nome usuario: " + usuarioLogin.getEmail());
		
		if (usuarioLogin.getId() == null) {
			//Login mal sucedido
			adicionarMsgErroLogin();
			return null;
		}else {
			//Login com sucesso
			adicionarUsuarioLogado(usuarioLogin);
			
			return "livro?faces-redirect=true";
		}
		
		//return null;
		
	}

	private Usuario verificaCredenciaisInformadas() {
		Usuario usuarioLogin = this.dao.buscaPorNomeSenha(this.usuario.getEmail(),this.usuario.getSenha());
		return usuarioLogin;
	}

	private void adicionarUsuarioLogado(Usuario usuarioLogin) {
		context.getExternalContext().getSessionMap().put("usuario", usuarioLogin);
	}

	private void adicionarMsgErroLogin() {
		context.addMessage("login", new FacesMessage("Usuário ou senha inválidos"));
	}
	
	public String logout() {
		System.out.println("Chamando logout");
		Usuario usuarioLogout = new Usuario();
		context.getExternalContext().getSessionMap().replace("usuario", usuarioLogout);
		return "login?faces-redirect=true";
	}
}
