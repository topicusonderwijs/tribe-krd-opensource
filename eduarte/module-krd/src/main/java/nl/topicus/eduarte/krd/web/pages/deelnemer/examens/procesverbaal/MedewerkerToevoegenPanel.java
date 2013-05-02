package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.procesverbaal;

import java.util.List;

import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.panels.datapanel.columns.AjaxDeleteColumn;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.SurveillantTable;
import nl.topicus.eduarte.web.components.modalwindow.medewerker.MedewerkerSelectieModalWindow;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class MedewerkerToevoegenPanel extends TypedPanel<Procesverbaal>
{
	private static final long serialVersionUID = 1L;

	private EduArteDataPanel<Surveillant> datapanel;

	private MedewerkerSelectieModalWindow modalWindow;

	public MedewerkerToevoegenPanel(String id, Procesverbaal procesverbaal,
			MedewerkerZoekFilter filter)
	{
		super(id, new CompoundPropertyModel<Procesverbaal>(procesverbaal));
		IModelDataProvider<Surveillant> provider =
			new IModelDataProvider<Surveillant>(new PropertyModel<List<Surveillant>>(
				getDefaultModel(), "surveillanten"));
		SurveillantTable table = new SurveillantTable();
		table.addColumn(new AjaxDeleteColumn<Surveillant>("Verwijder", "Verwijder")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<Surveillant> rowModel,
					AjaxRequestTarget target)
			{
				Surveillant surveillant = rowModel.getObject();
				if (surveillant != null)
					(getModelObject()).getSurveillanten().remove(surveillant);
				target.addComponent(datapanel);
			}
		});
		datapanel = new EduArteDataPanel<Surveillant>("surveillanten", provider, table);
		add(datapanel);
		modalWindow =
			new MedewerkerSelectieModalWindow("modalWindow", ModelFactory
				.getModel((Medewerker) null), filter, false);
		modalWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				Medewerker medewerker = modalWindow.getModelObject();
				if (medewerker != null)
				{
					Surveillant surveillant =
						new Surveillant(medewerker.getPersoon().getAchternaam(), medewerker
							.getPersoon().getVoorletters());
					getProcesverbaal().getSurveillanten().add(surveillant);
					target.addComponent(datapanel);
				}
			}
		});
		add(modalWindow);
		add(new AjaxLink<Void>("toevoegen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				modalWindow.setDefaultModelObject(null);
				modalWindow.show(target);
			}
		});

		final Form<Surveillant> form =
			new Form<Surveillant>("form", new CompoundPropertyModel<Surveillant>(new Surveillant()));
		form.add(new RequiredTextField<String>("achternaam"));
		form.add(new RequiredTextField<String>("voorletters"));
		form.add(new AjaxSubmitLink("handmatigToevoegen", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > form2)
			{
				Surveillant surveillant = (Surveillant) form2.getModelObject();
				if (surveillant != null)
				{
					getProcesverbaal().getSurveillanten().add(surveillant);
				}
				form.setModelObject(new Surveillant());
				target.addComponent(datapanel);
			}
		});
		add(form);
	}

	public Procesverbaal getProcesverbaal()
	{
		return getModelObject();
	}
}