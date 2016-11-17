package jinsoftms.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;

public class RuntimeExceptionUtil extends RuntimeException{
  static final long serialVersionUID = -7034897190745766939L;
  private static Logger log = Logger.getLogger(RuntimeExceptionUtil.class);

  public RuntimeExceptionUtil()
  {
    saveException();
  }

  public RuntimeExceptionUtil(String arg0) {
    super("!" + arg0);
    saveException();
  }

  public RuntimeExceptionUtil(String arg0, Throwable arg1) {
    super(arg0, arg1);
    saveException();
  }

  public RuntimeExceptionUtil(Throwable arg0) {
    super(arg0);
    saveException();
  }

  public RuntimeExceptionUtil(String className, String mothedName, String msg, Throwable arg0) {
    super(arg0);
    StackTraceElement[] stes = getStackTrace();
    int stel = stes.length;
    StackTraceElement[] stens = new StackTraceElement[stel + 1];
    for (int i = 0; i < stens.length; ++i)
      if (stel == i)
        stens[i] = new StackTraceElement(className, mothedName, "!" + msg, 0);
      else
        stens[i] = stes[i];

    setStackTrace(stens);
    saveException();
  }

  private void saveException() {
    StringWriter sw = new StringWriter();
    printStackTrace(new PrintWriter(sw));
    log.error(sw.toString());
  }
}