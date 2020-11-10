package persistencia;

import egresosIngresos.MedioDePago;

public class MedioDePagoMapperBD extends MapperBD<MedioDePago>{
	private static final MedioDePagoMapperBD instance = new MedioDePagoMapperBD();
	
	private MedioDePagoMapperBD () {}
	public static MedioDePagoMapperBD getInstance() {
		return instance;
	}
	
	
	
}