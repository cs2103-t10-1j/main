package lol;

public class LOLMain {
	public String userInput;
	public String feedback;
	
	public LOLMain(String userInput){
		this.userInput = userInput;
		this.feedback = passStringToParser(userInput);
	}
	
	public String passStringToParser(String userInput){
		return "String has been passed to Parser: " + userInput;
	}
	
	public String getFeedback(){
		return feedback;
	}
}
