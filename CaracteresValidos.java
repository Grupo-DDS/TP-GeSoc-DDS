package com.validarContraseņas;

public class CaracteresValidos{
	static String listaCararcteresValidos = " !\",#$%&()*+-,-./0123456789:;<=>ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`@abcdefghijklmnopqrstuvwxyz{}";
	static public boolean validar(String contrasenia){
		int i=0;
		for(i=0;i<contrasenia.length();i++){
			if(!listaCararcteresValidos.contains(Character.toString(contrasenia.charAt(i)))) {
				return false;
			}	
		}
		return true;
	}
}
