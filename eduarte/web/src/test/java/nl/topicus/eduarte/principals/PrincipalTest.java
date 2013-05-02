package nl.topicus.eduarte.principals;

import java.util.*;

import nl.topicus.cobra.modules.CobraAnnotationAuthorizationModule;
import nl.topicus.cobra.security.*;
import nl.topicus.cobra.security.impl.AnnotationHiveFactory;
import nl.topicus.cobra.security.impl.SecurityUtil;
import nl.topicus.cobra.security.test.AbstractPrincipalTester;
import nl.topicus.cobra.util.ComponentScanner;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.cobra.web.security.TargetBasedSecurityCheck;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.IgnoreEditPageWithoutWrite;
import nl.topicus.eduarte.app.security.actions.Beheer;
import nl.topicus.eduarte.app.security.actions.Deelnemer;
import nl.topicus.eduarte.app.security.actions.EduArteActionFactory;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.core.principals.App;
import nl.topicus.eduarte.core.principals.Root;

import org.apache.wicket.Page;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.hive.authorization.Permission;
import org.apache.wicket.security.hive.authorization.Principal;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@ContextConfiguration(locations = {"file:src/test/java/nl/topicus/eduarte/principals/spring-principaltest.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class})
public class PrincipalTest extends AbstractPrincipalTester<EduArtePrincipal>
{
	private static final String ACTION_FACTORY_KEY = "key";

	@Autowired
	private List<CobraAnnotationAuthorizationModule> modules;

	@BeforeClass
	public static void initActionFactory()
	{
		new EduArteActionFactory(ACTION_FACTORY_KEY);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected AnnotationHiveFactory<EduArtePrincipal> getHiveFactory()
	{
		List<IPrincipalSourceResolver<EduArtePrincipal>> resolvers =
			new ArrayList<IPrincipalSourceResolver<EduArtePrincipal>>();
		for (CobraAnnotationAuthorizationModule curModule : modules)
		{
			resolvers.add((IPrincipalSourceResolver<EduArtePrincipal>) curModule
				.getPrincipalSourceResolver());
		}
		AnnotationHiveFactory<EduArtePrincipal> factory =
			new AnnotationHiveFactory<EduArtePrincipal>(
				(EduArteActionFactory) org.apache.wicket.security.actions.Actions
					.getActionFactory(ACTION_FACTORY_KEY), resolvers);
		return factory;
	}

	@Test
	public void testDisplayOnWrite()
	{
		for (Class< ? extends IPrincipalSource<EduArtePrincipal>> curSource : getPrincipalSources())
		{
			if (curSource.isAnnotationPresent(Display.class)
				&& curSource.isAnnotationPresent(Write.class))
			{
				if (!curSource.getAnnotation(Write.class).read()
					.equals(NoReadPrincipalSource.class))
					addError(curSource.getName()
						+ " heeft een @Display en @Write annotatie met read."
						+ " Zet de @Display op het leesrecht");
			}
		}
		assertNoErrors();
	}

	@Test
	public void testDisplay()
	{
		Set<Class< ? extends IPrincipalSource<EduArtePrincipal>>> unimplied =
			new LinkedHashSet<Class< ? extends IPrincipalSource<EduArtePrincipal>>>(
				getPrincipalSources());
		for (Class< ? extends IPrincipalSource<EduArtePrincipal>> curSource : getPrincipalSources())
		{
			if (curSource.isAnnotationPresent(Implies.class))
			{
				for (Class< ? extends IPrincipalSource< ? extends Principal>> curImplied : curSource
					.getAnnotation(Implies.class).value())
					unimplied.remove(curImplied);
			}
			if (curSource.isAnnotationPresent(Write.class))
			{
				Class< ? extends IPrincipalSource< ? extends Principal>> read =
					curSource.getAnnotation(Write.class).read();
				// remove current, display should be on read
				if (!read.equals(NoReadPrincipalSource.class))
					unimplied.remove(curSource);
			}
		}
		for (Class< ? extends IPrincipalSource<EduArtePrincipal>> curSource : unimplied)
		{
			if (!curSource.isAnnotationPresent(Display.class))
				addError(curSource.getName()
					+ " heeft geen @Display annotatie en wordt ook niet geimplied door een ander recht");
		}
		assertNoErrors();
	}

	@Test
	public void testUniqueDisplayLabel()
	{
		Map<String, Class< ? extends IPrincipalSource<EduArtePrincipal>>> labels =
			new HashMap<String, Class< ? extends IPrincipalSource<EduArtePrincipal>>>();

		for (Class< ? extends IPrincipalSource<EduArtePrincipal>> curSource : getPrincipalSources())
		{
			if (curSource.isAnnotationPresent(Display.class))
			{
				Display display = curSource.getAnnotation(Display.class);
				String label = display.parent() + "." + display.label();
				if (labels.containsKey(label))
				{
					addError(curSource.getName() + " gebruikt hetzelfde label als "
						+ labels.get(label).getName() + ": " + label);
				}
				else
					labels.put(label, curSource);
			}
		}
		assertNoErrors();
	}

	@Test
	public void testPrincipalsWithNoPermissions()
	{
		Set<Class< ? extends IPrincipalSource<EduArtePrincipal>>> remaining =
			new HashSet<Class< ? extends IPrincipalSource<EduArtePrincipal>>>(getPrincipalSources());
		// special cases, should not be checked
		remaining.remove(App.class);
		remaining.remove(Root.class);

		for (Class< ? extends IPrincipalSource<EduArtePrincipal>> curSource : getPrincipalSources())
		{
			remaining.removeAll(SecurityUtil.getImplies(curSource));
		}
		for (EduArtePrincipal curPrincipal : getPrincipals())
		{
			if (remaining.contains(curPrincipal.getSourceClass()))
			{
				Set<Permission> permissions = getHive().getPermissions(curPrincipal);
				if (!permissions.isEmpty())
					remaining.remove(curPrincipal.getSourceClass());
			}
		}
		for (Class< ? extends IPrincipalSource<EduArtePrincipal>> curSource : remaining)
		{
			addError(curSource.getName()
				+ " wordt niet gebruikt op een pagina en heeft geen andere permissions");
		}
		assertNoErrors();
	}

	@Test
	public void testEditPages()
	{
		List<Class< ? extends IEditPage>> classes =
			ComponentScanner.scanForClasses(IEditPage.class, InPrincipal.class,
				"nl.topicus.eduarte");
		for (Class< ? extends IEditPage> curClass : classes)
		{
			if (curClass.isAnnotationPresent(IgnoreEditPageWithoutWrite.class))
				continue;
			InPrincipal principal = curClass.getAnnotation(InPrincipal.class);
			if (principal.value().length > 1)
				continue;
			Class< ? extends IPrincipalSource< ? extends Principal>> principalSource =
				principal.value()[0];
			if (!principalSource.isAnnotationPresent(Write.class)
				&& !principalSource.isAnnotationPresent(Everybody.class))
			{
				addError(curClass.getName() + " is een edit pagina en zit in de principal "
					+ principalSource.getName() + " waar geen @Write op zit");
			}
		}
		assertNoErrors();
	}

	@Test
	@Ignore("Nog 135 pages te fixen voor deze echt aan kan")
	public void testActionsSupportedBySecurityCheck()
	{
		List<Class< ? extends Page>> classes =
			ComponentScanner.scanForClasses(Page.class, null, "nl.topicus.eduarte");
		for (Class< ? extends Page> curPageClass : classes)
		{
			Set<Class< ? extends WaspAction>> actionsImplementedBySearch =
				getSearchActions(curPageClass);
			for (InPrincipal curPrincipal : SecurityUtil.getInPrincipals(curPageClass))
			{
				for (Class< ? extends IPrincipalSource< ? extends Principal>> curPrincipalSource : curPrincipal
					.value())
				{
					List<RequiredSecurityCheck> checks =
						TargetBasedSecurityCheck.getRequiredSecurityChecks(curPageClass);
					Set<Class< ? extends WaspAction>> actionsOnPrincipal =
						getActions(curPrincipalSource);
					for (RequiredSecurityCheck curCheck : checks)
					{
						Set<Class< ? extends WaspAction>> actionsOnCheck =
							getActions(curCheck.value());
						actionsOnPrincipal.removeAll(actionsOnCheck);
					}
					actionsOnPrincipal.removeAll(actionsImplementedBySearch);
					actionsOnPrincipal.remove(Instelling.class);
					actionsOnPrincipal.remove(Beheer.class);
					actionsOnPrincipal.remove(Deelnemer.class);
					if (!actionsOnPrincipal.isEmpty())
					{
						addError(curPageClass.getName()
							+ " definieerd geen SecurityCheck met de actions " + actionsOnPrincipal
							+ ", welke nodig zijn voor " + curPrincipalSource.getName()
							+ ". Voeg de juist SecurityCheck toe met een @RequiredSecurityCheck");
					}
				}
			}
		}
		assertNoErrors();
	}

	private Set<Class< ? extends WaspAction>> getActions(Class< ? > clazz)
	{
		Set<Class< ? extends WaspAction>> actionsOnClass =
			new HashSet<Class< ? extends WaspAction>>();
		if (clazz.isAnnotationPresent(Actions.class))
			actionsOnClass.addAll(Arrays.asList(clazz.getAnnotation(Actions.class).value()));
		return actionsOnClass;
	}

	private Set<Class< ? extends WaspAction>> getSearchActions(Class< ? > clazz)
	{
		Set<Class< ? extends WaspAction>> actionsOnClass =
			new HashSet<Class< ? extends WaspAction>>();
		if (clazz.isAnnotationPresent(SearchImplementsActions.class))
			actionsOnClass.addAll(Arrays.asList(clazz.getAnnotation(SearchImplementsActions.class)
				.value()));
		return actionsOnClass;
	}
}
