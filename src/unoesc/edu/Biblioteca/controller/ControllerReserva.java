package unoesc.edu.Biblioteca.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import unoesc.edu.Biblioteca.DAO.LivroDAO;
import unoesc.edu.Biblioteca.DAO.ReservaDAO;
import unoesc.edu.Biblioteca.DAO.UsuarioDAO;
import unoesc.edu.Biblioteca.model.Livro;
import unoesc.edu.Biblioteca.model.Reserva;
import unoesc.edu.Biblioteca.model.Usuario;


@ManagedBean(name="reservaMB")
@RequestScoped
public class ControllerReserva {

	private List<Reserva> listaReservas;
	private List<Usuario> listaUsuarios;
	private List<Livro>listaLivros;
	private List<Livro>selecionaLivros = null;
	
	
	private Reserva user = new Reserva();
	
	@ManagedProperty(value="#{ReservaDAO}")
	private ReservaDAO reservaDao;
	
	@ManagedProperty(value="#{UsuarioDAO}")
	private UsuarioDAO usuarioDao;
	
	@ManagedProperty(value="#{LivroDAO}")
	private LivroDAO livroDao;



	
	public void save() {
		
		if (user.getCodigoReserva() == 0) {
			for(Livro l: this.selecionaLivros) {
				System.out.println("aqui: " + l.getAutor());
				this.user.getLivros().add(l);
			}
			
			this.reservaDao.insertReserva(user);
			System.out.println("Salvou Reserva");
		} else {
			this.reservaDao.updateReserva(user);

		}
		
		this.user = new Reserva();

	}
	
	public void edit(int id) {
		List<Reserva> listaReservas = this.reservaDao.getallReservas();
	
		user = this.reservaDao.getReservaById(id); //Buscar o cara a ser editado

	}
	
	
	public void delete(int id) {
		System.out.println("Tentou deletar");
		this.reservaDao.deleteReserva(id);
	
		System.out.println("Removeu");
		
		
		
	}
	
	

	public List<Reserva> getListaReservas() {
		return this.reservaDao.getallReservas();
	}

	public void setListaReservas(List<Reserva> listaReservas) {
		this.listaReservas = listaReservas;
	}
	
	
	
	

	public List<Usuario> getListaUsuarios() {
		return this.usuarioDao.getallUsuarios();
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public UsuarioDAO getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDAO usuarioDao) {
		this.usuarioDao = usuarioDao;
	}
	
	

	public List<Livro> getListaLivros() {
		return this.livroDao.getallLivros();
	}

	public void setListaLivros(List<Livro> listaLivros) {
		this.listaLivros = listaLivros;
	}

	public LivroDAO getLivroDao() {
		return livroDao;
	}

	public void setLivroDao(LivroDAO livroDao) {
		this.livroDao = livroDao;
	}

	public Reserva getUser() {
		return user;
	}

	public void setUser(Reserva user) {
		this.user = user;
	}

	public ReservaDAO getReservaDao() {
		return reservaDao;
	}

	public void setReservaDao(ReservaDAO ReservaDao) {
		this.reservaDao = ReservaDao;
	}

	public List<Livro> getSelecionaLivros() {
		if(this.selecionaLivros == null) 
			this.selecionaLivros = new ArrayList<Livro>();
		return selecionaLivros;
	}

	public void setSelecionaLivros(List<Livro> selecionaLivros) {
		this.selecionaLivros = selecionaLivros;
	}

	
	
	
}
