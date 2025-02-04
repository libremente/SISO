package it.webred.cs.csa.web.manbean.listacasi;

import it.webred.cs.csa.ejb.client.AccessTableCatSocialeSessionBeanRemote;
import it.webred.cs.csa.ejb.client.AccessTableSoggettoSessionBeanRemote;
import it.webred.cs.csa.ejb.dto.BaseDTO;
import it.webred.cs.csa.ejb.dto.CasoSearchCriteria;
import it.webred.cs.csa.ejb.dto.PaginationDTO;
import it.webred.cs.csa.ejb.dto.retvalue.DatiCasoListaDTO;
import it.webred.cs.csa.web.manbean.listacasi.initDatiCaso.InitDatiCaso;
import it.webred.cs.data.DataModelCostanti.FiltroCasi;
import it.webred.cs.data.DataModelCostanti.PermessiCaso;
import it.webred.cs.data.model.CsACaso;
import it.webred.cs.data.model.CsASoggettoLAZY;
import it.webred.cs.data.model.CsCCategoriaSociale;
import it.webred.cs.data.model.CsOOperatoreSettore;
import it.webred.cs.jsf.bean.DatiCasoBean;
import it.webred.cs.jsf.manbean.superc.CsUiCompBaseBean;
import it.webred.ejb.utility.ClientUtility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class LazyListaCasiModel extends LazyDataModel<DatiCasoBean>  {
     
	private static final long serialVersionUID = 1L;
	

	//SISO-641
	String sortField;
	SortOrder sortOrder; 
	Map filters;
	
	@Override
    public DatiCasoBean getRowData(String rowKey) { 
		//TODO
        return null;
    }
  
    @Override
    public Object getRowKey(DatiCasoBean datiCaso) {
        return datiCaso.getSoggetto().getAnagraficaId();
    }
    
    private String getCategorieId(String desc){
    	try {

    		AccessTableCatSocialeSessionBeanRemote catSocService = (AccessTableCatSocialeSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableCatSocialeSessionBean");
		    BaseDTO bdto = new BaseDTO();
		    CsUiCompBaseBean.fillEnte(bdto);
		    bdto.setObj(desc);
    		List<CsCCategoriaSociale> lstCatSoc = (List<CsCCategoriaSociale>) catSocService.getCategorieSocialiByDesc(bdto);
    		
    		if(lstCatSoc!=null && !lstCatSoc.isEmpty()){
    			String lstIds = "";
    			for(CsCCategoriaSociale cs : lstCatSoc)
    			 lstIds += ","+Long.toString(cs.getId());
    			lstIds = lstIds.substring(1);
    			return lstIds;
    		}
    	
    	} catch (Exception e) {
			CsUiCompBaseBean.logger.error(e.getMessage(), e);
		}
		
    	return null;
    }
 
    @Override
    public List<DatiCasoBean> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
		CsUiCompBaseBean.logger.debug("*** Load List<DatiCasoBean> " + new Date());

        List<DatiCasoBean> data = new ArrayList<DatiCasoBean>();
 
		PaginationDTO dto = new PaginationDTO();
		
		//SISO-641
    	this.sortField = sortField;
    	this.sortOrder = sortOrder;
    	this.filters = filters;
    	
		CsUiCompBaseBean.fillEnte(dto);
		CasoSearchCriteria searchCriteria = new CasoSearchCriteria();
		CsOOperatoreSettore opSettore = (CsOOperatoreSettore) CsUiCompBaseBean.getSession().getAttribute("operatoresettore");
		if(opSettore != null) {
			//DEFAULT: filtro solo i casi dove sono presente come tipo operatore e con il settore scelto o quelli segnalatimi
			searchCriteria.setUsername(dto.getUserId());
			searchCriteria.setIdOperatore(opSettore.getCsOOperatore().getId());
			searchCriteria.setIdSettore(opSettore.getCsOSettore().getId());
			searchCriteria.setIdOrganizzazione(opSettore.getCsOSettore().getCsOOrganizzazione().getId());
			//PERMESSO CASI SETTORE: 
			if(CsUiCompBaseBean.checkPermesso(PermessiCaso.ITEM, PermessiCaso.VISUALIZZAZIONE_CASI_SETTORE))
				searchCriteria.setPermessoCasiSettore(true);
			//PERMESSO CASI ORGANIZZAZIONE: 
			if(CsUiCompBaseBean.checkPermesso(PermessiCaso.ITEM, PermessiCaso.VISUALIZZAZIONE_CASI_ORG))
				searchCriteria.setPermessoCasiOrganizzazione(true);
		}
		String filterSoggetto = (String) filters.get("soggetto");
		String filterDataNascita = (String) filters.get("dataNascita");
		String filterCF = (String) filters.get("codiceFiscale");
		String filterCatSociale = (String) filters.get("catSociale");
		String filterDataApertura = (String) filters.get("dataApertura");
		if(filterSoggetto != null)
			searchCriteria.setDenominazione(filterSoggetto);
		if(filterDataNascita != null)
			searchCriteria.setDataNascita(filterDataNascita);
		if(filterCF != null)
			searchCriteria.setCodiceFiscale(filterCF);
		if(filterCatSociale!=null){
			String lstCatSociale = this.getCategorieId(filterCatSociale);
			if(lstCatSociale!=null)
				searchCriteria.setLstCatSociale(lstCatSociale);
		}
		if(filterDataApertura!=null)
			searchCriteria.setDataApertura(filterDataApertura);
		
		String criteriaStati = (String)getSession().getAttribute(FiltroCasi.STATO);
		if(criteriaStati!=null && !criteriaStati.isEmpty())
			searchCriteria.setLstStati(criteriaStati);
			
		String criteriaOperatore = (String)getSession().getAttribute(FiltroCasi.OPERATORE);
		if(criteriaOperatore!=null && !criteriaOperatore.isEmpty()){
			searchCriteria.setIdOperatoreAltro(new Long(criteriaOperatore));
			String tipoOperatore = (String)getSession().getAttribute(FiltroCasi.TIPO_OPERATORE);
			searchCriteria.setOpResponsabile(null);
			if(tipoOperatore!=null && !tipoOperatore.equals("TUTTI"))
				searchCriteria.setOpResponsabile(tipoOperatore.equalsIgnoreCase("RESP"));
			
			//searchCriteria.setSoloOperatoreNR(nr!=null && nr ? true : false);
		}
		
		Long criteriaStudio = (Long)getSession().getAttribute(FiltroCasi.STUDIO);
		if(criteriaStudio!=null && criteriaStudio>0)
			searchCriteria.setTitStudioId(criteriaStudio);
		
		Long criteriaLavoro = (Long)getSession().getAttribute(FiltroCasi.LAVORO);
		if(criteriaLavoro!=null && criteriaLavoro>0)
			searchCriteria.setCondLavoroId(criteriaLavoro);
		
		String criteriaTutela = (String)getSession().getAttribute(FiltroCasi.TUTELA);
		if(criteriaTutela!=null && !criteriaTutela.isEmpty())
			searchCriteria.setTipoTutela(criteriaTutela);
		
		String[] criteriaTribunale = (String[])getSession().getAttribute(FiltroCasi.TRIBUNALE);
		if(criteriaTribunale!=null && criteriaTribunale.length>0){
			searchCriteria.setTribunale(criteriaTribunale);
		}
			
		//se devo filtrare per ass sociale eseguo manualmente la paginazione
	//	if(filterAssSociale == null) {
			dto.setFirst(first);
			dto.setPageSize(pageSize);
	//	}
		
		dto.setObj(searchCriteria);
			
		try {
			
			AccessTableSoggettoSessionBeanRemote soggettiService = (AccessTableSoggettoSessionBeanRemote) ClientUtility.getEjbInterface("CarSocialeA", "CarSocialeA_EJB", "AccessTableSoggettoSessionBean");
	
			List<DatiCasoListaDTO> list = soggettiService.getCasiSoggettoLAZY(dto);
			int index=0;
			CsUiCompBaseBean.logger.debug("Inizio ciclo lista soggetti :" + new Date());

			ForkJoinPool pool = new ForkJoinPool();
			List<InitDatiCaso> classiInit = new ArrayList<InitDatiCaso>();
			

			for(DatiCasoListaDTO dc: list){
				
				CsASoggettoLAZY sogg = dc.getSoggetto();
				CsACaso caso = sogg.getCsACaso();
				
				BaseDTO bDto = new BaseDTO();
				CsUiCompBaseBean.fillEnte(bDto);
				bDto.setObj(caso.getId());

				InitDatiCaso init = null;
				init = new InitDatiCaso(dc, bDto);
				classiInit.add(init);
				pool.execute(init);
			
	        }
			
			
			
			CsUiCompBaseBean.logger.debug("Fine ciclo lista soggetti :" + new Date());

			boolean poolEseguito=false;
			do
		      {
		            TimeUnit.MILLISECONDS.sleep(300);
		            boolean fine= true;
					for (InitDatiCaso initDatiCaso : classiInit) {
						if (initDatiCaso.isCompletedAbnormally())
							throw new Exception("Errore nel recupero dei dati della lista ");
						if (!initDatiCaso.isDone())
							fine = false;
					}
					poolEseguito = fine;
		      
		      } while (!poolEseguito) ;
			 
			pool.shutdown(); 

			for (InitDatiCaso initDatiCaso : classiInit) {
				DatiCasoBean bean = (DatiCasoBean) initDatiCaso.getRawResult();
				if(bean!=null) data.add(bean);
			}
			
			//rowCount
			Integer dataSize;
			/*if(filterAssSociale != null)
				dataSize = data.size();
			else */
				dataSize = soggettiService.getCasiSoggettoCount(dto);
			this.setRowCount(dataSize);
			
			/*//paginate
	        if(filterAssSociale != null && dataSize > pageSize) {
	            try {
	        		CsUiCompBaseBean.logger.debug("Fine Load<DatiCasoBean>:" + new Date());
	                return data.subList(first, first + pageSize);
	            }
	            catch(IndexOutOfBoundsException e) {
	        		CsUiCompBaseBean.logger.debug("Fine Load<DatiCasoBean>:" + new Date());
	                return data.subList(first, first + (dataSize % pageSize));
	            }
	        }
	        else {*/
	    		CsUiCompBaseBean.logger.debug("Fine Load<DatiCasoBean>:" + new Date());

	    		return data;
	       // }
		
		} catch (Exception e) {
			CsUiCompBaseBean.addErrorFromProperties("caricamento.error");
			CsUiCompBaseBean.logger.error(e.getMessage(), e);
		} catch (Throwable e1) {
			CsUiCompBaseBean.addErrorFromProperties("caricamento.error");
			CsUiCompBaseBean.logger.error(e1.getMessage(), e1);
		}
		
		
		CsUiCompBaseBean.logger.debug("Fine Load<DatiCasoBean>:" + new Date());
		
		return data;
    }

    public static HttpSession getSession() {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	}
	
}
