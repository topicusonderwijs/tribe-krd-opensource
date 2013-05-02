package nl.topicus.eduarte.krdparticipatie.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.enums.AbsentieSoort;
import nl.topicus.eduarte.krdparticipatie.principals.beheer.BeheerAbsentieredenen;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieRedenZoekFilter;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.markup.html.form.Form;

/**
 * Pagina voor het toevoegen/wijzigen van een absentiereden.
 * 
 * @author loite
 */
@PageInfo(title = "Absentiereden bewerken", menu = "Beheer > Participatie > Absentieredenen > [absentiereden]")
@InPrincipal(BeheerAbsentieredenen.class)
public class EditAbsentieRedenPage extends AbstractBeheerPage<Void> implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private final Form<AbsentieReden> form;

	private final AbsentieRedenZoekFilter filter;

	private static final AbsentieReden createDefaultAbsentieReden(AbsentieRedenZoekFilter filter)
	{
		AbsentieReden reden = new AbsentieReden();
		reden.setOrganisatieEenheid(filter == null ? null : filter.getOrganisatieEenheid());
		reden.setAbsentieSoort(AbsentieSoort.Absent);

		reden.setActief(true);

		return reden;
	}

	public EditAbsentieRedenPage(AbsentieRedenZoekFilter filter)
	{
		this(createDefaultAbsentieReden(filter), filter);
	}

	public EditAbsentieRedenPage(AbsentieReden absentieReden, AbsentieRedenZoekFilter filter)
	{
		super(ParticipatieBeheerMenuItem.Absentieredenen);
		this.filter = filter;
		form =
			new Form<AbsentieReden>("form", ModelFactory.getCompoundModel(absentieReden,
				new DefaultModelManager(AbsentieReden.class)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					AbsentieReden reden = getModelObject();
					if (reden.bestaatEnActief())
						error("Deze omschrijving of afkorting bestaat al in deze, hogere of lagere organisatie-eenheid");
					else
					{
						reden.saveOrUpdate();
						reden.commit();
						setResponsePage(new AbsentieRedenenPage(EditAbsentieRedenPage.this.filter));
					}
				}

			};

		AutoFieldSet<AbsentieReden> fieldset =
			new AutoFieldSet<AbsentieReden>("fieldset", form.getModel(), "Absentiereden");
		fieldset.setRenderMode(RenderMode.EDIT);
		OrganisatieEenheidLocatieFieldModifier orgEhdLocModifier =
			new OrganisatieEenheidLocatieFieldModifier();
		fieldset.addFieldModifier(orgEhdLocModifier);
		add(form);

		form.add(fieldset);
		createComponents();

	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, AbsentieRedenenPage.class));
		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				AbsentieReden reden = form.getModelObject();
				reden.delete();
				reden.commit();
				setResponsePage(new AbsentieRedenenPage(filter));
			}

			@Override
			public boolean isVisible()
			{
				AbsentieReden reden = form.getModelObject();
				return (reden.isSaved() && !reden.inGebruik());
			}
		});
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		if (filter != null)
		{
			filter.detach();
		}

	}

}
