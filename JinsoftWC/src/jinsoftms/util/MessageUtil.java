package jinsoftms.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import jinsoftms.database.wechat.Image;
import jinsoftms.database.wechat.ImageMessage;
import jinsoftms.database.wechat.Music;
import jinsoftms.database.wechat.MusicMessage;
import jinsoftms.database.wechat.News;
import jinsoftms.database.wechat.NewsMessage;
import jinsoftms.database.wechat.TextMessage;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageUtil {
	
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_NEWS = "news";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_MUSIC = "music";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubsribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	
	public static final String MESSAGE_SCANCODE = "scancode_push";
	
	/**
	 * 扩展 xstream，使其支持 CDATA 块
	 */
	private static XStream xstream = new XStream(new XppDriver(){
		public  HierarchicalStreamWriter createWriter(Writer out){

			return new PrettyPrintWriter(out) {
				// 对所有 xml 节点的转换都增加 CDATA 标记
				boolean cdata = true;
				
				@SuppressWarnings("rawtypes")
				public void startNode(String name,Class clazz){
					super.startNode(name,clazz);
				}
				
				protected void writeText(QuickWriter writer,String text){
					if(cdata){
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					}else{
						writer.write(text);
					}
				}
			};
		}
	});
	
	/**
	 * XML转换成MAP集合 
	 * @param req
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(javax.servlet.http.HttpServletRequest req) 
			throws IOException, DocumentException {
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		
		InputStream ins = req.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		
		List<Element> list = root.elements();
		
		for(Element e : list){
			map.put(e.getName(), e.getText());
		}
		
		ins.close();
		return map;
	}
	
	/**
	 * 文本消息对象转成XML
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		xstream.alias("xml", textMessage.getClass());
		return xstream.toXML(textMessage);
	}
	
	/**
	 * 初始化文本信息
	 * @return
	 */
	public static String initText(String fromUserName,String toUserName,String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MessageUtil.MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent(content);
		return textMessageToXml(text);
	}
	
	/**
	 * 主菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎你的关注,请按照菜单提示进行操作:\n\n");
		sb.append("1.公司介绍\n");
		sb.append("2.楼盘信息\n\n");
		sb.append("回复？调出此菜单。");
		return sb.toString();
	}

	public static String firstText() {
		StringBuffer sb = new StringBuffer();
		sb.append("公司简介");
		sb.append("广州市精软网络数据有限公司：");
		sb.append("经营：电子及电子计算机技术服务，计算机软件开发，电子计算机网络工程。");
		sb.append("拥有31个员工，其中管理人员3人，行政人员3人，技术设计人员3人，软件开发人员4人，高级网络技术人员2人，销售人员8人，维护人员6人，网络工程技术人员2人");
		sb.append("主营方向：信息管理系统的研发，集成信息收集、数据管理与统计分析平台、乃至相关控制系统的研发，网络技术的尖端应用以及拓展。");
//		sb.append("酒店软件：");
//		sb.append("自上世纪90年代以来总共为136间酒店建造了酒店管理系统，把先进的管理思维结合严谨计算机模式，形成的一系列管理体系和控制系统带到了这个行业，为当时酒店行业带来了很多的惊喜。近年来更在WINDOWS平台下结合移动通信设备、电话交换机及其PMS系统建立了实时的信息管理系统，在酒店管理应用方面登上了新的台阶。");
//		sb.append("物业管理软件：");
//		sb.append("在酒店管理系统的数据体系具有巨大流动性、实时性、储备性的基础上，从星级酒店管理的理念和流程中吸取管理精华，在上世纪90年代中页，我为公司针对物业管理行业研发了一系列行业专用软件，是广州市最早开发物业管理软件的公司之一。最大特点是对物业管理行业的深刻理解，最早是参考香港先进物业管理规范，最大限度地满足财务规范管理和报表灵活处理的特点。非常适合数据量大，高素质规范管理的集团公司使用。在大中型小区管理、写字楼、商业大厦管理、大型购物中心等方面都有许多成功的典型客户。跃升为物业管理软件行业数一数二的知名企业，其软件的使用性能深得从业人员的青睐，单在珠三角地区知名的物业管理企业中我公司软件系统的市场占有率达到58%，在广州市内更达到72%。");
//		sb.append("在2008年成功完成了物业集团版的开发,物业集团版最大的特点是: 采用了B/S和C/S相结合的网络技术,最大限度地保障数据安全、运作可靠、发展运行成本低,满足物业集团对基层管理的需求.受合生创展集团的邀请为其旗下康景物业管理公司分布全国的60多个小区实现一站式信息整体体系管理，信息内容涵盖数十万个家庭和企业及其数年来产生上亿的数据记录实现整体统筹管理的信息系统；同时这个系统受到使用设备为普通民用设备、网络的条件制约，也要满足跨地域60多个站点不间断实时工作的使用要求；可以认定现有的技术乃至理论体系都难以解决的问题，在经过创新思维后我们的团队解决了这个连大学研究机构都解决不了的难题；现我公司正在把这技术深入完善，并准备将其申请专利。");
//		sb.append("财务软件：");
//		sb.append("财务软件是我公司代表产品之一，也是在90年代我们在酒店软件研发的高峰期派生出来的副产品，得到行业协会的认证推广，与 “金蝶”、“用友”等知名品牌齐名，即使是这样对于我们来说相比物业管理软件取得的成就是微不足道的，只是通过这个方面说明我们的研发一直紧守着紧急的思维模式，可以说有财务软件对我们开发团队的锻炼，才有我们的今天。");
//		sb.append("管理软件：");
//		sb.append("除上面提及的软件平台外，我们还有一系列相关行业软件，譬如：《餐饮的点菜与结算系统》、《水疗管理系统及其销售管理系统》、《库存管理》、《物业综合事务办公系统》值得一提的是《人力资源管理系统》集人事详尽资料、人事管理、计生管理、考勤、排班、工资、公积金台帐、劳动保险等一体化大型综合专业化软件，用客户的话来说：“比国家人事部研发的软件还要专业、还要好用……”");
//		sb.append("接口软件：");
//		sb.append("从我们从事软件研发的这个行业以来就遇见软件的发展将向边缘化控制、多媒体控制方面发展。在90年代初人们还在用插在电脑上的适配器插卡连接电话交换机时，我们已经使用个人电脑的标准接口直接读取和传输数据。通过连接的外围设备有：银行POS机、电子门锁、门禁、考勤、身份识别等等。软件接口方面有：公安部身份备案系统，“金蝶”、“用友”财务软件等通用软件接口。");
//		sb.append("网络技术方面：");
//		sb.append("近年来网络技术日新月异，尤其在宽带连接发展迅猛，我们的强项是宽带接入解决方案方面，我们承建了13座酒店的大厦的宽带接入主干机房，3个小区的宽带接入主干机房，其中有个别比较特殊的个案：在某小区中由于布线一直沿用电话布线系统，要改造成综合布线系统成本投入过高布线难度也高，因此，我们的方案中使用了DSL拨号方案。再有酒店客人存在着用户流动大，使用设备、系统及其设置复杂、不标准，经常存在客人投诉网络连接问题，其原因是客人设置了固定IP而又不懂得修改，大大提升酒店维护工作的难度，针对酒店用户的这一特殊需求，早在2003年我们就开始向设备制造商提出了需求和解决方案的框架，这个想法在当时可以说是领先于行业内的，针对这个需求今年来很多制造商研发出所谓“酒店即插即用”功能：就是自动扫描局域网里电脑的IP然后产生一个路由服务，使其链接网络(UPNP功能)。");
//		sb.append("电子商务的广泛使用以及宽带网络的普及，大中型企业对网络的高层次使用需求越来越多，近年来我们建造了11个VPN网络。其中成本、费用受到限制的项目建造的难度更大，例如：通过DSL网络动态IP的节点，加上跨网络营运商拨入的VPN解决方案。");
//		sb.append("维护与服务：");消
//		sb.append("我们公司一直以来是以服务著称，在成立公司初期，很多客户都是由维护客户转化为软件客户，而且在我们的客户群中绝大多数都能够常年维系，不断扩大业务范围，这些年来流失的客户不超过5个占我们的客户群约2%，从这个侧面可以看出我们公司为客户服务的精神和态度，同时有优秀的服务必须拥有一支技术精湛、朝气蓬勃、素质高、反应快、业务熟、责任心强、富有进取创造精神的技术、市场队伍；时时把握“科技、信息、人力资源”三大支柱的整体优势，以最优化的资源配置与最佳的效益增长为源动力，坚持以人为本、以技术为基础、以市场为导向，在成长的路上，励精图治、成就斐然。");
		return sb.toString();
		
	}

	public static String secondText() {
		StringBuffer sb = new StringBuffer();
		sb.append("广州世界贸易中心大厦");
		sb.append("地址：广州环市东路371-375号");
		return sb.toString();
	}
	
	/**
	 * 图文消息转成XML
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * 图文消息组装
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	public static String initNewsMessage(String fromUserName,String toUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		
		News news = new News();
		news.setTitle("公司简介");
		news.setDescription("广州市精软网络数据有限公司："
				+ "经营：电子及电子计算机技术服务，计算机软件开发，电子计算机网络工程。"
				+ "拥有31个员工，其中管理人员3人，行政人员3人，技术设计人员3人，软件开发人员4人，高级网络技术人员2人，销售人员8人，维护人员6人，网络工程技术人员2人"
				+ "主营方向：信息管理系统的研发，集成信息收集、数据管理与统计分析平台、乃至相关控制系统的研发，网络技术的尖端应用以及拓展。");
		news.setPicUrl("http://jinsoft.vicp.net/jinsoftwechat/images/20120711164307937.gif");
		news.setUrl("www.jingsoft.com/AboutUs.aspx?id=4");
		newsList.add(news);
		
		News newstel = new News();
		newstel.setTitle("联系我们");
		newstel.setDescription("公司名称：广州市精软网络数据有限公司"
				+ "姓名：温先生 （公关营销部）"
				+ "电话:86-20-83597236, 83595506, 83577635"
				+ "传真:86-20-83492095"
				+ "手机：13751842821"
				+ "邮箱：jinsoft@126.com"
				+ "网址：http://www.jingsoft.com"
				+ "详细地址：广东省广州市越秀区淘金路54号世贸花园大厦20/F"
				+ "邮政编码：510095");
		newstel.setPicUrl("http://jinsoft.vicp.net/jinsoftwechat/images/20120711165254578.gif");
		newstel.setUrl("www.jingsoft.com/AboutUs.aspx?id=6");
		newsList.add(newstel);
		
		NewsMessage newsMessage = new NewsMessage();
		newsMessage.setFromUserName(toUserName);
		newsMessage.setToUserName(fromUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MESSAGE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		return message; 
	}
	
	/**
	 * 图片消息转成XML
	 * @param newsMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	/**
	 * 图片消息的组装
	 * @param fromUserName
	 * @param toUserName
	 * @return
	 */
	public static String initImageMessage(String fromUserName,String toUserName){
		String message = null;
		Image image = new Image();
		image.setMediaId("rLkWJtRt_Crt-FphMx93Dk1yP0m1YHz2VaUOzGoE112dbYqexU7tfdue7bAPNFtw");
		
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MESSAGE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		
		message=imageMessageToXml(imageMessage);
		return message;
	}
	
	/**
	 * 图片消息转成XML
	 * @param newsMessage
	 * @return
	 */
	public static String musicMessageToXml(MusicMessage musicMessage){
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
	
	public static String initMusicMessage(String fromUserName,String toUserName){
		String message = null;
		Music music = new Music();
		music.setThumbMediaId("0njvsw_-mbRdOsj78SLcy5J6NSehhco5iBD_4BvUtlXoKTZNFFYDrLvLOWkpDK_4");
		music.setTitle("夏洛特烦恼");
		music.setDescription("电影《夏洛特烦恼》同名主题曲《夏洛特烦恼》");
		music.setMusicUrl("http://jinsoft.vicp.net/jinsoftwechat/music/xialoutedefannao.mp3");
		music.setHQMusicUrl("http://jinsoft.vicp.net/jinsoftwechat/music/xialoutedefannao.mp3");
		
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(MESSAGE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);
		
		message=musicMessageToXml(musicMessage);
		return message;
	}
	
	
}
