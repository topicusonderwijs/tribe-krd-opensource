package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.io.Serializable;
import java.util.Date;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class DeelnemerCollectieveAfnameContext implements Serializable, IDetachable
{
	private static final long serialVersionUID = 1L;

	private IModel<Productregel> productregel;

	private IModel<Onderwijsproduct> onderwijsproduct;

	private IModel<Opleiding> opleiding;

	private Date beginDatum;

	private Date eindDatum;

	private BijBestaandeKeuzeEnum bijBestaandeKeuzeEnum;

	public DeelnemerCollectieveAfnameContext(Productregel regel, Opleiding opleiding)
	{
		setProductregel(regel);
		setOpleiding(opleiding);
		setBijBestaandeKeuzeEnum(BijBestaandeKeuzeEnum.GeenWijziging);
	}

	public Productregel getProductregel()
	{
		if (productregel != null)
			return productregel.getObject();
		return null;
	}

	public void setProductregel(Productregel productregel)
	{
		this.productregel = ModelFactory.getModel(productregel);
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

	public BijBestaandeKeuzeEnum getBijBestaandeKeuzeEnum()
	{
		return bijBestaandeKeuzeEnum;
	}

	public void setBijBestaandeKeuzeEnum(BijBestaandeKeuzeEnum bijBestaandeKeuzeEnum)
	{
		this.bijBestaandeKeuzeEnum = bijBestaandeKeuzeEnum;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(productregel);
		ComponentUtil.detachQuietly(onderwijsproduct);
		ComponentUtil.detachQuietly(opleiding);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		if (onderwijsproduct != null)
			return onderwijsproduct.getObject();
		return null;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = ModelFactory.getModel(onderwijsproduct);
	}

	public Opleiding getOpleiding()
	{
		if (opleiding != null)
			return opleiding.getObject();
		return null;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = ModelFactory.getModel(opleiding);
	}
}
