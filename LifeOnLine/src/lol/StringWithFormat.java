package lol;

public class StringWithFormat {
	String str;
	String format;
	boolean isJustAdded = false;
	
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
	
	public void setIsJustAdded(boolean isJustAdded){
		this.isJustAdded = true;
	}
	
	public String getString(){
		return str;
	}
	
	public String getFormat(){
		return format;
	}
	
	public boolean getIsJustAdded(){
		return isJustAdded;
	}
	
	public static void copy(StringWithFormat str1, StringWithFormat str2){
		str2.setString(str1.getString());
		str2.setFormat(str1.getFormat());
	}
}
