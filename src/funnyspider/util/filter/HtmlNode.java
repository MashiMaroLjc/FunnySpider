package funnyspider.util.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HtmlNode {
	private String name;
	//含有html标签的字符串形式
	private String innerHTML = "";
	//纯文本
	private String innerText = "";
	private HashMap<String,String> map = new HashMap<String,String>();
	
	HtmlNode(String str) {
		char ch;
		int index = -1;
		do{
			index++;
			ch = str.charAt(index);
			
		}while(ch!=' '&&ch!='>');
		this.name = str.substring(1,index);
		//提取名字
		int flagIndex = str.indexOf(">");
		//结束的标签符号位置
		if(index != flagIndex){
			//该标签需要提取属性 
			String sub = str.substring(1, flagIndex).replace("\"", "").replace("\'", "");
			while((sub.indexOf(" =")!=-1)||(sub.indexOf("= ")!=-1)){
				//处理非法空格
				sub=sub.replace(" =", "=").replace("= ", "=");
			}
			String[] s =sub.split(" ");
			//提取属性
			for(String s1:s){
				if(s1.indexOf('=') != -1){
					String[] v = s1.split("=");
					String key = v[0];
					String value = null;
					if(v.length>1){
						value= v[1];
					}
					map.put(key, value);
				}
			}
		}
		
		int endIndex = str.indexOf("</"+this.name+">");
		//结束标志内容
		if(endIndex != -1){
			this.innerHTML =str.substring(flagIndex+1, endIndex);
		}else{
			//自动找下一个标签的开始
			int temp = str.indexOf("<",flagIndex+1);
			if(temp!=-1){
				this.innerHTML =str.substring(flagIndex+1, temp);
			}
		}
		this.innerText = Filter.filterTag(innerHTML);

	}
	
	
	public String getName(){
		return name;
	}
	
	public String getInnerHtml(){
		return innerHTML;
	}

	public String getInnerText(){
		return innerText;
	}
	
	/**
	 * 获取该标签的所有属性名称
	 * @return 所有属性的名称集合
	 */
	public Set<String> getPropertys(){
		return map.keySet();
	}
	
	/**
	 * 获取属性值
	 * @param property 属性名
	 * @return 值
	 */
	public String getValue(String property){
		return map.get(property);
	}
	
	/**
	 * 检查该节点是否存在该属性
	 * @param property 属性
	 * @return 布尔值
	 */
	public boolean existProperty(String property){
		return map.containsKey(property);
	}
	/**
	 * 检查该节点是否存在该属性，且属性值等于指定值
	 * @param property 属性
	 * @param value 限制对应值
	 * @return 布尔值
	 */
	public boolean existPropertyAndValue(String property,String value){
		return this.existProperty(property) && map.get(property).equals(value);
	}
	
	/**
	 * 返回标签的属性个数
	 * @return
	 */
	public int size(){
		return map.size();
	}
	
	/**
	 * 获取&lt;name property：key&gt;的形式的字符串
	 * @return 返回&lt;name property：key&gt;的形式的字符串
	 */
	public String toString(){
		String str = "<" + name;
		int length = size();
		if(length > 0){str+=" ";}
		int i =0;
		for(Map.Entry<String, String> e : map.entrySet()){
			i++;
			if(e.getValue()!= null){str += e.getKey() + "=\"" + e.getValue() + "\"";}
			else{str += e.getKey() + "=\"\"";}
			if(i<length){str+=" ";}
		}
		return str + ">";
	}
	
}
