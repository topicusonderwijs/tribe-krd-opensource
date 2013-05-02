package nl.topicus.eduarte.web.pages.beheer.rapportage;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.templates.ExtractPropertyUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.core.principals.rapportage.DocumentTemplateWrite;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.resultaat.ResultaatRapportageConfiguratiePanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

@InPrincipal(DocumentTemplateWrite.class)
@PageInfo(title = "Overzicht samenvoegvelden", menu = {"Beheer > Rapportage > Overzicht samenvoegvelden"})
public class MergeFieldsPage extends AbstractBeheerPage<Void>
{
	private String context = DocumentTemplateContext.Verbintenis.toString();

	private IModel<String> mergefields;

	private Label fields;

	private boolean flat = true;

	private boolean rememberDone = true;

	private boolean showTypes = false;

	private Map<String, MergeFieldResolver> resolvers = new TreeMap<String, MergeFieldResolver>();

	public MergeFieldsPage()
	{
		super("Overzicht samenvoegvelden", BeheerMenuItem.OverzichtSamenvoegvelden);

		fillResolvers();

		mergefields = new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				StringWriter writer = new StringWriter();
				ExtractPropertyUtil extractPropertyUtil =
					new ExtractPropertyUtil(flat, showTypes, rememberDone, Organisatie.class);
				resolvers.get(getContext()).resolveMergeFields(extractPropertyUtil, writer);
				extractPropertyUtil.extractClass(Medewerker.class, "huidigemedewerker", writer);
				extractPropertyUtil.extractClass(Organisatie.class, "huidigeorganisatie", writer);

				extractPropertyUtil.extractClass(Account.class, "huidigaccount", writer);
				extractPropertyUtil.extractClass(Date.class, "datum", writer);
				String text = writer.toString();
				return text;
			}
		};
		fields = new Label("mergefields", mergefields);
		fields.setOutputMarkupId(true);
		add(fields);
		add(new AbstractAjaxDropDownChoice<String>("context", new PropertyModel<String>(this,
			"context"), new ArrayList<String>(resolvers.keySet()))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, String newSelection)
			{
				target.addComponent(fields);
			}

		});
		add(new AjaxCheckBox("flat", new PropertyModel<Boolean>(this, "flat"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(fields);
			}
		});
		add(new AjaxCheckBox("showTypes", new PropertyModel<Boolean>(this, "showTypes"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(fields);
			}
		});
		add(new AjaxCheckBox("rememberDone", new PropertyModel<Boolean>(this, "rememberDone"))
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(fields);
			}
		});

		createComponents();
	}

	private void fillResolvers()
	{
		for (DocumentTemplateContext curContext : DocumentTemplateContext.values())
			resolvers.put(curContext.toString(), new ContextMergeFieldResolver(curContext));
		resolvers.put(DocumentTemplateContext.Factuur.toString(), new FactuurMergeFieldResolver());
		resolvers.put("Resultaten", new RapportageConfiguratieMergeFieldResolver(
			ResultaatRapportageConfiguratiePanel.class));
	}

	public void setContext(String context)
	{
		this.context = context;
	}

	public String getContext()
	{
		return context;
	}
}
