package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.csa.ejb.client.CarSocialeServiceException;
import it.webred.cs.csa.ejb.dto.DiarioAnagraficaDTO;
import it.webred.cs.csa.ejb.dto.PaiDTO;
import it.webred.cs.csa.ejb.dto.PaiDTOExt;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsCDiarioConchi;
import it.webred.cs.data.model.CsCDiarioDove;
import it.webred.cs.data.model.CsCTipoColloquio;
import it.webred.cs.data.model.CsDClob;
import it.webred.cs.data.model.CsDColloquio;
import it.webred.cs.data.model.CsDColloquioBASIC;
import it.webred.cs.data.model.CsDDiario;
import it.webred.cs.data.model.CsDDiarioAna;
import it.webred.cs.data.model.CsDDiarioBASIC;
import it.webred.cs.data.model.CsDDiarioDoc;
import it.webred.cs.data.model.CsDDocIndividuale;
import it.webred.cs.data.model.CsDIsee;
import it.webred.cs.data.model.CsDPai;
import it.webred.cs.data.model.CsDRelazione;
import it.webred.cs.data.model.CsDScuola;
import it.webred.cs.data.model.CsDTriage;
import it.webred.cs.data.model.CsDValutazione;
import it.webred.cs.data.model.CsFlgIntervento;
import it.webred.cs.data.model.CsLoadDocumento;
import it.webred.cs.data.model.CsRelDiarioDiariorif;
import it.webred.cs.data.model.CsRelDiarioDiariorifPK;
import it.webred.cs.data.model.CsRelRelazioneProbl;
import it.webred.cs.data.model.CsRelRelazioneTipoint;
import it.webred.cs.data.model.CsRelSettCatsocEsclusiva;
import it.webred.cs.data.model.CsTbTipoDiario;
import it.webred.cs.data.model.CsTbTipoPai;
import it.webred.cs.data.model.affido.CsPaiAffido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Named
public class DiarioDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	public void loadPadreFiglioEntities(CsDDiario diario){
		if( diario != null ) {
			if(diario.getCsDDiariFiglio()!=null) diario.getCsDDiariFiglio().size();
			if(diario.getCsDDiariPadre()!=null)  diario.getCsDDiariPadre().size();
		}
	}
	
	public void loadDocEntities(CsDDiario diario) {
		if (diario != null) {
			diario.getCsDDiarioDocs().size();
		}
	}

	public List<PaiDTOExt> loadPaiEntities(CsDDiario diario) {
		List<PaiDTOExt> lstPai = new LinkedList<PaiDTOExt>();
		loadPadreFiglioEntities(diario);
		if (diario.getCsDDiariPadre() != null) {
			for (CsDDiario d : diario.getCsDDiariPadre()) {
				CsDPai pai = findPaiByDiarioId(d.getId());
				if (pai != null)
					lstPai.add(new PaiDTOExt(pai));
			}
		}
		return lstPai;
	}

	public CsDDiario newDiario(CsDDiario diario) {
		em.persist(diario);
		loadPadreFiglioEntities(diario);
		return diario;
	}

	public CsDDiario updateDiarioNR(CsDDiario diario) {
		em.merge(diario);
		return diario;
	}

	public CsDDiario updateDiario(CsDDiario diario) {
		diario = em.merge(diario);
		loadPadreFiglioEntities(diario);
		return diario;
	}

	public void saveDiarioChild(Object obj) {
		em.persist(obj);
	}

	@SuppressWarnings("unchecked")
	public List<CsDDiario> getDiarioByCaso(Long casoId) {
		Query q = em.createNamedQuery("CsDDiario.getDiarioByCasoId").setParameter("casoId", casoId);
		List<CsDDiario> lst = q.getResultList();
		for (CsDDiario d : lst)
			loadPadreFiglioEntities(d);

		return lst;
	}

	public List<CsDColloquioBASIC> getColloquios(Long idSoggetto) throws Exception {
		Query q = em.createNamedQuery("CsDColloquioBASIC.findBySoggetto");
		q.setParameter("idSoggetto", idSoggetto);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsDColloquioBASIC> findAllColloquio() {
		Query q = em.createNamedQuery("CsDColloquioBASIC.findAllColloquio");
		return q.getResultList();
	}

	public void updateColloquio(CsDColloquio colloquio) {
		updateDiarioNR(colloquio.getCsDDiario());
		em.merge(colloquio);
	}

	public CsTbTipoDiario findTipoDiarioById(long idTipoDiario) {
		CsTbTipoDiario tipodiario = em.find(CsTbTipoDiario.class, idTipoDiario);
		return tipodiario;
	}

	public CsDRelazione findRelazioneById(Long idDiario) {
		return em.find(CsDRelazione.class, idDiario);
	}

	@SuppressWarnings("unchecked")
	public List<CsDRelazione> findRelazioniByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDRelazione.findRelazioniByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsDRelazione> findRelazioniInScadenza() {
		Query q = em.createNamedQuery("CsDRelazione.findRelazioniInScadenza");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<CsDPai> findPaiByCaso(Long idCaso) {
		List<CsDPai> lst = new ArrayList<CsDPai>();
		try{
			if(idCaso!=null){
				Query q = em.createNamedQuery("CsDPai.findPaiByCaso");
				q.setParameter("idCaso", idCaso);
				lst = q.getResultList();
			}
		}catch(Exception e){
			logger.error("findPaiByCaso "+ e.getMessage(), e);
		}
		return lst;
	}
	
	public CsDDiario findDiarioById(Long obj) {
		CsDDiario diario = em.find(CsDDiario.class, obj);
		loadPadreFiglioEntities(diario);
		return diario;
	}

	public CsDDiarioBASIC findDiarioBASICById(Long obj) {
		CsDDiarioBASIC diario = em.find(CsDDiarioBASIC.class, obj);
		return diario;
	}

	public void deleteDiario(Long obj) {
		Query q = em.createNamedQuery("CsDDiario.deleteDiario");
		q.setParameter("idDiario", obj);
		q.executeUpdate();
	}

	public void deleteClob(Long obj) {
		Query q = em.createNamedQuery("CsDClob.delete");
		q.setParameter("idClob", obj);
		q.executeUpdate();
	}

	public void deleteValutazione(Long obj) {
		Query q = em.createNamedQuery("CsDValutazione.delete");
		q.setParameter("idDiario", obj);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<CsDValutazione> getSchedeValutazioneSoggetto(Long anagraficaId, int tipoDiarioId) {
		Query q = em.createNamedQuery("CsDValutazione.findValutazioneBySoggetto");
		q.setParameter("anagraficaId", anagraficaId);
		q.setParameter("tipoDiarioId", new Long(tipoDiarioId));
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsDValutazione> getSchedeValutazioneUdcId(Long schedaId, int tipoDiarioId) {
		Query q = em.createNamedQuery("CsDValutazione.findValutazioneByUdcId");
		q.setParameter("schedaId", schedaId);
		q.setParameter("tipoDiarioId", new Long(tipoDiarioId));
		return q.getResultList();
	}

	

	@SuppressWarnings("unchecked")
	public List<CsDValutazione> findAllSchedeValutazioneTipo(int tipoDiarioId) {
		Query q = em.createNamedQuery("CsDValutazione.findAllSchedeValutazione");
		q.setParameter("tipoDiarioId", tipoDiarioId);
		return q.getResultList();
	}

	public CsDClob saveClob(CsDClob clob) {
		em.persist(clob);
		return clob;
	}

	public void updateClob(CsDClob clob) {
		em.merge(clob);
	}

	public void updateValutazione(CsDValutazione valutazione) {
		updateDiarioNR(valutazione.getCsDDiario());
		em.merge(valutazione);
		this.loadDocEntities(valutazione.getCsDDiario());
	}

	public void updateRelazione(CsDRelazione relazione) {
		this.updateDiarioNR(relazione.getCsDDiario());
		em.merge(relazione);
	}
	
	public void updateTriage(CsDRelazione relazione,CsDTriage triage) {
		this.updateDiarioNR(relazione.getCsDDiario());
		em.merge(relazione);
        em.merge(triage);
	}

	public void saveRelazione(CsDRelazione relazione) {
		em.persist(relazione);
	}

	public List<CsDDiarioDoc> findDiarioDocById(Long idDiario) {
		Query q = em.createNamedQuery("CsDDiarioDoc.findDiarioDocById");
		q.setParameter("idDiario", idDiario);
		return q.getResultList();
	}

	public void saveDiarioDoc(CsDDiarioDoc dd) {
		em.persist(dd);
	}

	public void deleteDiarioDoc(Long idDocumento) {
		Query q = em.createNamedQuery("CsDDiarioDoc.deleteDiarioDocByIdDocumento");
		q.setParameter("idDocumento", idDocumento);
		q.executeUpdate();
	}

	public void updateSchedaPai(CsDPai pai) {
		updateDiarioNR(pai.getCsDDiario());
		em.merge(pai);
	}

	public void saveSchedaPai(CsDPai pai) {
		em.persist(pai);
	}

	public void deleteSchedaPai(CsDPai pai) {
		Query q = em.createNamedQuery("CsDPai.deletePAIByIdDiario");
		q.setParameter("idDiario", pai.getDiarioId());
		q.executeUpdate();
	}

	public void deleteRelDiarioDiarioRif(Long idDiario) {
		Query q = em.createNativeQuery("delete from CS_REL_DIARIO_DIARIORIF where diario_id=?");
		q.setParameter(1, idDiario);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<CsDDocIndividuale> findDocIndividualiByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDDocIndividuale.findDocIndividualeByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}

	public void updateDocIndividuale(CsDDocIndividuale docIndividuale) {
		updateDiarioNR(docIndividuale.getCsDDiario());
		em.merge(docIndividuale);
	}

	public void deleteDocIndividualeById(Long idDiario) {
		Query q = em.createNamedQuery("CsDDocIndividuale.deleteDocIndividualeByIdDiario");
		q.setParameter("idDiario", idDiario);
		q.executeUpdate();
	}

	public CsDDocIndividuale findDocIndividuale(Long id) {
		CsDDocIndividuale d = em.find(CsDDocIndividuale.class, id);
		this.loadDocEntities(d.getCsDDiario());
		return d;
	}

	public CsDValutazione findValutazioneById(Long id, boolean loadRelatedEntities) {
		Query q = em.createNamedQuery("CsDValutazione.findValutazioneById").setParameter("idValutazione", id);

		List<CsDValutazione> l = q.getResultList();
		if (l.size() > 0) {
			CsDValutazione v = (CsDValutazione) l.get(0);
			if (loadRelatedEntities)
				loadPadreFiglioEntities(v.getCsDDiario());
			return v;
		}
		return null;
	}

	public CsDValutazione findValutazioneChildByPadreId(Long valPadreId, int tipoDiarioFiglioId) {
		CsDValutazione padre = this.findValutazioneById(valPadreId, false);
		if (padre != null) {
			for (CsDDiario figlio : padre.getCsDDiario().getCsDDiariFiglio()) {
				if (figlio.getCsTbTipoDiario().getId() == tipoDiarioFiglioId) {
					CsDValutazione valFiglio = findValutazioneById(figlio.getId(), true);
					valFiglio.getCsDDiario().getCsDClob();
					return valFiglio;
				}
			}
		}
		return null;
	}

	public CsDClob findClobById(Long idClob) {
		Query q = em.createNamedQuery("CsDValutazione.findClobById").setParameter("idClob", idClob);

		List l = q.getResultList();
		if (l.size() > 0)
			return (CsDClob) l.get(0);

		return null;
	}

	public void saveDiarioRif(Long idDiarioPadre, Long idDiarioRifFiglio) {
		CsRelDiarioDiariorif csRif = new CsRelDiarioDiariorif();
		csRif.setId(new CsRelDiarioDiariorifPK());
		csRif.getId().setDiarioId(idDiarioPadre);
		csRif.getId().setDiarioIdRif(idDiarioRifFiglio);
		em.persist(csRif);
	}
	public int deleteDiarioRif(Long idDiarioPadre, Long idDiarioRifFiglio) {
		Query q = em.createNativeQuery("delete from CS_REL_DIARIO_DIARIORIF where DIARIO_ID=? and DIARIO_ID_RIF=?");
		q.setParameter(1, idDiarioPadre);
		q.setParameter(2, idDiarioRifFiglio);
		
		int deletedCount =q.executeUpdate();
		return deletedCount;
	}

	@SuppressWarnings("unchecked")
	public List<CsRelSettCatsocEsclusiva> findRelSettCatsocEsclusivaByTipoDiarioId(Long tipoDiarioId) {
		Query q = em.createNamedQuery("CsRelSettCatsocEsclusiva.findRelSettCatsocEsclusivaByTipoDiarioId");
		q.setParameter("tipoDiarioId", tipoDiarioId);
		return q.getResultList();
	}

	@SuppressWarnings({ "rawtypes" })
	public CsRelSettCatsocEsclusiva findRelSettCatsocEsclusivaByIds(Long tipoDiarioId, Long categoriaSocialeId, Long settoreId) {
		Query q = em.createNamedQuery("CsRelSettCatsocEsclusiva.findRelSettCatsocEsclusivaByIds");
		q.setParameter("tipoDiarioId", tipoDiarioId);
		q.setParameter("categoriaSocialeId", categoriaSocialeId);
		q.setParameter("settoreId", settoreId);
		List l = q.getResultList();
		if (l.size() > 0)
			return (CsRelSettCatsocEsclusiva) l.get(0);

		return null;
	}

	public void saveCsRelSettCatsocEsclusiva(CsRelSettCatsocEsclusiva rel) {
		em.persist(rel);
	}

	public void updateCsRelSettCatsocEsclusiva(CsRelSettCatsocEsclusiva rel) {
		em.merge(rel);
	}

	public void deleteCsRelSettCatsocEsclusiva(Long tipoDiarioId, Long categoriaSocialeId, Long settoreId) {
		Query q = em.createNamedQuery("CsRelSettCatsocEsclusiva.deleteRelSettCatsocEsclusivaByIds");
		q.setParameter("tipoDiarioId", tipoDiarioId);
		q.setParameter("categoriaSocialeId", categoriaSocialeId);
		q.setParameter("settoreId", settoreId);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<CsDScuola> findScuoleByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDScuola.findScuolaByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}

	public void updateScuola(CsDScuola scuola) {
		updateDiario(scuola.getCsDDiario());
		em.merge(scuola);
	}

	public void deleteScuolaById(Long idDiario) {
		Query q = em.createNamedQuery("CsDScuola.deleteScuolaByIdDiario");
		q.setParameter("idDiario", idDiario);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<CsDIsee> findIseeByCaso(Long idCaso) {
		Query q = em.createNamedQuery("CsDIsee.findIseeByCaso");
		q.setParameter("idCaso", idCaso);
		return q.getResultList();
	}

	public void updateIsee(CsDIsee scuola) {
		updateDiarioNR(scuola.getCsDDiario());
		em.merge(scuola);
	}

	public void deleteIseeById(Long idDiario) {
		Query q = em.createNamedQuery("CsDIsee.deleteIseeByIdDiario");
		q.setParameter("idDiario", idDiario);
		q.executeUpdate();
	}

	public CsDColloquio findColloquioById(long id) {
		CsDColloquio colloquio = em.find(CsDColloquio.class, id);
		return colloquio;
	}

	public CsDPai findPaiByDiarioId(long id) {
		CsDPai pai = em.find(CsDPai.class, id);
		return pai;
	}

	public CsDIsee findIseeId(Long id) {
		CsDIsee isee = em.find(CsDIsee.class, id);
		return isee;
	}

	public CsFlgIntervento findFglInterventoById(Long id) {
		CsFlgIntervento fgl = em.find(CsFlgIntervento.class, id);
		return fgl;
	}

	public CsDValutazione findValutazioneById(Long id) {
		CsDValutazione val = em.find(CsDValutazione.class, id);
		loadDocEntities(val.getCsDDiario());
		int size = val.getCsDDiario().getCsDDiarioDocs().size();
		return val;
	}
	
	public CsCDiarioConchi getConchiById(Long id) {
		CsCDiarioConchi ret = em.find(CsCDiarioConchi.class, id);
		return ret;
	}
	
	public CsDRelazione findLastRelazioneProblAperte(Long idCaso)
	{
		TypedQuery<CsDRelazione> q = em.createNamedQuery("CsDRelazione.findLastRelazioneProblAperte", CsDRelazione.class);
		q.setParameter("idCaso", idCaso);
		List<?> results = q.getResultList();
		if(results==null || results.isEmpty())
		{
			return null;
		}
		
		CsDRelazione relazione = (CsDRelazione) results.get(0);	
		return relazione;
//		
//		TypedQuery<CsRelRelazioneProbl> qq = em.createNamedQuery("CsRelRelazioneProbl.findRelRelazioneProblAperte", CsRelRelazioneProbl.class);
//		q.setParameter("_diarioId", relazione.getDiarioId());
//		
//		List<CsRelRelazioneProbl> ret = qq.getResultList();	

	}

	public void saveCsRelRelazioneProbl(CsRelRelazioneProbl csRelRelazioneProbl) {
		em.persist(csRelRelazioneProbl);
		
	}
	public void deleteCsRelRelazioneProbl(CsRelRelazioneProbl csRelRelazioneProbl) {
		em.remove(csRelRelazioneProbl);
		
	}

	public void saveCsRelRelazioneTipoint(CsRelRelazioneTipoint csRelRelazioneTipoint) {
		em.persist(csRelRelazioneTipoint);		
	}
	public void deleteCsRelRelazioneTipoint(CsRelRelazioneTipoint csRelRelazioneTipoint) {
		em.remove(csRelRelazioneTipoint);
		
	}

	public List<CsDDiario> findDiarioBySchedaId(Long schedaId) {
		Query q = em.createNamedQuery("CsDDiario.getDiarioBySchedaId");
		q.setParameter("schedaId", schedaId);
		return q.getResultList();
		
	}

	public List<CsDValutazione> getSchedeValutazioneByDiarioId(Long diarioId) {
		Query q = em.createNamedQuery("CsDValutazione.getValutazioniByDiarioId");
		q.setParameter("diarioId", diarioId);
		return q.getResultList();
	}

	public CsDValutazione saveSchedaValutazione(CsDValutazione schedaValutazione) {
		em.persist(schedaValutazione);
		return schedaValutazione;
	}

	//	public List<CsDDiario> findAllDiarioPadreByDiarioId(Long diarioId) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//	public List<CsDDiario> findAllDiarioFiglioByDiarioId(long id) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}


	//INIZIO SISO-438
	public CsDDiarioDoc mergeCsDDiarioDoc(CsDDiarioDoc csDDiarioDoc) { 
		try{
			return em.merge(csDDiarioDoc);
		}catch(Throwable e){
			throw new CarSocialeServiceException(e);
		}
	
	} 

	public void saveCsRelDiarioDiariorif(CsRelDiarioDiariorif csRif) {
		try{
			em.persist(csRif);
		}catch(Throwable e){
			throw new CarSocialeServiceException(e);
		} 
	}

	public void savecCsDDocIndividuale(CsDDocIndividuale csDDocIndividuale) {
		try{
			em.persist(csDDocIndividuale);
		}catch(Throwable e){
			throw new CarSocialeServiceException(e);
		} 
		
	}

	public CsLoadDocumento mergeCsLoadDocumento(CsLoadDocumento csLoadDocumento) {
		return   em.merge(csLoadDocumento);
	}

	public CsDDiario mergeCsDDiario(CsDDiario diarioDocIndividuale) {
		return   em.merge(diarioDocIndividuale);
	}

	public void persistCsDDiarioDoc(CsDDiarioDoc csDDiarioDoc) {
		em.persist(csDDiarioDoc);		
	}

	public  CsRelDiarioDiariorif findCsRelDiarioDiariorif(long idPadre, long diarioIdRif) {
		Query q = em.createQuery(" select rel from CsRelDiarioDiariorif rel "
				+ " where rel.id.diarioId=:idPadre and rel.id.diarioIdRif=:diarioIdRif ");
		q.setParameter("idPadre", idPadre);
		q.setParameter("diarioIdRif", diarioIdRif);

		List<CsRelDiarioDiariorif> l = q.getResultList();
		if (l.size() > 0) {
			CsRelDiarioDiariorif entity = (CsRelDiarioDiariorif) l.get(0); 
			return entity;
		}
		return null;
	}

//	public void deleteCsRelDiarioDiariorif(CsRelDiarioDiariorif rif) {
//		em.remove(rif);		
//	}
	
	@SuppressWarnings("unchecked")
	public List<CsDValutazione> getSchedeValutazioneByDiarioId(long diarioId) {
		Query q = em.createNamedQuery("CsDValutazione.findValutazioneByDiarioId");
		q.setParameter("diarioId", diarioId); 
		return q.getResultList();
	}

	public List<CsDDocIndividuale> findDocIndividualeByCfSchedaSegnalato(String cf, long organizzazioneid) {  
		List<CsDDocIndividuale> result = new ArrayList<CsDDocIndividuale>();
		
		String sql = " select doc.diario_id  from CS_D_DOC_INDIVIDUALE doc "
				+ " join CS_D_DIARIO diario on (doc.DIARIO_ID = diario.id ) "
				+ " join SS_SCHEDA scheda on (scheda.id = diario.SCHEDA_ID) "
				+ " join SS_SCHEDA_SEGNALATO segn on (scheda.SEGNALATO = segn.id )  "
				+ " join SS_ANAGRAFICA ana on (ana.id = segn.anagrafica) "
				+ " join ss_scheda_accesso acc on (scheda.accesso = acc.id) " 
				+ " where ana.cf =:cf  "
				+ "		and scheda.completa = 1 "
				+ "		and acc.REL_UPO_ORGANIZZAZIONE_ID =:organizzazioneid" ; 
				
		Query q = em.createNativeQuery(sql); 
		q.setParameter("cf", cf);
		q.setParameter("organizzazioneid", organizzazioneid);
		List lista = q.getResultList(); 
		
		if (lista.size()>0) {
			for (Object obj : lista) {
				CsDDocIndividuale doc = findDocIndividuale(((BigDecimal) obj).longValue());
				result.add(doc); 
			}
		}
		 
		return result;
	}
 
	
	//FINE SISO-438
	
	//SISO-155
	public List<CsTbTipoPai> findListaProgettiPai(){
		Query query = em.createQuery("SELECT p FROM CsTbTipoPai p WHERE p.progetto != null");
		
		return (List<CsTbTipoPai>)query.getResultList();
	}
	
	//SISO-763
	public List<CsDDiarioAna> findDiarioAnagraficaByDiarioId(Long diarioId){		
		Query query = em.createQuery("SELECT da FROM CsDDiarioAna da WHERE diarioId = ?1");
		query.setParameter(1, diarioId);
		
		return (List<CsDDiarioAna>) query.getResultList();
	}
	
	public CsDDiarioAna saveDiarioAnagrafica(CsDDiarioAna diarioAnagrafica) throws Exception {
		CsDDiarioAna toReturn = em.merge(diarioAnagrafica);
		em.flush();
		return toReturn;
	}
	
	public void deleteDiarioAnagrafica(Long diarioId) throws Exception {
		Query query = em.createQuery("DELETE FROM CsDDiarioAna da WHERE da.diarioId = ?1");
		query.setParameter(1, diarioId);
		query.executeUpdate();
	}
	
	public List<CsACaso> findDiarioAnaCasoIdsByAnagraficaId(Long anagraficaId, Long tipoDiario) throws Exception {
		Query query = em.createQuery("SELECT d.csACaso FROM CsDDiario d WHERE d.id in (SELECT da.diarioId FROM CsDDiarioAna da WHERE da.anagraficaId = ?1 AND d.csTbTipoDiario.id = ?2)");
		query.setParameter(1, anagraficaId);
		query.setParameter(2, tipoDiario);
		return (List<CsACaso>) query.getResultList();
	}
}
