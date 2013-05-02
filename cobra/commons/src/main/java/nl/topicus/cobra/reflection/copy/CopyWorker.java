package nl.topicus.cobra.reflection.copy;

public interface CopyWorker<T>
{
	public T createInstance(T object);

	public void fillInstance(T source, T destination);
}
