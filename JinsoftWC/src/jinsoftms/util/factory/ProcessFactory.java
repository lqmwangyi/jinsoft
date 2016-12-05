package jinsoftms.util.factory;

import java.util.concurrent.ConcurrentHashMap;

import jinsoftms.handlerprocess.BusinessInterface;
import jinsoftms.handlerprocess.OperationJDBCMapKey;

public class ProcessFactory {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static ConcurrentHashMap<String, Class<BusinessInterface>> classMap = new ConcurrentHashMap(1500);
	private static Class<OperationJDBCMapKey> jdbcClass = null;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static BusinessInterface createBusinessLogical(String blClassName) {
		Class classZZ;
	    try {
	    	
	    	classZZ = (Class)classMap.get(blClassName);
	    	if (classZZ == null) {
	    		classZZ = Class.forName(blClassName);
	    		classMap.put(blClassName, classZZ);
	    	}
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
