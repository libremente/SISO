package com.osmosit.siso.flussoinps.psa_2016;

import java.util.List;
import java.util.Map;

import com.osmosit.siso.flussoinps.logic.Cost;

class ValidityChecker {

	private final DataTypeUtils u;



	public ValidityChecker(DataTypeUtils utils) {
		this.u = utils;
	}



	CheckValidityResponse checkValidity(
			String codiceEnte,
			String indirizzoEnte,
			String codiceFiscaleOperatore,
			List<Map<String, Object>> listaBeneficiariConErogazione) {
		CheckValidityResponse response = new CheckValidityResponse(); 

		response.check(!u.isNullOrEmpty(codiceEnte), "codiceEnte"); 
		response.check(!u.isNullOrEmpty(indirizzoEnte), "indirizzoEnte");
		response.check(!u.isNullOrEmpty(codiceFiscaleOperatore), "codiceFiscaleOperatore");

		for (Map<String, Object> datiBeneficiario : listaBeneficiariConErogazione) {
			
			String soggetto = u.getString(datiBeneficiario, Cost.ANAGRAFICA_COGNOME)+" "+u.getString(datiBeneficiario, Cost.ANAGRAFICA_NOME);
			
			response.check(!u.isNullOrEmpty(u.getString(datiBeneficiario, Cost.ANAGRAFICA_COGNOME)), "ANAGRAFICA_COGNOME");
			response.check(!u.isNullOrEmpty(u.getString(datiBeneficiario, Cost.ANAGRAFICA_NOME)), "ANAGRAFICA_NOME");
			response.check(u.getInteger(datiBeneficiario, Cost.ANAGRAFICA_ANNONASCITA) != null, "ANAGRAFICA_ANNONASCITA");
			response.check(!u.isNullOrEmpty(u.getString(datiBeneficiario, Cost.ANAGRAFICA_LUOGONASCITA)), "ANAGRAFICA_LUOGONASCITA");
			response.check(u.getInteger(datiBeneficiario, Cost.ANAGRAFICA_SESSO) != null, "ANAGRAFICA_SESSO");
			response.check(u.getInteger(datiBeneficiario, Cost.ANAGRAFICA_CITTAD_ISO) != null, "ANAGRAFICA_CITTAD_ISO");

			@SuppressWarnings("unchecked")
			List<Map<String, Object>> prestazioni = (List<Map<String, Object>>) datiBeneficiario.get("listaPrestazioni");
			if (prestazioni.size() > 0) {
				for (Map<String, Object> datiPrestazione : prestazioni) {
					Integer presenzaProvaMezzi = u.getInteger(datiPrestazione, Cost.PRESTAZIONE_PRESENZA_PROVA_MEZZI);

					if (presenzaProvaMezzi == null) {
						response.check(false, "presenzaProvaMezzi [beneficiario:"+soggetto+"]");
					}
					Integer caratterePrestazione = u.getInteger(datiPrestazione, Cost.PRESTAZIONE_CARATTERE);
					if (caratterePrestazione == null) {
						response.check(false, "caratterePrestazione [beneficiario:"+soggetto+"]");
					}
					if (presenzaProvaMezzi == 1) {
						// prestazione soggetta ad ISEE 
						/*
						response.check(!u.isNullOrEmpty(u.getString(datiPrestazione, Cost.PRESTAZIONE_NUMPROT_DSU)), "PRESTAZIONE_NUMPROT_DSU [beneficiario:"+soggetto+"]");
						response.check(u.getInteger(datiPrestazione, Cost.PRESTAZIONE_ANNO_PROT) != null, "PRESTAZIONE_ANNO_PROT [beneficiario:"+soggetto+"]");
						response.check(u.getDate(datiPrestazione, Cost.PRESTAZIONE_DATA_DSU) != null, "PRESTAZIONE_DATA_DSU [beneficiario:"+soggetto+"]");
						*/
					}
					response.check(!u.isNullOrEmpty(u.getString(datiPrestazione, Cost.PRESTAZIONE_CODICE)), "PRESTAZIONE_CODICE [beneficiario:"+soggetto+"]");
					response.check(!u.isNullOrEmpty(u.getString(datiPrestazione, Cost.PRESTAZIONE_DENOMINAZIONE)), "PRESTAZIONE_DENOMINAZIONE [beneficiario:"+soggetto+"]");
					response.check(!u.isNullOrEmpty(u.getString(datiPrestazione, Cost.PRESTAZIONE_PROTOC_ENTE)), "PRESTAZIONE_PROTOC_ENTE [beneficiario:"+soggetto+"]");

					if (caratterePrestazione.intValue() == 1) {
						// prestazione periodica
						response.check(u.getDate(datiPrestazione, Cost.PRESTAZIONE_DATA_INIZIO) != null, "PRESTAZIONE_DATA_INIZIO [beneficiario:"+soggetto+"]");
						response.check(u.getDate(datiPrestazione, Cost.PRESTAZIONE_DATA_FINE) != null, "PRESTAZIONE_DATA_FINE [beneficiario:"+soggetto+"]");
						response.check(u.getInteger(datiPrestazione, Cost.PRESTAZIONE_PERIOD_EROG) != null, "PRESTAZIONE_PERIOD_EROG [beneficiario:"+soggetto+"]");
						response.check(u.getBigDecimal(datiPrestazione, Cost.PRESTAZIONE_IMPORTO_MENS) != null, "PRESTAZIONE_IMPORTO_MENS [beneficiario:"+soggetto+"]");
					} else {
						// prestazione occasionale
						response.check(u.getDate(datiPrestazione, Cost.PRESTAZIONE_DATA_EROG) != null, "PRESTAZIONE_DATA_EROG [beneficiario:"+soggetto+"]");
						response.check(u.getBigDecimal(datiPrestazione, Cost.PRESTAZIONE_IMPORTO) != null, "PRESTAZIONE_IMPORTO [beneficiario:"+soggetto+"]");
					} 

					Integer presaInCarico = u.getInteger(datiPrestazione, Cost.PRESTAZIONE_PRESA_IN_CARICO);
					if (presaInCarico == null) {
						response.check(false, "PRESTAZIONE_PRESA_IN_CARICO");
					}
					if (presaInCarico == 1) { 
						response.check(u.getBigDecimal(datiPrestazione, Cost.PRESTAZIONE_AREA_UTENZA) != null, "PRESTAZIONE_AREA_UTENZA");
					}
				}
			}
		}

		return response;
	}

}
