package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.model.IModel;

public class CollectiefAanmakenModel implements IModel<CollectiefAanmakenModel>
{
	private static final long serialVersionUID = 1L;

	private ModelManager plaatsingModelManager = new DefaultModelManager(Plaatsing.class);

	private ModelManager verbintenisModelManager =
		new DefaultModelManager(Deelnemer.class, Plaatsing.class, Verbintenis.class);

	private IModel<Plaatsing> nieuwePlaatsing =
		ModelFactory.getModel(new Plaatsing(), plaatsingModelManager);

	private IModel<Verbintenis> nieuweVerbintenis =
		ModelFactory.getModel(new Verbintenis(new Deelnemer()), verbintenisModelManager);

	private Class< ? extends Entiteit> soort;

	private boolean onderwijsproductAfnamesAanmaken = false;

	public CollectiefAanmakenModel(Class< ? extends Entiteit> soort)
	{
		this.soort = soort;
	}

	public Plaatsing getNieuwePlaatsing()
	{
		return nieuwePlaatsing.getObject();
	}

	public void setNieuwePlaatsing(Plaatsing nieuwePlaatsing)
	{
		this.nieuwePlaatsing = ModelFactory.getModel(nieuwePlaatsing, plaatsingModelManager);
	}

	public void setNieuwePlaatsing(IModel<Plaatsing> nieuwePlaatsing)
	{
		this.nieuwePlaatsing = nieuwePlaatsing;
	}

	public Verbintenis getNieuweVerbintenis()
	{
		return nieuweVerbintenis.getObject();
	}

	public void setNieuweVerbintenis(Verbintenis nieuweVerbintenis)
	{
		this.nieuweVerbintenis = ModelFactory.getModel(nieuweVerbintenis, verbintenisModelManager);
	}

	public void setNieuweVerbintenis(IModel<Verbintenis> nieuweVerbintenis)
	{
		this.nieuweVerbintenis = nieuweVerbintenis;
	}

	public ModelManager getPlaatsingModelManager()
	{
		return plaatsingModelManager;
	}

	public ModelManager getVerbintenisModelManager()
	{
		return verbintenisModelManager;
	}

	public Class< ? extends Entiteit> getSoort()
	{
		return soort;
	}

	public void setSoort(Class< ? extends Entiteit> soort)
	{
		this.soort = soort;
	}

	public Boolean getOnderwijsproductAfnamesAanmaken()
	{
		return onderwijsproductAfnamesAanmaken;
	}

	public void setOnderwijsproductAfnamesAanmaken(Boolean onderwijsproductAfnamesAanmaken)
	{
		this.onderwijsproductAfnamesAanmaken = onderwijsproductAfnamesAanmaken;
	}

	@Override
	public CollectiefAanmakenModel getObject()
	{
		return this;
	}

	@Override
	public void setObject(CollectiefAanmakenModel object)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(nieuwePlaatsing);
		ComponentUtil.detachQuietly(nieuweVerbintenis);
	}

}