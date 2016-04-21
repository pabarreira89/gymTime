package gymTime.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private int employeeId;
	private String fullName;
	@ManyToOne
	private Employment employment;
		
	/**
	 * Recupera el id del empleado que lo identifica en la base de datos
	 * @return employeeId
	 */
	public int getEmployeId() {
		return employeeId;
	}
	/**
	 * Recupera el nombre completo del empleado en la base de datos.
	 * @return fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * Establece el nombre completo del empleado.
	 * @param fullName
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}	
	/**
	 * Recupera el trabajo del empleado
	 *   
	 * @return job
	 */

	public Employment getEmployment() {
		return employment;
	}
	
	/**
	 * Establece el trabaoj al que pertenece el empleado, y acutliza el listado de empleados del trabajo anterior
	 * @param job
	 */
	public void setEmployment(Employment job) {
		if(this.employment!=null){
			this.employment.internalRemovePlayer(this);
		}
		this.employment = job;
		if(this.employment!=null){
			this.employment.internalAddPlayer(this);
		}
	}
}
