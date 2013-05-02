package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.link.ConfirmationLink;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.web.pages.shared.jobs.AbstractJobBeheerPage;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.security.checks.ISecurityCheck;

public abstract class CollectieveStatusovergangSelectieTarget<R extends IdObject, S extends IdObject, T extends Enum<T>>
		extends AbstractSelectieTarget<R, S>
{
	private static final long serialVersionUID = 1L;

	private CollectieveStatusovergangEditModel<T> model;

	private Class< ? extends AbstractJobBeheerPage< ? >> jobOverzichtPageClass;

	public CollectieveStatusovergangSelectieTarget(
			Class< ? extends AbstractJobBeheerPage< ? >> jobOverzichtPageClass,
			CollectieveStatusovergangEditModel<T> model)
	{
		super(jobOverzichtPageClass, "Voltooien");
		this.model = model;
		this.jobOverzichtPageClass = jobOverzichtPageClass;
	}

	@Override
	public Link<Void> createLink(String linkId, final ISelectionComponent<R, S> base)
	{
		return new ConfirmationLink<Void>(linkId,
			"Weet u zeker dat u de status van de geselecteerde items wilt wijzigen?")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				AbstractJobBeheerPage< ? > overzichtPage =
					ReflectionUtil.invokeConstructor(jobOverzichtPageClass);
				overzichtPage.startJob(new CollectieveStatusovergangJobDataMap<T>(base, model));
				setResponsePage(overzichtPage);
			}
		};
	}

	@Override
	public abstract ISecurityCheck getSecurityCheck();

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(model);
	}
}