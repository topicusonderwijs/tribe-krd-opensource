package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import java.util.Date;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.web.pages.intake.stap4.VerbintenisPanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class CollectiefVerbintenisAanmakenPanel extends VerbintenisPanel
{
	private static final long serialVersionUID = 1L;

	public CollectiefVerbintenisAanmakenPanel(String id, CollectiefAanmakenModel aanmakenModel,
			Form<Void> form)
	{
		super(id, new CompoundPropertyModel<Verbintenis>(new PropertyModel<Verbintenis>(
			aanmakenModel, "nieuweVerbintenis")), null, form);
	}

	@Override
	protected Date getGeboortedatum()
	{
		return null;
	}

	@Override
	protected boolean isVoegRelevanteVooropleidingToe()
	{
		return false;
	}

	@Override
	protected void voegValidatorsToe()
	{
		form.add(new VerbintenisStatusOpleidingValidator(getVerbintenisModel(), statusCombobox));
	}

	private class VerbintenisStatusOpleidingValidator extends AbstractFormValidator
	{
		private static final long serialVersionUID = 1L;

		private IModel<Verbintenis> verbintenisCompountModel;

		private FormComponent< ? > statusVeld;

		public VerbintenisStatusOpleidingValidator(IModel<Verbintenis> verbintenisCompountModel,
				FormComponent< ? > statusVeld)
		{
			this.verbintenisCompountModel = verbintenisCompountModel;
			this.statusVeld = statusVeld;
		}

		@Override
		public FormComponent< ? >[] getDependentFormComponents()
		{
			return new FormComponent[] {statusVeld};
		}

		@Override
		public void validate(@SuppressWarnings("hiding") Form< ? > form)
		{
			Verbintenis verbintenis = getNieuweVerbintenis();

			boolean statusVolledig = verbintenis.getStatus().equals(VerbintenisStatus.Volledig);
			boolean taxonomieIsBVE = verbintenis.isBVEVerbintenis();

			if (statusVolledig && taxonomieIsBVE)
				error(statusVeld);
		}

		private Verbintenis getNieuweVerbintenis()
		{
			return verbintenisCompountModel.getObject();
		}
	}
}
