package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.eduarte.krd.web.components.choice.BronAanleverpuntComboBox;
import nl.topicus.eduarte.krd.web.components.choice.BronSchooljaarComboBox;
import nl.topicus.eduarte.krd.zoekfilters.BronSchooljaarStatusZoekFilter;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Panel voor het wijzigen van bronschooljaarstatussen
 * 
 * @author vandekamp
 */
public class WijzigStatusPanel extends ModalWindowBasePanel<Void>
{
	private static final long serialVersionUID = 1L;

	private Form<BronSchooljaarStatusZoekFilter> myForm;

	private IModel<BronOnderwijssoort> onderwijssoortModel =
		new Model<BronOnderwijssoort>(BronOnderwijssoort.BEROEPSONDERWIJS);

	private IModel<BronStatus> statusModel =
		new Model<BronStatus>(BronStatus.VolledigheidsverklaringGeregistreerd);

	public WijzigStatusPanel(String id, WijzigStatusModalWindow modalWindow)
	{
		super(id, modalWindow);
		myForm = new Form<BronSchooljaarStatusZoekFilter>("form");
		BronSchooljaarStatusZoekFilter filter = new BronSchooljaarStatusZoekFilter();
		myForm.setModel(new CompoundPropertyModel<BronSchooljaarStatusZoekFilter>(filter));
		myForm.add(new BronSchooljaarComboBox("bronSchooljaarStatus", filter).setRequired(true));
		myForm.add(new EnumCombobox<BronOnderwijssoort>("onderwijssoort", onderwijssoortModel,
			BronOnderwijssoort.values()).setRequired(true));
		myForm.add(new EnumCombobox<BronStatus>("status", statusModel, BronStatus.values())
			.setRequired(true));

		BronAanleverpuntComboBox aanleverpuntCombo = new BronAanleverpuntComboBox("aanleverpunt");
		aanleverpuntCombo.setRequired(true);
		aanleverpuntCombo.connectListForAjaxRefresh(myForm.get("bronSchooljaarStatus"));
		myForm.add(aanleverpuntCombo);

		add(myForm);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		AjaxOpslaanButton submitLink = new AjaxOpslaanButton(panel, myForm)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form< ? > form)
			{
				BronSchooljaarStatusZoekFilter filter = myForm.getModelObject();
				BronSchooljaarStatus schooljaarStatus = filter.getBronSchooljaarStatus();
				BronStatus status = statusModel.getObject();
				switch (onderwijssoortModel.getObject())
				{
					case BEROEPSONDERWIJS:
						schooljaarStatus.setStatusBO(status);
						break;
					case EDUCATIE:
						schooljaarStatus.setStatusED(status);
						break;
					case VAVO:
						schooljaarStatus.setStatusVAVO(status);
						break;
					case VOORTGEZETONDERWIJS:
						schooljaarStatus.setStatusVO(status);
						break;
					default:
						break;
				}
				schooljaarStatus.saveOrUpdate();
				schooljaarStatus.commit();
				getModalWindow().close(target);

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > form)
			{
				refreshFeedback(target);
			}
		};
		panel.addButton(submitLink);
		AjaxAnnulerenButton annuleren = new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		};
		panel.addButton(annuleren);
		super.fillBottomRow(panel);
	}
}
