package unoesc.edu.Biblioteca.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import unoesc.edu.Biblioteca.model.Login;



@Service(value="loginDAO")
public class LoginDAOImpl implements LoginDAO {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Login getLoginById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Login p = (Login) session.get(Login.class, new Integer(id));
		
		//System.out.println("nome: " + p.getNome());
		
		return p;
	}

	@Override
	@Transactional
	public List<Login> getLogins() {
		
		return this.sessionFactory.getCurrentSession().createQuery("from Login").list();
	}

	@Override
	@Transactional
	public boolean deleteLogin(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Login p = (Login) session.load(Login.class, new Integer(id));
		if (p!=null) {
			session.delete(p);
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public boolean insertLogin(Login p) {
		
		this.sessionFactory.getCurrentSession().save(p);
		
		return false;
	}

	@Override
	@Transactional
	public boolean updateLogin(Login p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(p);
		return true;
	}

	@Override
	@Transactional
	public Login validaLogin(String login, String senha) {
		Session session = this.sessionFactory.getCurrentSession();
		Login p = (Login) session.createQuery("from Login where login=:login and senha=:pwd")
								.setParameter("login", login)
								.setParameter("pwd", senha)
								.uniqueResult();
		return p;
	}

}
