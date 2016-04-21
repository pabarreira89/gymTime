package gymTime.interfaz;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.NotifyChange;

import gymTime.entities.Employee;
import gymTime.entities.Employment;
import gymTime.interfaz.jpa.DesktopEntityManagerManager;
import gymTime.util.Transaction;
import gymTime.util.TransactionUtil;

public class EmployeeVM {
	
	private Employee currentEmployee = null;
	private boolean edit = false;

	/**
	 * Recupera el listado de usuarios
	 * @return
	 */
	public List<Employee> getEmployees() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("select u from Employee u", Employee.class).getResultList();
	}
	
	/**
	 * Recupera el listado de trabajos de empleados
	 * @return
	 */
	public List<Employment> getTeams() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("select t from Employment t WHERE t.deleted = 0",Employment.class).getResultList();
	}
	
	/**
	 * Elimina un usuario seleccionado
	 * @param el usuario que va ha ser borrado
	 */
	@Command
	@NotifyChange("employees")
	public void delete(@BindingParam("employee") final Employee u){
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
	@DependsOn("employees")
	public int getCount(){
		return this.getEmployees().size();
	}
	
	/**
	 * Devuelve el usuario actual a editar, o crear.
	 * @return
	 */
	public Employee getCurrentEmpoyee(){
		return currentEmployee;
	}
	
	/**
	 * Crea un nuevo usuario
	 */
	@Command
	@NotifyChange("currentEmployee")
	public void newPlayer(){
		this.currentEmployee = new Employee();
		this.edit = false;
	}
	
	/**
	 * Cancela las operaciones sobre un usuario.
	 */
	@Command
	@NotifyChange("currentEmployee")
	public void cancel(){
		this.currentEmployee = null;
	}
	
	/**
	 * Guarda los cambios de un usuario sobre la base de datos, en caso de tratarse de un usuarui nuevo lo almacena.
	 */
	@Command
	@NotifyChange({"currentEmployee","employees"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			//@Override
			public void run(EntityManager em) {
				if(!edit)
				{
					em.persist(currentEmployee);
				}
			}
		}, em);
		this.currentEmployee = null;
	}
	
	/**
	 * Establece el jugador actual para ser editado.
	 * @param e Jugador que va ha ser editado.
	 */
	@Command
	@NotifyChange("currentEmployee")
	public void edit(@BindingParam("employee") Employee e){
		this.currentEmployee = e;
		this.edit = true;
			
	}
}