package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;

import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.IModel;

public class UitnodigingRadioGroup extends RadioGroup<UitnodigingStatus>
{
	private static final long serialVersionUID = 1L;

	private static class UitnodigingModel implements IModel<UitnodigingStatus>
	{
		private static final long serialVersionUID = 1L;

		private IModel<UitnodigingStatus> wrapped;

		public UitnodigingModel(IModel<UitnodigingStatus> wrapped)
		{
			this.wrapped = wrapped;
		}

		@Override
		public UitnodigingStatus getObject()
		{
			UitnodigingStatus status = wrapped.getObject();
			if (status == null)
				return null;
			switch (status)
			{
				case DIRECTE_PLAATSING:
					return UitnodigingStatus.DIRECTE_PLAATSING;
				default:
					return UitnodigingStatus.UITGENODIGD;
			}
		}

		@Override
		public void setObject(UitnodigingStatus newStatus)
		{
			if (newStatus == null)
			{
				return;
			}
			UitnodigingStatus oldStatus = wrapped.getObject();
			if (newStatus.equals(UitnodigingStatus.DIRECTE_PLAATSING)
				|| oldStatus.equals(UitnodigingStatus.DIRECTE_PLAATSING))
				wrapped.setObject(newStatus);
		}

		@Override
		public void detach()
		{
			wrapped.detach();
		}
	}

	public UitnodigingRadioGroup(String id, IModel<UitnodigingStatus> model)
	{
		super(id, new UitnodigingModel(model));
	}
}
