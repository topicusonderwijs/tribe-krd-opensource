/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.test.AbstractHibernateConfigTest;
import nl.topicus.eduarte.entities.codenaamactief.CodeNaamActiefInstellingEntiteit;
import nl.topicus.eduarte.entities.contract.Contract;

import org.hibernate.mapping.PersistentClass;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test case om te controleren of de hibernate configuratie wel klopt. B.V. de annotaties.
 * 
 * @author marrink
 */
@ContextConfiguration(locations = "file:src/test/java/nl/topicus/eduarte/entities/inithibernate.xml")
public class HibernateConfigTest extends AbstractHibernateConfigTest
{
	/**
	 * Controleert of alle constraints geplaatst zijn op de subclasses van deze abstract
	 * entiteiten.
	 */
	@Test
	public void testUniqueConstraintCodeNaamActiefInstellingEntiteit()
	{
		PersistentClassFieldVisitor visit = new PersistentClassFieldVisitor()
		{
			/**
			 * Classes waar we van we al geconstateerd hebben dat ze een constraint moeten
			 * krijgen.
			 */
			private List<Class< ? extends IdObject>> processed =
				new ArrayList<Class< ? extends IdObject>>();

			private void logError(String key, String error)
			{
				List<String> list = configProblems.get(key);
				if (list == null)
				{
					list = new ArrayList<String>();
					configProblems.put(key, list);
				}
				list.add(error);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void handle(PersistentClass clazz, Field field)
			{
				Class< ? extends IdObject> subclass = clazz.getMappedClass();

				if (processed.contains(subclass)
					&& sessionFactoryBean.getConfiguration().getClassMapping(subclass.getName()) != null)
					return; // is al gelogged

				processed.add(subclass);

				javax.persistence.Table tableAnnotation =
					subclass.getAnnotation(javax.persistence.Table.class);

				if (tableAnnotation != null)
				{
					if (tableAnnotation.uniqueConstraints() == null
						|| tableAnnotation.uniqueConstraints().length == 0)
					{
						logError(
							subclass.getName(),
							"Replace @Table annotation with: @javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {\"code\", \"organisatie\"})})");
					}
					else
					{
						int matchcount = 0;
						for (int i = 0; i < tableAnnotation.uniqueConstraints().length; i++)
						{
							javax.persistence.UniqueConstraint constraint =
								tableAnnotation.uniqueConstraints()[i];

							if (constraint.columnNames() == null
								|| constraint.columnNames().length == 0)
							{
								logError(subclass.getName(),
									"Found empty to @UniqueConstraint annotation.");
							}
							else if (constraint.columnNames().length >= 2)
							{
								boolean codeval = false;
								boolean instellingval = false;

								for (String colName : constraint.columnNames())
								{
									if ("code".equals(colName))
										codeval = true;
									if ("organisatie".equals(colName))
										instellingval = true;
								}

								if (codeval && instellingval)
									matchcount++;
							}
						}

						if (matchcount == 0)
						{
							logError(
								subclass.getName(),
								"Add @UniqueConstraint annotation to existing @Table annotation: @UniqueConstraint(columnNames = {\"code\", \"organisatie\"}");
						}
					}
				}
				else
				{
					logError(
						subclass.getName(),
						"Add @Table annotation: @javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {\"code\", \"organisatie\"})})");
				}
			}
		};
		iterateIdObjects(visit, CodeNaamActiefInstellingEntiteit.class);

		assertNoConfigurationErrors("Hibernate configuratie/annotaties kloppen niet voor:\n",
			configProblems);
	}

	@Override
	protected boolean skipField(String fieldName, Field field)
	{
		return field.getDeclaringClass().equals(Contract.class) && fieldName.equals("locaties");
	}
}
