package it.webred.cs.csa.ejb.dao;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import it.webred.cs.csa.ejb.dto.ErogazioniSearchCriteria;

public class ErogazioniQueryBuilder {

	private ErogazioniSearchCriteria criteria;
	
	public ErogazioniQueryBuilder(ErogazioniSearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	
	public static void main (String s[])
	{
		
		String sql = 
				
				"SELECT c.INTERVENTO_ESEG_ID, c.BENEFICIARIO_ANNO_NASCITA , c.BENEFICIARIO_LUOGO_NASCITA, "+
				// 4, 5, 6
				"c.BENEFICIARIO_SESSO, c.BENEFICIARIO_COD_CITTADI, c.BENEFICIARIO_SEC_CITTADI, "+
				// 7, 8, 9
				"c.BENEFICIARIO_REGIONE_RES, c.BENEFICIARIO_COMUNE_RES, c.BENEFICIARIO_NAZIONE_RES, "+
				// 10, 11, 12
				"c.NUM_PROT_DSU, c.ANNO_PROT_DSU, c.DATA_DSU, "+
				// 13, 14, 15
				"c.COD_PRESTAZIONE, c.DENOM_PRESTAZIONE, c.PROT_DOM_PREST, "+
				// 16, 17, 18
				"c.SOGLIA_ISEE, i.SOGGETTO_CF, i.SOGGETTO_COGNOME, "+
				// 19, 20, 21
				"i.SOGGETTO_NOME, i.SPESA, i.COMPART_ALTRE, "+
				// 22, 23, 24
				"i.COMPART_SSN, i.COMPART_UTENTI, i.PERC_GESTITA_ENTE, "+
				// 25, 26, 27
				"i.VALORE_GESTITA_ENTE, i.DATA_ESECUZIONE, i.ENTE_OPERATORE_EROGANTE, "+
				// 28, 29, 30
				"i.NOME_OPERATORE_EROG, i.NOTE, i.NOTE_ALTRE_COMPART, "+
				// 31, 32, 33
				"m.SPESA as m_spesa, m.COMPART_ALTRE as m_compart_altre, m.COMPART_SSN as m_compart_ssn, "+
				// 34, 35, 36
				"m.COMPART_UTENTI as m_compart_utenti, m.PERC_GESTITA_ENTE as m_perc_gestita_ente, m.VALORE_GESTITA_ENTE as m_valore_gestita_ente, "+
				// 37
				" m.NOTE_ALTRE_COMPART as m_note_altre_compart, c.CARATTERE, "+
				" m.SOGGETTO_CF as m_soggetto_cf, m.SOGGETTO_COGNOME as m_soggetto_cognome, m.SOGGETTO_NOME as m_soggetto_nome, "+
				"i.DATA_ESECUZIONE_A " + 	//SISO-538
				" from CS_I_PS c left JOIN CS_I_INTERVENTO_ESEG i ON c.INTERVENTO_ESEG_ID= i.ID "+
				" left JOIN CS_I_INTERVENTO_ESEG_MAST m ON i.INTERVENTO_ESEG_MAST_ID = m.ID "+
				" where i.DATA_ESECUZIONE >= TO_DATE(:dataInizio, 'dd/MM/yyyy') and i.DATA_ESECUZIONE <= TO_DATE("+
				":dataFine, 'dd/MM/yyyy') and i.OPERATORE_SETTORE_ID= :operatoreId";
		
		System.out.println(sql);
	}	
	
	
	//INIZIO modifica  SISO-538
	private String createQueryErogazioniBody() {
		String body = " SELECT intervento_eseg.id as intervento_eseg_id, "+							//0
				" mast_sogg.anno_nascita as beneficiario_anno_nascita, "	+        				//1
				" ps.beneficiario_luogo_nascita as beneficiario_luogo_nascita, "+       			//2
			" ps.beneficiario_sesso as beneficiario_sesso, "+               						//3
			" ps.beneficiario_cod_cittadi, "+               										//4
			" ps.beneficiario_regione_res, "+               										//5
			" ps.beneficiario_comune_res,  "+               										//6
			" ps.beneficiario_nazione_res, "+               										//7
			" trim('-' from (ps.prefix_prot_dsu||'-'||ps.num_prot_dsu||'-'||PS.PROG_PROT_DSU)) protocollo_dsu, "	+  	//8
			" ps.anno_prot_dsu, "+               													//9
			" ps.data_dsu, "+               														//10
			" ps.cod_prestazione, "+               													//11
			" ps.denom_prestazione, "+               												//12
			" mast.prot_dom_prest, "+               												//13
			" mast_sogg.cf, "+               														//14
			" mast_sogg.cognome, "+             												  	//15
			" mast_sogg.nome, "+               														//16
			" intervento_eseg.spesa, "+              											 	//17
			" intervento_eseg.compart_altre, "+               										//18
			" intervento_eseg.compart_ssn, "+               		//19
			" intervento_eseg.compart_utenti, "+               		//20
			" intervento_eseg.perc_gestita_ente, "+               	//21
			" intervento_eseg.valore_gestita_ente, "+              	//22
			" intervento_eseg.data_esecuzione, "+               	//23
			" intervento_eseg.ente_operatore_erogante, "+        	       	//24
			" intervento_eseg.nome_operatore_erog, "+               	//25
			" intervento_eseg.note, "+               	//26
			" intervento_eseg.note_altre_compart, "+               	//27
			" mast.spesa               AS m_spesa, "+               	//28
			" mast.compart_altre       AS m_compart_altre, "+               	//29
			" mast.compart_ssn         AS m_compart_ssn,  "+               	//30
			" mast.compart_utenti      AS m_compart_utenti, "+               	//31
			" mast.perc_gestita_ente   AS m_perc_gestita_ente, "+               	//32
			" mast.valore_gestita_ente AS m_valore_gestita_ente, "+               			//33
			" mast.note_altre_compart  AS m_note_altre_compart,  "+               			//34
			" ps.carattere,  "+               			//35
			" mast_sogg.cf         AS m_soggetto_cf, "+               				//36
			" mast_sogg.cognome    AS m_soggetto_cognome, "+               				//37
			" mast_sogg.nome       AS m_soggetto_nome, "+               					//38 
			" mast.id, "+               						//39
			" mast.intervento_id, "+               			//40 
			" intervento_eseg.data_esecuzione_a, " +										//41   SISO-538
			" ps.flag_prova_mezzi, "+               			//42 
			" ps.flag_in_carico, "+               	    	//43  
			" mast.categoria_sociale_id, "+               	    //44  
			
			/* SISO-719 */
			" cat_soc.descrizione, " +					// 45
			" export_flusso.identificazione_flusso, " +	// 46
			" export_flusso.data_esportazione, " +		// 47
			" export_flusso.revocabile " +				// 48
			
			" FROM   cs_i_ps ps "+
			" left join AR_TB_PRESTAZIONI_INPS artab ON ps.cod_prestazione=artab.codice "+
			" left join cs_i_intervento_eseg_mast mast  ON ps.INT_ESEG_MAST_ID = mast.id "+    
			" left join cs_i_intervento_eseg_mast_sogg mast_sogg  ON mast_sogg.int_eseg_mast_id = mast.id " +
			" left join cs_i_intervento_eseg intervento_eseg ON intervento_eseg.INTERVENTO_ESEG_MAST_ID = mast.id "+
			" join cs_cfg_int_eseg_stato  eseg_stato ON eseg_stato.id = intervento_eseg.stato_id "+
			" join cs_o_settore sett ON mast.settore_erogante_id= sett.id "+
			" join cs_o_organizzazione  organizzazione ON organizzazione.id = sett.organizzazione_id "+
			
			/* SISO-719 */
			" join cs_c_categoria_sociale cat_soc ON cat_soc.id = mast.categoria_sociale_id " +
			
			/* SISO-719
			 * 
			 * Con la nuova gestione delle esportazioni e la loro possibile revoca, si è creata una VIEW che indica
			 * se un INTERVENTO_ESEG_ID può essere esportato */
//			" left join cs_i_ps_export ps_export ON ps_export.intervento_eseg_id = intervento_eseg.id ";
			" left join v_erog_export_status export_status ON export_status.intervento_eseg_id = intervento_eseg.id " +
		
			/* SISO-719
			 * 
			 * Nuova VIEW per i dati sulle esportazioni già effettuate */
			" left join v_erog_export_flusso export_flusso ON export_flusso.intervento_eseg_id = intervento_eseg.id ";
		
		
		return body;
	}
	
	public String createQueryErogazioniDaEportarePerIdMaster() {
		String sql = createQueryErogazioniBody()  +
				
		" WHERE  mast.id = :idMaster                   "	+               	  
		"		AND eseg_stato.tipo = 'E' 	" +
		"		AND (artab.flag_non_esportare is null or artab.flag_non_esportare<>1) "+
		
		/* SISO-719
		 * 
		 * Con la nuova gestione delle esportazioni e la loro possibile revoca, si è creata una VIEW che indica
		 * se un INTERVENTO_ESEG_ID può essere esportato */
//		"		AND ps_export.id is null ";
		"		AND (export_status.status_esportato is null or export_status.status_esportato <> 1) ";
		
		return sql;
	}
	

	/**
	 * query che seleziona tutte le righe di erogazioni non esportate per le erogazioni con le spese in testata la cui chiusura cade nell' periodo tra data inizio e data fine
	 * questa selezione serve per avvertire l'utente che alcune erogazioni possono non essere state esportate nella esportazione precedente;
	 * Esempio:
	 * erogazioni che cadono tutte a gennaio con chiusura a febbraio, esportazione effettuata ogni mese che quindi a gennaio non ha esportato e a febbraio vede solo la chiusura
	 */
	public String createQueryErogazioniMasterChiusuraInPeriodo(){
		String sql = createQueryErogazioniBody()  +       
				
		" WHERE  mast.id IN (" + 
			" SELECT  mast_inner.id " + 
			" FROM   cs_i_ps ps " + 
			" LEFT JOIN cs_i_intervento_eseg_mast mast_inner 		ON ps.int_eseg_mast_id = mast_inner.id " + 
			" LEFT JOIN cs_i_intervento_eseg intervento_eseg_inner 	ON intervento_eseg_inner.intervento_eseg_mast_id = mast_inner.id " + 
			" JOIN cs_cfg_int_eseg_stato eseg_stato_inner 			ON eseg_stato_inner.id = intervento_eseg_inner.stato_id " + 
			" JOIN cs_o_settore sett_inner 							ON mast_inner.settore_erogante_id = sett_inner.id " + 
			" JOIN cs_o_organizzazione organizzazione_inner 			ON organizzazione_inner.id = sett_inner.organizzazione_id " + 
			" WHERE  intervento_eseg_inner.data_esecuzione >= To_date(  :dataInizio, 'dd/MM/yyyy') " + 
			" AND intervento_eseg_inner.data_esecuzione <= To_date( :dataFine  , 'dd/MM/yyyy' ) " + 
			" AND eseg_stato_inner.tipo = 'P' " + 
			" AND eseg_stato_inner.id = 5 " +  
			" AND organizzazione_inner.belfiore = :belfioreOperatore) "	+   
		" AND eseg_stato.tipo = 'E'          " +
		
		/* SISO-719
		 * 
		 * Con la nuova gestione delle esportazioni e la loro possibile revoca, si è creata una VIEW che indica
		 * se un INTERVENTO_ESEG_ID può essere esportato */
//		" AND ps_export.id is null "+
		" AND (export_status.status_esportato is null or export_status.status_esportato <> 1) "+
		
		" AND (artab.flag_non_esportare is null or artab.flag_non_esportare<>1) ";
		
		return sql;
	}
	
	
	
	
	
	//FINE modifica  SISO-538

	public String createQueryErogazioniDaEportare(){
		String sql = createQueryErogazioniBody()  +       
				
		" WHERE  intervento_eseg.data_esecuzione >= To_date(:dataInizio, 'dd/MM/yyyy') "	+               	 
		" AND intervento_eseg.data_esecuzione <= To_date(:dataFine, 'dd/MM/yyyy') "	+                   	 
//		" AND intervento_eseg.operatore_settore_id = :operatoreId          " +
		" AND eseg_stato.tipo = 'E' 	" + 

		/* SISO-719 Escludere le righe dei soggetti non di riferimento */
		" AND mast_sogg.flag_riferimento = 1 " +
		
		" AND organizzazione.belfiore = :belfioreOperatore "+
		" AND (artab.flag_non_esportare is null or artab.flag_non_esportare<>1) "+
		
		/* SISO-719
		 * 
		 * Con la nuova gestione delle esportazioni e la loro possibile revoca, si è creata una VIEW che indica
		 * se un INTERVENTO_ESEG_ID può essere esportato */
//		" AND ps_export.id is null ";
		" AND (export_status.status_esportato is null or export_status.status_esportato <> 1) ";
		
		return sql;
	}
	
	
	public String createQueryErogazioniEsportate(){
		String sql = createQueryErogazioniBody()  + 
				
		" WHERE  intervento_eseg.data_esecuzione >= To_date(:dataInizio, 'dd/MM/yyyy')       "	+   	 
		" AND intervento_eseg.data_esecuzione <= To_date(:dataFine, 'dd/MM/yyyy')    "	+       	 
//		" AND intervento_eseg.operatore_settore_id = :operatoreId          " +
		" AND eseg_stato.tipo = 'E' 	" + 
		" AND organizzazione.belfiore = :belfioreOperatore "+ 
		" AND (artab.flag_non_esportare is null or artab.flag_non_esportare<>1) "+
		
		/* SISO-719
		 * 
		 * Con la nuova gestione delle esportazioni e la loro possibile revoca, si è creata una VIEW che indica
		 * se un INTERVENTO_ESEG_ID risulta già esportato */
//		" AND ps_export.id is not null ";  
		" AND export_status.status_esportato = 1 ";
		
		return sql;
	}

	public String createQueryListaErogazioni(boolean count) {

		//I casi segnalati verso l'utente loggato sono in prima posizione
		//seguiti dai casi per cui ha i permessi in ordine di iter decrescente
		String sql = "";
		
		String joinLastFoglioIntervento="LEFT JOIN (SELECT e1.* FROM cs_flg_intervento e1 "+
                                   " JOIN (  "+
                                   "   SELECT E2.INTERVENTO_ID, MAX (diario_id) AS diario_ID "+
                                   "   FROM cs_flg_intervento e2 "+
                                   "    GROUP BY INTERVENTO_ID) e2 "+
                                   "    ON e1.INTERVENTO_ID =e2.INTERVENTO_ID AND e1.diario_ID = e2.diario_id) ";
		
		String joinLastErogazione = 				
				"LEFT JOIN (SELECT e1.*, stato.nome as stato_erogazione "+
                "      FROM cs_i_intervento_eseg e1 LEFT JOIN Cs_Cfg_Int_Eseg_Stato stato ON  (e1.stato_id = stato.id) "+
                "      JOIN ( "+
                "        SELECT E2.INTERVENTO_ESEG_MAST_ID , MAX(id) AS id "+
                "        FROM cs_i_intervento_eseg e2 "+
                "        GROUP BY INTERVENTO_ESEG_MAST_ID)  e2 "+
                "        ON e1.INTERVENTO_ESEG_MAST_ID = e2.INTERVENTO_ESEG_MAST_ID AND e1.id = e2.id) inteseg ON  (inteseg.intervento_eseg_mast_id = master.id) "+
				"left join Cs_Cfg_Int_Eseg_Stato stato on (inteseg.stato_id=stato.id) ";
		
		String from_plus = " left join CS_C_TIPO_INTERVENTO_CUSTOM tipoInterventoCustom on (master.TIPO_INTERVENTO_CUSTOM_ID = tipoInterventoCustom.id) ";
		
		if(count)
			sql += "SELECT count(*) FROM (";
		
		sql += "SELECT DISTINCT * ";

		String sqlErogazioni = 
				" select "
				+ "null as id_Intervento, "
				+ "null as data_Rich_Intervento, "
				+ "tipoint.id as id_tipo_intervento, "
				+ "TIPOINT.DESCRIZIONE as tipo_intervento, "
				+ "null as tipo_Foglio, "
				+ "null as dt_amministrativa, "
		/*		+ "soggetto.caso_id, "
				+ "soggetto.anagrafica_id,  "*/
				+ "nvl(ana.cognome, mastersogg.cognome) cognome, " //SOGGETTO_MASTER RIFERIMENTO
				+ "nvl(cast(ana.nome as VARCHAR2(100)), mastersogg.nome) nome, " //SOGGETTO_MASTER RIFERIMENTO
    			+ "nvl(ana.CF, mastersogg.cf) cf, " //SOGGETTO_MASTER RIFERIMENTO
				+ "INTESEG.DATA_ESECUZIONE as data_erogazione, "
				+ "null as diario_id, "
				+ "stato_erogazione, "
				+ "'E' tipoId , "
				+"inteseg.id id_eseguito, "
				+"master.DESC_INTERVENTO_ESEG as master_descr_intervento, "
				+"master.SPESA as master_spesa, "
				+"master.PERC_GESTITA_ENTE as master_perc_gestita_ente, "
				+"master.VALORE_GESTITA_ENTE as master_val_gestita_ente, "
				+"master.COMPART_UTENTI as master_compart_utenti,  "
				+"master.COMPART_SSN as master_compart_ssn, "
				+"master.COMPART_ALTRE as master_compart_altre, "
				+"master.NOTE_ALTRE_COMPART as master_note_altre, "
				+"fin.descrizione as linea_finanziamento, "
				+"master.CODICE_FIN1 as master_cod_fin1, "		
				+"tipoInterventoCustom.DESCRIZIONE as tipoCustom, "
				+"tipoInterventoCustom.ID as tipoCustomId, "
				+"master.id master_id,  "
				+"master.flag_spesa_calc spesa_calcolata, "
				+"master.flag_compart_calc compart_calcolata, "
				+"master.tipo_beneficiario, "
				+"inteseg.dt_ins as data_ins,"
				+"master.CATEGORIA_SOCIALE_ID, "
				+"master.DIARIO_PAI_ID " //SISO-748
				+"from "
				+"Cs_O_Operatore_Settore opSett, " 
				+"V_EROG_MAST_PR master "
				+joinLastErogazione +
				"left join Cs_I_Intervento_Eseg_Mast_sogg mastersogg on (master.id=mastersogg.int_eseg_mast_id) "+
				"left join Cs_o_Settore sett on (master.settore_erogante_id=sett.id) "+
				"left join v_lineafin fin on (master.FF_ORIGINE_ID=fin.id) "+
				"left join Cs_A_Anagrafica ana on (mastersogg.cf=ana.cf) "+
				"left join Cs_A_Soggetto soggetto on (ana.id=soggetto.anagrafica_id) "+
				"left join Cs_It_Step itStep on (soggetto.caso_id=itStep.caso_id) "+
				from_plus+
				"left join Cs_C_Tipo_Intervento tipoInt on (tipoInt.id=master.tipo_intervento_id) "+	
				
				 "where "
				+ "MASTER.INTERVENTO_ID is null and "
				+ "MASTERSOGG.FLAG_RIFERIMENTO=1  "+
				" and opSett.id=MASTER.OPERATORE_SETTORE_ID "+
//				"where INTESEG.INTERVENTO_ID is null and opSett.id=INTESEG.OPERATORE_SETTORE_ID "+
				
				"and (soggetto.anagrafica_id is null or soggetto.anagrafica_id = (select max(anagrafica_id) from cs_a_anagrafica, cs_a_soggetto where anagrafica_id=id and cf=MASTERSOGG.cf)) "+
				"and (itStep.id is null or itStep.id=(select max(its2.id) from Cs_It_Step its2 where itStep.caso_id=its2.caso_id)) "+
				"and (itStep.settore_id=:settoreId OR "
				    + "OPSETT.ID = :settoreId OR " //Necessaria anche questa condizione per permettere a chi inserito il master di vedere l'erogazione se il settore titolare/erogante/gestore è diverso dal suo.
					 + "MASTER.SETTORE_TITOLARE_ID = :settoreId OR "
					 + "MASTER.settore_erogante_id=:settoreId OR "
					 /* == SISO-663 SM == */
					 + "MASTER.settore_gestore_id=:settoreId)";
					 /* --===-- */
		
		
		String sqlInterventi=
				"select "
				+ "inter.id id_Intervento, "
				+ "inter.dt_Ins data_Rich_Intervento, "
				+ "tipoint.id as id_tipo_intervento, "
				+ "TIPOINT.DESCRIZIONE as tipo_intervento, "
				+ "decode(flag_att_sosp_c,'A','Attivazione '||tipo_Attivazione)|| "+
					"decode(flag_att_sosp_c,'S','Sospensione '||decode(descr_Sospensione,null,'','('||descr_Sospensione||')'))|| "+
					"decode(flag_att_sosp_c,'C','Chiusura '||decode(mot.descrizione,null,'','('||mot.descrizione||')')) as tipo_Foglio , "
				+ "diario.dt_amministrativa, "
				+ "anagrafica.cognome, "
				+ "cast(anagrafica.nome as VARCHAR2(100)) as nome, "
				+ "anagrafica.cf, "
				+ "INTESEG.DATA_ESECUZIONE as data_erogazione, "
				+ "foglio.diario_id, "
				+ "stato_erogazione, "
				+ "'I' tipoId, "
				+"inteseg.id id_eseguito, "
				+"master.DESC_INTERVENTO_ESEG as master_descr_intervento, "
				+"master.SPESA as master_spesa, "
				+"master.PERC_GESTITA_ENTE as master_perc_gestita_ente, "
				+"master.VALORE_GESTITA_ENTE as master_val_gestita_ente, "
				+"master.COMPART_UTENTI as master_compart_utenti, "
				+"master.COMPART_SSN as master_compart_ssn, "
				+"master.COMPART_ALTRE as master_compart_altre, "
				+"master.NOTE_ALTRE_COMPART as master_note_altre, "
				+"fin.descrizione as linea_finanziamento, "
				+"master.CODICE_FIN1 as master_cod_fin1, "									
				+"tipoInterventoCustom.DESCRIZIONE as tipoCustom, "
				+"tipoInterventoCustom.ID as tipoCustomId, "
				+"master.id master_id,  "
				+"master.flag_spesa_calc spesa_calcolata, "
				+"master.flag_compart_calc compart_calcolata, "
				+"master.tipo_beneficiario, "
				+"inteseg.dt_ins as data_ins,"
				+"master.CATEGORIA_SOCIALE_ID, "
				+"master.DIARIO_PAI_ID " + //SISO-748
				"from Cs_I_Intervento inter  "+
				joinLastFoglioIntervento + "foglio on (foglio.intervento_Id=inter.id)  "+
				//"left join Cs_Flg_Intervento foglio on (foglio.intervento_Id=inter.id)  "+
				"left join Cs_D_Diario diario on (diario.id=foglio.diario_id)  "+
				"left join Cs_A_Caso caso on (caso.id=diario.caso_id) "+
				"left join Cs_A_Soggetto soggetto on (soggetto.caso_Id=caso.id) "+
				"left join Cs_C_Tipo_Intervento tipoInt on (INTER.SCTI_TIPO_INTERVENTO_ID=tipoInt.id) "+
				"left join Cs_A_Anagrafica anagrafica on (anagrafica.id=soggetto.anagrafica_id) "+
				"left join cs_tb_motivo_chiusura_int mot on mot.id=foglio.motivo_chiusura_int_id "+
				"left join V_EROG_MAST_PR master on (inter.id=master.intervento_id) "+
				"left join v_lineafin fin on (master.FF_ORIGINE_ID=fin.id) "+
				from_plus+
				"LEFT JOIN (SELECT e1.*, stato.nome as stato_erogazione "+
                "      FROM cs_i_intervento_eseg e1 LEFT JOIN Cs_Cfg_Int_Eseg_Stato stato ON  (e1.stato_id = stato.id) "+
                "      JOIN ( "+
                "        SELECT E2.INTERVENTO_ESEG_MAST_ID , MAX(id) AS id "+
                "        FROM cs_i_intervento_eseg e2 "+
                "        GROUP BY INTERVENTO_ESEG_MAST_ID)  e2 "+
                "        ON e1.INTERVENTO_ESEG_MAST_ID = e2.INTERVENTO_ESEG_MAST_ID AND e1.id = e2.id) inteseg ON  (inteseg.intervento_eseg_mast_id = master.id)" +
				"left join Cs_Cfg_Int_Eseg_Stato stato on (inteseg.stato_id=stato.id) "+
				" where ";
				//"where foglio.diario_Id=(select max(diario_Id) from Cs_Flg_Intervento where foglio.intervento_Id=inter.id) ";//commentato da osmosit
				//"and (master.id is null or MASTER.id = (select max(id) from Cs_I_Intervento_Eseg_Mast where master.intervento_id=inter.id)) ";//commentato da mk
//				"where foglio.diario_Id=(select max(diario_Id) from Cs_Flg_Intervento where intervento_Id=inter.id) "+
//				"and (inteseg.id is null or INTESEG.id = (select max(id) from Cs_I_Intervento_Eseg where intervento_id=inter.id)) ";
		
				if(criteria.isPermessoAutorizzativo())
					//sqlInterventi+="and INTER.SCTI_SETTORE_ID = :settoreId ";
					sqlInterventi+=" INTER.SCTI_SETTORE_ID = :settoreId ";//commentato da osmosit
				else {
					//sqlInterventi+="and INTER.SCTI_ER_SETTORE_ID = :settoreId ";//commentato da osmosit
					sqlInterventi+=" MASTER.SETTORE_EROGANTE_ID = :settoreId ";
					/* == SISO-663 SM == */
					sqlInterventi+=" OR MASTER.SETTORE_GESTORE_ID = :settoreId ";
					/* --===-- */
				}
				
				//SISO-748
				if(criteria.isSearchByCaso()){
					sqlErogazioni += " and mastersogg.CASO_ID = :casoId ";
				}
	
		if(criteria.isSearchErogatiNoIntervento())
			sql += " FROM (" + sqlErogazioni+") ";
		else if(criteria.isSearchConRichiesta()){ 
			//if(criteria.isPermessoAutorizzativo())
				sql += " FROM ("+sqlInterventi+") ";
			//else
			//	sql=null;
		}else
			sql += " FROM ((" + sqlErogazioni + ") UNION ALL ("+ sqlInterventi + " )) ";
		
		String whereCond = getSQLCriteria();
		sql += " WHERE 1 = 1 " + whereCond;
		
		
		if(!count)
			sql += " ORDER BY cognome, nome, id_intervento, data_ins desc ";
		else
			sql += ") ";
		
		return sql;

	}
	
	public String createQueryListaSoggettiErogazioni() {
		String sql = "SELECT DISTINCT INT_ESEG_MAST_ID FROM cs_i_intervento_eseg_MAST_sogg where 1=1 ";
		
		sql+=this.getSQLCriteriaSogg();
		
		return sql;
	}
	
	private String getSQLCriteriaSogg(){
		String sqlCriteria = "";
		
		sqlCriteria += StringUtils.isBlank(criteria.getDenominazione()) ? "" : " AND UPPER(COGNOME)||' '||UPPER(NOME) LIKE '%" + criteria.getDenominazione().toUpperCase() + "%'";
		
		sqlCriteria += StringUtils.isBlank(criteria.getCognome()) ? "" : " AND UPPER(COGNOME) LIKE '%" + criteria.getCognome().toUpperCase() + "%'";

		sqlCriteria += StringUtils.isBlank(criteria.getNome()) ? "" : " AND UPPER(NOME) LIKE '%" + criteria.getNome().toUpperCase() + "%'";
		
		sqlCriteria += StringUtils.isBlank(criteria.getCodiceFiscale()) ? "" : " AND UPPER(CF) LIKE '%" + criteria.getCodiceFiscale().toUpperCase() + "%'";
		
		return sqlCriteria;
	}
	
	private String getSQLCriteria() {
		//campi usati per il filter 
		String sqlCriteria = "";
		
	
		sqlCriteria += (criteria.getDataErogazione() == null ? "" : " AND DATA_EROGAZIONE >= TO_DATE('" + criteria.getDataErogazione() + "', 'dd/MM/yyyy')");
		
		sqlCriteria += (criteria.getStatoErogazione() == null  || criteria.getStatoErogazione().trim().equals("") ? "" : " AND UPPER(stato_erogazione) LIKE '%" + criteria.getStatoErogazione().toUpperCase() + "%'");
		
		sqlCriteria += StringUtils.isBlank(criteria.getLineaFinanziamento()) ? "" : " AND UPPER(linea_finanziamento) LIKE '%" + criteria.getLineaFinanziamento().toUpperCase() + "%'";
	
		String sqlCriteriaSogg = "";
		if(!this.getSQLCriteriaSogg().isEmpty())
			sqlCriteriaSogg ="("+getSQLCriteriaSogg().replaceFirst("AND", "")+")";
		
		String sqlCriteriaMaster="";
		if(criteria.getLstMasterId()!=null){
				String lstMasterId = "";
				for(BigDecimal val : criteria.getLstMasterId())
					lstMasterId+= val.toString()+",";
				lstMasterId = !lstMasterId.isEmpty() ? lstMasterId.substring(0, lstMasterId.length()-1) : "";
				sqlCriteriaMaster = !lstMasterId.isEmpty() ? "master_id IN ("+lstMasterId+")" : ""; 
		}
		
		if(!sqlCriteriaSogg.isEmpty() && !sqlCriteriaMaster.isEmpty())
			sqlCriteria += " AND ( "+sqlCriteriaSogg+ " OR "+ sqlCriteriaMaster +")";
		else if(!sqlCriteriaSogg.isEmpty() && sqlCriteriaMaster.isEmpty() && criteria.isSearchByBeneficiario())
			sqlCriteria += " AND "+sqlCriteriaSogg;
		else if(sqlCriteriaSogg.isEmpty()  && sqlCriteriaMaster.isEmpty() &&  criteria.isSearchByBeneficiario())
			sqlCriteria += " AND "+sqlCriteriaMaster;
			
		if(criteria.getLstTipoIntervento()!=null){
			String lstTipi = "";
			for(Long val : criteria.getLstTipoIntervento())
				lstTipi+= val.toString()+",";
			lstTipi = !lstTipi.isEmpty() ? lstTipi.substring(0, lstTipi.length()-1) : lstTipi;
			sqlCriteria += lstTipi.equals("") ? "" :  " AND id_tipo_intervento IN ("+lstTipi+")";
		}
		
		if(criteria.getLstTipoInterventoCustom()!=null){//mk_osmosit
			String lstTipiCustom = "";
			for(Long val : criteria.getLstTipoInterventoCustom())
				lstTipiCustom+= val.toString()+",";
			lstTipiCustom = !lstTipiCustom.isEmpty()  ? lstTipiCustom.substring(0, lstTipiCustom.length()-1) : lstTipiCustom;
			sqlCriteria += lstTipiCustom.equals("") ? "" :  " AND tipoCustomId IN ("+lstTipiCustom+")";
		}
		
		
		if(criteria.getLstTipoBeneficiario()!=null){
			String lstBen = "";
			for(String val : criteria.getLstTipoBeneficiario())
				lstBen+= "'"+val+"',";
			lstBen = !lstBen.isEmpty()  ? lstBen.substring(0, lstBen.length()-1) : lstBen;
			sqlCriteria += lstBen.equals("") ? "" :  " AND tipo_beneficiario IN ("+lstBen+")";
		}
		
		
	/*	if(criteria.isSearchSoloRichiesti() && criteria.isPermessoAutorizzativo())
			sqlCriteria += " AND ID_ESEGUITO is null ";*/
			
		
		return sqlCriteria;
	}
	
	
	//SISO-780
	public String createQueryErogazioniPeriodiche() {
		String sql = createQueryErogazioniBody()  +
				
				" WHERE  mast.id in :idsMaster                  "	+               	  
				" AND eseg_stato.tipo = 'E' 	" +
				" AND intervento_eseg.data_esecuzione <= To_date(:dataFine, 'dd/MM/yyyy')    "	+   
				" AND (artab.flag_non_esportare is null or artab.flag_non_esportare<>1) "+

				//SISO-780 solo periodiche
				" AND ps.carattere = 1";
		
		return sql;
	}
	 
}
