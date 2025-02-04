package it.webred.cs.csa.web.manbean.scheda.parenti;

import it.webred.cs.csa.ejb.client.AccessTableConfigurazioneSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableIterStepSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSchedaSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.ComponenteDTO;
import it.webred.cs.csa.ejb.dto.StatoCartellaDTO;
import it.webred.cs.data.model.CsAAnagrafica;
import it.webred.cs.data.model.CsAComponente;
import it.webred.cs.data.model.CsAFamigliaGruppo;
import it.webred.cs.data.model.CsASoggettoLAZY;
import it.webred.cs.data.model.CsTbContatto;
import it.webred.cs.data.model.CsTbDisponibilita;
import it.webred.cs.data.model.CsTbPotesta;
import it.webred.cs.data.model.CsTbTipoRapportoCon;
import it.webred.cs.jsf.bean.ValiditaCompBaseBean;
import it.webred.cs.jsf.interfaces.IParenti;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ct.config.model.AmTabNazioni;
import it.webred.ejb.utility.ClientUtility;
import it.webred.jsf.bean.ComuneBean;
import it.webred.jsf.bean.SessoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

public class ParentiComp extends ValiditaCompBaseBean implements IParenti {

	private List<CsAComponente> lstParenti = new ArrayList<CsAComponente>();
	private List<CsAComponente> lstConoscenti = new ArrayList<CsAComponente>();
	private HashMap<String,String> mappaSchedeCS = new HashMap<String,String>();

	private List<CsAAnagrafica> lstComponentiCalc;
	private List<SelectItem> lstComponentiCalcItems;
	private boolean loadedComponentiCalc = false;
	
	protected AccessTableSchedaSessionBeanRemote schedaService = (AccessTableSchedaSessionBeanRemote) getCarSocialeEjb("AccessTableSchedaSessionBean");
	protected AccessTableSoggettoSessionBeanRemote soggettoService = (AccessTableSoggettoSessionBeanRemote) getCarSocialeEjb("AccessTableSoggettoSessionBean");
	private AccessTableConfigurazioneSessionBeanRemote confService = (AccessTableConfigurazioneSessionBeanRemote) getCarSocialeEjb("AccessTableConfigurazioneSessionBean");

	private int idxSelected;
	private boolean nuovo;
	private String viveSolo;
	private String haParenti;
	private String parentiSconosciuti;
	
	private Integer disponibilitaDatiAnagraficiRisorse;
	
	// private String colorR;

	public String getViveSolo() {
		return viveSolo;
	}

	public void setViveSolo(String viveSolo) {
		this.viveSolo = viveSolo;
	}

	public String getHaParenti() {
		return haParenti;
	}

	public void setHaParenti(String haParenti) {
		this.haParenti = haParenti;
	}

	public String getParentiSconosciuti() {
		return parentiSconosciuti;
	}

	public void setParentiSconosciuti(String parentiSconosciuti) {
		this.parentiSconosciuti = parentiSconosciuti;
	}

	private boolean showNewParente = false;
	private boolean showNewConoscente = false;
	private NuovoParenteBean nuovoParenteBean = new NuovoParenteBean();
	private NuovoConoscenteBean nuovoConoscenteBean = new NuovoConoscenteBean();

	private CsAAnagrafica anagraficaCompDaAltraScheda = new CsAAnagrafica();
	private Long anagraficaCompDaAltraScheda_id = null;
//	// TODO solo in debug
//	private Long ComponenteIdDaAltraScheda = new Long(0);
	
	// SISO-724 mappa CF - stato cartella
	private HashMap<String, String> cf2StatoCartella = new HashMap<String, String>();

