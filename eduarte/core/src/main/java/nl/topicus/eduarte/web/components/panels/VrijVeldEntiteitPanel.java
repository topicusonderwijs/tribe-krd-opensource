/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

/**
 * <p>
 * Panel welke een standaard datapanel toont met de gekoppelde {@link VrijVeldEntiteit}en.
 * Dit panel kan als volgt worden gebruikt:
 * </p>
 * <code>
 * new VrijVeldEntiteitPanel<PersoonVrijVeld>("id", new PropertyModel(getPersoon(),
 * "vrijVelden");<br/>
 * panel.setDossierScherm(true);<br/>
 * </code>
 * <p>
 * Dit zal je een panel geven welke uit de opgegeven lijst (personalia velden) de vrije
 * velden toont welke zijn bedoelt voor een toon/edit pagina.
 * <ul>
 * <li>Wanneer je {@link VrijVeldEntiteitPanel#setIntakeScherm(Boolean)} aanroept zal je
 * vrije velden te zien krijgen die bij een intake pagina horen.</li>
 * <li>Wanneer je {@link VrijVeldEntiteitPanel#setZoekenScherm(Boolean)} aanroept zal je
 * vrije velden te zien krijgen die bij een zoeken pagina horen.</li>
 * </ul>
 * Een combinatie hiervan werkt ook.
 * </p>
 * 
 * @author hoeve
 */
public class VrijVeldEntiteitPanel<E extends VrijVeldEntiteit, T extends VrijVeldable<E>> extends
		AbstractVrijVeldEntiteitPanel<T>
{
	private static final long serialVersionUID = 1L;

	private Boolean dossierScherm;

	private Boolean intakeScherm;

	private Boolean zoekenScherm;

	public VrijVeldEntiteitPanel(String id, IModel<T> vrijveldenModel)
	{
		super(id, vrijveldenModel);

		CustomDataPanel<E> data = new EduArteDataPanel<E>("gegevens", new IDataProvider<E>()
		{
			private static final long serialVersionUID = 1L;

			private List<E> tempVrijVelden;

			@Override
			public Iterator<E> iterator(int first, int count)
			{
				if (tempVrijVelden == null)
					loadTempVrijVelden();

				return tempVrijVelden.subList(first, first + count).iterator();
			}

			private void loadTempVrijVelden()
			{
				List<E> vrijvelden = getVrijvelden();
				if (vrijvelden == null)
					vrijvelden = Collections.emptyList();

				if (getDossierScherm() != null || getIntakeScherm() != null
					|| getZoekenScherm() != null)
				{
					for (int i = 0; i < vrijvelden.size();)
					{
						E vrijVeld = vrijvelden.get(i);
						boolean remove = false;

						if (getDossierScherm() != null
							&& vrijVeld.getVrijVeld().isDossierscherm() != getDossierScherm())
							remove = true;
						else if (getIntakeScherm() != null
							&& vrijVeld.getVrijVeld().isIntakescherm() != getIntakeScherm())
							remove = true;
						else if (getZoekenScherm() != null
							&& vrijVeld.getVrijVeld().isUitgebreidzoeken() != getZoekenScherm())
							remove = true;

						if (remove)
							vrijvelden.remove(vrijVeld);
						else
							i++;
					}
				}

				tempVrijVelden = vrijvelden;
			}

			@Override
			public IModel<E> model(E object)
			{
				return ModelFactory.getModel(object);
			}

			@Override
			public int size()
			{
				if (tempVrijVelden == null)
					loadTempVrijVelden();

				return tempVrijVelden.size();
			}

			@Override
			public void detach()
			{
				tempVrijVelden = null;
			}
		}, new VrijVeldEntiteitTable("Vrije velden"));

		add(data);
	}

	private class VrijVeldEntiteitTable extends CustomDataPanelContentDescription<E>
	{
		private static final long serialVersionUID = 1L;

		public VrijVeldEntiteitTable(String title)
		{
			super(title);

			addColumn(new CustomPropertyColumn<E>("Naam", "Naam", "vrijVeld.naam", "vrijVeld.naam"));
			addColumn(new CustomPropertyColumn<E>("Waarde", "Waarde", "omschrijving",
				"omschrijving"));
		}
	}

	public List<E> getVrijvelden()
	{
		return getModelObject().getVrijVelden();
	}

	/**
	 * Geeft aan of de selectie page alleen {@link VrijVeld}en moet tonen welke op een
	 * dossier scherm gekozen moeten kunnen worden.
	 * 
	 * @param dossierScherm
	 */
	public void setDossierScherm(Boolean dossierScherm)
	{
		this.dossierScherm = dossierScherm;
	}

	/**
	 * Geeft aan of de selectie page alleen {@link VrijVeld}en moet tonen welke op een
	 * dossier scherm gekozen moeten kunnen worden.
	 */
	public Boolean getDossierScherm()
	{
		return dossierScherm;
	}

	/**
	 * Geeft aan of de selectie page alleen {@link VrijVeld}en moet tonen welke op een
	 * intake scherm gekozen moeten kunnen worden.
	 * 
	 * @param intakeScherm
	 */
	public void setIntakeScherm(Boolean intakeScherm)
	{
		this.intakeScherm = intakeScherm;
	}

	/**
	 * Geeft aan of de selectie page alleen {@link VrijVeld}en moet tonen welke op een
	 * intake scherm gekozen moeten kunnen worden.
	 */
	public Boolean getIntakeScherm()
	{
		return intakeScherm;
	}

	/**
	 * Geeft aan of de selectie page alleen {@link VrijVeld}en moet tonen welke op een
	 * uitgebreid zoeken scherm gekozen moeten kunnen worden.
	 * 
	 * @param zoekenScherm
	 */
	public void setZoekenScherm(Boolean zoekenScherm)
	{
		this.zoekenScherm = zoekenScherm;
	}

	/**
	 * Geeft aan of de selectie page alleen {@link VrijVeld}en moet tonen welke op een
	 * uitgebreid zoeken scherm gekozen moeten kunnen worden.
	 */
	public Boolean getZoekenScherm()
	{
		return zoekenScherm;
	}

	@Override
	public VrijVeldZoekFilter getVrijVeldZoekFilter()
	{
		VrijVeldZoekFilter vvZF = new VrijVeldZoekFilter();
		vvZF.setActief(true);
		vvZF.setDossierScherm(getDossierScherm());
		vvZF.setIntakeScherm(getIntakeScherm());
		vvZF.setUitgebreidZoekenScherm(getZoekenScherm());

		return vvZF;
	}

	@Override
	public boolean isVisible()
	{
		return getModelObject() != null && super.isVisible();
	}
}
