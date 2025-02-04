package it.webred.cs.csa.ejb.dao;

import it.webred.cs.csa.ejb.CarSocialeBaseDAO;
import it.webred.cs.csa.ejb.client.CarSocialeServiceException;
import it.webred.cs.data.model.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@Named
public class ConfigurazioneDAO extends CarSocialeBaseDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<CsTbStatoCivile> getStatoCivile() {

		try {

			Query q = em.createNamedQuery("CsTbStatoCivile.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public CsTbStatoCivile getStatoCivileByIdExtCet(String codEnte, String staciv) {
		CsTbStatoCivile tb = null;
		try {
            if(codEnte!=null && staciv!=null){
				Query q = em.createNamedQuery("CsTbMapStacivGit2Cs.findByIdCet");
				CsTbMapGit2CsPK id = new CsTbMapGit2CsPK();
				id.setCodOrig(staciv);
				id.setEnte(codEnte);
				q.setParameter("id", id);
				List<CsTbMapStacivGit2Cs> lista = q.getResultList();
				if (!lista.isEmpty()) {
				   if(lista.size()>1)
					   logger.warn("In mappa esiste più di un valore associato a ID_EXT["+staciv+"] COD_ENTE["+codEnte+"] di stato civile");
				   tb = lista.get(0).getCsTbStatoCivile();
				}else
					logger.warn("Nessun valore di CS_TB_STATO_CIVILE associato a ID_EXT["+staciv+"] COD_ENTE["+codEnte+"]: Configurare le corrispondende tra id degli stati civili (IDS_MAPPING_GIT2CS) ");
            }

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return tb;
	}

	@SuppressWarnings("unchecked")
	public CsTbStatoCivile getStatoCivileByDescrizione(String descrizione) {

		try {

			Query q = em.createNamedQuery("CsTbStatoCivile.findStatoByDescrizione");
			q.setParameter("descrizione", descrizione);
			List<CsTbStatoCivile> lista = q.getResultList();
			if (!lista.isEmpty())
				return lista.get(0);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbStatoCivile getStatoCivileByCodice(String obj) {
		if(obj!=null)
			return em.find(CsTbStatoCivile.class, obj);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbStatus> getStatus() {

		try {

			Query q = em.createNamedQuery("CsTbStatus.findAll");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbStatus getStatusById(Long id) {
		return em.find(CsTbStatus.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<CsTbSchedaMultidim> getParametriSM(String idTipo) {

		try {
			Query q = em.createNamedQuery("CsTbSchedaMultidim.findByTipo");
			q.setParameter("idTipo", idTipo);
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbSchedaMultidim getParametroSM(String idTipo, String codice) {
		try {
			Query q = em.createNamedQuery("CsTbSchedaMultidim.findByTipoCodice");
			q.setParameter("idTipo", idTipo);
			q.setParameter("codice", codice);
			List<CsTbSchedaMultidim> result = q.getResultList();
			if (result != null && !result.isEmpty())
				return result.get(0);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbTipoAbitazione getLabeTipoAbit(String codice){
		try{
		Query q = em.createNamedQuery("CsTbTipoAbitazione.findByCodice");
			q.setParameter("codice", codice);
			return (CsTbTipoAbitazione) q.getSingleResult();
			
		} catch (Exception e ){
	    
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbAbitTitoloGodimento getLabeTitoloGodimento(String codice){
		try{
		    Query q = em.createNamedQuery("CsTbAbitTitoloGodimento.findByCodice");
			q.setParameter("codice", codice);
			return (CsTbAbitTitoloGodimento) q.getSingleResult();
			
		} catch (Exception e ){
	    
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsTbPermesso getPermessoById(Long id) {
		return em.find(CsTbPermesso.class, id);
	}

	//SISO-792
	public CsTbAssenzaPermesso getAssenzaPermessoById(Long id) {
		return em.find(CsTbAssenzaPermesso.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbPermesso> getPermesso() {

		try {

			Query q = em.createNamedQuery("CsTbPermesso.findAll");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<CsTbAssenzaPermesso> getPermessoSenzaTitoloSoggiorno() {

		try {

			Query q = em.createNamedQuery("CsTbAssenzaPermesso.findAll");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsCCategoriaSociale getCategoriaSocialeById(Long id) {

		CsCCategoriaSociale cs = em.find(CsCCategoriaSociale.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsCCategoriaSociale> getCategorieSociali() {

		try {

			Query q = em.createNamedQuery("CsCCategoriaSociale.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipologiaFamiliare> getTipologieFamiliari() {

		try {

			Query q = em.createNamedQuery("CsTbTipologiaFamiliare.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbTipologiaFamiliare getTipologiaFamiliareById(Long id) {
		return em.find(CsTbTipologiaFamiliare.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<CsTbResponsabilita> getResponsabilita() {

		try {

			Query q = em.createNamedQuery("CsTbResponsabilita.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbProblematica> getProblematiche() {

		try {

			Query q = em.createNamedQuery("CsTbProblematica.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbProblematica getProblematicaById(Long id) {

		CsTbProblematica cs = em.find(CsTbProblematica.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbStesuraRelazioniPer> getStesuraRelazioniPer() {

		try {

			Query q = em.createNamedQuery("CsTbStesuraRelazioniPer.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTitoloStudio> getTitoliStudio() {

		try {

			Query q = em.createNamedQuery("CsTbTitoloStudio.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbTitoloStudio getTitoloStudioById(Long id) {

		CsTbTitoloStudio cs = em.find(CsTbTitoloStudio.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbProfessione> getProfessioni() {

		try {

			Query q = em.createNamedQuery("CsTbProfessione.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbCondLavoro> getCondLavoro() {

		try {

			Query q = em.createNamedQuery("CsTbCondLavoro.findAll");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoContratto> getTipoContratto() {

		try {

			Query q = em.createNamedQuery("CsTbTipoContratto.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbSettoreImpiego> getSettoreImpiego() {

		try {

			Query q = em.createNamedQuery("CsTbSettoreImpiego.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTutela> getTutele() {

		try {

			Query q = em.createNamedQuery("CsTbTutela.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoIndirizzo> getTipoIndirizzo() {

		try {

			Query q = em.createNamedQuery("CsTbTipoIndirizzo.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoContributo> getTipoContributo() {

		try {

			Query q = em.createNamedQuery("CsTbTipoContributo.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsOOrganizzazione> getOrganizzazioni() {

		try {

			Query q = em.createNamedQuery("CsOOrganizzazione.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsOOrganizzazione> getOrganizzazioniBelfiore() {

		try {
			Query q = em.createNamedQuery("CsOOrganizzazione.findAllAbilBelfiore");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsOOrganizzazione> getOrganizzazioniAll() {
		Query q = em.createNamedQuery("CsOOrganizzazione.findAll");
		return q.getResultList();
	}

	public void salvaOrganizzazione(CsOOrganizzazione org) {
		em.persist(org);
	}

	public void updateOrganizzazione(CsOOrganizzazione org) {
		em.merge(org);
	}

	public void eliminaOrganizzazione(Long id) {
		Query q = em.createNamedQuery("CsOOrganizzazione.eliminaById");
		q.setParameter("id", id);
		q.executeUpdate();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CsOSettoreBASIC> findSettoreBASICByOrganizzazione(long idOrganizzazione) {
		Query q = em.createNamedQuery("CsOSettore.findSettoreBASICByOrganizzazione").setParameter("idOrganizzazione", idOrganizzazione);
		List results = q.getResultList();
		return results;
	}

	public void salvaSettore(CsOSettore cs) {
		em.persist(cs);
	}

	public void updateSettore(Object cs) {
		em.merge(cs);
	}

	public void eliminaSettore(Long id) {
		Query q = em.createNamedQuery("CsOSettore.eliminaById");
		q.setParameter("id", id);
		q.executeUpdate();
	}

	public CsTbIcd10 getIcd10ById(Long id) {

		CsTbIcd10 cs = em.find(CsTbIcd10.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<String> getIcd10CodIniziali() {

		try {

			Query q = em.createNamedQuery("CsTbIcd10.findAllCodIniziali");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbIcd10> getIcd10ByCodIniziali(String codIniziale) {

		try {

			Query q = em.createNamedQuery("CsTbIcd10.findCodiciByCodIniziale").setParameter("codIniziale", codIniziale);
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbIcd9 getIcd9ById(Long id) {

		CsTbIcd9 cs = em.find(CsTbIcd9.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<String> getIcd9CodIniziali() {

		try {

			Query q = em.createNamedQuery("CsTbIcd9.findAllCodIniziali");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbIcd9> getIcd9ByCodIniziali(String codIniziale) {

		try {

			Query q = em.createNamedQuery("CsTbIcd9.findCodiciByCodIniziale").setParameter("codIniziale", codIniziale);
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoRapportoCon> getTipoRapportoConParenti() {

		try {

			Query q = em.createNamedQuery("CsTbTipoRapportoCon.findAllAbilParenti");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoRapportoCon> getTipoRapportoConConoscenti() {

		try {

			Query q = em.createNamedQuery("CsTbTipoRapportoCon.findAllAbilConoscenti");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbPotesta> getPotesta() {

		try {

			Query q = em.createNamedQuery("CsTbPotesta.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbDisponibilita> getDisponibilita() {

		try {

			Query q = em.createNamedQuery("CsTbDisponibilita.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbDisponibilita getDisponibilitaById(Long id) {

		try {
			return em.find(CsTbDisponibilita.class, id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoPai> getTipoPai() {

		try {

			Query q = em.createNamedQuery("CsTbTipoPai.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbMotivoChiusuraPai> getMotivoChiusuraPai() {

		try {

			Query q = em.createNamedQuery("CsTbMotivoChiusuraPai.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbContatto> getContatti() {

		try {

			Query q = em.createNamedQuery("CsTbContatto.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbMacroSegnal> getMacroSegnalazioni() {

		try {

			Query q = em.createNamedQuery("CsTbMacroSegnal.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbMacroSegnal getMacroSegnalazioneById(Long id) {

		CsTbMacroSegnal cs = em.find(CsTbMacroSegnal.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbMicroSegnal> getMicroSegnalazioni() {

		try {

			Query q = em.createNamedQuery("CsTbMicroSegnal.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbMicroSegnal getMicroSegnalazioneById(Long id) {

		CsTbMicroSegnal cs = em.find(CsTbMicroSegnal.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbMotivoSegnal> getMotivoSegnalazioni() {

		try {

			Query q = em.createNamedQuery("CsTbMotivoSegnal.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbMotivoSegnal getMotivoSegnalazioneById(Long id) {

		CsTbMotivoSegnal cs = em.find(CsTbMotivoSegnal.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbDisabEnte> getDisabEnte() {

		try {

			Query q = em.createNamedQuery("CsTbDisabEnte.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbDisabGravita> getDisabGravita() {

		try {

			Query q = em.createNamedQuery("CsTbDisabGravita.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbDisabGravita getDisabGravitaById(Long id) {
		return em.find(CsTbDisabGravita.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<CsTbDisabTipologia> getDisabTipologia() {

		try {

			Query q = em.createNamedQuery("CsTbDisabTipologia.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbDisabTipologia getDisabTipologiaById(Long id) {
		return em.find(CsTbDisabTipologia.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<CsTbServComunita> getServComunita() {

		try {

			Query q = em.createNamedQuery("CsTbServComunita.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbServLuogoStr> getServLuogoStr() {

		try {

			Query q = em.createNamedQuery("CsTbServLuogoStr.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbServResRetta> getServResRetta() {

		try {

			Query q = em.createNamedQuery("CsTbServResRetta.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbServSemiresRetta> getServSemiresRetta() {

		try {

			Query q = em.createNamedQuery("CsTbServSemiresRetta.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbBuono> getBuoni() {

		try {

			Query q = em.createNamedQuery("CsTbBuono.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbEsenzioneRiduzione> getEsenzioniRiduzioni() {

		try {

			Query q = em.createNamedQuery("CsTbEsenzioneRiduzione.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbMotivoChiusuraInt> getMotiviChiusuraIntervento() {

		try {

			Query q = em.createNamedQuery("CsTbMotivoChiusuraInt.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbMotivoChiusuraInt getMotivoChiusuraIntervento(Long id) {

		try {

			CsTbMotivoChiusuraInt tb = em.find(CsTbMotivoChiusuraInt.class, id.longValue());
			return tb;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoDiario> getTipiDiario() {

		try {

			Query q = em.createNamedQuery("CsTbTipoDiario.findAll");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoRetta> getTipiRetta() {

		try {

			Query q = em.createNamedQuery("CsTbTipoRetta.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoRientriFami> getTipiRientriFami() {

		try {

			Query q = em.createNamedQuery("CsTbTipoRientriFami.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoComunita> getTipiComunita() {

		try {

			Query q = em.createNamedQuery("CsTbTipoComunita.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public CsTbTipoComunita getTipoComunitaByDesc(String desc) {
		try {

			Query q = em.createNamedQuery("CsTbTipoComunita.findByDescrizione");
			q.setParameter("descrizione", desc);
			List<CsTbTipoComunita> lst = q.getResultList();
			if (lst.size() > 0)
				return lst.get(0);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbTipoRetta getTipoRetta(Long id) {
		return em.find(CsTbTipoRetta.class, id);
	}

	public CsTbTipoContributo getTipoContributo(Long id) {
		return em.find(CsTbTipoContributo.class, id);
	}

	public CsTbTipoRientriFami getTipoRientriFami(Long id) {
		return em.find(CsTbTipoRientriFami.class, id);
	}

	public List<CsTbTipoOperatore> getTipiOperatore() {
		try {
			Query q = em.createNamedQuery("CsTbTipoOperatore.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbInterventiUOL> getInterventiUOL() {

		try {

			Query q = em.createNamedQuery("CsTbInterventiUOL.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbInterventiUOL getInterventiUOLById(Long id) {

		CsTbInterventiUOL cs = em.find(CsTbInterventiUOL.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoCirc4> getTipiCirc4() {

		try {

			Query q = em.createNamedQuery("CsTbTipoCirc4.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbTipoCirc4 getTipoCirc4ById(Long id) {

		CsTbTipoCirc4 cs = em.find(CsTbTipoCirc4.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoProgetto> getTipiProgetto() {

		try {

			Query q = em.createNamedQuery("CsTbTipoProgetto.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbTipoProgetto getTipoProgettoById(Long id) {

		CsTbTipoProgetto cs = em.find(CsTbTipoProgetto.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public CsCfgParametri getParametro(String sezione, String chiave) {

		try {

			Query q = em.createNamedQuery("CsCfgParametri.findParamBySezioneChiave");
			q.setParameter("sezione", sezione);
			q.setParameter("chiave", chiave);
			List<CsCfgParametri> lst = q.getResultList();
			if (lst.size() > 0)
				return lst.get(0);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbScuola> getScuole() {

		try {

			Query q = em.createNamedQuery("CsTbScuola.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public List<String> getComuniScuole() {

		try {

			Query q = em.createNamedQuery("CsTbScuola.findComuni");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbScuola> getScuoleByComuneTipo(String comune, Long tipoId) {

		try {

			Query q = em.createNamedQuery("CsTbScuola.findByComuneTipo");
			q.setParameter("comune", comune);
			q.setParameter("idTipo", tipoId);
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public List<CsTbScuola> getScuoleByComuneAnnoTipo(String anno, Long tipoId, String belfiore) {
		try {

			Query q = em.createNamedQuery("CsTbScuola.findByComuneAnnoTipo");
			q.setParameter("anno", anno);
			q.setParameter("idTipo", tipoId);
			q.setParameter("comune", belfiore);
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbTipoScuola> getTipoScuole() {

		try {

			Query q = em.createNamedQuery("CsTbTipoScuola.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbGVulnerabile> getGrVulnerabilita() {

		try {

			Query q = em.createNamedQuery("CsTbGVulnerabile.findAllAbil");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbCondLavoro getCondLavoro(Long id) {
		try {

			return em.find(CsTbCondLavoro.class, id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbProfessione getProfessione(Long id) {
		try {

			return em.find(CsTbProfessione.class, id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbGVulnerabile getGrVulnerabile(String id) {
		try {

			return em.find(CsTbGVulnerabile.class, id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public CsOOrganizzazione getOrganizzazioneByBelfiore(String belfiore) {
		try {
			if (belfiore != null) {
				Query q = em.createNamedQuery("CsOOrganizzazione.findByBelfiore");
				q.setParameter("belfiore", belfiore);
				List<CsOOrganizzazione> lst = q.getResultList();
				if (lst.size() > 0)
					return lst.get(0);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}




	/* SISO-663 SM */

	public CsOOrganizzazione getOrganizzazioneCapofila() {
		try {
			TypedQuery<CsOOrganizzazione> q = em.createNamedQuery("CsOOrganizzazione.findCapofila", CsOOrganizzazione.class);
			List<CsOOrganizzazione> lst = q.getResultList();
			if(lst.size()>0)
				return lst.get(0);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	/* -=- */



	@SuppressWarnings("unchecked")
	public List<CsOSettore> getSettoreAll() {
		try {

			Query q = em.createNamedQuery("CsOSettore.findAll");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public CsOSettore getSettoreById(Long id) {
		try{
			return em.find(CsOSettore.class, id);
		}catch(Throwable e){
			logger.error(e.getMessage(), e);
		}		
		return null;
	}

	public CsTbCittadinanzaAcq getCittadinanzaAcq(Long id) {
		try {

			return em.find(CsTbCittadinanzaAcq.class, id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbCittadinanzaAcq> getCittadinanzeAcquisite() {
		try {

			Query q = em.createNamedQuery("CsTbCittadinanzaAcq.findAll");
			return q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public CsTbSettoreImpiego getSettoreImpiegoById(Long id) {
		try {

			return em.find(CsTbSettoreImpiego.class, id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public List<CsTbSottocartellaDoc> getTipoCartelle(){
		try{
			Query q = em.createNamedQuery("CsTbSottocartellaDoc.findAll");
			return q.getResultList();
			
		}catch(Throwable e){
			throw new CarSocialeServiceException(e);
		}
	}

	public CsTbSottocartellaDoc getTipoCartellaById(Long id) {
		try{
			return em.find(CsTbSottocartellaDoc.class, id);
		}catch(Throwable e){
			throw new CarSocialeServiceException(e);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<CsTbUnitaMisura> getCsTbUnitaMisura() {
		List<CsTbUnitaMisura> lst = new ArrayList<CsTbUnitaMisura>();
		try {
			Query q = em.createNamedQuery("CsTbUnitaMisura.findAll");
			lst = q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lst;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbMacroAttivita> getMacroAttivita() {
		List<CsTbMacroAttivita> lst = new ArrayList<CsTbMacroAttivita>();
		try {

			Query q = em.createNamedQuery("CsTbMacroAttivita.findAllAbil");
			lst = q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lst;
	}

	public CsTbMacroAttivita getMacroAttivitaById(Long id) {

		CsTbMacroAttivita cs = em.find(CsTbMacroAttivita.class, id);
		return cs;

	}

	@SuppressWarnings("unchecked")
	public List<CsTbMicroAttivita> getMicroAttivita() {
		List<CsTbMicroAttivita> lst = new ArrayList<CsTbMicroAttivita>();
		try {

			Query q = em.createNamedQuery("CsTbMicroAttivita.findAllAbil");
			lst = q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lst;
	}

	public CsTbMicroAttivita getMicroAttivitaById(Long id) 
	{
		CsTbMicroAttivita cs = em.find(CsTbMicroAttivita.class, id);
		return cs;
	}

	@SuppressWarnings("unchecked")
	public List<CsTbMicroAttivita> getMicroAttivitaByIdMacroAttivita(Long idMacro) 
	{   List<CsTbMicroAttivita> lst = new ArrayList<CsTbMicroAttivita>();
		try {
			Query q = em.createNamedQuery("CsTbMicroAttivita.findAllAbilByIdMacro");
			q.setParameter("idMacro", idMacro);
			lst = q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lst;
		
	}

	
	public List<CsTbProbl> getProbl() {
		List<CsTbProbl> lst = new ArrayList<CsTbProbl>();
		try {

			Query q = em.createNamedQuery("CsTbProbl.findAllAbil");
			lst = q.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lst;
	}

	public CsTbProbl getProblById(Long id) {
		CsTbProbl cs = em.find(CsTbProbl.class, id);
		return cs;
	}

	public CsTbTipoRapportoCon getTipoRapportoDaRelazPar(String obj,String belfiore) throws CarSocialeServiceException {
		CsTbTipoRapportoCon rapporto = null;
		
		try {
			if(obj!=null && belfiore!=null){
				Query q = em.createNamedQuery("CsTbMapTipoRapGit2Cs.findByRelazPar");
				CsTbMapGit2CsPK id = new CsTbMapGit2CsPK();
				id.setCodOrig(obj!=null ? obj.toUpperCase() : "");
				id.setEnte(belfiore);
				q.setParameter("id",id );
				List<CsTbMapTipoRapGit2Cs> lst = q.getResultList();
				if(!lst.isEmpty()){
					rapporto = lst.get(0).getCsTbTipoRapportoCon();
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		//if(rapporto==null) caricaMappaRelazioniParentaliEnte(belfiore); //Verifica se la configurazione è stata fatta e lancia eccezione
		
		return rapporto;
	}

	public List<CsTbMapTipoRapGit2Cs> caricaMappaRelazioniParentaliEnte(String enteId) throws CarSocialeServiceException {
		List<CsTbMapTipoRapGit2Cs> lista  = null;
		try {
			if(enteId!=null){
				Query q = em.createNamedQuery("CsTbMapTipoRapGit2Cs.mappaRelazioniEnte");
				q.setParameter("ente",enteId );
				lista = (List<CsTbMapTipoRapGit2Cs>) q.getResultList();
			}else
				logger.warn("caricaMappaRelazioniParentaliEnte - Parametro <Ente> non impostato!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
		if(lista==null || lista.isEmpty())
			throw new CarSocialeServiceException("Configurare la mappa delle relazioni parentali (da GIT a CS) per l'ente "+ enteId);
		
		return lista;
	}

	public CsTbTipoRapportoCon getTipoRapportoConByCodice(Long obj) {
		if(obj!=null)
			return em.find(CsTbTipoRapportoCon.class, obj);
		else
			return null;
	}

	public CsOOrganizzazione getOrganizzazioneById(Long obj) {
		if(obj!=null)
			return em.find(CsOOrganizzazione.class, obj);
		else
			return null;
	}
	
	public List<CsTbTipoAlert> getTipiAlert(){
		Query q = em.createNamedQuery("CsTbTipoAlert.findAll");
		List<CsTbTipoAlert> lista = q.getResultList();
		return lista;
		
	}
	
	public List<CsTbFormaGiuridica> getFormeGiuridiche(){
		Query q = em.createNamedQuery("CsTbFormaGiuridica.findAll");
		List<CsTbFormaGiuridica> lista = q.getResultList();
		return lista;
	}

	public String findCodFormProgetto(String progetto, Long tipoIntervento, Long tipoInterventoCustom, Long idCatSoc) {
		Query q = em.createNamedQuery("CsCInterventoPrForm.find");
		q.setParameter("progetto", progetto);
		q.setParameter("tipoInterventoId", tipoIntervento);
		q.setParameter("tipoInterventoCustomId", tipoInterventoCustom);
		q.setParameter("catSocialeId", idCatSoc);
		List<CsCInterventoPrForm> lst = q.getResultList();
		if(!lst.isEmpty()) return ((CsCInterventoPrForm)lst.get(0)).getRifFormInterventoPrDett();
		return null;
	}

	public CsTbFormaGiuridica getFormaGiuridicaById(String obj) {
		if(obj!=null)
			return em.find(CsTbFormaGiuridica.class, obj);
		else
			return null;
	}

	public CsTbIngMercato getIngMercatoById(String obj) {
		if(obj!=null)
			return em.find(CsTbIngMercato.class, obj);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public List<CsCTipoColloquio> findAllTipoColloquios() {
		Query q = em.createNamedQuery("CsCTipoColloquio.findAllTipoColloquios");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsCDiarioDove> findAllDiarioDoves() {
		Query q = em.createNamedQuery("CsCDiarioDove.findAllDiarioDoves");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<CsCDiarioConchi> findAllDiarioConchis() {
		Query q = em.createNamedQuery("CsCDiarioConchi.findAllDiarioConchis");
		return q.getResultList();
	}

	public CsCTipoColloquio getTipoColloquio(Long id) {
		CsCTipoColloquio tipo = null;
		if(id!=null) tipo= em.find(CsCTipoColloquio.class, id);
		return tipo;
	}
	
	public CsCDiarioConchi getDiarioConChi(Long id) {
		CsCDiarioConchi tipo = null;
		if(id!=null) tipo= em.find(CsCDiarioConchi.class, id);
		return tipo;
	}
	
	public CsCDiarioDove getDiarioDove(Long id) {
		CsCDiarioDove tipo = null;
		if(id!=null) tipo= em.find(CsCDiarioDove.class, id);
		return tipo;
	}

	public CsCfgIntEsegStato findCsCfgIntEsegStato(Long id) {
		CsCfgIntEsegStato stato = null;
		if(id!=null) stato = em.find(CsCfgIntEsegStato.class, id);
		return stato;
	}

	public List<CsTbSinaDomanda> getListaDomandaSina() {
		Query q = em.createNamedQuery("CsTbSinaDomanda.findAll");
		return q.getResultList();
	}
	public List<CsTbTipoAbitazione> getListaTipoAbitazione(){
		Query q = em.createNamedQuery("CsTbTipoAbitazione.findAll");
		return q.getResultList();
	}
	public List<CsTbAbitTitoloGodimento> getListaTitoloGod(){
		Query q = em.createNamedQuery("CsTbAbitTitoloGodimento.findAll");
		return q.getResultList();
	}
	 @SuppressWarnings("unchecked")
	public List<ArTbPrestazioniInps> getPrestazioniInpsSina(){
		 String[] codici = {"A1.04","A1.10","A1.11","A1.12","A1.13","A1.14","A1.15","A1.16","A1.23","A2.07","A2.08","A2.14","A2.20","A2.21","A3.02"};
	    return this.findArTbPrestazioniInpsByCodice(codici);
	}
	 
	public List<ArTbPrestazioniInps> findArTbPrestazioniInpsByCodice( String[] codici) {
		if(codici!=null && codici.length>0){
			try{
				Query q = em.createNamedQuery("ArTbPrestazioniInps.findByCodice");  
				q.setParameter("codice", Arrays.asList(codici));
				return  ( List<ArTbPrestazioniInps> ) q.getResultList();
			}catch(Throwable e){
				logger.error(e.getMessage(), e);
			}
		}
		return null;
	}

	public List<CsTbAbitGestProprietario> getListaGestProprietario() {
		Query q = em.createNamedQuery("CsTbAbitGestProprietario.findAll");
		return q.getResultList();
	}
}
