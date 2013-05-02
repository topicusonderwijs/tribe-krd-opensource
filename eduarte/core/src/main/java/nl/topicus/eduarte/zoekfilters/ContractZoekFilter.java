/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.SoortContract;
import nl.topicus.eduarte.entities.contract.TypeFinanciering;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.ContractVrijVeld;
import nl.topicus.eduarte.web.components.choice.SoortContractCombobox;

import org.apache.wicket.model.IModel;

public class ContractZoekFilter extends AbstractVrijVeldableZoekFilter<ContractVrijVeld, Contract>
		implements ICodeNaamZoekFilter<Contract>
{
	private static final long serialVersionUID = 1L;

	private String naam;

	private String code;

	@AutoForm(editorClass = SoortContractCombobox.class)
	private IModel<SoortContract> soortContract;

	private IModel<TypeFinanciering> typeFinanciering;

	private IModel<Date> inschrijfdatumModel;

	private IModel<Medewerker> beheerder;

	private IModel<ExterneOrganisatie> externeOrganisatie;

	@AutoForm(label = "Toon inactieve contracten")
	private boolean toonInactief = false;

	public ContractZoekFilter()
	{
		super(ContractVrijVeld.class);
	}

	@Override
	public String getNaam()
	{
		return naam;
	}

	@Override
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Override
	public String getCode()
	{
		return code;
	}

	@Override
	public void setCode(String code)
	{
		this.code = code;
	}

	public SoortContract getSoortContract()
	{
		return getModelObject(soortContract);
	}

	public void setSoortContract(SoortContract soortContract)
	{
		this.soortContract = makeModelFor(soortContract);
	}

	public void setSoortContractModel(IModel<SoortContract> soortContractModel)
	{
		this.soortContract = soortContractModel;
	}

	public TypeFinanciering getTypeFinanciering()
	{
		return getModelObject(typeFinanciering);
	}

	public void setTypeFinanciering(TypeFinanciering typeFinanciering)
	{
		this.typeFinanciering = makeModelFor(typeFinanciering);
	}

	public void setTypeFinancieringModel(IModel<TypeFinanciering> typeFinancieringModel)
	{
		this.typeFinanciering = typeFinancieringModel;
	}

	public IModel<Date> getInschrijfdatumModel()
	{
		return inschrijfdatumModel;
	}

	public void setInschrijfdatumModel(IModel<Date> inschrijfdatumModel)
	{
		this.inschrijfdatumModel = inschrijfdatumModel;
	}

	public Date getInschrijfdatum()
	{
		return getModelObject(inschrijfdatumModel);
	}

	public void setToonInactief(boolean toonInactief)
	{
		this.toonInactief = toonInactief;
	}

	public boolean isToonInactief()
	{
		return toonInactief;
	}

	public Medewerker getBeheerder()
	{
		return getModelObject(beheerder);
	}

	public void setBeheerder(Medewerker beheerder)
	{
		this.beheerder = makeModelFor(beheerder);
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = makeModelFor(externeOrganisatie);
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return getModelObject(externeOrganisatie);
	}

	@Override
	public Class<Contract> getEntityClass()
	{
		return Contract.class;
	}
}
