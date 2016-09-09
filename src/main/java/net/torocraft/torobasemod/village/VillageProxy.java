package net.torocraft.torobasemod.village;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VillageProxy implements InvocationHandler {

	private Object obj;

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result;
		try {
			System.out.println("before method " + method.getName());
			result = method.invoke(obj, args);
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		} catch (Exception e) {
			throw new RuntimeException("unexpected invocation exception: " + e.getMessage());
		} finally {
			System.out.println("after method " + method.getName());
		}
		return result;
	}

}
