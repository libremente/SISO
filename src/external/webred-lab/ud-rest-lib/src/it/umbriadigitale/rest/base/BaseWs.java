package it.umbriadigitale.rest.base;

import it.umbriadigitale.rest.base.responseMan.ResponseManager;
import it.umbriadigitale.rest.dto.BaseBody;
import it.umbriadigitale.rest.dto.BaseRequest;
import it.umbriadigitale.rest.dto.BaseResponse;
import it.umbriadigitale.rest.service.BaseService;
import it.umbriadigitale.rest.service.BaseService.TipoServizioRest;
import it.umbriadigitale.rest.service.BaseServicePost;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;


public class BaseWs {
	

	private  String path;

	protected Logger logger = Logger.getLogger("ud-rest-lib");
	// package dela classe si implementazione dei servizi 
	String pack;
	
	public BaseWs (String pack)  {
		this.pack = pack;
		
	}
	
	@POST
    @Path("/{service}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response executeServicePOST(@PathParam("service") String service, @QueryParam("req") String req, @RequestBody String json) {
       // logger.info("executeServicePOST "+req);
        logger.info("executeServicePOST "+json);
		return executeService(service, req, json, it.umbriadigitale.rest.service.BaseServicePost.TipoServizioRest.POST);
	}
	
	@GET
    @Path("/{service}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response executeServiceGET(@PathParam("service") String service, @QueryParam("req") String req) {
		logger.info("executeServiceGET "+req);
		return executeService(service, req, it.umbriadigitale.rest.service.BaseService.TipoServizioRest.GET);
    }
	
	
	private Response executeService( String service, String req, TipoServizioRest tipoServizioRest) {
		
		logger.info(service + " - req:" + req);
		
		BaseResponse response = null;
		BaseBody  body = null;
		String fullPathServizio = pack.concat(".").concat(service);
		
		BaseService<BaseRequest, BaseResponse> servizio ;
		try {
			servizio = ServiceFactory.build(fullPathServizio, req);
		} catch (Exception e) {
			logger.error("Errore reperimento servizio",e);
			return ResponseManager.build(e, "Errore reperimento servizio");
		}
		
		try {
			// IL SERVIZIO VIENE CHIAMATO CON UN METODO NON CORRETTO
			if (!tipoServizioRest.equals(servizio.getTipoServizioRest()))
				return  ResponseManager.build(new Exception("Tipo Metodo non compatibile con servizio"), "TipoServizioRest non valido, metodo accettato:" + servizio.getTipoServizioRest());

		} catch (Exception e) {
			logger.error("TipoServizioRest non valido o chiamata con metodo errato",e);
			return ResponseManager.build(e, "Errore in controllo servizio");
		}

		
		
		try {
			
			response = servizio.execute();
		} catch (Exception e) {
			logger.error("Errore chiamata servizio",e);
			
			return ResponseManager.build(e, "Errore chiamata servizio");
	
	
		}
	
		return  ResponseManager.build(Status.OK,response);
	
	}
	
	private Response executeService( String service, String req, String sbody, it.umbriadigitale.rest.service.BaseServicePost.TipoServizioRest tipoServizioRest) {
		
		logger.info(service + " - req:" + req);
		logger.info(service + " - body:" + sbody);
		
		BaseResponse response = null;
		BaseBody  body = null;
		String fullPathServizio = pack.concat(".").concat(service);
		
		BaseServicePost<BaseRequest, BaseResponse, String> servizio ;
		try {
			servizio = ServiceFactory.build(fullPathServizio, req, sbody);
		} catch (Exception e) {
			logger.error("Errore reperimento servizio",e);
			return ResponseManager.build(e, "Errore reperimento servizio");
		}
		
		try {
			// IL SERVIZIO VIENE CHIAMATO CON UN METODO NON CORRETTO
			if (!tipoServizioRest.equals(servizio.getTipoServizioRest())){
				String msg = "Tipo Metodo non compatibile con servizio";
				logger.error(msg);
				return  ResponseManager.build(new Exception("Tipo Metodo non compatibile con servizio"), "TipoServizioRest non valido, metodo accettato:" + servizio.getTipoServizioRest());
			}
		} catch (Exception e) {
			logger.error("TipoServizioRest non valido o chiamata con metodo errato",e);
			return ResponseManager.build(e, "Errore in controllo servizio");
		}

		
		
		try {
			
			response = servizio.execute();
		} catch (Exception e) {
			logger.error("Errore chiamata servizio",e);
			
			return ResponseManager.build(e, "Errore chiamata servizio");
	
	
		}
	
		return  ResponseManager.build(Status.OK,response);
	
	}
	

	
}
