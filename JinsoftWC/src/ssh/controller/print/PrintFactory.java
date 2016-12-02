package ssh.controller.print;

import ssh.database.out.Output;
import ssh.database.out.Printer;
import ssh.service.OutputFactory;

public class PrintFactory implements OutputFactory {

	public Output getOutput() {
		// TODO Auto-generated method stub
		return new Printer();
	}

}
