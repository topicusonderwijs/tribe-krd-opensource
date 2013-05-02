package nl.topicus.eduarte.entities.rapportage;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.personen.Deelnemer;

public enum DocumentTemplateContext
{
	Verbintenis
	{
		@Override
		public String getModelName()
		{
			return "verbintenis";
		}

		@Override
		public Class<Verbintenis> getContextClass()
		{
			return Verbintenis.class;
		}
	},
	BPVVerbintenis
	{
		@Override
		public String getModelName()
		{
			return "bpvInschrijving";
		}

		@Override
		public Class<BPVInschrijving> getContextClass()
		{
			return BPVInschrijving.class;
		}
	},
	Groep
	{
		@Override
		public String getModelName()
		{
			return "groep";
		}

		@Override
		public Class<Groep> getContextClass()
		{
			return Groep.class;
		}
	},

	/**
	 * Een enum waarde welke alleen bestemd is voor de {@link OnderwijsDocumentTemplate}.
	 * Omdat we niet kunnen erven van een Enum moeten het maar zo.
	 */
	Examendeelname
	{
		@Override
		public String getModelName()
		{
			return "examendeelname";
		}

		@Override
		public Class<Examendeelname> getContextClass()
		{
			return Examendeelname.class;
		}
	},
	Opleiding
	{
		@Override
		public String getModelName()
		{
			return "opleiding";
		}

		@Override
		public Class<Opleiding> getContextClass()
		{
			return Opleiding.class;
		}
	},
	/**
	 * enums voor de financiele module
	 */
	Debiteur
	{
		@Override
		public String getModelName()
		{
			return "debiteur";
		}

		@SuppressWarnings("unchecked")
		@Override
		public Class getContextClass()
		{
			return nl.topicus.eduarte.entities.Debiteur.class;
		}
	},
	Factuur
	{
		@Override
		public String getModelName()
		{
			return "factuur";
		}

		@SuppressWarnings("unchecked")
		@Override
		/*
		 * Dit returned de Deelnemer class omdat gegenereerde facturen ook gekoppeld
		 * worden aan een deelnemer. Het systeem koppelde echter enkel bijlagen aan hun
		 * eigen context (Factuur aan Factuur), met deze uitbreiding kunnen bijlagen ook
		 * aan een andere context gehangen worden (Factuur aan Deelnemer).
		 */
		public Class getContextClass()
		{
			return Deelnemer.class;
		}
	};

	/**
	 * @return de naam van het begin entiteit waarmee de rapportage tooling het model
	 *         afstruint.
	 */
	public abstract String getModelName();

	/**
	 * @return de class van het context object.
	 */
	public abstract Class< ? extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> getContextClass();

	/**
	 * Alternatief voor values(), dit geeft niet de {@link Examendeelname} waarde mee.
	 * 
	 * @return de waarden waaruit geselecteerd kan worden bij het maken van een generiek
	 *         samenvoegdocument (en dus niet een examendocument).
	 */
	public static DocumentTemplateContext[] getValues()
	{
		List<DocumentTemplateContext> values = new ArrayList<DocumentTemplateContext>();
		values.add(DocumentTemplateContext.Verbintenis);
		values.add(DocumentTemplateContext.Groep);
		values.add(DocumentTemplateContext.BPVVerbintenis);
		values.add(DocumentTemplateContext.Opleiding);

		return values.toArray(new DocumentTemplateContext[0]);
	}
}