	@Override
	public void salvaNuovoParente() {

		CsAComponente comp = null;
		CsAAnagrafica compAna = null;
		CsTbTipoRapportoCon compTipoRapporto = null;
		boolean esisteCF = false;
		if (nuovo) {
			comp = new CsAComponente();
			compAna = new CsAAnagrafica();
			compTipoRapporto = new CsTbTipoRapportoCon();
			
			//Verifico che il CF non sia già presente
			int i = 0;
			if(nuovoParenteBean.getCodFiscale()!=null && !nuovoParenteBean.getCodFiscale().isEmpty()){
				while(i<lstParenti.size() && !esisteCF){
					String cf = ((CsAComponente)lstParenti.get(i)).getCsAAnagrafica().getCf();
					if(cf!=null && !cf.isEmpty() && nuovoParenteBean.getCodFiscale().toUpperCase().equals(cf.toUpperCase())){
						esisteCF = true;
						this.addError("Il soggetto con cod.fiscale '"+nuovoParenteBean.getCodFiscale()+"' è già presente", "");
						return;
					}
					i++; //BUG FIX MOD-RL 2016-10-21
				}
			}
			
		} else {
			comp = lstParenti.get(idxSelected);
			compAna = comp.getCsAAnagrafica();
			compTipoRapporto = comp.getCsTbTipoRapportoCon();
		}

		compAna.setCf(nuovoParenteBean.getCodFiscale());
		compAna.setCognome(nuovoParenteBean.getCognome());
		comp.setDtIns(new Date());
		comp.setIndirizzoRes(nuovoParenteBean.getIndirizzo());
		compAna.setNome(nuovoParenteBean.getNome());
		comp.setNumCivRes(nuovoParenteBean.getCivico());
		compAna.setSesso(nuovoParenteBean.getDatiSesso().getSesso());
		comp.setUserIns(getCurrentUsername());
		compAna.setCell(nuovoParenteBean.getCellulare());
		compAna.setCittadinanza(nuovoParenteBean.getCittadinanza());
		comp.setConvivente(nuovoParenteBean.getConvivente());
		CsTbContatto contatto = new CsTbContatto();
		contatto.setId(nuovoParenteBean.getIdContatto());
		comp.setCsTbContatto(contatto);
		comp.setCsTbDisponibilita(null);
		Long idDisponibilita = nuovoParenteBean.getIdDisponibilita();
		if (idDisponibilita != null && idDisponibilita.intValue() != 0) {
			BaseDTO b = new BaseDTO();
			fillEnte(b);
			b.setObj(idDisponibilita);
			CsTbDisponibilita disponibilita = confService
					.getDisponibilitaById(b);
			comp.setCsTbDisponibilita(disponibilita);
		}
		compTipoRapporto = nuovoParenteBean.getRapportoModel();
		comp.setCsTbTipoRapportoCon(compTipoRapporto);
		CsTbPotesta potesta = new CsTbPotesta();
		potesta.setId(nuovoParenteBean.getIdPotesta());
		comp.setCsTbPotesta(potesta);
		
		comp.setProfessioneId(nuovoParenteBean.getFormLavoroMan().getIdProfessione());
		comp.setCondLavorativaId(nuovoParenteBean.getFormLavoroMan().getIdCondLavorativa());
		
		compAna.setDataDecesso(nuovoParenteBean.getDataDecesso());
		nuovoParenteBean.setDecesso(compAna.getDataDecesso() != null);
		compAna.setDataNascita(nuovoParenteBean.getDataNascita());
		compAna.setEmail(nuovoParenteBean.getEmail());
		comp.setIndirizzoRes(nuovoParenteBean.getIndirizzo());
		compAna.setNote(nuovoParenteBean.getNote());
		comp.setNumCivRes(nuovoParenteBean.getCivico());
		compAna.setTel(nuovoParenteBean.getTelefono());
		if (nuovoParenteBean.getComuneNazioneNascitaBean().getComuneMan()
				.getComune() != null) {
			compAna.setComuneNascitaCod(nuovoParenteBean
					.getComuneNazioneNascitaBean().getComuneMan().getComune()
					.getCodIstatComune());
			compAna.setComuneNascitaDes(nuovoParenteBean
					.getComuneNazioneNascitaBean().getComuneMan().getComune()
					.getDenominazione());
			compAna.setProvNascitaCod(nuovoParenteBean
					.getComuneNazioneNascitaBean().getComuneMan().getComune()
					.getSiglaProv());
		} else {
			compAna.setComuneNascitaCod(null);
			compAna.setComuneNascitaDes(null);
			compAna.setProvNascitaCod(null);
		}
		if (nuovoParenteBean.getComuneNazioneNascitaBean().getNazioneMan()
				.getNazione() != null) {
			compAna.setStatoNascitaCod(nuovoParenteBean
					.getComuneNazioneNascitaBean().getNazioneMan().getNazione()
					.getCodIstatNazione());
			compAna.setStatoNascitaDes(nuovoParenteBean
					.getComuneNazioneNascitaBean().getNazioneMan().getNazione()
					.getNazione());
		} else {
			compAna.setStatoNascitaCod(null);
			compAna.setStatoNascitaDes(null);
		}
		if (nuovoParenteBean.getComuneNazioneResidenzaBean().getComuneMan()
				.getComune() != null) {
			comp.setComResCod(nuovoParenteBean.getComuneNazioneResidenzaBean()
					.getComuneMan().getComune().getCodIstatComune());
			comp.setComResDes(nuovoParenteBean.getComuneNazioneResidenzaBean()
					.getComuneMan().getComune().getDenominazione());
			comp.setProvResCod(nuovoParenteBean.getComuneNazioneResidenzaBean()
					.getComuneMan().getComune().getSiglaProv());
		} else {
			comp.setComResCod(null);
			comp.setComResDes(null);
			comp.setProvResCod(null);
		}
		if (nuovoParenteBean.getComuneNazioneResidenzaBean().getNazioneMan()
				.getNazione() != null) {
			comp.setComAltroCod(nuovoParenteBean
					.getComuneNazioneResidenzaBean().getNazioneMan()
					.getNazione().getCodIstatNazione());
			comp.setComAltroDes(nuovoParenteBean
					.getComuneNazioneResidenzaBean().getNazioneMan()
					.getNazione().getNazione());
		} else {
			comp.setComAltroCod(null);
			comp.setComAltroDes(null);
		}
		comp.setCsAAnagrafica(compAna);

		if (nuovo && !esisteCF)
			lstParenti.add(comp);

		setShowNewParente(false);

	}

