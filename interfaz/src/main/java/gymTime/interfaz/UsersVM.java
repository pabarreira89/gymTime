package gymTime.interfaz;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;

import gymTime.entities.Employee;
import gymTime.entities.Employee.user_type;
import gymTime.interfaz.jpa.DesktopEntityManagerManager;
import gymTime.util.Transaction;
import gymTime.util.TransactionUtil;

public class UsersVM {
	
	private Employee currentUser = null;
	private boolean edit = false;

	/**
	 * Recupera el listado de usuarios
	 * @return
	 */
	public List<Employee> getUsers() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("select u from User u", Employee.class).getResultList();
	}
	
	/**
	 * Recupera el listado de tipos de usuario
	 * @return
	 */
	public List<user_type> getTypes(){
		return new ArrayList<user_type>(EnumSet.allOf(user_type.class));
	}
	
	/**
	 * Elimina un usuario seleccionado
	 * @param el usuario que va ha ser borrado
	 */
	@Command
	@NotifyChange("users")
	public void delete(@BindingParam("user") final Employee u){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				em.remove(u);
			}
		}, em);
		
	}
	
	/**
	 * Recupera el numero de usuarios existentes en el sistema
	 * @return
	 */
	@DependsOn("users")
	public int getCount(){
		return this.getUsers().size();
	}
	
	/**
	 * Devuelve el usuario actual a editar, o crear.
	 * @return
	 */
	public Employee getCurrentPlayer(){
		return currentUser;
	}
	
	/**
	 * Crea un nuevo usuario
	 */
	@Command
	@NotifyChange("currentUser")
	public void newPlayer(){
		this.currentUser = new PlaUseryer();
		this.edit = false;
	}
	
	/**
	 * Cancela las operaciones sobre un usuario.
	 */
	@Command
	@NotifyChange("currentUser")
	public void cancel(){
		this.currentUser = null;
	}
	
	/**
	 * Guarda los cambios de un usuario sobre la base de datos, en caso de tratarse de un usuarui nuevo lo almacena.
	 */
	@Command
	@NotifyChange({"currentUser","users"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				if(!edit)
				{
					em.persist(currentUser);
				}
			}
		}, em);
		this.currentUser = null;
	}
	
	/**
	 * Establece el jugador actual para ser editado.
	 * @param e Jugador que va ha ser editado.
	 */
	@Command
	@NotifyChange("currentUser")
	public void edit(@BindingParam("user") Employee e){
		this.currentUser = e;
		this.edit = true;
			
	}
}