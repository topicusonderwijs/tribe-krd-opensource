package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.model.IModel;

/**
 * Panel voor het selecteren van onderwijssoort(Beroepsonderwijs, Educatie, VAVO, VO)
 * 
 * @author vandekamop
 */
public class NieuwAanleverpuntPanel extends ModalWindowBasePanel<BronAanleverpunt>
{
	private static final long serialVersionUID = 1L;

	private Form<BronAanleverpunt> myForm;

	public NieuwAanleverpuntPanel(String id, NieuwAanleverpuntModalWindow modalWindow,
			IModel<BronAanleverpunt> aanleverpuntModel)
	{
		super(id, modalWindow);
		myForm = new Form<BronAanleverpunt>("form");
		myForm.setModel(aanleverpuntModel);
		myForm.add(ComponentUtil.fixLength(new RequiredTextField<Integer>("nummer", Integer.class),
			2));
		myForm.add(ComponentUtil.fixLength(new RequiredTextField<Integer>("laatsteBatchNrBO",
			Integer.class), 6));
		myForm.add(ComponentUtil.fixLength(new RequiredTextField<Integer>("laatsteBatchNrED",
			Integer.class), 6));
		myForm.add(ComponentUtil.fixLength(new RequiredTextField<Integer>("laatsteBatchNrVAVO",
			Integer.class), 6));
		myForm.add(ComponentUtil.fixLength(new RequiredTextField<Integer>("laatsteBatchNrVO",
			Integer.class), 6));
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
				DataAccessHelper<BronAanleverpunt> helper =
					DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class);
				List<BronAanleverpunt> aanleverpunten = helper.list(BronAanleverpunt.class);
				BronAanleverpunt bronAanleverpunt = myForm.getModelObject();
				for (BronAanleverpunt aanleverpunt : aanleverpunten)
				{
					if (!aanleverpunt.equals(bronAanleverpunt)
						&& aanleverpunt.getNummer() == bronAanleverpunt.getNummer())
					{
						error("Aanleverpuntnummer bestaat al");
						refreshFeedback(target);
						return;
					}
				}
				bronAanleverpunt.save();
				bronAanleverpunt.commit();
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > form)
			{
				refreshFeedback(target);
			}
		};
		submitLink.setMakeDefault(true);
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
	}
}
