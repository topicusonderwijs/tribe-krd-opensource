package nl.topicus.eduarte.web.components.choice;

import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.TaxonomieElementTypeDataAccessHelper;
import nl.topicus.eduarte.entities.taxonomie.SoortTaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.providers.TaxonomieProvider;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Combobox die de taxonomieelementtypes van een bepaalde taxonomie toont, dus voor
 * bijvoorbeeld het CGO Opleidingsdomein, Diplomagebied, Kwalificatiedossier, Uitstroom,
 * Kerntaak, Werkproces, Werkproces-competentie.
 * 
 * @author loite
 */
public class TaxonomieElementTypeCombobox extends AbstractAjaxDropDownChoice<TaxonomieElementType>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends
			LoadableDetachableModel<List<TaxonomieElementType>>
	{
		private static final long serialVersionUID = 1L;

		private final TaxonomieProvider taxonomieProvider;

		private final boolean includeerTaxonomie;

		private final SoortTaxonomieElement soort;

		private ListModel(TaxonomieProvider taxonomieProvider, boolean includeerTaxonomie,
				SoortTaxonomieElement soort)
		{
			this.taxonomieProvider = taxonomieProvider;
			this.includeerTaxonomie = includeerTaxonomie;
			this.soort = soort;
		}

		@Override
		protected List<TaxonomieElementType> load()
		{
			Taxonomie taxonomie = taxonomieProvider.getTaxonomie();
			if (taxonomie == null)
			{
				return Collections.emptyList();
			}
			List<TaxonomieElementType> res =
				DataAccessRegistry.getHelper(TaxonomieElementTypeDataAccessHelper.class).list(
					taxonomie, soort);
			if (includeerTaxonomie)
			{
				res.add(0, TaxonomieElementType.getTaxonomieType());
			}
			return res;
		}

	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param taxonomieProvider
	 * @param includeerTaxonomie
	 *            Moet ook het landelijk gedefinieerde type 'Taxonomie' getoond worden.
	 */
	public TaxonomieElementTypeCombobox(String id, TaxonomieProvider taxonomieProvider,
			boolean includeerTaxonomie)
	{
		this(id, null, taxonomieProvider, includeerTaxonomie);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param taxonomieProvider
	 * @param includeerTaxonomie
	 *            Moet ook het landelijk gedefinieerde type 'Taxonomie' getoond worden.
	 */
	public TaxonomieElementTypeCombobox(String id, IModel<TaxonomieElementType> model,
			TaxonomieProvider taxonomieProvider, boolean includeerTaxonomie)
	{
		super(id, model, new ListModel(taxonomieProvider, includeerTaxonomie, null),
			new ToStringRenderer());
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param taxonomieProvider
	 * @param includeerTaxonomie
	 *            Moet ook het landelijk gedefinieerde type 'Taxonomie' getoond worden.
	 * @param soort
	 *            Alleen een bepaalde soort taxonomie-elementen tonen
	 */
	public TaxonomieElementTypeCombobox(String id, IModel<TaxonomieElementType> model,
			TaxonomieProvider taxonomieProvider, boolean includeerTaxonomie,
			SoortTaxonomieElement soort)
	{
		super(id, model, new ListModel(taxonomieProvider, includeerTaxonomie, soort),
			new ToStringRenderer());
	}

}
