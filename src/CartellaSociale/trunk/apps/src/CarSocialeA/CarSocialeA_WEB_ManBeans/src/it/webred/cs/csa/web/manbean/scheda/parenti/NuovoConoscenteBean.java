package it.webred.cs.csa.web.manbean.scheda.parenti;

import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.data.model.CsTbTipoRapportoCon;
import it.webred.cs.jsf.interfaces.INuovoConoscente;
import it.webred.ct.support.datarouter.CeTBaseObject;

import java.util.List;


public class NuovoConoscenteBean extends AnagraficaNucleoBean implements INuovoConoscente{

	private AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getEjb("CarSocialeA", "CarSocialeA_EJB", "AccessTableConfigurazioneSessionBean");

	private String dialogHeader = "Anagrafica conoscente";
	
	private List<CsTbTipoRapportoCon> lstParentelaModel;
	
	@Override
	public void reset() {
		super.reset();
	}

	public String getDialogHeader() {
		return dialogHeader;
	}

	public void setDialogHeader(String dialogHeader) {
		this.dialogHeader = dialogHeader;
	}

	public List<CsTbTipoRapportoCon> getLstParentelaModel() {
		CeTBaseObject bo = new CeTBaseObject();
		fillEnte(bo);
		lstParentelaModel = confService.getTipoRapportoConConoscenti(bo);
		return lstParentelaModel;
	}

}
