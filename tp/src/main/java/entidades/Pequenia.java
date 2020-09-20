package entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Pequenia")
public class Pequenia extends Empresa{
	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	int idPequenia;
	private static Pequenia instance = null;
	
	public Pequenia() {}

	public int getIdPequenia() {
		return idPequenia;
	}

	public void setIdPequenia(int idPequenia) {
		this.idPequenia = idPequenia;
	}
	
	public static Pequenia getInstance() {
		if (instance == null)
			instance = new Pequenia();
		return instance;
	}
	
}
