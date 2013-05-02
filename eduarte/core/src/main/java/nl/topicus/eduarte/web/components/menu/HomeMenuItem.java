/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.menu;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.menu.AccessMenuItemKey;

/**
 * @author marrink
 */
public enum HomeMenuItem implements AccessMenuItemKey
{
	/**
	 * Algemene pagina.
	 */
	Home('H'),
	/**
	 * Account gegevens van de gebruiker.
	 */
	Account('A'),

	MijnGroepen('G'),

	MijnDeelnemers('D'),

	TeDoen("Te Doen", 'T'),

	UitgezetteTaken('U'),

	Intakegesprekken('I'),

	Contractverplichtingen('V'),

	/**
	 * Pagina die alle actie (ook historische) weergeeft van een medewerker
	 */
	AlleTaken('K'),
	/**
	 * Pagina's om in te stellen welke signalen ontvangen mogen worden.
	 */
	BeheerSignalen('B'),
	/**
	 * Pagina's om groepen te selecteren waarvan we signalen ontvangen willen.
	 */
	Signaleringsgroepen('G'),
	/**
	 * Pagina's om deelnemers te selecteren waarvan we signalen ontvangen willen.
	 */
	Signaleringsdeelnemers('D'),
	/**
	 * Het overzicht van signalen die een gebruiker ontvangen heeft.
	 */
	OverzichtSignalen('O'),

	TrajectInstellingen("Instellingen", 'I'),

	Rapportages("Rapportages", 'R'),

	Toetsfilters('F'),

	AmarantisCurriculumWizard('M')
	{
		@Override
		public String getLabel()
		{
			return "Curriculum Wizard";
		}
	};

	private final String label;

	private Character key;

	private HomeMenuItem()
	{
		this.label = StringUtil.convertCamelCase(name());
	}

	private HomeMenuItem(String label)
	{
		this.label = label;
	}

	private HomeMenuItem(Character key)
	{
		this.label = StringUtil.convertCamelCase(name());
		this.key = key;
	}

	private HomeMenuItem(String label, Character key)
	{
		this.label = label;
		this.key = key;
	}

	public String getLabel()
	{
		return label;
	}

	@Override
	public Character getAccessKey()
	{
		return key;
	}
}
