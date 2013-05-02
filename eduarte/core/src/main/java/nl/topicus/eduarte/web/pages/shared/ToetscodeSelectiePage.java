/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.shared;

import static nl.topicus.eduarte.web.components.resultaat.AbstractToetsBoomComparator.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.IdBasedModelSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.selection.CheckboxSelectionColumn;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.datapanel.selection.bottomrow.AbstractSelecterenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.core.principals.deelnemer.resultaten.DeelnemerResultatenboom;
import nl.topicus.eduarte.core.principals.deelnemer.resultaten.DeelnemerResultatenmatrix;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.ToetsCodeColumn;
import nl.topicus.eduarte.web.components.resultaat.StructuurToetsComparator;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Toetsfilters", menu = {
	"Home > Instellingen > Toetsfilters > toevoegen > Toetscodes selecteren",
	"Home > Instellingen > Toetsfilters > [filter] > Toetscodes selecteren"})
@InPrincipal( {DeelnemerResultatenboom.class, DeelnemerResultatenmatrix.class})
@RechtenSoorten(RechtenSoort.INSTELLING)
public class ToetscodeSelectiePage extends AbstractDynamicContextPage<Set<String>> implements
		IEditPage, ISelectionComponent<Serializable, Toets>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private EduArteDataPanel<Toets> datapanel;

	private SecurePage contextPage;

	private SecurePage returnPage;

	@SpringBean
	private ResultaatstructuurDataAccessHelper structuurHelper;

	private IModel<Cohort> cohort;

	private IModel<List<Onderwijsproduct>> selectie;

	private IModel<List<Toets>> alleToetsen;

	private IdBasedModelSelection<Toets> toetsSelectie;

	public ToetscodeSelectiePage(final IModel<Set<String>> toetscodesModel, Cohort cohort,
			List<Onderwijsproduct> selectie, final SecurePage contextPage, SecurePage returnPage)
	{
		super(toetscodesModel, new SubpageContext(contextPage));

		this.cohort = ModelFactory.getModel(cohort);
		this.selectie = ModelFactory.getListModel(selectie);
		alleToetsen = new LoadableDetachableModel<List<Toets>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Toets> load()
			{
				return getSortedToetsen();
			}
		};
		this.contextPage = contextPage;
		this.returnPage = returnPage;

		initToetsSelectie();

		add(form = new Form<Void>("form")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				SortedSet<String> selectedCodes = new TreeSet<String>();
				for (Toets curToets : getAllToetsen())
				{
					if (toetsSelectie.isSelected(curToets))
						selectedCodes.add(curToets.getCode());
				}
				toetscodesModel.setObject(selectedCodes);
				setResponsePage(contextPage);
			}
		});

		CustomDataPanelContentDescription<Toets> table =
			new CustomDataPanelContentDescription<Toets>("Toetscodes");
		table.addColumn(new ToetsCodeColumn(new Model<Integer>(getMaxDepth())));
		table.addColumn(new CustomPropertyColumn<Toets>("Onderwijsproduct", "Onderwijsproduct",
			"resultaatstructuur.onderwijsproduct"));
		table.addColumn(new CustomPropertyColumn<Toets>("Cohort", "Cohort",
			"resultaatstructuur.cohort"));
		table.addColumn(new CheckboxSelectionColumn<Serializable, Toets>("Selectie", "",
			toetsSelectie).setPositioning(Positioning.FIXED_LEFT));

		form.add(datapanel =
			new EduArteDataPanel<Toets>("datapanel", new ListModelDataProvider<Toets>(alleToetsen),
				table));
		createComponents();
	}

	private Set<String> getToetsCodes()
	{
		return getContextModelObject();
	}

	private List<Toets> getAllToetsen()
	{
		List<Toets> ret = new ArrayList<Toets>();
		for (Resultaatstructuur curStructuur : structuurHelper.getResultaatstructuren(selectie
			.getObject(), cohort.getObject()))
		{
			ret.addAll(curStructuur.getToetsen());
		}
		return ret;
	}

	private void initToetsSelectie()
	{
		toetsSelectie = new IdBasedModelSelection<Toets>();
		Set<String> selected = getToetsCodes();
		for (Toets curToets : getAllToetsen())
		{
			if (selected.contains(curToets.getCode()))
				toetsSelectie.add(curToets);
		}
	}

	private List<Toets> getSortedToetsen()
	{
		List<Toets> ret = getAllToetsen();
		Collections.sort(ret, new StructuurToetsComparator(ASCENDING)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getPathPrefix(Resultaatstructuur structuur)
			{
				return "";
			}
		});
		return ret;
	}

	private int getMaxDepth()
	{
		int maxDepth = 0;
		for (Toets curToets : getAllToetsen())
		{
			maxDepth = Math.max(curToets.getDepth(), maxDepth);
		}
		return maxDepth + 1;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AbstractSelecterenButton.addSelectieButtons(panel, this);
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new TerugButton(panel, returnPage).setLabel("Vorige"));
		panel.addButton(new AnnulerenButton(panel, contextPage));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachFields(this);
	}

	@Override
	public CustomDataPanel<Toets> getDataPanel()
	{
		return datapanel;
	}

	@Override
	public DetachableZoekFilter<Toets> getFilter()
	{
		return null;
	}

	@Override
	public List<Serializable> getSelectedElements()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Toets> getSelectedSearchElements()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public IdBasedModelSelection<Toets> getSelection()
	{
		return toetsSelectie;
	}

	@Override
	public void onSelectionChanged(AjaxRequestTarget target)
	{
		// doe niets, want dat hoeft hier niet
	}
}
