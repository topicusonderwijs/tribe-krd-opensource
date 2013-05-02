package nl.topicus.eduarte.web.components.panels.afspraak;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.link.ConfirmationAjaxLink;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.quicksearch.ISelectListener;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.participatie.Afspraak;
import nl.topicus.eduarte.entities.participatie.AfspraakParticipant;
import nl.topicus.eduarte.entities.participatie.AfspraakType;
import nl.topicus.eduarte.entities.participatie.enums.AfspraakTypeCategory;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.UitnodigingRadioGroup;
import nl.topicus.eduarte.web.components.quicksearch.contract.ContractSearchEditor;
import nl.topicus.eduarte.web.components.quicksearch.deelnemermedewerkergroep.DeelnemerMedewerkerGroepSearchEditor;
import nl.topicus.eduarte.zoekfilters.ContractZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class AfspraakParticipantEditPanel extends TypedPanel<Afspraak>
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends IdObject> editorModel;

	public AfspraakParticipantEditPanel(String id, Form< ? > form,
			final IModel<Afspraak> afspraakModel, IModel< ? extends IdObject> editorModel,
			final boolean allowDeleteAuthor)
	{
		super(id, afspraakModel);
		this.editorModel = editorModel;
		setOutputMarkupId(true);
		add(new ListView<AfspraakParticipant>("participanten",
			new AbstractReadOnlyModel<List<AfspraakParticipant>>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public List<AfspraakParticipant> getObject()
				{
					return getParticipantenToShow();
				}
			})
		{
			private static final long serialVersionUID = 1L;

			private boolean isStatusRadioVisible(ListItem<AfspraakParticipant> item)
			{
				return item.getModelObject() != null;
			}

			private boolean isStatusRadioEnabled(ListItem<AfspraakParticipant> item)
			{
				AfspraakParticipant afspraakD = item.getModelObject();
				return afspraakD != null
					&& !afspraakD.getUitnodigingStatus().equals(UitnodigingStatus.INGETEKEND)
					&& Afspraak.isDirectePlaatsingAllowed(afspraakD)
					&& !Afspraak.isDirectePlaatsingVerplicht(afspraakD, getEditor());
			}

			@Override
			protected void populateItem(final ListItem<AfspraakParticipant> item)
			{
				UitnodigingRadioGroup statusGroup =
					new UitnodigingRadioGroup("uitnodigingStatus",
						new PropertyModel<UitnodigingStatus>(item.getModel(), "uitnodigingStatus"));
				statusGroup.add(new AjaxFormChoiceComponentUpdatingBehavior()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target)
					{
						AfspraakParticipant afspraakD = item.getModelObject();
						afspraakD.resetUitnodigingVerstuurd(true);
						target.addComponent(item);
					}
				});
				item.add(statusGroup);
				Radio<UitnodigingStatus> plaatsing =
					new Radio<UitnodigingStatus>("statusPlaatsing", new Model<UitnodigingStatus>(
						UitnodigingStatus.DIRECTE_PLAATSING))
					{
						private static final long serialVersionUID = 1L;

						@Override
						public boolean isVisible()
						{
							return super.isVisible() && isStatusRadioVisible(item);
						}

						@Override
						public boolean isEnabled()
						{
							return super.isEnabled() && isStatusRadioEnabled(item);
						}
					};
				statusGroup.add(plaatsing);
				Radio<UitnodigingStatus> uitnodiging =
					new Radio<UitnodigingStatus>("statusUitnodiging", new Model<UitnodigingStatus>(
						UitnodigingStatus.UITGENODIGD))
					{
						private static final long serialVersionUID = 1L;

						@Override
						public boolean isVisible()
						{
							return super.isVisible() && isStatusRadioVisible(item);
						}

						@Override
						public boolean isEnabled()
						{
							return super.isEnabled() && isStatusRadioEnabled(item);
						}
					};
				statusGroup.add(uitnodiging);

				final DeelnemerMedewerkerGroepSearchEditor textField =
					new DeelnemerMedewerkerGroepSearchEditor("participant",
						new ConvertToEntityModelWrapper(new AfspraakParticipantModel(
							AfspraakParticipantEditPanel.this, item)))
					{
						private static final long serialVersionUID = 1L;

						@Override
						public boolean isEnabled()
						{
							AfspraakParticipant afspraakD = item.getModelObject();
							return super.isEnabled()
								&& afspraakD == null
								|| !(afspraakD.isAuteur() || afspraakD.getUitnodigingStatus()
									.equals(UitnodigingStatus.INGETEKEND));
						}
					};

				textField.addListener(new ISelectListener()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onUpdate(AjaxRequestTarget target)
					{
						target.addComponent(AfspraakParticipantEditPanel.this);
					}
				});

				textField.setOutputMarkupId(true);
				statusGroup.add(textField);
				textField.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public String getObject()
					{
						AfspraakParticipant afspraakD = item.getModelObject();
						if (// !textField.isEnabled() ||
						afspraakD == null)
							return null;
						switch (afspraakD.getUitnodigingStatus())
						{
							case GEACCEPTEERD:
								return "inpGeaccepteerd";
							case GEWEIGERD:
								return "inpGeweigerd";
							case UITGENODIGD:
								return "inpUitgenodigd";
							default:
								return null;
						}
					}
				}, " "));

				statusGroup.add(textField);

				statusGroup.add(new EntiteitTypeImage("image", item.getModel()));

				statusGroup.add(new ConfirmationAjaxLink<Void>("removeAuthorLink",
					"Weet u zeker dat u uzelf wilt verwijderen uit de lijst met participanten?")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getParticipanten().remove(item.getIndex());
						target.addComponent(AfspraakParticipantEditPanel.this);
					}

					@Override
					public boolean isVisible()
					{
						AfspraakParticipant afspraakD = item.getModelObject();
						return super.isVisible() && AfspraakParticipantEditPanel.this.isEnabled()
							&& afspraakD != null && afspraakD.isAuteur() && allowDeleteAuthor;
					}
				});
				statusGroup.add(new AjaxLink<Void>("removeLink")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						getParticipanten().remove(item.getIndex());
						target.addComponent(AfspraakParticipantEditPanel.this);
					}

					@Override
					public boolean isVisible()
					{
						AfspraakParticipant afspraakD = item.getModelObject();
						return super.isVisible() && afspraakD != null && !afspraakD.isAuteur();
					}
				});
				statusGroup.add(new ConfirmationAjaxLink<Void>("resendUitnodiging",
					"Wilt u de uitnodiging (opnieuw) versturen?")
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target)
					{
						AfspraakParticipant afspraakD = item.getModelObject();
						afspraakD.setUitnodigingStatus(UitnodigingStatus.UITGENODIGD);
						afspraakD.setUitnodigingVerstuurd(false);
						target.addComponent(item);
					}

					@Override
					public boolean isVisible()
					{
						AfspraakParticipant afspraakD = item.getModelObject();
						return super.isVisible()
							&& afspraakD != null
							&& afspraakD.isUitnodigingVerstuurd()
							&& EnumSet.of(UitnodigingStatus.GEWEIGERD,
								UitnodigingStatus.UITGENODIGD, UitnodigingStatus.DIRECTE_PLAATSING)
								.contains(afspraakD.getUitnodigingStatus());
					}
				});
				statusGroup.add(new ContractSearchEditor("contract", new PropertyModel<Contract>(
					item.getModel(), "contract"), new ContractZoekFilter())
				{

					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible()
					{
						if (EduArteContext.get().getAccount() instanceof DeelnemerAccount)
							return false;
						AfspraakParticipant afspraakD = item.getModelObject();
						return afspraakD != null && afspraakD.getDeelnemer() != null;
					}
				});
				item.setOutputMarkupId(true);
			}
		});

		form.add(new AbstractFormValidator()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent< ? >[] getDependentFormComponents()
			{
				return null;
			}

			@Override
			public void validate(@SuppressWarnings("hiding") Form< ? > form)
			{
				if (getAfspraak().getInloopCollege() == null && getParticipanten().isEmpty())
					form.error("Er zijn geen participanten aan de afspraak toegevoegd.");
			}
		});
	}

	public Afspraak getAfspraak()
	{
		return getModelObject();
	}

	public List<AfspraakParticipant> getParticipanten()
	{
		if (getAfspraak() != null)
			return getAfspraak().getParticipanten();
		return null;
	}

	private List<AfspraakParticipant> getParticipantenToShow()
	{
		List<AfspraakParticipant> participanten;
		if (getParticipanten() != null)
			participanten = new ArrayList<AfspraakParticipant>(getParticipanten());
		else
			participanten = new ArrayList<AfspraakParticipant>();

		if (isEnabled())
			participanten.add(null);
		return participanten;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(editorModel);
	}

	@Override
	public boolean isEnabled()
	{
		AfspraakType type = getAfspraak().getAfspraakType();
		return super.isEnabled()
			&& (type == null || !type.getCategory().equals(AfspraakTypeCategory.PRIVE));
	}

	public static void onAfspraakTijdChange(List<AfspraakParticipant> participanten)
	{
		for (AfspraakParticipant curParticipant : participanten)
		{
			if (EnumSet.of(UitnodigingStatus.GEACCEPTEERD, UitnodigingStatus.GEWEIGERD).contains(
				curParticipant.getUitnodigingStatus()))
			{
				curParticipant.setUitnodigingStatus(UitnodigingStatus.UITGENODIGD);
				curParticipant.resetUitnodigingVerstuurd(true);
			}
		}
	}

	public IdObject getEditor()
	{
		return editorModel.getObject();
	}
}
