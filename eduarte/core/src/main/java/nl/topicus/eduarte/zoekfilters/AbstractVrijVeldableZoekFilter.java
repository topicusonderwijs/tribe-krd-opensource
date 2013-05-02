package nl.topicus.eduarte.zoekfilters;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;

import org.apache.wicket.model.IModel;

public class AbstractVrijVeldableZoekFilter<V extends VrijVeldEntiteit, T> extends
		AbstractZoekFilter<T> implements VrijVeldable<V>
{
	private static final long serialVersionUID = 1L;

	private Class<V> vrijVeldEntiteitClass;

	private List<IModel<V>> vrijVeldenModels;

	private List<V> vrijVelden;

	public AbstractVrijVeldableZoekFilter(Class<V> vrijVeldEntiteitClass)
	{
		this.vrijVeldEntiteitClass = vrijVeldEntiteitClass;
	}

	@Override
	public List<V> getVrijVelden()
	{
		if (vrijVelden == null && vrijVeldenModels == null)
			vrijVelden = new ArrayList<V>();
		else if (vrijVelden == null)
		{
			vrijVelden = new ArrayList<V>();

			for (IModel<V> vrijveldModel : vrijVeldenModels)
				vrijVelden.add(vrijveldModel.getObject());
		}

		return vrijVelden;
	}

	@Override
	public List<V> getVrijVelden(VrijVeldCategorie categorie)
	{
		List<V> res = new ArrayList<V>();
		for (V pvv : getVrijVelden())
		{
			if (pvv.getVrijVeld().getCategorie().equals(categorie))
			{
				res.add(pvv);
			}
		}
		return res;
	}

	@Override
	public V newVrijVeld()
	{
		return ReflectionUtil.invokeConstructor(vrijVeldEntiteitClass);
	}

	@Override
	public void setVrijVelden(List<V> vrijvelden)
	{
		this.vrijVelden = vrijvelden;
	}

	@Override
	protected void detachModels(Class< ? > clazz)
	{

		if (vrijVelden != null)
		{
			ModelManager modelManager =
				new DefaultModelManager(VrijVeldOptieKeuze.class, vrijVeldEntiteitClass);
			vrijVeldenModels = new ArrayList<IModel<V>>();

			for (V vrijVeld : getVrijVelden())
				if (vrijVeld.isIngevuld())
					vrijVeldenModels.add(modelManager.getModel(vrijVeld, null));
		}

		if (vrijVeldenModels != null)
		{
			for (IModel<V> vrijveldModel : vrijVeldenModels)
				vrijveldModel.detach();
		}
		vrijVelden = null;
		super.detachModels(clazz);
	}
}
