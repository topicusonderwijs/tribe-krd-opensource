package nl.topicus.eduarte.krd.bron.jobs;

import java.util.List;

import nl.topicus.cobra.quartz.DetachableJobDataMap;
import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.SoortOnderwijsTax;
import nl.topicus.eduarte.krd.web.components.choice.SoortOnderwijsMultipleCheckbox;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.markup.html.form.DropDownChoice;

public class BronExamenverzamelingenAanmakenJobDataMap extends DetachableJobDataMap
{
	private static final long serialVersionUID = 1L;

	public BronExamenverzamelingenAanmakenJobDataMap()
	{
		setSchooljaar(Schooljaar.huidigSchooljaar());
		setAlleenGewijzigdeDeelnames(true);
	}

	@SuppressWarnings("unchecked")
	@AutoForm(include = false)
	public List<Long> getExamendeelnames()
	{
		return (List<Long>) get("examendeelnames");
	}

	@AutoForm(include = false)
	public void setExamendeelnames(List<Long> examendeelnames)
	{
		put("examendeelnames", examendeelnames);
	}

	@AutoForm(required = true, editorClass = DropDownChoice.class)
	public Schooljaar getSchooljaar()
	{
		return (Schooljaar) get("schooljaar");
	}

	public void setSchooljaar(Schooljaar schooljaar)
	{
		put("schooljaar", schooljaar);
	}

	@AutoForm(required = true, editorClass = EnumCombobox.class)
	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return (BronOnderwijssoort) get("bronOnderwijssoort");
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		put("bronOnderwijssoort", bronOnderwijssoort);
	}

	@AutoForm(required = true)
	public Boolean isAlleenGewijzigdeDeelnames()
	{
		return (Boolean) get("alleenGewijzigdeDeelnames");
	}

	public void setAlleenGewijzigdeDeelnames(Boolean alleenGewijzigdeDeelnames)
	{
		put("alleenGewijzigdeDeelnames", alleenGewijzigdeDeelnames);
	}

	@SuppressWarnings("unchecked")
	@AutoForm(editorClass = SoortOnderwijsMultipleCheckbox.class)
	public List<SoortOnderwijsTax> getSoortOnderwijsTax()
	{
		return (List<SoortOnderwijsTax>) get("soortOnderwijsTax");
	}

	public void setSoortOnderwijsTax(List<SoortOnderwijsTax> soortOnderwijsTax)
	{
		put("soortOnderwijsTax", soortOnderwijsTax);
	}

}