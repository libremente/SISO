package it.webred.jsf.interfaces;

import javax.faces.event.AjaxBehaviorEvent;

public interface IComuneNazione {
	
	public void comuneNazioneValueChangeListener(AjaxBehaviorEvent event);

	public boolean isNazione();

	public boolean isComune();
	
}
