package nl.topicus.eduarte.web.pages;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.ExceptionUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.menu.EmptyMenu;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.wiquery.CollapsablePanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteSession;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.InstellingDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.RequestLogger.SessionData;
import org.apache.wicket.protocol.http.request.WebClientInfo;

@PageInfo(title = "Foutmelding", menu = {"Geen"})
@InPrincipal(Always.class)
@RechtenSoorten( {RechtenSoort.BEHEER, RechtenSoort.DEELNEMER, RechtenSoort.INSTELLING,
	RechtenSoort.DIGITAALAANMELDER, RechtenSoort.EXTERNEORGANISATIE})
public class EduArteErrorPage extends SecurePage
{
	private static final long serialVersionUID = 1L;

	/** KRD intern = 228, KRD = 188. */
	private static final String MANTIS_PROJECT_ID = "228";

	private final Page page;

	private final Throwable thrownError;

	// TODO geen SecurePage extenden maar auto redirect > login page moet wel er in
	// blijven (zonder RestartResponse...Exception
	// TODO menu kan ook weg

	public EduArteErrorPage(Page page, Throwable e)
	{
		super(page instanceof SecurePage ? ((SecurePage) page).getSelectedItem()
			: CoreMainMenuItem.Home);

		this.page = page;
		this.thrownError = e;

		DetailPanel details = new DetailPanel("details", new Model<Throwable>(e));
		add(details);
		createComponents();

		add(new Label("versie", getVersion()));
		add(new Label("build", getBuild()));
		add(new Label("tijd", getTime()));

		String stacktrace = getStacktrace(e);
		String fingerprint = getFingerprint(stacktrace);

		add(new ExternalLink("zoek", "http://bugs.topicus.nl/search.php?project_id="
			+ MANTIS_PROJECT_ID + "&hide_status_id=80&custom_field_43=" + fingerprint)
			.setVisible(EduArteApp.get().isDevelopment()));
		add(new WebMarkupContainer("meld").setVisible(EduArteApp.get().isDevelopment()));
	}

	public Throwable getThrownError()
	{
		return thrownError;
	}

	private String getTime()
	{
		TimeUtil timeutil = TimeUtil.getInstance();
		return timeutil.formatDateTime(timeutil.currentDateTime(), "yyyy-MM-dd hh:mm:ss.SSS");
	}

	@Override
	protected void addServerCallBack()
	{
		// niet doen voor de error page
		EduArteSession.get().getAndClearDetachException();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		if (page != null)
		{
			panel.addButton(new TerugButton(panel, page));

			ErrorPageMetaData metadata = new ErrorPageMetaData();
			metadata.setVorigeRenderingFout(true);
			page.setMetaData(ErrorPageMetaData.KEY, metadata);
		}
	}