	@Override
	public String calcoloEta(Date dateN, Date dataDecesso) {
		String eta = "-";
		if (dateN != null) {
			long today = System.currentTimeMillis();
			Date dataToday = dataDecesso != null ? dataDecesso
					: new Date(today);
			long age = dataToday.getTime() - dateN.getTime();
			int days = (int) Math.round(age / 86400000.0);
			int ageFinal = (days / 365);
			eta = dataDecesso != null ? "Deceduto a "
					+ Integer.toString(ageFinal) + " anni" : Integer
					.toString(ageFinal);
		}
		return eta;
	}

	
	@Override
	public void salvaNuovoConoscente() {

		CsAComponente comp = null;
		CsAAnagrafica compAna = null;
		CsTbTipoRapportoCon compTipoRapporto = null;
		if (nuovo) {
			comp = new CsAComponente();
			compAna = new CsAAnagrafica();
			compTipoRapporto = new CsTbTipoRapportoCon();
		} else {
			comp = lstConoscenti.get(idxSelected);
			compAna = comp.getCsAAnagrafica();
			compTipoRapporto = comp.getCsTbTipoRapportoCon();
		}

		compAna.setCognome(nuovoConoscenteBean.getCognome());
		compAna.setNome(nuovoConoscenteBean.getNome());
		compAna.setCf(nuovoConoscenteBean.getCodFiscale());
		compAna.setSesso(nuovoConoscenteBean.getDatiSesso().getSesso());
		compAna.setDataNascita(nuovoConoscenteBean.getDataNascita());
		if (nuovoConoscenteBean.getComuneNazioneNascitaBean().getComuneMan().getComune() != null) {
			compAna.setComuneNascitaCod(nuovoConoscenteBean
					.getComuneNazioneNascitaBean().getComuneMan().getComune()
					.getCodIstatComune());
			compAna.setComuneNascitaDes(nuovoConoscenteBean
					.getComuneNazioneNascitaBean().getComuneMan().getComune()
					.getDenominazione());
			compAna.setProvNascitaCod(nuovoConoscenteBean
					.getComuneNazioneNascitaBean().getComuneMan().getComune()
					.getSiglaProv());
		} else {
			compAna.setComuneNascitaCod(null);
			compAna.setComuneNascitaDes(null);
			compAna.setProvNascitaCod(null);
		}
		
		compAna.setTel(nuovoConoscenteBean.getTelefono());
		compAna.setCell(nuovoConoscenteBean.getCellulare());
		compAna.setEmail(nuovoConoscenteBean.getEmail());
		compAna.setNote(nuovoConoscenteBean.getNote());
		
		
		comp.setDtIns(new Date());
		comp.setIndirizzoRes(nuovoConoscenteBean.getIndirizzo());
		comp.setNumCivRes(nuovoConoscenteBean.getCivico());
		comp.setUserIns(getCurrentUsername());
		compTipoRapporto = nuovoConoscenteBean.getRapportoModel();
		comp.setCsTbTipoRapportoCon(compTipoRapporto);
		comp.setConvivente(nuovoConoscenteBean.getConvivente());
		comp.setIndirizzoRes(nuovoConoscenteBean.getIndirizzo());
		
		comp.setNumCivRes(nuovoConoscenteBean.getCivico());
		
		if (nuovoConoscenteBean.getComuneNazioneResidenzaBean().getComuneMan()
				.getComune() != null) {
			comp.setComResCod(nuovoConoscenteBean
					.getComuneNazioneResidenzaBean().getComuneMan().getComune()
					.getCodIstatComune());
			comp.setComResDes(nuovoConoscenteBean
					.getComuneNazioneResidenzaBean().getComuneMan().getComune()
					.getDenominazione());
			comp.setProvResCod(nuovoConoscenteBean
					.getComuneNazioneResidenzaBean().getComuneMan().getComune()
					.getSiglaProv());
		} else {
			comp.setComResCod(null);
			comp.setComResDes(null);
			comp.setProvResCod(null);
		}
		if (nuovoConoscenteBean.getComuneNazioneResidenzaBean().getNazioneMan().getNazione() != null) {
			comp.setComAltroCod(nuovoConoscenteBean
					.getComuneNazioneResidenzaBean().getNazioneMan()
					.getNazione().getCodIstatNazione());
			comp.setComAltroDes(nuovoConoscenteBean
					.getComuneNazioneResidenzaBean().getNazioneMan()
					.getNazione().getNazione());
		} else {
			comp.setComAltroCod(null);
			comp.setComAltroDes(null);
		}
		comp.setCsAAnagrafica(compAna);

		
		
		if (nuovo)
			lstConoscenti.add(comp);

		setShowNewConoscente(false);

	}
	
