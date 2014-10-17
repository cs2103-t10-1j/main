package lol;

public class StringWithFormat {
	String str;
	String format;
	
	public StringWithFormat(String str, String format){
		this.str = str;
		this.format = format;
	}
	
	public void setString(String str){
		this.str = str;
	}
	
	public void setFormat(String format){
		this.format = format;
	}
	
	public String getString(){
		return str;
	}
	
	public String getFormat(){
		return format;
	}
}
