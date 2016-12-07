package jinsoftms.util.factory;

import java.util.concurrent.ConcurrentHashMap;

import wechatms.handlerprocess.BusinessInterface;
import wechatms.handlerprocess.OperationJDBCMapKey;


public class ProcessFactory {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static ConcurrentHashMap<String, Class<BusinessInterface>> classMap = new ConcurrentHashMap(1500);
	private static Class<OperationJDBCMapKey> jdbcClass = null;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BusinessInterface createBusinessLogical(String blClassName) {
		Class classZZ;
	    try {
	    	System.out.println(blClassName);
	    	classZZ = (Class)classMap.get(blClassName);
	    	System.out.println(classZZ);
	    	if (classZZ == null) {
	    		classZZ = Class.forName(blClassName);
	    		System.out.println(classZZ);
	    		classMap.put(blClassName, classZZ);
	    	}
	    	System.out.println(classZZ);
	    	return ((BusinessInterface)classZZ.newInstance()); } catch (Exception ex) {
	    }
	    return null;
	}
	
	@SuppressWarnings("unchecked")
	public static OperationJDBCMapKey createOperationJDBCMapKey(String opClassName){
		try{
			if (jdbcClass == null)
		        jdbcClass = (Class<OperationJDBCMapKey>) Class.forName(opClassName);

			return ((OperationJDBCMapKey)jdbcClass.newInstance()); } catch (Exception ex) {
		}
		return null;
	}
	
	
}
