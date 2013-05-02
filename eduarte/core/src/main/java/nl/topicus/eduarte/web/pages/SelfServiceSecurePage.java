/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.menu.main.MainMenuItem;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.model.IModel;

/**
 * Basis pagina voor alle beveiligde pagina's in het deelnemer portaal. In feite dus alles
 * behalve de login / loguit pagina's. Al deze pagina's hebben een naam / titel, deze
 * dient via de constructor gezet te worden of via een {@link PageInfo} annotatie
 * 
 * @author marrink
 */
@RechtenSoorten(RechtenSoort.DEELNEMER)
public abstract class SelfServiceSecurePage extends SecurePage
{
	private final IModel<Verbintenis> verbintenisModel;

	protected final IModel<Deelnemer> deelnemerModel;

	protected static final Verbintenis getDefaultVerbintenis()
	{
		return getIngelogdeDeelnemer().getEersteInschrijvingOpPeildatum();
	}

	public SelfServiceSecurePage(MainMenuItem selectedItem)
	{
		this(selectedItem, getIngelogdeDeelnemer());
	}

	public SelfServiceSecurePage(MainMenuItem selectedItem, Deelnemer deelnemer)
	{
		this(selectedItem, getDefaultVerbintenis(), deelnemer);
	}

	public SelfServiceSecurePage(MainMenuItem selectedItem, Verbintenis verbintenis)
	{
		this(selectedItem, verbintenis, verbintenis.getDeelnemer());
	}

	public SelfServiceSecurePage(MainMenuItem selectedItem, Verbintenis verbintenis,
			Deelnemer deelnemer)
	{
		super(selectedItem);
		verbintenisModel = ModelFactory.getModel(verbintenis);
		deelnemerModel = ModelFactory.getModel(deelnemer);
		get(ID_LAYSIDE).setVisible(false);
	}

	public Deelnemer getContextDeelnemer()
	{
		return deelnemerModel.getObject();
	}

	public IModel<Deelnemer> getContextDeelnemerModel()
	{
		return deelnemerModel;
	}

	public Verbintenis getContextVerbintenis()
	{
		return verbintenisModel.getObject();
	}

	public IModel<Verbintenis> getContextVerbintenisModel()
	{
		return verbintenisModel;
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		deelnemerModel.detach();
		verbintenisModel.detach();
	}
}
