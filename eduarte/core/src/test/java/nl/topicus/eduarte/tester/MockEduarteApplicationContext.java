/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.tester;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.wicket.Application;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.test.AnnotApplicationContextMock;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;

@SuppressWarnings("unchecked")
public class MockEduarteApplicationContext extends AnnotApplicationContextMock implements
		WebApplicationContext
{
	private static final long serialVersionUID = 1L;

	public MockEduarteApplicationContext()
	{
	}

	@Override
	public ServletContext getServletContext()
	{
		return ((WebApplication) Application.get()).getServletContext();
	}

	@Override
	public String getId()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getBean(String name, Object[] args) throws BeansException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Object> getBeansWithAnnotation(Class< ? extends Annotation> annotationType)
			throws BeansException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException
	{
		throw new UnsupportedOperationException();
	}
}