	private void loadModificaAnagrafica(CsAComponente from, AnagraficaNucleoBean to){
		
		to.setCognome(from.getCsAAnagrafica().getCognome());
		to.setNome(from.getCsAAnagrafica().getNome());
		to.setCodFiscale(from.getCsAAnagrafica().getCf());
		to.setDatiSesso(new SessoBean(from.getCsAAnagrafica().getSesso()));
		to.setDataNascita(from.getCsAAnagrafica().getDataNascita());
		
		to.setTelefono(from.getCsAAnagrafica().getTel());
		to.setCellulare(from.getCsAAnagrafica().getCell());
		to.setEmail(from.getCsAAnagrafica().getEmail());
				
		to.setIndirizzo(from.getIndirizzoRes());
		to.setCivico(from.getNumCivRes());
		
		ComuneBean comuneRes = new ComuneBean(from.getComResCod(),from.getComResDes(), from.getProvResCod());
		to.getComuneNazioneResidenzaBean().getComuneMan().setComune(comuneRes);
		
		if (from.getComAltroCod() != null) {
			AmTabNazioni amTabNazioni = new AmTabNazioni();
			amTabNazioni.setCodIstatNazione(from.getComAltroCod());
			amTabNazioni.setNazione(from.getComAltroDes());
			to.getComuneNazioneResidenzaBean().setValue(
					to.getComuneNazioneResidenzaBean().getNazioneValue());
			to.getComuneNazioneResidenzaBean().getNazioneMan().setNazione(amTabNazioni);
		}
		
		to.setNote(from.getCsAAnagrafica().getNote());
		
		ComuneBean comuneNas = new ComuneBean(from.getCsAAnagrafica()
				.getComuneNascitaCod(), from.getCsAAnagrafica()
				.getComuneNascitaDes(), from.getCsAAnagrafica()
				.getProvNascitaCod());
		to.getComuneNazioneNascitaBean().getComuneMan().setComune(comuneNas);
		if (from.getCsAAnagrafica().getStatoNascitaCod() != null) {
			AmTabNazioni amTabNazioni = new AmTabNazioni();
			amTabNazioni.setCodIstatNazione(from.getCsAAnagrafica().getStatoNascitaCod());
			amTabNazioni.setNazione(from.getCsAAnagrafica().getStatoNascitaDes());
			to.getComuneNazioneNascitaBean().setValue(
					to.getComuneNazioneNascitaBean().getNazioneValue());
			to.getComuneNazioneNascitaBean().getNazioneMan().setNazione(amTabNazioni);
		}
		
		if (from.getCsTbTipoRapportoCon() != null)
			to.setIdParentela(from.getCsTbTipoRapportoCon().getId());
		
	}

