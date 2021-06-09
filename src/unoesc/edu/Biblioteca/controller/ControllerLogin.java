package unoesc.edu.Biblioteca.controller;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.primefaces.event.CaptureEvent;

import com.sun.istack.NotNull;

import unoesc.edu.Biblioteca.DAO.LoginDAO;
import unoesc.edu.Biblioteca.model.Login;
import unoesc.edu.Biblioteca.util.SessionContext;


@ManagedBean(name="loginMB")
@RequestScoped
public class ControllerLogin {
	
	private Login Login = new Login();
	private List<Login> listLogin;
	private static String photoFile;
	
	private boolean logado = false;
	
	private boolean isRoot = false;
	
	@ManagedProperty(value="#{LoginDAO}")
	private LoginDAO LoginDao;
	
	public ControllerLogin() {
	 Login user =	(Login) SessionContext.getInstance().getAttribute("LoginLogado");
		if (user!=null) {
			logado = true;
			isRoot = user.getRoot();
		}
	}

    private String getRandomImageName() {
		int i = (int) (Math.random() * 10000000);

		return String.valueOf(i);
	}


    public void oncapture(CaptureEvent captureEvent) {
        this.photoFile = getRandomImageName();
        byte[] data = captureEvent.getData();

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String newFileName = "/home/gschreiner/imagensBD/"+ this.photoFile + ".jpeg";
        //String newFileName = externalContext.getRealPath("WEB-INF")+ File.separator + "resources" + File.separator + "img" +
         //                           File.separator + "users"  + filename + ".jpeg";
        

		FileImageOutputStream imageOutput;
		try {
			System.out.println("Criou?: "+ newFileName);
			File file = new File(newFileName);
			file.createNewFile();
			imageOutput = new FileImageOutputStream(file);
			imageOutput.write(data, 0, data.length);
			imageOutput.close();
			this.Login.setImagem(this.photoFile);
			System.out.println("aqui: " + this.photoFile);
		}
        catch(IOException e) {
        	System.err.print(e);
			throw new FacesException("Error in writing captured image.", e);
		}
    }
	
	
	

	public void save() {
		this.Login.setSenha(this.encryptSenha(this.getLogin().getSenha()));
		this.Login.setImagem(this.photoFile);
		System.out.println("teste: "+ this.photoFile);
		
		if (Login.getId() == 0) {
			this.LoginDao.insertLogin(Login);
		} else {
			this.LoginDao.updateLogin(Login);
		}
		
		Login = new Login();
	}
	
	
	public void LoginEdit() {

		this.LoginDao.updateLogin(Login);
	}
	
	
	public void LoginDelete(int id) {
		this.LoginDao.deleteLogin(id);
	}
	
	public void load (int id) {
		this.Login = LoginDao.getLoginById(id);
		this.photoFile = this.Login.getImagem();
	}

	public void fazLogin() throws IOException {
		FacesMessage message = null;
        
		Login = LoginDao.validaLogin(this.getLogin().getLogin(), this.encryptSenha(this.getLogin().getSenha()));
		
		if (Login	!=	null) {
			logado = true;
			SessionContext.getInstance().setAttribute("LoginLogado", Login);
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Login realizado com sucesso!", this.getLogin().getLogin());
		}else {
			message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro no Login!", "Usuário ou senha inválido");
			SessionContext.getInstance().setAttribute("LoginLogado", null);
			logado = false;
			Login = new Login();
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
         
        FacesContext.getCurrentInstance().addMessage(null, message);
        PrimeFaces.current().ajax().addCallbackParam("loggedIn", logado);
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect("index.xhtml");
       // return "redirect:views/Index.xhtml";
	}
	
	
	public void fazLogout() throws IOException {
		FacesMessage message = null;
        
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Logout!","Até mais.");
			SessionContext.getInstance().setAttribute("LoginLogado", null);
			logado = false;
			isRoot = false;
			Login = new Login();
         
        FacesContext.getCurrentInstance().addMessage(null, message);
       
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}
	
	
	private String encryptSenha (String senha) {
		MessageDigest mDigest;
		  try { 
		      //Instanciamos o nosso HASH MD5, poderíamos usar outro como
		      //SHA, por exemplo, mas optamos por MD5.
		      mDigest = MessageDigest.getInstance("MD5");
		       
		      //Convert a String valor para um array de bytes em MD5
		      byte[] valorMD5 = mDigest.digest(senha.getBytes("UTF-8"));
		       
		      //Convertemos os bytes para hexadecimal, assim podemos salvar
		      //no banco para posterior comparação se senhas
		      StringBuffer sb = new StringBuffer();
		      for (byte b : valorMD5){
		             sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1,3));
		      }
		 
		      return sb.toString();
		       
		  } catch (NoSuchAlgorithmException e) {
		      e.printStackTrace();
		      return null;
		  } catch (UnsupportedEncodingException e) {
		      e.printStackTrace();
		      return null;
		  }
	}
	
	public Login getLogin() {
		return Login;
	}


	public void setLogin(Login Login) {
		this.Login = Login;
	}

	public List<Login> getListLogin() {
		listLogin = LoginDao.getLogins();
		return listLogin;
	}


	public void setListLogin(List<Login> listLogin) {
		this.listLogin = listLogin;
	}


	public LoginDAO getLoginDao() {
		return LoginDao;
	}


	public void setLoginDao(LoginDAO LoginDao) {
		this.LoginDao = LoginDao;
	}


	public boolean isLogado() {
		return logado;
	}


	public void setLogado(boolean logado) {
		this.logado = logado;
	}
	
	public boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getPhotoFile() {
		return photoFile;
	}

	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}
	
}