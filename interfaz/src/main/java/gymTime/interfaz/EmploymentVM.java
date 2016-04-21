package gymTime.interfaz;

import java.util.ArrayList;
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

public class EmploymentVM {
	private Employment currentEmployment = null;
	private Employment viewEmployment = null;
	public Employee newEmployeeFromEmployment = null;
	private boolean edit = false;
	private boolean addEmployeeToEmployment = false;
	
	/**
	 * Recupera el listado de equipos que no han sido eliminados
	 * @return
	 */
	public List<Employment> getEmployments() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT t FROM Employment t WHERE t.deleted = 0",Employment.class).getResultList();		
	}
		
	/**
	 * Recupera el listado de equipos pertenecientes 
	 * @return
	 */
	@DependsOn("viewEmployment")
	public List<Employee> getEmployeesFromEmployment(){
		if (viewEmployment != null){
			return viewEmployment.getEmployees();
		}else
			return new ArrayList<Employee> ();
	}
	/**
	 * Recupera el listado de jugadores que no pertencen a ningun equipo.
	 * @return
	 */
	@DependsOn("viewEmployment")
	public List<Employee> getEmployees(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT p FROM Employee p WHERE employment = null",Employee.class).getResultList();
	}
	/**
	 * Recupera el equipo actual a editar o 
	 * @return
	 */
	public Employment getCurrentEmployment(){
		return currentEmployment;
	}
	/**
	 * Recupera el equipo actual a visualizar
	 * @return
	 */
	public Employment getViewEmployment(){
		return viewEmployment;
	}
	/**
	 * recupera un estado un booleano para comprobar si se está añadiendo un jugador a un equipo
	 * @return
	 */
	public boolean getAddEmployeeToEmployment(){
		return addEmployeeToEmployment; 
	}
	/**
	 * Recupera el nuevo jugador para un equipo
	 * @return
	 */
	public Employee getNewEmployeeFromEmployment(){
		return newEmployeeFromEmployment; 
	}
	/**
	 * Establece un nuevo jugador para un equipo a visualizar.
	 * @param p Nuevo jugador para el equipo  a visulizar
	 */
	public void setNewEmployeeFromEmployment(Employee p){
		newEmployeeFromEmployment = p; 
	}
	/**
	 * Establece un equipo como eliminado, y todos sus jugadores son expulsados del mismo.
	 * El equipo  no es eliminado de la BD para poder conservar los partidos que el mismo ha jugado.
	 * @param t
	 */
	@Command
	@NotifyChange("employments")
	public void delete(@BindingParam("employment")Employment t){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				Employee[] employees = new Employee[t.getEmployees().size()];
				t.getEmployees().toArray(employees);
				for (int i = 0; i < employees.length ; i++) {
					employees[i].setEmployment(null);
				}
				t.setEmployees(new ArrayList<Employee>());
				t.setDeleted(true);
			}
		}, em);
	}
	/**
	 * Devuelve el numero de equipos existentes en el listado
	 * @return
	 */
	@DependsOn("teams")
	public int getCount(){
		return this.getEmployments().size();
	}
	
	/**
	 * Crea un nuevo equipo 
	 */
	@Command
	@NotifyChange("currentEmployment")
	public void newTeam(){
		this.currentEmployment = new Employment();
		this.edit = false;
	}
	/**
	 * Cancela los cambios realizados el equipo actual
	 */
	@Command
	@NotifyChange({"currentEmployment", "viewEmployment" })
	public void cancel(){
		this.currentEmployment = null;
		this.viewEmployment = null;
	}
	/**
	 * Guarda los cambios al en el equipo actual, o lo guarda si es un nuevo equipo.
	 */
	@Command
	@NotifyChange({"currentEmployment","employments"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				if(!edit)
				{
					em.persist(currentEmployment);
				}
			}
		}, em);
		this.currentEmployment = null;
	}
	/**
	 * Establece el equipo pasado por parametro como equipo actual para ser modificado.
	 * @param t
	 */
	@Command
	@NotifyChange("currentEmployment")
	public void edit(@BindingParam("employment")Employment t){
		this.currentEmployment = t;
		this.edit = true;
	}
	/**
	 * Establece el equipo pasado por parametro para ser visualizado.
	 * @param t
	 */
	@Command
	@NotifyChange("viewEmployment")
	public void showEmployees(@BindingParam("employment") Employment t){
		viewEmployment = t;
	}	
	/**
	 * Activa el formulario de ala de jugador en un equipo y limpia el mismo
	 */
	@Command
	@NotifyChange({"addEmployeeToEmployment","newEmployeeFromEmployment"})
	public void addEmployee(){
		addEmployeeToEmployment = true;
		newEmployeeFromEmployment = null;
	}
	/**
	 * Cancela las acciones de alta de nuevo jugador en un equipo
	 */
	@Command
	@NotifyChange({"addEmployeeToEmployment","newEmployeeFromEmployment"})
	public void cancelAddEmployee(){
		addEmployeeToEmployment = false;
		newEmployeeFromEmployment = null;
	}
	/**
	 * Guarda el jugador establecido en el equipo visible.
	 */
	@Command
	@NotifyChange({"viewEmployment","addEmployeeToEmployment", "newEmployeeFromEmployment"})
	public void saveEmployeeInEmployment(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				newEmployeeFromEmployment.setEmployment(viewEmployment);
			}
		}, em);
		addEmployeeToEmployment = false;
		newEmployeeFromEmployment = null;
	}
	/**
	 * Expulsa un jugador del equipo a visualizar.
	 * @param p
	 */
	@Command
	@NotifyChange({"viewEmployment","addEmployeeToEmployment"})
	public void pushOutEmployee(@BindingParam("employee") Employee p){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtil.doTransaction(new Transaction() {
			@Override
			public void run(EntityManager em) {
				p.setEmployment(null);
			}
		}, em);
	}
}