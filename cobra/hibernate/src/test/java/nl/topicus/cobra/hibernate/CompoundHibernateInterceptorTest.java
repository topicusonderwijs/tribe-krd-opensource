package nl.topicus.cobra.hibernate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.junit.Test;

public class CompoundHibernateInterceptorTest
{
	@Test
	public void noNullPointersOnNoInterceptors()
	{
		CompoundHibernateInterceptor compound = new CompoundHibernateInterceptor();
		compound.addInterceptor(null);
		compound.afterTransactionBegin(null);
		compound.afterTransactionCompletion(null);
		compound.beforeTransactionCompletion(null);
		compound.findDirty(null, null, null, null, null, null);
		compound.getEntity(null, null);
		compound.getEntityName(null);
		compound.instantiate(null, null, null);
		compound.isTransient(null);
		compound.onCollectionRecreate(null, null);
		compound.onCollectionRemove(null, null);
		compound.onCollectionUpdate(null, null);
		compound.onDelete(null, null, null, null, null);
		compound.onFlushDirty(null, null, null, null, null, null);
		compound.onLoad(null, null, null, null, null);
		compound.onPrepareStatement(null);
		compound.onSave(null, null, null, null, null);
		compound.postFlush(null);
		compound.preFlush(null);
		compound.addInterceptor(new EmptyInterceptor()
		{
			private static final long serialVersionUID = 1L;
		});
	}

	@Test
	public void singleFindDirtyReturnsNull()
	{
		Interceptor interceptor = mock(Interceptor.class);
		when(interceptor.findDirty(null, null, null, null, null, null)).thenReturn(null);
		CompoundHibernateInterceptor compound = new CompoundHibernateInterceptor(interceptor);
		int[] dirty = compound.findDirty(null, null, null, null, null, null);
		assertNull(dirty);

		verify(interceptor).findDirty(null, null, null, null, null, null);
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void singleFindDirtyReturnsEmpty()
	{
		Interceptor interceptor = mock(Interceptor.class);
		when(interceptor.findDirty(null, null, null, null, null, null)).thenReturn(new int[] {});

		Interceptor compound = new CompoundHibernateInterceptor(interceptor);
		int[] dirty = compound.findDirty(null, null, null, null, null, null);
		assertArrayEquals(new int[] {}, dirty);

		verify(interceptor).findDirty(null, null, null, null, null, null);
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void singleFindDirtyReturnsDirtyValues()
	{
		Interceptor interceptor = mock(Interceptor.class);
		when(interceptor.findDirty(null, null, null, null, null, null))
			.thenReturn(new int[] {3, 4});

		Interceptor compound = new CompoundHibernateInterceptor(interceptor);
		int[] dirty = compound.findDirty(null, null, null, null, null, null);
		assertArrayEquals(new int[] {3, 4}, dirty);

		verify(interceptor).findDirty(null, null, null, null, null, null);
		verifyNoMoreInteractions(interceptor);
	}

	@Test
	public void compoundFindDirtyReturnsNull()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.findDirty(null, null, null, null, null, null)).thenReturn(null);

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.findDirty(null, null, null, null, null, null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		int[] dirty = compound.findDirty(null, null, null, null, null, null);

		assertNull(dirty);

		verify(interceptor1).findDirty(null, null, null, null, null, null);
		verify(interceptor2).findDirty(null, null, null, null, null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void compoundFindDirtyReturnsEmpty()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.findDirty(null, null, null, null, null, null)).thenReturn(null);

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.findDirty(null, null, null, null, null, null)).thenReturn(new int[] {});

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		int[] dirty = compound.findDirty(null, null, null, null, null, null);

		assertArrayEquals(new int[] {}, dirty);

		verify(interceptor1).findDirty(null, null, null, null, null, null);
		verify(interceptor2).findDirty(null, null, null, null, null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void compoundFindDirtyReturnsUnion()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.findDirty(null, null, null, null, null, null)).thenReturn(
			new int[] {1, 2});

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.findDirty(null, null, null, null, null, null)).thenReturn(
			new int[] {0, 2, 3});

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		int[] dirty = compound.findDirty(null, null, null, null, null, null);

		assertArrayEquals(new int[] {0, 1, 2, 3}, dirty);

