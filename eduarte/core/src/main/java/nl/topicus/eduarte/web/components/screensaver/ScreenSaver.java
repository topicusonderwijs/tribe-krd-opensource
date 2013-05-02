package nl.topicus.eduarte.web.components.screensaver;

import nl.topicus.cobra.web.behaviors.ClientSideCallable;
import nl.topicus.cobra.web.behaviors.ServerCallAjaxBehaviour;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.settings.ScreenSaverConfiguration;
import nl.topicus.eduarte.entities.settings.ScreenSaverSetting;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.CloseButtonCallback;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ScreenSaver extends CobraModalWindow<Void> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private long logoutTime;

	@SpringBean
	private SettingsDataAccessHelper helper;

	public ScreenSaver(String id)
	{
		super(id);
		setOutputMarkupId(true);
		add(new ServerCallAjaxBehaviour());
		setTitle("Schermbeveiliging");
		setInitialHeight(200);
		setInitialWidth(400);
		setResizable(false);
		setCloseButtonCallback(new CloseButtonCallback()
		{

			private static final long serialVersionUID = 1L;

			@Override
			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				logoff(target);
				return true;
			}
		});
		setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(ScreenSaver.this);
			}
		});
	}

	@Override
	public boolean isVisible()
	{
		return super.isVisible() && getSetting().isActief();
	}

	ScreenSaverConfiguration getSetting()
	{
		return helper.getSetting(ScreenSaverSetting.class).getValue();
	}

	@Override
	protected CobraModalWindowBasePanel<Void> createContents(String id)
	{
		return new ScreenSaverPanel(id, this);
	}

	@Override
	@ClientSideCallable
	public void show(AjaxRequestTarget target)
	{
		EduArteSession.get().setScreensaverEnabled(true);
		logoutTime = System.currentTimeMillis() + getSetting().getSessieTimeout() * 60000;
		super.show(target);
		target.appendJavascript("window.onbeforeunload=null;");
		target.appendJavascript("changeMaskStyle('wicket-mask-extra-dark');");
	}

	@ClientSideCallable
	public void keepAlive(AjaxRequestTarget target)
	{
		if (logoutTime != 0 && System.currentTimeMillis() >= logoutTime)
			logoff(target);
	}

	@ClientSideCallable
	public void logoff(AjaxRequestTarget target)
	{
		if (EduArteSession.get().logoff())
			target.appendJavascript("window.location='';");
	}

	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager)
	{
		ResourceRefUtil.addTimers(wiQueryResourceManager);
		wiQueryResourceManager.addJavaScriptResource(new JavascriptResourceReference(
			ScreenSaver.class, "screensaver.js"));
	}

	@Override
	public JsStatement statement()
	{
		return new JsQuery(this).$().chain("screenSaver",
			Integer.toString(getSetting().getTimeout() * 60000),
			Integer.toString(getSetting().getSessieTimeout() * 60000), Integer.toString(5 * 60000),
			"'#screenSaverLogoutCounter'",
			Boolean.toString(EduArteSession.get().isScreensaverEnabled()));
	}

	public long getMinutesRemaining()
	{
		long remaining = logoutTime - System.currentTimeMillis();
		return remaining / 1000;
	}
}
