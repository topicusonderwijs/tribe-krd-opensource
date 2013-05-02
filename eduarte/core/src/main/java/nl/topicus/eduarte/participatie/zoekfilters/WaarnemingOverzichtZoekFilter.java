package nl.topicus.eduarte.participatie.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.Waarneming;
import nl.topicus.eduarte.entities.participatie.enums.AbsentiePresentieEnum;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingWeergaveEnum;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.LesUurComboBox;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieFactory;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter;

import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class WaarnemingOverzichtZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Waarneming> implements
		RapportageConfiguratieFactory<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemerModel;

	private IModel<Groep> groepModel;

	private IModel<Contract> contract;

	private Date beginDatum;

	private Date eindDatum;

	@AutoForm(editorClass = LesUurComboBox.class)
	private IModel<LesuurIndeling> vanafLesuur;

	@AutoForm(editorClass = LesUurComboBox.class)
	private IModel<LesuurIndeling> totLesuur;

	private AbsentiePresentieEnum absentieOfPresentie;

	private WaarnemingWeergaveEnum waarnemingWeergave;

	private boolean toonLegeRegels;

	private boolean toonTotalenKolommen;

	private IModel<LesweekIndeling> lesweekIndeling;

	public WaarnemingOverzichtZoekFilter()
	{
	}

	public WaarnemingOverzichtZoekFilter(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public WaarnemingOverzichtZoekFilter(Groep groep)
	{
		setGroep(groep);
	}

	public IModel<Deelnemer> getDeelnemerModel()
	{
		return deelnemerModel;
	}

	public void setDeelnemerModel(IModel<Deelnemer> deelnemerModel)
	{
		this.deelnemerModel = deelnemerModel;
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemerModel);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		deelnemerModel = makeModelFor(deelnemer);
	}

	public Groep getGroep()
	{
		return getModelObject(groepModel);
	}

	public void setGroep(Groep groep)
	{
		groepModel = makeModelFor(groep);
	}

	public Date getBeginDatum()
	{
		return beginDatum;
	}

	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	public Date getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	public AbsentiePresentieEnum getAbsentieOfPresentie()
	{
		return absentieOfPresentie;
	}

	public void setAbsentieOfPresentie(AbsentiePresentieEnum absentiePresentieEnum)
	{
		this.absentieOfPresentie = absentiePresentieEnum;
	}

	public WaarnemingWeergaveEnum getWaarnemingWeergave()
	{
		return waarnemingWeergave;
	}

	public void setWaarnemingWeergave(WaarnemingWeergaveEnum waarnemingWeergave)
	{
		this.waarnemingWeergave = waarnemingWeergave;
	}

	public boolean isToonLegeRegels()
	{
		return toonLegeRegels;
	}

	public void setToonLegeRegels(boolean toonLegeRegels)
	{
		this.toonLegeRegels = toonLegeRegels;
	}

	public LesuurIndeling getVanafLesuur()
	{
		if (vanafLesuur != null)
			return vanafLesuur.getObject();
		return null;
	}

	public void setVanafLesuur(LesuurIndeling vanafLesuur)
	{
		this.vanafLesuur = makeModelFor(vanafLesuur);
	}

	public LesuurIndeling getTotLesuur()
	{
		if (totLesuur != null)
			return totLesuur.getObject();
		return null;
	}

	public void setTotLesuur(LesuurIndeling totLesuur)
	{
		this.totLesuur = makeModelFor(totLesuur);
	}

	/**
	 * @param datum
	 * @return de datum van het begin van de week
	 */
	public Date getBeginWeek(Date datum)
	{
		int week = TimeUtil.getInstance().getWeekOfYear(datum);
		int jaar = TimeUtil.getInstance().getYear(datum);
		return TimeUtil.getInstance().getWeekBeginEnEindDatum(jaar, week)[0];
	}

	/**
	 * @param datum
	 * @return de datum van het einde van de week
	 */
	public Date getEindWeek(Date datum)
	{
		int week = TimeUtil.getInstance().getWeekOfYear(datum);
		int jaar = TimeUtil.getInstance().getYear(datum);
		return TimeUtil.getInstance().getWeekBeginEnEindDatum(jaar, week)[1];
	}

	public boolean isToonTotalenKolommen()
	{
		return toonTotalenKolommen;
	}

	public void setToonTotalenKolommen(boolean toonTotalenKolommen)
	{
		this.toonTotalenKolommen = toonTotalenKolommen;
	}

	public Contract getContract()
	{
		return getModelObject(contract);
	}

	public void setContract(Contract contract)
	{
		this.contract = makeModelFor(contract);
	}

	public void setLesweekIndeling(LesweekIndeling lesweekIndeling)
	{
		this.lesweekIndeling = makeModelFor(lesweekIndeling);
	}

	public LesweekIndeling getLesweekIndeling()
	{
		return getModelObject(lesweekIndeling);
	}

	@Override
	public Object createConfiguratie(Verbintenis contextObject)
	{
		return this;
	}
}
