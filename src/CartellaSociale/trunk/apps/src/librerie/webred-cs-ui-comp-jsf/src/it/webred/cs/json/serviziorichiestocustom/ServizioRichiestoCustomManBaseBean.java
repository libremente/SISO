package it.webred.cs.json.serviziorichiestocustom;

import it.webred.classfactory.WebredClassFactory;
import it.webred.cs.csa.ejb.client.AccessTableDiarioSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableDocumentoSessionBeanRemote; 
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.DocIndividualeDTO;
import it.webred.cs.data.DataModelCostanti; 
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDDocIndividuale;
import it.webred.cs.data.model.CsDValutazione; 
import it.webred.cs.data.model.CsLoadDocumento;
import it.webred.cs.json.ISchedaValutazione;
import it.webred.cs.json.SchedaValutazioneManBean;
import it.webred.cs.json.OrientamentoLavoro.IOrientamentoLavoro;
import it.webred.cs.json.intermediazione.IIntermediazioneAb;
import it.webred.cs.json.mediazioneculturale.IMediazioneCult;
import it.webred.cs.json.orientamentoistruzione.IOrientamentoIstruzione;
import it.webred.ejb.utility.ClientUtility;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.sun.mail.iap.ByteArray;

/**
 * SISO-438
 */
public abstract class  ServizioRichiestoCustomManBaseBean extends SchedaValutazioneManBean implements IServizioRichiestoCustom{
	private static final long serialVersionUID = -3614523007196464172L;
//	private AccessTableInterventoSessionBeanRemote interventoService = getAccessTableInterventoService();
	private AccessTableDiarioSessionBeanRemote diarioService = getAccessTableDiarioService();
	private AccessTableDocumentoSessionBeanRemote documentoService = getAccessTableDocumentoService();
	
//	boolean loadToClone = false;
	private List<ServizioRichiestoDocumentoAllegato> listaDocumentiDaSalvare;
	private List<ServizioRichiestoDocumentoAllegato> listaDocumentiSalvati;

//    private StreamedContent fileDownload;
	
	public ServizioRichiestoCustomManBaseBean() {
		listaDocumentiDaSalvare = new ArrayList<ServizioRichiestoDocumentoAllegato>();
		listaDocumentiSalvati = new ArrayList<ServizioRichiestoDocumentoAllegato>();
	}
	
	
	private AccessTableDocumentoSessionBeanRemote getAccessTableDocumentoService() { 
		try {
			if (documentoService == null) {
				documentoService = (AccessTableDocumentoSessionBeanRemote) 
							ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableDocumentoSessionBean");
			}
		} catch(Exception e){ 
			logger.error(e.getMessage(), e);
		}
			
		return documentoService;
	}


	private AccessTableDiarioSessionBeanRemote getAccessTableDiarioService() {
		try {
			if (diarioService == null) {
				diarioService = (AccessTableDiarioSessionBeanRemote) 
							ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableDiarioSessionBean");
			}
		} catch(Exception e){ 
			logger.error(e.getMessage(), e);
		}
			
		return diarioService;
	}


	public void eliminaDocumenti() throws Exception {
		for (ServizioRichiestoDocumentoAllegato documentoAllegato: listaDocumentiSalvati) {
			eliminaServizioRichiestoDocumentoAllegato(documentoAllegato);
		}
	}
	

	public void saveDocumenti(Long diarioId, Long casoId) throws Exception {

		for (ServizioRichiestoDocumentoAllegato documentoAllegato : listaDocumentiDaSalvare) {
			if (!listaDocumentiSalvati.contains(documentoAllegato)) {
				salvaServizioRichiestoDocumentoAllegato(documentoAllegato, diarioId, casoId);
			}
		}
		
		List<ServizioRichiestoDocumentoAllegato> documentiDaCancellare = getDocumentiDaCancellare();


		for (ServizioRichiestoDocumentoAllegato documentoDaCancellare : documentiDaCancellare) {
				eliminaServizioRichiestoDocumentoAllegato(documentoDaCancellare);				
			}
		
 
		initDocumenti(diarioId);		
//		listaDocumentiSalvati.clear();
//		listaDocumentiSalvati.addAll(listaDocumentiDaSalvare);
		
	}
	