		verify(interceptor1).findDirty(null, null, null, null, null, null);
		verify(interceptor2).findDirty(null, null, null, null, null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void afterTransactionCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.afterTransactionBegin(null);

		verify(interceptor1).afterTransactionBegin(null);
		verify(interceptor2).afterTransactionBegin(null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void afterTransactionCompletionCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.afterTransactionCompletion(null);

		verify(interceptor1).afterTransactionCompletion(null);
		verify(interceptor2).afterTransactionCompletion(null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void afterTransactionCompletionCalledForEachInterceptorWithSameParameter()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);
		Transaction tx = mock(Transaction.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.afterTransactionCompletion(tx);

		verify(interceptor1).afterTransactionCompletion(tx);
		verify(interceptor2).afterTransactionCompletion(tx);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void beforeTransactionCompletionCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.beforeTransactionCompletion(null);

		verify(interceptor1).beforeTransactionCompletion(null);
		verify(interceptor2).beforeTransactionCompletion(null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void getEntityFallsThrough()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.getEntity(null, null)).thenReturn(null);
		when(interceptor2.getEntity(null, null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertNull(compound.getEntity(null, null));

		verify(interceptor1).getEntity(null, null);
		verify(interceptor2).getEntity(null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void getEntityReturnsFirstResult()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Object entity1 = new Object();

		when(interceptor1.getEntity(null, null)).thenReturn(entity1);
		when(interceptor2.getEntity(null, null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertSame(entity1, compound.getEntity(null, null));

		verify(interceptor1).getEntity(null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void getEntityReturnsFirstResultAfterNullResults()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Object entity = new Object();

		when(interceptor1.getEntity(null, null)).thenReturn(null);
		when(interceptor2.getEntity(null, null)).thenReturn(entity);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertSame(entity, compound.getEntity(null, null));

		verify(interceptor1).getEntity(null, null);
		verify(interceptor2).getEntity(null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void getEntityNameFallsThrough()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.getEntityName(null)).thenReturn(null);
		when(interceptor2.getEntityName(null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertNull(compound.getEntityName(null));

		verify(interceptor1).getEntityName(null);
		verify(interceptor2).getEntityName(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void getEntityNameReturnsFirstResult()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		String name = "name";

		when(interceptor1.getEntityName(null)).thenReturn(name);
		when(interceptor2.getEntityName(null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertSame(name, compound.getEntityName(null));

		verify(interceptor1).getEntityName(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void getEntityNameReturnsFirstResultAfterNullResults()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		String name = "name";

		when(interceptor1.getEntityName(null)).thenReturn(null);
		when(interceptor2.getEntityName(null)).thenReturn(name);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertSame(name, compound.getEntityName(null));

		verify(interceptor1).getEntityName(null);
		verify(interceptor2).getEntityName(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void instantiateFallsThrough()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.instantiate(null, null, null)).thenReturn(null);
		when(interceptor2.instantiate(null, null, null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertNull(compound.instantiate(null, null, null));

		verify(interceptor1).instantiate(null, null, null);
		verify(interceptor2).instantiate(null, null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void instantiateReturnsFirstResult()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Object entity = new Object();

		when(interceptor1.instantiate(null, null, null)).thenReturn(entity);
		when(interceptor2.instantiate(null, null, null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertSame(entity, compound.instantiate(null, null, null));

		verify(interceptor1).instantiate(null, null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void instantiateReturnsFirstResultAfterNullResults()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Object entity = new Object();

		when(interceptor1.instantiate(null, null, null)).thenReturn(null);
		when(interceptor2.instantiate(null, null, null)).thenReturn(entity);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertSame(entity, compound.instantiate(null, null, null));

		verify(interceptor1).instantiate(null, null, null);
		verify(interceptor2).instantiate(null, null, null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void isTransientFallsThrough()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.isTransient(null)).thenReturn(null);
		when(interceptor2.isTransient(null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertNull(compound.isTransient(null));

		verify(interceptor1).isTransient(null);
		verify(interceptor2).isTransient(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void isTransientReturnsFirstResultWithFalse()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.isTransient(null)).thenReturn(false);
		when(interceptor2.isTransient(null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertFalse(compound.isTransient(null));

		verify(interceptor1).isTransient(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void isTransientReturnsFirstResultWithTrue()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.isTransient(null)).thenReturn(true);
		when(interceptor2.isTransient(null)).thenReturn(null);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertTrue(compound.isTransient(null));

		verify(interceptor1).isTransient(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void isTransientReturnsFirstResultAfterNullResultsCaseTrue()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.isTransient(null)).thenReturn(null);
		when(interceptor2.isTransient(null)).thenReturn(true);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertTrue(compound.isTransient(null));

		verify(interceptor1).isTransient(null);
		verify(interceptor2).isTransient(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void isTransientReturnsFirstResultAfterNullResultsCaseFalse()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.isTransient(null)).thenReturn(null);
		when(interceptor2.isTransient(null)).thenReturn(false);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertFalse(compound.isTransient(null));

		verify(interceptor1).isTransient(null);
		verify(interceptor2).isTransient(null);
		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onCollectionRecreateCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.onCollectionRecreate(null, null);

		verify(interceptor1).onCollectionRecreate(null, null);
		verify(interceptor2).onCollectionRecreate(null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onCollectionRemoveCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.onCollectionRemove(null, null);

		verify(interceptor1).onCollectionRemove(null, null);
		verify(interceptor2).onCollectionRemove(null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onCollectionUpdateCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.onCollectionUpdate(null, null);

		verify(interceptor1).onCollectionUpdate(null, null);
		verify(interceptor2).onCollectionUpdate(null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onDeleteCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.onDelete(null, null, null, null, null);

		verify(interceptor1).onDelete(null, null, null, null, null);
		verify(interceptor2).onDelete(null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onFlushDirtyCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.onFlushDirty(null, null, null, null, null, null)).thenReturn(false);
		when(interceptor2.onFlushDirty(null, null, null, null, null, null)).thenReturn(false);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertFalse(compound.onFlushDirty(null, null, null, null, null, null));

		verify(interceptor1).onFlushDirty(null, null, null, null, null, null);
		verify(interceptor2).onFlushDirty(null, null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onFlushDirtyUnionOfEachInterceptor1()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.onFlushDirty(null, null, null, null, null, null)).thenReturn(false);
		when(interceptor2.onFlushDirty(null, null, null, null, null, null)).thenReturn(true);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertTrue(compound.onFlushDirty(null, null, null, null, null, null));

		verify(interceptor1).onFlushDirty(null, null, null, null, null, null);
		verify(interceptor2).onFlushDirty(null, null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onFlushDirtyUnionOfEachInterceptor2()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.onFlushDirty(null, null, null, null, null, null)).thenReturn(true);
		when(interceptor2.onFlushDirty(null, null, null, null, null, null)).thenReturn(false);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertTrue(compound.onFlushDirty(null, null, null, null, null, null));

		verify(interceptor1).onFlushDirty(null, null, null, null, null, null);
		verify(interceptor2).onFlushDirty(null, null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onFlushDirtyUnionOfEachInterceptor3()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		when(interceptor1.onFlushDirty(null, null, null, null, null, null)).thenReturn(true);
		when(interceptor2.onFlushDirty(null, null, null, null, null, null)).thenReturn(true);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertTrue(compound.onFlushDirty(null, null, null, null, null, null));

		verify(interceptor1).onFlushDirty(null, null, null, null, null, null);
		verify(interceptor2).onFlushDirty(null, null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onLoadCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.onLoad(null, null, null, null, null)).thenReturn(true);

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.onLoad(null, null, null, null, null)).thenReturn(true);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.onLoad(null, null, null, null, null);

		verify(interceptor1).onLoad(null, null, null, null, null);
		verify(interceptor2).onLoad(null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onLoadRecordsChangeAndCallsEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.onLoad(null, null, null, null, null)).thenReturn(true);

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.onLoad(null, null, null, null, null)).thenReturn(false);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);

		assertTrue(compound.onLoad(null, null, null, null, null));
		verify(interceptor1).onLoad(null, null, null, null, null);
		verify(interceptor2).onLoad(null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onLoadRecordsNoChangeAndCallsEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.onLoad(null, null, null, null, null)).thenReturn(false);

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.onLoad(null, null, null, null, null)).thenReturn(false);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);

		assertFalse(compound.onLoad(null, null, null, null, null));
		verify(interceptor1).onLoad(null, null, null, null, null);
		verify(interceptor2).onLoad(null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onPrepareStatementCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		String sql0 = "select * from dual";
		String sql1 = "select * from dual1";
		String sql2 = "select * from dual12";
		when(interceptor1.onPrepareStatement(sql0)).thenReturn(sql1);
		when(interceptor2.onPrepareStatement(sql1)).thenReturn(sql2);
		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		assertEquals(sql2, compound.onPrepareStatement(sql0));

		verify(interceptor1).onPrepareStatement(sql0);
		verify(interceptor2).onPrepareStatement(sql1);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onSaveRecordsChangeAndCallsEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.onSave(null, null, null, null, null)).thenReturn(true);

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.onSave(null, null, null, null, null)).thenReturn(false);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);

		assertTrue(compound.onSave(null, null, null, null, null));
		verify(interceptor1).onSave(null, null, null, null, null);
		verify(interceptor2).onSave(null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void onSaveRecordsNoChangeAndCallsEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		when(interceptor1.onSave(null, null, null, null, null)).thenReturn(false);

		Interceptor interceptor2 = mock(Interceptor.class);
		when(interceptor2.onSave(null, null, null, null, null)).thenReturn(false);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);

		assertFalse(compound.onSave(null, null, null, null, null));
		verify(interceptor1).onSave(null, null, null, null, null);
		verify(interceptor2).onSave(null, null, null, null, null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void postFlushCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.postFlush(null);

		verify(interceptor1).postFlush(null);
		verify(interceptor2).postFlush(null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}

	@Test
	public void preFlushCalledForEachInterceptor()
	{
		Interceptor interceptor1 = mock(Interceptor.class);
		Interceptor interceptor2 = mock(Interceptor.class);

		Interceptor compound = new CompoundHibernateInterceptor(interceptor1, interceptor2);
		compound.preFlush(null);

		verify(interceptor1).preFlush(null);
		verify(interceptor2).preFlush(null);

		verifyNoMoreInteractions(interceptor1, interceptor2);
	}
}
