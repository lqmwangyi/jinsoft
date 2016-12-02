package jinsoftms.util.factory;

public class ProcessFactory {
	private static Class<?> loginClass;
	
	public static Class<?> createLoginAction(String ClassName){
		try{
			if(loginClass==null){
				loginClass=Class.forName(ClassName);
			}
			return loginClass; } catch (Exception ex) {
		}
		return null;
	}
}
