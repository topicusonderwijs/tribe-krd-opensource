package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductNiveauaanduiding;

import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author idserda
 */
public class OnderwijsproductNiveauaanduidingCombobox extends
		DropDownChoice<OnderwijsproductNiveauaanduiding>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<OnderwijsproductNiveauaanduiding>>
	{
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unchecked")
		@Override
		protected List<OnderwijsproductNiveauaanduiding> load()
		{
			return DataAccessRegistry.getHelper(
				CodeNaamActiefLandelijkOfInstellingEntiteitDataAccessHelper.class).list(
				OnderwijsproductNiveauaanduiding.class, "code");
		}
	}

	public OnderwijsproductNiveauaanduidingCombobox(String id)
	{
		super(id, new ListModel(), new ChoiceRenderer<OnderwijsproductNiveauaanduiding>("naam",
			"id"));
	}

	public OnderwijsproductNiveauaanduidingCombobox(String id,
			IModel<OnderwijsproductNiveauaanduiding> model)
	{
		super(id, model, new ListModel(), new ChoiceRenderer<OnderwijsproductNiveauaanduiding>(
			"naam", "id"));
	}
}
