package nl.topicus.eduarte.web.pages.beheer.landelijk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.CobraEncryptonProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.entities.organisatie.ModuleAfname;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

@InPrincipal(Always.class)
@RechtenSoorten(RechtenSoort.BEHEER)
@PageInfo(title = "Samenvattingzin bewerken", menu = "Beheer > Samenvattingzinnen > [zin]")
public class ModuleAfnameEditPage extends AbstractBeheerPage<ModuleAfname> implements IEditPage
{
	private Form<Void> form;

	public ModuleAfnameEditPage(ModuleAfname afname)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(afname, new DefaultModelManager(
			ModuleAfname.class)), BeheerMenuItem.Modules);

		form = new Form<Void>("form");
		add(form);

		ArrayList<EduArteModuleKey> modules =
			new ArrayList<EduArteModuleKey>(EnumSet.allOf(EduArteModuleKey.class));
		Iterator<EduArteModuleKey> it = modules.iterator();
		while (it.hasNext())
		{
			if (!it.next().isAfneembaar())
				it.remove();
		}
		Collections.sort(modules, new Comparator<EduArteModuleKey>()
		{
			@Override
			public int compare(EduArteModuleKey o1, EduArteModuleKey o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});

		AutoFieldSet<ModuleAfname> fieldset =
			new AutoFieldSet<ModuleAfname>("fieldset", getContextModel(), "Module-afname");
		fieldset.setPropertyNames("organisatie", "module", "checksum", "actief");
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.addFieldModifier(new ConstructorArgModifier("module", modules));
		fieldset.addFieldModifier(new EduArteAjaxRefreshModifier("organisatie", "module",
			"checksum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getAfname().setOrganizationName(getAfname().getOrganisatie().getNaam());
				getAfname().berekenChecksum(new CobraEncryptonProvider());
			}
		});
		fieldset.addFieldModifier(new EduArteAjaxRefreshModifier("module", "checksum")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				getAfname().berekenChecksum(new CobraEncryptonProvider());
			}
		});
		fieldset.setRenderMode(RenderMode.EDIT);
		form.add(new UniqueConstraintFormValidator(fieldset, "module-afname", "organisatie")
			.setProperties("moduleName"));
		form.add(fieldset);

		createComponents();
	}

	private ModuleAfname getAfname()
	{
		return (ModuleAfname) getDefaultModelObject();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<ModuleAfname> getAfnameModel()
	{
		return (IChangeRecordingModel<ModuleAfname>) getDefaultModel();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				IChangeRecordingModel<ModuleAfname> model = getAfnameModel();
				model.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				EduArteApp.get().clearActiveModuleCache();
				setResponsePage(ModuleAfnameZoekenPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, ModuleAfnameZoekenPage.class));

		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				IChangeRecordingModel<ModuleAfname> model = getAfnameModel();
				model.deleteObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				EduArteApp.get().clearActiveModuleCache();
				setResponsePage(ModuleAfnameZoekenPage.class);
			}

			@Override
			public boolean isVisible()
			{
				return getAfname().isSaved();
			}
		});
	}
}