	private void eliminaServizioRichiestoDocumentoAllegato(
			ServizioRichiestoDocumentoAllegato documentoDaCancellare) throws Exception {

		BaseDTO dto = new BaseDTO();
		fillEnte(dto);
		dto.setObj(documentoDaCancellare.getDiarioId());
		CsDDocIndividuale docIndividuale = diarioService.findDocIndividualeById(dto);
		dto = new BaseDTO();
		fillEnte(dto);
		dto.setObj(docIndividuale); 
		diarioService.deleteDocIndividuale(dto);
	}


	private void salvaServizioRichiestoDocumentoAllegato(
			ServizioRichiestoDocumentoAllegato documentoAllegato, Long diarioId,  Long casoId) throws Exception { 
		
		DocIndividualeDTO dto = new DocIndividualeDTO(); 
		fillEnte(dto); 
	//	dto.setIdDiarioRichiestaServizio(documentoAllegato.getDiarioId());
	//	dto.setUserIns(userIns); 
		dto.setNome(documentoAllegato.getNome());
		dto.setTipo(documentoAllegato.getContentType());
		dto.setDocumento(documentoAllegato.getContents());
		dto.setIdDiarioRichiestaServizio(diarioId);
		dto.setCasoId(casoId);
		dto.setSottoCartella(DataModelCostanti.CsTbSottocartellaDoc.ID_ALTRO);
		dto.setLetto(false);
		dto.setPrivato(false);
		
		diarioService.createAndsaveDocIndividuale(dto);
		
	}


	public void cloneDocumenti(IServizioRichiestoCustom iServizioRichiestoCustom){ 
		for (ServizioRichiestoDocumentoAllegato documentoAllegato : iServizioRichiestoCustom.getListaDocumentiSalvati()) {
			documentoAllegato.setNome(documentoAllegato.getNome());
			documentoAllegato.setContents(documentoAllegato.getContents()); 
			documentoAllegato.setContentType(documentoAllegato.getContentType());
//			documentoAllegato.setDiarioId(docIndividuale.getCsDDiario().getId()); 			
			listaDocumentiDaSalvare.add(documentoAllegato);
		}
		
	}
	
	//SISO-659 interventi erogati in accoglienza
	protected void initConsuntivo(long richiestaServizioDiarioId) throws Exception{  
		
	}
	
	protected void initDocumenti(long richiestaServizioDiarioId) throws Exception{  
		listaDocumentiDaSalvare.clear();;
		listaDocumentiSalvati.clear();;
		 
		it.webred.cs.csa.ejb.dto.BaseDTO dto = new it.webred.cs.csa.ejb.dto.BaseDTO();
		fillEnte(dto); 
		dto.setObj( richiestaServizioDiarioId);
		
		CsDDiario diarioConFigli = diarioService.findDiarioById(dto);
		 
		List<CsDDiario> diariFiglio = diarioConFigli.getCsDDiariFiglio();
		
		for (CsDDiario figlio : diariFiglio) {
			if (figlio.getCsTbTipoDiario().getId() == DataModelCostanti.TipoDiario.DOC_INDIVIDUALE_ID) {
 
				
				dto = new it.webred.cs.csa.ejb.dto.BaseDTO();
				fillEnte(dto); 
				dto.setObj(figlio.getId());
				CsLoadDocumento loadDoc = documentoService.findLoadDocumentoByDiarioId(dto);		
				CsDDocIndividuale docIndividuale = diarioService.findDocIndividualeById(dto);

				ServizioRichiestoDocumentoAllegato documentoAllegato = new ServizioRichiestoDocumentoAllegato();
				documentoAllegato.setNome(docIndividuale.getDescrizione());
				documentoAllegato.setContents(loadDoc.getDocumento()); 
				documentoAllegato.setContentType(loadDoc.getTipo());
				documentoAllegato.setDiarioId(docIndividuale.getCsDDiario().getId()); 
				
				listaDocumentiDaSalvare.add(documentoAllegato);
//				if (!loadToClone) {
				listaDocumentiSalvati.add(documentoAllegato);					
//				}
			}
		}
		
	}
	
