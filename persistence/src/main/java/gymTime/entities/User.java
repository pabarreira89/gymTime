package gymTime.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue
	private int userId;
	private String fullName;
	private user_type type;
	
	public static enum user_type {
		ADM, EMP
	}
	
	/**
	 * Recupera el id del usuario que lo identifica en la base de datos
	 * @return userId
	 */
	public int getUser_id() {
		return userId;
	}
	/**
	 * Recupera el nombre completo del usuario en la base de datos.
	 * @return full_name
	 */
	public String getFull_name() {
		return fullName;
	}
	/**
	 * Establece el nombre completo del usuario.
	 * @param fullName
	 */
	public void setFull_name(String fullName) {
		this.fullName = fullName;
	}	
	/**
	 * Recupera el tipo del usuario
	 *  public static enum user_type {
	 *		ADM, EMP
	 *	} 
	 * @return type
	 */
	@Enumerated(EnumType.STRING)
	public user_type getType() {
		return type;
	}
	
	/**
	 * Establece el tipo para el usuario
	 * @param type
	 *  public static enum user_type {
	 *		ADM, EMP
	 *	}
	 */
	public void setType(user_type type) {
		this.type = type;
	}	
}
