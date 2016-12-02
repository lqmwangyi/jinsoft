package ssh.controller;

public class Singleton {
	//定义一个静态Singleton变量，用来缓存对象
	private static Singleton instance;
	
	private Singleton(){}
	
	public static Singleton getInstance(){
		if(instance == null){
			instance = new Singleton();
		}
		return instance;
	}
}