	@Override
	public void loadModificaParente() {
		CsAComponente comp = lstParenti.get(idxSelected);
		this.loadModificaAnagrafica(comp, nuovoParenteBean);
		nuovoParenteBean.setCittadinanza(comp.getCsAAnagrafica().getCittadinanza());
		nuovoParenteBean.setConvivente(comp.getConvivente());
		nuovoParenteBean.setDataDecesso(comp.getCsAAnagrafica().getDataDecesso());
		nuovoParenteBean.setDecesso(comp.getCsAAnagrafica().getDataDecesso() != null);

		if (comp.getCsTbContatto() != null)
			nuovoParenteBean.setIdContatto(comp.getCsTbContatto().getId());
		if (comp.getCsTbDisponibilita() != null)
			nuovoParenteBean.setIdDisponibilita(comp.getCsTbDisponibilita().getId());
		if (comp.getCsTbPotesta() != null)
			nuovoParenteBean.setIdPotesta(comp.getCsTbPotesta().getId());

		nuovoParenteBean.getFormLavoroMan().setIdCondLavorativa(comp.getCondLavorativaId());
		nuovoParenteBean.getFormLavoroMan().setIdProfessione(comp.getProfessioneId());
		
	}

	@Override
	public void loadModificaConoscente() {
		CsAComponente comp = lstConoscenti.get(idxSelected);
		this.loadModificaAnagrafica(comp, nuovoConoscenteBean);
	}

	@Override
	public void eliminaParente() {
		lstParenti.remove(idxSelected);
	}

	@Override
	public void eliminaConoscente() {

		lstConoscenti.remove(idxSelected);

	}

	public void setLstComponentiCalc(List<CsAAnagrafica> lstComponentiCalc) {
		this.lstComponentiCalc = lstComponentiCalc;
	}

	public List<SelectItem> getLstComponentiCalc() {
		if (!loadedComponentiCalc) {
			loadLstComponentiCalc();
		}
		return this.lstComponentiCalcItems;
	}
	
	
	public void nuovoDaAnagrafeWrapper(String idSoggetto){
		//TODO recupero anagrafica del soggetto
		BaseDTO dto=new BaseDTO();
		fillEnte(dto);
		dto.setObj(idSoggetto);
		CsASoggettoLAZY soggettoTrovato=soggettoService.getSoggettoById(dto);
		if(soggettoTrovato!=null && soggettoTrovato.getAnagraficaId()>0)
		{
			CsAAnagrafica anagraficaSoggettoTrovato=soggettoTrovato.getCsAAnagrafica();
			nuovoParenteBean.precaricaParenteDaComponente(anagraficaSoggettoTrovato);
			this.nuovo = true;
			this.showNewParente = true;
			
		}
		
	}
	
