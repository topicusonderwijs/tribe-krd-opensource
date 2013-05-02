package nl.topicus.eduarte.krd.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.ToepassingResultaatExamenvak;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

public class ToepassingResultaatExamenPanel<T> extends TypedPanel<T>
{
	private static final long serialVersionUID = 1L;

	public ToepassingResultaatExamenPanel(String id, IModel<T> model, Verbintenis verbintenis)
	{
		super(id, model);

		EnumCombobox<ToepassingResultaatExamenvak> toepassingBVE =
			new EnumCombobox<ToepassingResultaatExamenvak>("toepassingResultaatExamenvak",
				ToepassingResultaatExamenvak.values());
		toepassingBVE.setChoiceRenderer(new IChoiceRenderer<ToepassingResultaatExamenvak>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(ToepassingResultaatExamenvak object)
			{
				return object.getBRONString();
			}

			@Override
			public String getIdValue(ToepassingResultaatExamenvak object, int index)
			{
				return object.getBRONString();
			}

		});
		toepassingBVE.setVisible(verbintenis.isVAVOVerbintenis());

		EnumCombobox<ToepassingResultaat> toepassingVO =
			new EnumCombobox<ToepassingResultaat>("toepassingResultaat", ToepassingResultaat
				.values());
		toepassingVO.setChoiceRenderer(new IChoiceRenderer<ToepassingResultaat>()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(ToepassingResultaat object)
			{
				return object.getBRONString();
			}

			@Override
			public String getIdValue(ToepassingResultaat object, int index)
			{
				return object.getBRONString();
			}

		});

		setRenderBodyOnly(true);

		// WebMarkupContainer toepassingVOContainer =
		// new WebMarkupContainer("toepassingResultaatContainer");
		toepassingVO.setVisible(verbintenis.isVOVerbintenis());
		// toepassingVOContainer.setVisible(verbintenis.isVOVerbintenis());
		// toepassingVOContainer.add();
		//
		// WebMarkupContainer toepassingBVEContainer =
		// new WebMarkupContainer("toepassingResultaatExamenvakContainer");
		toepassingBVE.setVisible(verbintenis.isBVEVerbintenis());
		// toepassingBVEContainer.setVisible(verbintenis.isBVEVerbintenis());
		// toepassingBVEContainer.add(toepassingBVE);

		add(toepassingVO);
		add(toepassingBVE);
	}
}