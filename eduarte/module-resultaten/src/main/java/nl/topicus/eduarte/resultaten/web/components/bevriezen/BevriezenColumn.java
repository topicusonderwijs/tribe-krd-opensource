package nl.topicus.eduarte.resultaten.web.components.bevriezen;

import static nl.topicus.eduarte.web.components.resultaat.ResultatenModel.*;

import java.util.BitSet;
import java.util.List;

import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.wiquery.tristate.TriState;
import nl.topicus.cobra.web.components.wiquery.tristate.TriStateCheckBox;
import nl.topicus.eduarte.entities.resultaatstructuur.IBevriezing;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.resultaat.ResultatenboomTable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public abstract class BevriezenColumn extends AbstractCustomColumn<Toets>
{
	private static final long serialVersionUID = 1L;

	private int pogingIdx;

	private boolean disableBevrorenToetsen;

	public BevriezenColumn(int pogingNr, boolean disableBevrorenToetsen)
	{
		super(ResultatenboomTable.getHeader(pogingNr), ResultatenboomTable.getHeader(pogingNr));
		this.pogingIdx = pogingNr + OFFSET;
		this.disableBevrorenToetsen = disableBevrorenToetsen;
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<Toets> rowModel, int span)
	{
		cellItem.add(new CheckboxPanel(componentId, "checkbox", rowModel));
	}

	protected IModel<TriState> getCheckBoxModel(final IModel<Toets> rowModel)
	{
		return new IModel<TriState>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public TriState getObject()
			{
				Toets toets = rowModel.getObject();
				if (disableBevrorenToetsen && toets.isBevroren(pogingIdx - OFFSET))
					return TriState.On;
				return getBevrorenState(toets, pogingIdx);
			}

			@Override
			public void setObject(TriState object)
			{
				if (TriState.Partial.equals(object))
					return;

				List< ? extends IBevriezing> bevriezingen = getBevriezingen(rowModel.getObject());
				for (IBevriezing curBevriezing : bevriezingen)
				{
					BitSet bevrorenPogingen = curBevriezing.getBevorenPogingenAsSet();
					bevrorenPogingen.set(pogingIdx, TriState.On.equals(object));
					curBevriezing.setBevorenPogingenAsSet(bevrorenPogingen);
				}
			}

			@Override
			public void detach()
			{
			}
		};
	}

	protected abstract List< ? extends IBevriezing> getBevriezingen(Toets toets);

	protected boolean isCheckBoxVisible(IModel<Toets> rowModel)
	{
		Toets toets = rowModel.getObject();
		if (pogingIdx == ALTERNATIEF_IDX)
			return toets.isAlternatiefResultaatMogelijk();
		if (pogingIdx == RESULTAAT_IDX)
			return true;
		if (toets.isSamengesteld() || toets.isVariant())
			return false;
		return toets.getAantalHerkansingen() + POGING_START_IDX >= pogingIdx;
	}

	@SuppressWarnings("unused")
	protected void onCheckboxSelectionChanged(IModel<TriState> checkboxModel,
			IModel<Toets> rowModel, AjaxRequestTarget target)
	{
	}

	private TriState getBevrorenState(Toets toets, int checkPogingIdx)
	{
		TriState base = TriState.Off;
		if (!toets.isEindresultaat())
		{
			base = getBevrorenState(toets.getParent(), RESULTAAT_IDX);
		}

		List< ? extends IBevriezing> bevriezingen = getBevriezingen(toets);
		if (checkPogingIdx > RESULTAAT_IDX)
		{
			return base.or(getBevrorenState(bevriezingen, RESULTAAT_IDX)).or(
				getBevrorenState(bevriezingen, checkPogingIdx));
		}
		return base.or(getBevrorenState(bevriezingen, checkPogingIdx));
	}

	private TriState getBevrorenState(List< ? extends IBevriezing> bevriezingen, int checkPogingIdx)
	{
		boolean allBevroren = true;
		boolean allNietBevroren = true;

		for (IBevriezing curBevriezing : bevriezingen)
		{
			boolean thisBevroren = curBevriezing.getBevorenPogingenAsSet().get(checkPogingIdx);
			allBevroren &= thisBevroren;
			allNietBevroren &= !thisBevroren;
			if (!allBevroren && !allNietBevroren)
				return TriState.Partial;
		}
		return allBevroren ? TriState.On : TriState.Off;
	}

	private final class CheckboxPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		public CheckboxPanel(String id, String checkboxId, final IModel<Toets> rowModel)
		{
			super(id);
			final TriStateCheckBox checkBox =
				new TriStateCheckBox(checkboxId, getCheckBoxModel(rowModel), false)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible()
					{
						return super.isVisible() && isCheckBoxVisible(rowModel);
					}

					@Override
					public boolean isEnabled()
					{
						if (disableBevrorenToetsen
							&& (rowModel.getObject()).isBevroren(pogingIdx - OFFSET))
							return false;
						return super.isEnabled();
					}
				};
			checkBox.getValueField().add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					checkBox.validate();
					checkBox.updateModel();
					onCheckboxSelectionChanged(checkBox.getModel(), rowModel, target);
				}
			});
			add(checkBox);
		}
	}
}
