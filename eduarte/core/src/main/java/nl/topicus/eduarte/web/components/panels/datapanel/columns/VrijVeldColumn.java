package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldEntiteit;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class VrijVeldColumn<T> extends CustomPropertyColumn<T>
{
	private static final long serialVersionUID = 1L;

	private IModel<VrijVeld> vrijVeldModel;

	public VrijVeldColumn(VrijVeld vrijVeld, String pathToVrijVeldable)
	{
		super("Vrij veld " + vrijVeld.getNaam(), vrijVeld.getNaam(), pathToVrijVeldable);
		this.vrijVeldModel = ModelFactory.getModel(vrijVeld);
	}

	@Override
	protected IModel< ? > createLabelModel(IModel<T> itemModel)
	{
		final IModel< ? > vrijVeldableModel = super.createLabelModel(itemModel);
		return new LoadableDetachableModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String load()
			{

				Object modelObject = vrijVeldableModel.getObject();
				if (modelObject instanceof VrijVeldable< ? >)
				{
					VrijVeldable< ? extends VrijVeldEntiteit> vrijVeldable =
						(VrijVeldable< ? >) modelObject;
					for (VrijVeldEntiteit curEntiteit : vrijVeldable.getVrijVelden())
					{
						if (curEntiteit.getVrijVeld().equals(vrijVeldModel.getObject()))
						{
							return curEntiteit.getOmschrijving();
						}
					}
				}
				return null;
			}

			@Override
			public void detach()
			{
				super.detach();
				vrijVeldableModel.detach();
			}
		};
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(vrijVeldModel);
		super.detach();
	}
}