	private boolean famigliaGrupposContainsFamigliaById(List<CsAFamigliaGruppo> famiglie,Long famigliaCurrId)
	{
		boolean result=false;
		for (CsAFamigliaGruppo csAFamigliaGruppo : famiglie) {
			if(csAFamigliaGruppo.getId().equals(famigliaCurrId))
			{
				result=true;
			}
		}
		
		return result;
	}
	private void loadLstComponentiCalc() {
		
		CsASoggettoLAZY soggetto = (CsASoggettoLAZY) getSession().getAttribute("soggetto");

		this.lstComponentiCalc = new ArrayList<CsAAnagrafica>();
		BaseDTO dto = new BaseDTO();
		fillEnte(dto);
		dto.setObj(soggetto.getCsAAnagrafica().getCf());
		dto.setObj2(new Date());
		lstComponentiCalc=schedaService.findComponentiGiaFamigliariBySoggettoCf(dto);
		
		List<SelectItem> items=new ArrayList<SelectItem>();
		HashMap<CsASoggettoLAZY,List<SelectItem>> Schede=new HashMap<CsASoggettoLAZY, List<SelectItem>>();
		if(lstComponentiCalc!=null)
			{
			for (CsAAnagrafica csAAnagraficaCurr : lstComponentiCalc) {
				if(csAAnagraficaCurr.getCsASoggetto()!=null && csAAnagraficaCurr.getCsASoggetto().getCsACaso()!=null)
				{
					Schede.put(csAAnagraficaCurr.getCsASoggetto(), new ArrayList<SelectItem>());
				}
			}
			for (CsAAnagrafica csAAnagraficaCurr : lstComponentiCalc) {
				SelectItem sel=new SelectItem();
				sel.setLabel(csAAnagraficaCurr.getCognome()+" - "+csAAnagraficaCurr.getNome());
				sel.setValue(csAAnagraficaCurr.getId());
				for (CsASoggettoLAZY soggettoCurr : Schede.keySet()) {
					if(csAAnagraficaCurr.getCsAComponente()!=null && csAAnagraficaCurr.getCsAComponente().getCsAFamigliaGruppo()!=null && famigliaGrupposContainsFamigliaById(soggettoCurr.getCsAFamigliaGruppos(), csAAnagraficaCurr.getCsAComponente().getCsAFamigliaGruppo().getId()))
					{
						Schede.get(soggettoCurr).add(sel);
					}
					else if(csAAnagraficaCurr.getCsASoggetto()!=null && csAAnagraficaCurr.getCsASoggetto()==soggettoCurr)
					{
						//è il soggetto titolare della scheda
						Schede.get(soggettoCurr).add(sel);
					}
				}
				
			}
				for (CsASoggettoLAZY soggettoGruppo : Schede.keySet()) {
					SelectItemGroup gruppoNew=new SelectItemGroup("Scheda: "+soggettoGruppo.getCsACaso().getId());
					SelectItem[] itemsDelGruppo=new SelectItem[Schede.get(soggettoGruppo).size()];
					int i=0;
					for (SelectItem selectItem : Schede.get(soggettoGruppo)) {
						if(selectItem.getValue().equals(soggettoGruppo.getAnagraficaId()))
						{
							selectItem.setLabel(selectItem.getLabel()+" (*tit.scheda)");
						}
						itemsDelGruppo[i]=selectItem;
						i++;
					}
					gruppoNew.setSelectItems(itemsDelGruppo);
					items.add(gruppoNew);
				}
			}
		this.lstComponentiCalcItems=items;
		this.setLoadedComponentiCalc(true);
	}

	public void setLoadedComponentiCalc(boolean loadedComponentiCalc) {
		this.loadedComponentiCalc = loadedComponentiCalc;
	}

	@Override
	public List<CsAComponente> getLstParenti() {
		return lstParenti;

	}

	public void setLstParenti(List<CsAComponente> lstParenti) {
		this.lstParenti = lstParenti;
	}

	@Override
	public int getIdxSelected() {
		return idxSelected;
	}

	public void setIdxSelected(int idxSelected) {
		this.idxSelected = idxSelected;
	}

	public NuovoParenteBean getNuovoParenteBean() {
		return nuovoParenteBean;
	}

