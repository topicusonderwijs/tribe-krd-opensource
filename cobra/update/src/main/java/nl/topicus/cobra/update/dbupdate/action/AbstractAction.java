package nl.topicus.cobra.update.dbupdate.action;

import org.apache.commons.cli.Option;

public abstract class AbstractAction implements Action
{
	public AbstractAction()
	{
	}

	@Override
	public final void performAction(Option option)
	{
		preAction();
		doAction(option);
		postAction();
	}

	protected void preAction()
	{
	}

	protected abstract void doAction(Option option);

	protected void postAction()
	{
	}
}
