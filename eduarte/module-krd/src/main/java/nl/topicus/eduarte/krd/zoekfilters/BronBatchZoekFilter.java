/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.zoekfilters;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;

import org.apache.wicket.model.IModel;

/**
 * Filter voor {@link IBronBatch} .
 * 
 * @author vandekamp
 */
public class BronBatchZoekFilter extends AbstractBronZoekFilter<IBronBatch>
{
	private static final long serialVersionUID = 1L;

	@AutoForm(editorClass = EnumCombobox.class, htmlClasses = "unit_160")
	private BronOnderwijssoort onderwijssoort;

	@AutoForm(label = "Batchnr", htmlClasses = "unit_40")
	private Integer batchnummer;

	@AutoForm(label = "In behandeling", editorClass = JaNeeCombobox.class)
	private Boolean heeftMeldingenInBehandeling;

	public BronBatchZoekFilter(BronAanleverpunt aanleverpunt, Schooljaar schooljaar)
	{
		super(aanleverpunt, schooljaar);
	}

	public BronBatchZoekFilter(IModel<BronSchooljaarStatus> bronSchooljaarStatusModel)
	{
		super(bronSchooljaarStatusModel);
	}

	public BronOnderwijssoort getOnderwijssoort()
	{
		return onderwijssoort;
	}

	public void setOnderwijssoort(BronOnderwijssoort onderwijssoort)
	{
		this.onderwijssoort = onderwijssoort;
	}

	public void setBatchnummer(Integer batchnummer)
	{
		this.batchnummer = batchnummer;
	}

	public Integer getBatchnummer()
	{
		return batchnummer;
	}

	public Boolean isHeeftMeldingenInBehandeling()
	{
		return heeftMeldingenInBehandeling;
	}

	public void setHeeftMeldingenInBehandeling(Boolean heeftMeldingenInBehandeling)
	{
		this.heeftMeldingenInBehandeling = heeftMeldingenInBehandeling;
	}
}