	private List<ServizioRichiestoDocumentoAllegato> getDocumentiDaCancellare() {
		List<ServizioRichiestoDocumentoAllegato> result = new ArrayList<ServizioRichiestoDocumentoAllegato>();
		
		for (ServizioRichiestoDocumentoAllegato  documentoAllegatoSalvato: listaDocumentiSalvati) {

			boolean presente = false;
			for (ServizioRichiestoDocumentoAllegato  documentoAllegatoDaSalvare : listaDocumentiDaSalvare) {
				if (documentoAllegatoSalvato == documentoAllegatoDaSalvare) { 
					presente = true;
				}
			}
			if (!presente) {
				result.add(documentoAllegatoSalvato);
			}
						 
		}
		
		return result;
	}
	

	public abstract ServizioRichiestoCustomBaseBean getJsonCurrent();

	public String getTipoInterventoCustom() { 
		return getJsonCurrent().getTipoInterventoCustom();
	}
	 
	public int getTipoInterventoCustomId() {
		return getJsonCurrent().getTipoInterventoCustomId();
	}

	public void setTipoInterventoCustomId(int tipoInterventoCustomId) {
		this.getJsonCurrent().setTipoInterventoCustomId(tipoInterventoCustomId);
	}

	public void setTipoInterventoCustom(String tipoInterventoCustom) {
		this.getJsonCurrent().setTipoInterventoCustom(tipoInterventoCustom);
	}
	public void addFile(UploadedFile file) {
		ServizioRichiestoDocumentoAllegato doc = new ServizioRichiestoDocumentoAllegato();
		doc.setNome(file.getFileName());
		doc.setContents(file.getContents());
		doc.setContentType(file.getContentType()); 
		
		listaDocumentiDaSalvare.add(doc);
	} 
	

	public void eliminaDocumento(ServizioRichiestoDocumentoAllegato doc) {
		listaDocumentiDaSalvare.remove(doc);
	}

	
//	public void prepareDocStream(ServizioRichiestoDocumentoAllegato  doc) {
//		System.out.println(doc.getNome());
//        InputStream stream =  new ByteArrayInputStream(doc.getContents());
//        fileDownload = new DefaultStreamedContent(stream, 
//        						doc.getContentType(), //"image/jpg",
//        						doc.getNome() //"downloaded_optimus.jpg"
//        						);
//		
//	}
//
//    public StreamedContent getFileDownload() {
//        return fileDownload;
//    }
 
    
	
	public List<ServizioRichiestoDocumentoAllegato> getListaDocumentiDaSalvare() {
		return listaDocumentiDaSalvare;
	}

	public void setListaDocumentiDaSalvare(
			List<ServizioRichiestoDocumentoAllegato> listaDocumentiDaSalvare) {
		this.listaDocumentiDaSalvare = listaDocumentiDaSalvare;
	}

	public String getNumero(){
		return String.valueOf(listaDocumentiDaSalvare.size());
	}

	@Override
	public String toString() {
		return "ServizioRichiestoCustom [tipoInterventoCustom="
				+ getJsonCurrent().getTipoInterventoCustom() + " ]";
	}
	
	

	public static IServizioRichiestoCustom initByVersion(String defaultVersion) {
		IServizioRichiestoCustom man = null;
		try {
			man = (IServizioRichiestoCustom) WebredClassFactory.newInstance("", IServizioRichiestoCustom.class, defaultVersion != null ? defaultVersion : "");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return man;
	}

	public static IServizioRichiestoCustom initByModel(CsDValutazione val 
			//,boolean loadToClone
			) throws Exception {
		IServizioRichiestoCustom man = null;
		if (val != null) {

			String className = val.getVersioneScheda();

			// la versione di default è utile se si vuole comunque
			// instanziare una versione intermedia tra la prima e la max
			String defaultVersion = "";
			man = (IServizioRichiestoCustom) WebredClassFactory.newInstance(className, IServizioRichiestoCustom.class, defaultVersion);
//			man.setLoadToClone(loadToClone);
			man.init(null, val);
		}
		return man;
	}

	public static IServizioRichiestoCustom init(IServizioRichiestoCustom man){
		IServizioRichiestoCustom interfaccia = initByVersion("");
		interfaccia.init(man);
		return interfaccia;
	}
	

	public List<ServizioRichiestoDocumentoAllegato> getListaDocumentiSalvati(){
		return listaDocumentiSalvati;
	}


//	public boolean isLoadToClone() {
//		return loadToClone;
//	}
//
//
//	public void setLoadToClone(boolean loadToClone) {
//		this.loadToClone = loadToClone;
//	}
//	
	
	
}
