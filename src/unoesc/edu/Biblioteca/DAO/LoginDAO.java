package unoesc.edu.Biblioteca.DAO;

import java.util.List;

import unoesc.edu.Biblioteca.model.Login;

public interface LoginDAO {
	Login getLoginById (int id);
	List<Login> getLogins();
	boolean deleteLogin(int id);
	boolean insertLogin(Login p);
	boolean updateLogin(Login p);
	Login validaLogin(String login, String senha);
}
