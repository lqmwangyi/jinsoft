package jinsoftms.util;

import java.math.BigDecimal;

public class StaticField {
	public static final String versionName = "version";
	public static final String addDateTimeName = "AddTime";
	public static final String addWhoName = "AddWho";
	public static final String editDateTimeName = "EditTime";
	public static final String editWhoName = "EditWho";
	public static final String pageFieldName = "temp$rowid$";
	public static final String stringDefaultValue = "";
	public static final String idFieldName = "ID";
	public static final String procedureReturnName = "##ProcedureReturnName##";
	public static final String ireportSubDirName = "SUBREPORT_DIR";
	public static final String ireportLogImageName = "IMAGE";
	public static final String MSSQL_ISNULL = "isnull(";
	public static final String ORA_ISNULL = "nvl(";
	public static final String DB2_ISNULL = "COALESCE(";
	public static final String BreakDBConnectionExceptionMsg1 = "Connection aborted by peer";
	public static final String BreakDBConnectionExceptionMsg2 = "该连接已关闭";
	public static final String BinaryToJsonText = "DBType is Binary";
	public static final String OrderNo_FieldName = ".OrderNo";
	public static final Boolean booleanDefaultValue = Boolean.FALSE;
	public static final BigDecimal numberDefaultValue = BigDecimal.ZERO;

	public static String getIsnullFuncationName(int type) {
	    switch (type)  {
		    case 1:
		      return "isnull(";
		    case 2:
		      return "nvl(";
		    case 3:
		      return "COALESCE(";
	    }
	    return "isnull(";
	}
  
}
