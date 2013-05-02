package nl.topicus.eduarte.web.components.modalwindow.maatregeltoekenning;

import java.util.List;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxOpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.participatie.MaatregelToekenning;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.MaatregelComboBox;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class MaatregelToekenningEditPanel extends ModalWindowBasePanel<List<MaatregelToekenning>>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private IModel< ? extends List<MaatregelToekenning>> maatregelToekenningModel;

	public MaatregelToekenningEditPanel(String id, MaatregelToekenningEditModalWindow modalWindow)
	{
		super(id, modalWindow);
		maatregelToekenningModel = modalWindow.getMaatregelToekenningenModel();
		form = new Form<Void>("form");
		add(form);

		ListView<MaatregelToekenning> listview =
			new ListView<MaatregelToekenning>("listview", maatregelToekenningModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<MaatregelToekenning> item)
				{
					item.setModel(new CompoundPropertyModel<MaatregelToekenning>(item.getModel()));
					item.add(new Label("deelnemer.persoon.volledigeNaam"));
					MaatregelComboBox maatregelCombo = new MaatregelComboBox("maatregel", true);
					maatregelCombo.setNullValid(false);
					item.add(maatregelCombo);
					item.add(new DatumField("maatregelDatum"));
					item.add(new Label("veroorzaaktDoor.absentieReden.omschrijving"));
					item.add(new TextField<String>("opmerkingen"));
				}
			};
		listview.setReuseItems(true);
		form.add(listview);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxOpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings("hiding")
			protected void onSubmit(AjaxRequestTarget target, Form< ? > form)
			{
				for (MaatregelToekenning maatregelToekenning : getMaatregelToekenningListObject())
				{
					maatregelToekenning.saveOrUpdate();
					maatregelToekenning.commit();
				}
				getModalWindow().close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form< ? > _form)
			{
				refreshFeedback(target);
			}
		});
		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(maatregelToekenningModel);
	}

	protected List<MaatregelToekenning> getMaatregelToekenningListObject()
	{
		return maatregelToekenningModel.getObject();
	}
}