	public void setNuovoParenteBean(NuovoParenteBean nuovoParenteBean) {
		this.nuovoParenteBean = nuovoParenteBean;
	}

	@Override
	public List<CsAComponente> getLstConoscenti() {
		return lstConoscenti;
	}

	public void setLstConoscenti(List<CsAComponente> lstConoscenti) {
		this.lstConoscenti = lstConoscenti;
	}

	public NuovoConoscenteBean getNuovoConoscenteBean() {
		return nuovoConoscenteBean;
	}

	public void setNuovoConoscenteBean(NuovoConoscenteBean nuovoConoscenteBean) {
		this.nuovoConoscenteBean = nuovoConoscenteBean;
	}

	@Override
	public boolean isNuovo() {
		return nuovo;
	}

	public void setNuovo(boolean nuovo) {
		this.nuovo = nuovo;
	}

	public boolean isShowNewParente() {
		return showNewParente;
	}

	public void setShowNewParente(boolean showNewParente) {
		this.showNewParente = showNewParente;
	}

	public boolean isShowNewConoscente() {
		return showNewConoscente;
	}

	public void setShowNewConoscente(boolean showNewConoscente) {
		this.showNewConoscente = showNewConoscente;
	}

	public Long getAnagraficaCompDaAltraScheda_id() {
		return this.anagraficaCompDaAltraScheda_id;
	}

	public void setAnagraficaCompDaAltraScheda_id(Long anagraficaCompDaAltraScheda_id) {
		
		this.anagraficaCompDaAltraScheda_id=anagraficaCompDaAltraScheda_id;
		CsAAnagrafica anagraficaCompDaAltraSchedaSel=null;
		if(anagraficaCompDaAltraScheda_id!=null && anagraficaCompDaAltraScheda_id>0)
			{
					BaseDTO dto=new BaseDTO();
					fillEnte(dto);
					dto.setObj(anagraficaCompDaAltraScheda_id);
					anagraficaCompDaAltraSchedaSel= soggettoService.getAnagraficaById(dto);
			}
		if (anagraficaCompDaAltraSchedaSel != null
				&& anagraficaCompDaAltraSchedaSel.getId() > 0) {
			this.anagraficaCompDaAltraScheda = anagraficaCompDaAltraSchedaSel;
			this.nuovo = true;
			this.showNewParente = true;
			nuovoParenteBean
					.precaricaParenteDaComponente(this.anagraficaCompDaAltraScheda);
		} else {
			this.anagraficaCompDaAltraScheda = new CsAAnagrafica();
			this.nuovo = false;
			this.showNewParente = false;
			nuovoParenteBean.reset();
		}
	}
	
	public String loadStatoCartella(String cf){
		boolean exists = cf2StatoCartella.containsKey(cf);
		String s = null;
		
		if(!exists){
			s = super.loadStatoCartella(cf);
			cf2StatoCartella.put(cf, s);
		}else{
			s = cf2StatoCartella.get(cf);	
		}
		
		if(s!=null && !s.isEmpty())
			return "<b>Cartella Sociale esistente</b>"+ s;
		else return s;
	}

	@Override
	public Boolean isHaParenti() {
		Boolean val = null;
		if(this.haParenti!=null) 
			val = this.haParenti.equalsIgnoreCase("true") ? true : false;
		return val;
	}

	@Override
	public Boolean isParentiSconosciuti() {
		Boolean val = null;
		if(this.parentiSconosciuti!=null) 
			val = this.parentiSconosciuti.equalsIgnoreCase("true") ? true : false;
		return val;
	}

	@Override
	public Boolean isViveSolo() {
		Boolean val = null;
		if(this.viveSolo!=null) 
			val = this.viveSolo.equalsIgnoreCase("true") ? true : false;
		return val;
	}

	public Integer getDisponibilitaDatiAnagraficiRisorse() {
		return disponibilitaDatiAnagraficiRisorse;
	}

	public void setDisponibilitaDatiAnagraficiRisorse(
			Integer disponibilitaDatiAnagraficiRisorse) {
		this.disponibilitaDatiAnagraficiRisorse = disponibilitaDatiAnagraficiRisorse;
	}

	

	
}
