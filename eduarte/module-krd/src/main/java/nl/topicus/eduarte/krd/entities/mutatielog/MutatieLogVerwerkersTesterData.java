package nl.topicus.eduarte.krd.entities.mutatielog;

import java.io.Serializable;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.krd.web.components.text.RangeField;

public class MutatieLogVerwerkersTesterData implements Serializable
{
	private static final long serialVersionUID = 1L;

	@AutoForm(label = "Deelnemer(s)", editorClass = RangeField.class, description = "Geef hier de deelnemernummer(s) op.")
	private Range deelnemerRange;

	@AutoForm(label = "Verbintenis(sen)", editorClass = RangeField.class, description = "Geef hier de deelnemernummer(s) op.")
	private Range verbintenisRange;

	private String groepscode;

	private String opleidingcode;

	@AutoForm(label = "Groepsdeelnames (groepcodes)", description = "Geef hier de codes op van de groepen waarvan je de groepsdeelnames wil synchroniseren.")
	private String groepscodeVoorGroepsdeelnames;

	public Range getDeelnemerRange()
	{
		return deelnemerRange;
	}

	public void setDeelnemerRange(Range deelnemerRange)
	{
		this.deelnemerRange = deelnemerRange;
	}

	public Range getVerbintenisRange()
	{
		return verbintenisRange;
	}

	public void setVerbintenisRange(Range verbintenisRange)
	{
		this.verbintenisRange = verbintenisRange;
	}

	public String getGroepscode()
	{
		return groepscode;
	}

	public void setGroepscode(String groepscode)
	{
		this.groepscode = groepscode;
	}

	public String getOpleidingcode()
	{
		return opleidingcode;
	}

	public void setOpleidingcode(String opleidingcode)
	{
		this.opleidingcode = opleidingcode;
	}

	public MutatieLogVerwerkersTesterData()
	{
		this.deelnemerRange = new Range();
		this.verbintenisRange = new Range();
	}

	public String getGroepscodeVoorGroepsdeelnames()
	{
		return groepscodeVoorGroepsdeelnames;
	}

	public void setGroepscodeVoorGroepsdeelnames(String groepscodeVoorGroepsdeelnames)
	{
		this.groepscodeVoorGroepsdeelnames = groepscodeVoorGroepsdeelnames;
	}
}
