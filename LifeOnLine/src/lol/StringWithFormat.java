/**
 * This class acts as an object which is used by FormatToString class to display strings with 
 * certain format.
 * It has two main attributes which are string and format. String is the intended string which 
 * is displayed to the user and the corresponding format is the format intended to be implemented 
 * to that displayed string.
 */

package lol;

//@author A0112166R
public class StringWithFormat {
	/******************Attributes******************/
	private String str;
	private String format;
	boolean isJustAdded = false; //this boolean stores data which indicate whether the current 
								 //string is just added by the user
	
	/******************Constructor******************/
	public StringWithFormat(String str, String format){
		this.str = str;
		this.format = format;
	}
	
	/******************Accessors******************/
	public String getString(){
		return str;
	}
	
	public String getFormat(){
		return format;
	}
	
	public boolean getIsJustAdded(){
		return isJustAdded;
	}
	
	/******************Mutators******************/
	public void setString(String str){
		this.str = str;
	}
	
	public void setFormat(String format){
		this.format = format;
	}
	
	public void setIsJustAdded(boolean isJustAdded){
		this.isJustAdded = true;
	}
	
	/******************Class methods******************/

	/**
	 * Copy StringWithFormat object strWithFormat1 to StringWithFormat object strWithFormat2
	 * 
	 * @param strWithFormat1 
	 * @param strWithFormat2 
	 */
	public static void copy(StringWithFormat strWithFormat1, StringWithFormat strWithFormat2){
		strWithFormat2.setString(strWithFormat1.getString());
		strWithFormat2.setFormat(strWithFormat1.getFormat());
	}
}
