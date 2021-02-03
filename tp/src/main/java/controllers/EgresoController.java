package controllers;


import static app.RequestUtil.getQueryCompra;
import static app.RequestUtil.getQueryComprobanteNumero;
import static app.RequestUtil.getQueryComprobanteTipo;
import static app.RequestUtil.getQueryDNICUITProveedor;
import static app.RequestUtil.getQueryFecha;
import static app.RequestUtil.getQueryMedio;
import static app.RequestUtil.getQueryNombreProveedor;
import static com.API.MedioDePago.buscarMedioDePagoPorID;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.API.MedioDePago;

import app.Path;
import app.ViewUtil;
import comprasPresupuestos.Compra;
import egresosIngresos.DocumentoComercial;
import egresosIngresos.OperacionEgreso;
import egresosIngresos.OrganizacionProveedora;
import egresosIngresos.Persona;
import persistencia.CompraMapperBD;
import persistencia.OperacionEgresoMapperBD;
import spark.Request;
import spark.Response;
import spark.Route;

public class EgresoController {
	public static Route cargarEgreso = (Request request, Response response) -> {
		Map<String, Object> model = new HashMap<>();
		LoginController.ensureUserIsLoggedIn(request, response);
		OperacionEgreso nuevaOperacionEgreso = new OperacionEgreso();
		List<MedioDePago> mediosDePagoExistentes = MedioDePago.obtenerTodosDeBD();
		model.put("medios",mediosDePagoExistentes);
		model.remove("compras_disponibles");
		model.put("compras_disponibles",Compra.obtenerComprasSinEgresos());  
		try {
			if (!getQueryCompra(request).equals("") && !getQueryComprobanteTipo(request).equals("")
					&& !getQueryFecha(request).equals("") && !getQueryMedio(request).equals("")
					&& !getQueryNombreProveedor(request).equals("") && !getQueryDNICUITProveedor(request).equals("")
					&& !getQueryComprobanteNumero(request).equals("")) {
				Compra compraEncontrada = Compra
						.buscarCompraPorNumeroEnBD(Long.parseLong(getQueryCompra(request).trim()));
				if (compraEncontrada != null)
					nuevaOperacionEgreso.setCompra(compraEncontrada);
				else {
					model.put("compraInexistente", true);
					return ViewUtil.render(request, model, Path.Template.EGRESOS);
				}
				if (getQueryComprobanteTipo(request).trim().length() == 1) {
					DocumentoComercial comprobante = new DocumentoComercial(
							Integer.parseInt(getQueryComprobanteNumero(request).trim()),
							getQueryComprobanteTipo(request).trim().charAt(0));
					DocumentoComercial.insertarDocumentoEnBD(comprobante);
					nuevaOperacionEgreso.setComprobante(comprobante);
				} else {
					model.put("comprobanteUnSoloDigito", true);
					return ViewUtil.render(request, model, Path.Template.EGRESOS);
				}

				String[] fecha = getQueryFecha(request).split("/");
				try {
					LocalDate date = LocalDate.of(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]),
							Integer.parseInt(fecha[0]));
					nuevaOperacionEgreso.setFechaOperacion(date);
				} catch (java.time.DateTimeException e) {
					model.put("errorFecha", true);
					return ViewUtil.render(request, model, Path.Template.EGRESOS);
				}
				MedioDePago mp = buscarMedioDePagoPorID(getQueryMedio(request),mediosDePagoExistentes);
				nuevaOperacionEgreso.setMedioDePago(mp);
				//TODO PROBAR
				if (getQueryDNICUITProveedor(request).trim().length() <= 8) {
					int dniPersona = Integer.parseInt(getQueryDNICUITProveedor(request).trim());
					Persona proveedorEncontradoBD = Persona.buscarPersonaPorDNIEnBD(dniPersona);
					if (proveedorEncontradoBD != null)
						nuevaOperacionEgreso.setProveedor(proveedorEncontradoBD);
					else {
						Persona nuevaPersona = new Persona();

						nuevaPersona.setDni(dniPersona);
						nuevaPersona.setNombreApellido(getQueryNombreProveedor(request).trim());
						Persona.insertarNuevaPersonaEnBD(nuevaPersona);

						nuevaOperacionEgreso.setProveedor(nuevaPersona);
					}
				} else {
					int cuitProveedor = Integer.parseInt(getQueryDNICUITProveedor(request).trim());

					OrganizacionProveedora proveedorEncontradoBD = OrganizacionProveedora
							.buscarProveedorPorCUITEnBD(cuitProveedor);
					if (proveedorEncontradoBD != null)
						nuevaOperacionEgreso.setProveedor(proveedorEncontradoBD);
					else {
						OrganizacionProveedora nuevaOrgProveedora = new OrganizacionProveedora();
						nuevaOrgProveedora.setCuit(cuitProveedor);
						nuevaOrgProveedora.setRazonSocial(getQueryNombreProveedor(request).trim());
						OrganizacionProveedora.insertarNuevoProveedorEnBD(nuevaOrgProveedora);
						nuevaOperacionEgreso.setProveedor(nuevaOrgProveedora);
					}
				}

				OperacionEgreso.insertarNuevoEgresoEnBD(nuevaOperacionEgreso);
				nuevaOperacionEgreso.setMontoTotal(nuevaOperacionEgreso.getValorDeEgreso());
				OperacionEgresoMapperBD.getInstance().update(nuevaOperacionEgreso);
				model.put("numeroEgreso", nuevaOperacionEgreso.getId());
				model.put("cargaEgresoExitosa", true);
				nuevaOperacionEgreso.getCompra().setId_operacion_egreso(nuevaOperacionEgreso.getId());
				CompraMapperBD.getInstance().update(nuevaOperacionEgreso.getCompra());
				model.remove("compras_disponibles");
				model.put("compras_disponibles",Compra.obtenerComprasSinEgresos());  

			} else {
				model.put("errorDatosIncompletos", true);
				return ViewUtil.render(request, model, Path.Template.EGRESOS);
			}
		} catch (NullPointerException e) {

		}
		return ViewUtil.render(request, model, Path.Template.EGRESOS);

	};

	public static Route mis_egresos = (Request request, Response response) -> {
		LoginController.ensureUserIsLoggedIn(request, response);

		// obtencion, generacion del Modelo (MVC)
		HashMap<String, Object> model = new HashMap<>();
		List<OperacionEgreso> egresos = OperacionEgreso.buscarEgresosLazy();

		DecimalFormat formatoPrecio = new DecimalFormat("#.##");
		model.put("formato", formatoPrecio);
		model.put("egresos", egresos);
		return ViewUtil.render(request, model, Path.Template.MIS_EGRESOS);

	};
	
	
	
}
