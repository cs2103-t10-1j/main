package lol;

public class LOLMain {
	private static String feedback;
	private static String showToUser;
	
	public static void passStringToControl(String userInput){
		feedback = LOLControl.executeUserInput(userInput);
	}
	
	public static String getFeedback(){
		return feedback;
	}
	
	//used to get String for task display from control
	public static String getShowToUser(){
		return showToUser;
	}
}
