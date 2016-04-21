package gymTime.entities;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;

@Entity
public class Employment {
	@Id
	@GeneratedValue
	private int employmentId;
	private ratingJob rating;
	public static enum ratingJob {
		Administrator, Employeed
	}
	@Column(columnDefinition = "TINYINT")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean deleted = false; 
	
	@OneToMany(mappedBy="employment")
	private List<Employee> employees = new ArrayList<Employee>();
	
	/**
	 * Recupera el id del trabajo 
	 * @return
	 */
	public int getEmploymentId() {
		return employmentId;
	}

	/**
	 * Recupera el nombre del trabajo
	 *  public static enum employments {
	 *	Administrator, Employee
	 *	} 
	 * @return name
	 */
	@Enumerated(EnumType.STRING)
	public ratingJob getRating() {
		return rating;
	}
	
	/**
	 * Establece eel nombre del trabajo
	 * @param name
	 *  public static enum ratingJob {
	 *	Administrator, Employee
	 *	}
	 */
	public void setRating(ratingJob rating) {
		this.rating = rating;
	}
		
	/**
	 * Recupera el listado de jugadores que tiene el equipo
	 * @return
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * Establece el listado de jugadores que tiene el equipo.
	 * @param players
	 */
	public void setPlayers(List<Employee> employees) {
		this.employees = employees;
	}


	/**
	 * Comprueba si el equipo está marcado como borrado
	 * @return
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Establece el equipo como eliminado.
	 * @param deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * Metodos internos para añadir jugadores a la lista de jugadores.
	 * @param player
	 */
	void internalAddPlayer(Employee Employee) {
		employees.add(Employee);
	}

	/**
	 * Metodos internos para quitar jugadores de la lista de jugadores.
	 * @param player
	 */
	void internalRemovePlayer(Employee Employee) {
		employees.remove(Employee);
	}

}

