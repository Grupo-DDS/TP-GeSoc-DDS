import javax.swing.JOptionPane;

public class Validador{
	public static void main(String[] args){
		String contrasenia = JOptionPane.showInputDialog("Ingrese su contraseņa");
		ReguladorDeContrasenias.regular(contrasenia);	 
	}	
}