	/**
	 * @see nl.topicus.eduarte.web.pages.SecurePage#createMenu(java.lang.String)
	 */
	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new EmptyMenu(id);
	}

	/**
	 * @see org.apache.wicket.Page#isErrorPage()
	 */
	@Override
	public boolean isErrorPage()
	{
		return true;
	}

	private final class DetailPanel extends CollapsablePanel<Throwable>
	{
		private static final long serialVersionUID = 1L;

		public DetailPanel(String id, IModel<Throwable> content)
		{
			super(id, content, "Details");
			setLoadAjax(false);

			Throwable msg = content.getObject();
			Throwable rootCause = ExceptionUtil.getRootCause(msg);

			String stacktrace = getStacktrace(msg);
			String fingerprint = getFingerprint(stacktrace);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);

			print(pw, "Applicatie", getApplicationName());
			print(pw, "Versie", getVersion());
			print(pw, "Build", getBuild());
			pw.println();

			Account account = getIngelogdeAccount();
			Persoon persoon = account.getEigenaar();
			String gebruiker =
				persoon != null ? persoon.getVolledigeNaam() + " (" + account.getGebruikersnaam()
					+ ")" : account.getGebruikersnaam();
			getIngelogdeAccount();
			print(pw, "Gebruiker", gebruiker);
			print(pw, "Instelling", getOrganisatieNaam());
			print(pw, "Tijd melding", getTime());
			print(pw, "Sessie id", getSession().getId());

			SessionData[] sessions = getApplication().getRequestLogger().getLiveSessions();
			SessionData sessionData = null;
			for (SessionData session : sessions)
			{
				if (getSession().getId().equals(session.getSessionId()))
				{
					sessionData = session;
					break;
				}
			}
			if (sessionData != null)
			{
				print(pw, "Sessie start", TimeUtil.getInstance().formatDateTime(
					sessionData.getStartDate(), "yyyy-MM-dd hh:mm:ss.SSS"));
			}
			print(pw, "Browser info", ((WebClientInfo) getWebRequestCycle().getClientInfo())
				.getUserAgent());

			Date peildatum = EduArteContext.get().getPeildatum();
			if (peildatum != null)
			{
				print(pw, "Peildatum", TimeUtil.getInstance().formatDateAsIsoString(peildatum));
			}
			else
			{
				print(pw, "Peildatum", "geen ingesteld");
			}
			pw.println();

			print(pw, "Foutmelding", rootCause.getMessage());
			print(pw, "Foutklasse", rootCause.getClass().getName());
			pw.println();
			if (page != null)
			{
				if (page instanceof SecurePage)
				{
					SecurePage secure = (SecurePage) page;
					print(pw, "Pagina", secure.getNavigationStackName());
				}
				print(pw, "Pagina class", page.getClass().getName());

				try
				{
					Object modelObject = page.getDefaultModelObject();
					if (modelObject != null)
					{
						String object =
							page.getDefaultModelObjectAsString() + " ("
								+ modelObject.getClass().getSimpleName() + ")";
						print(pw, "Pagina model", object);
					}
				}
				catch (Exception e)
				{
					print(pw, "Pagina model", "Fout: " + e.getMessage());
				}
			}
			if (fingerprint != null)
			{
				print(pw, "Fingerprint", fingerprint);
			}
			pw.println();
			pw.println("Volledige stacktrace:");
			pw.println(stacktrace);

			add(new Label("stacktrace", sw.toString()));

			String version = getVersion();
			if (version.endsWith("-SNAPSHOT"))
			{
				version = version.substring(0, version.lastIndexOf("-SNAPSHOT"));
			}
			String summary = null;
			if (page != null)
			{
				summary =
					page.getClass().getSimpleName() + " veroorzaakt een "
						+ rootCause.getClass().getSimpleName();
			}
			else
			{
				StackTraceElement place = rootCause.getStackTrace()[0];
				summary =
					rootCause.getClass().getSimpleName() + " veroorzaakt op " + place.getFileName()
						+ ":" + place.getLineNumber();
			}
			add(new HiddenField<String>("summary", new Model<String>(summary))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getInputName()
				{
					return getId();
				}
			});
			add(new HiddenField<String>("project_id", new Model<String>(MANTIS_PROJECT_ID))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getInputName()
				{
					return getId();
				}
			});

			add(new HiddenField<String>("product_version", new Model<String>(version))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getInputName()
				{
					return getId();
				}
			});

			add(new HiddenField<String>("severity", new Model<String>("70"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getInputName()
				{
					return getId();
				}
			});

			String description = null;
			if (page != null)
			{
				description =
					"De " + page.getClass().getName() + " veroorzaakte een "
						+ rootCause.getClass().getSimpleName() + ".\n\nDe volledige exception is: "
						+ rootCause.toString();
			}
			else
			{
				try
				{
					description =
						"Een " + rootCause.getClass().getSimpleName()
							+ " is veroorzaakt door een onbekend request target: "
							+ getRequestCycle().getRequestTarget().getClass().getName();
				}
				catch (Exception e)
				{
				}
			}
			add(new HiddenField<String>("description", new Model<String>(description))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getInputName()
				{
					return getId();
				}
			});

			add(new HiddenField<String>("additional_info", new Model<String>(sw.toString()))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getInputName()
				{
					return getId();
				}
			});

			add(new HiddenField<String>("custom_field_43", new Model<String>(fingerprint))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public String getInputName()
				{
					return getId();
				}
			});
		}

		private void print(PrintWriter pw, String key, CharSequence charSequence)
		{
			int aantalTabs = (32 - key.length()) / 8;
			StringBuilder tabs = new StringBuilder();
			for (int i = 0; i < aantalTabs; i++)
			{
				tabs.append('\t');
			}
			pw.printf("%s:%s%s\n", key, tabs.toString(), charSequence);
		}

		private CharSequence getApplicationName()
		{
			InstellingDataAccessHelper helper =
				DataAccessRegistry.getHelper(InstellingDataAccessHelper.class);
			return helper.getApplicationTitle();
		}
	}

	private String getStacktrace(Throwable throwable)
	{
		StringWriter stacktraceWriter = new StringWriter();
		PrintWriter stacktracePrinter = new PrintWriter(stacktraceWriter);
		ExceptionUtil.printStackTrace(stacktracePrinter, throwable);
		String stacktrace = stacktraceWriter.toString();
		int index = stacktrace.lastIndexOf("WicketFilter.doFilter");
		index = Math.max(index, stacktrace.indexOf('\n', index));
		if (index > 0)
			stacktrace = stacktrace.substring(0, index);
		return stacktrace;
	}

	private String getFingerprint(String stacktrace)
	{
		// verwijder de regelnummers om eventuele code verplaatsingen niet mee te laten
		// tellen bij het bepalen van de vingerafdruk
		Pattern pattern = Pattern.compile("\\:\\d+\\)");
		Matcher matcher = pattern.matcher(stacktrace);
		String stacktraceZonderRegelnummers = matcher.replaceAll(")");

		String fingerprint = null;
		try
		{
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			sha.update(stacktraceZonderRegelnummers.getBytes());
			StringBuilder sb = new StringBuilder();
			byte[] bytes = sha.digest();
			for (int i = 0; i < Math.min(7, bytes.length); i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			fingerprint = sb.toString().substring(0, 7);
		}
		catch (NoSuchAlgorithmException e)
		{
			return "onbekend";
		}
		return fingerprint;
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}
}
