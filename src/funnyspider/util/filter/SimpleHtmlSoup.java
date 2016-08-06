package funnyspider.util.filter;

import java.util.ArrayList;

/**
 * 简单的HTMLSoup
 *
 */
public class SimpleHtmlSoup {
	private String html;
	
	public SimpleHtmlSoup(String html){
		this.html = html;
	}
	
	public String getHtml(){
		return html;
	}
	/**
	 * 实现内部逻辑
	 * 由property,value控制添加的部分,具体实现，为了适应情况1和情况2
	 * @param name 名称
	 * @param property 属性
	 * @param value 属性值
	 */
	private  void addList(String sta,String name,String property,String value,ArrayList<HtmlNode> l){
		String sub = null;
		int index = 0;int location = 0;int nextTag = 0;
		while((index = html.indexOf(sta,location)) != -1){
			int endIndex = html.indexOf("</"+name+">", index);
			if(endIndex != -1){
				sub = html.substring(index, endIndex+("</"+name+">").length());
			}else if((nextTag = html.indexOf("<",index+1))!=-1){
				//还有下一个标签
				sub = html.substring(index,nextTag);
			}else{
				//没有封闭则全部都当成子节点,且该标签在末尾
				sub = html.substring(index);	
			}
			location = index + sub.length();
			HtmlNode n = new HtmlNode(sub);
			if(property != null){
				//属性限制
				if(value != null){
					//值限制
					if(n.existPropertyAndValue(property, value)){
						l.add(n);
					}
				}else{
					//无值限制
					if(n.existProperty(property)){
						l.add(n);
					}
				}
			}else{
				//无限制
				l.add(n);
			}	
		}	
	}
	
	
	private HtmlNode[] addByContrl(String name,String property,String value){
		ArrayList<HtmlNode> list = new ArrayList<HtmlNode>();
		String sta1 = "<" + name+" ";
		//第一种情况
		this.addList(sta1, name, property, value, list);
		String sta2 = "<"+name+">";
		//第二种情况
		this.addList(sta2, name, property, value, list);
		return (HtmlNode[])list.toArray(new HtmlNode[list.size()]);
	}
	
	
	
	public HtmlNode[] getNode(String name){
		return addByContrl(name, null, null);
	}
	
	/**
	 * 根据标签名称和属性限定返回Node数组
	 * @param name 名字
	 * @param propery 属性
	 * @return 符合名字的节点形成的数组
	 */
	
	public HtmlNode[] getNode(String name,String propery){
		return this.addByContrl(name, propery, null);
	}
	
	/**
	 * 根据标签名称和属性的值限定返回Node数组
	 * @param name 名字
	 * @param propery 属性
	 * @param value 对应值
	 * @return 符合名字的节点形成的数组
	 */
	
	public HtmlNode[] getNode(String name,String propery,String value){
		return this.addByContrl(name, propery,value);
	}
	
}
