package com.validarContraseņas;
import javax.swing.JOptionPane;

public class Validador{
	static String mensaje= "Contraseņa invalida\n";
	
	public static void main(String[] args){
		String contrasenia = JOptionPane.showInputDialog("Ingrese su contraseņa");
		
		if(!LongitudValida.validar(contrasenia))
			mensaje += "Su contraseņa es demasiado corta\n";
		if(!CaracteresValidos.validar(contrasenia))
			mensaje += "Utilice caracteres validos\n";
		if(ArchivoContraseniasInvalidas.validar(contrasenia))
			mensaje += "Su contraseņa es demasiado frecuente\n ";
		if(CaracteresRepetidos.validar(contrasenia))
			mensaje += "Su contraseņa tiene muchos caracteres repetidos\n ";
		if(CaracteresConsecutivos.validar(contrasenia))
			mensaje += "Su contraseņa tiene muchos caracteres consecutivos\n";
		if(LongitudValida.validar(contrasenia) && CaracteresValidos.validar(contrasenia)  && !ArchivoContraseniasInvalidas.validar(contrasenia) && !CaracteresRepetidos.validar(contrasenia))
			mensaje = "Contraseņa Valida";
		JOptionPane.showMessageDialog(null,mensaje);
	}
}