package lol;

import java.awt.event.*;

import javax.swing.*;

public class InputTextFieldListener implements ActionListener {
	JTextField inputTF;
	JTextArea mainDisplayTA;
	JTextArea feedbackDisplayTA;

	public InputTextFieldListener(JTextArea mainDisplayTA, JTextArea feedbackDisplayTA, JTextField inputTF){
		this.inputTF = inputTF;
		this.mainDisplayTA = mainDisplayTA;
		this.feedbackDisplayTA = feedbackDisplayTA;
	}

	@Override
	public void actionPerformed(ActionEvent event){ 
		String inputStr = inputTF.getText();
		String feedback =  passStringToControlAndGetFeedback(inputStr);

		refreshMainDisplayTA();

		feedbackDisplayTA.setText(feedback);

		clear(inputTF);
	}

	public String passStringToControlAndGetFeedback(String inputStr){
		return LOLControl.executeUserInput(inputStr);
	}

	public void refreshMainDisplayTA(){
		clear(mainDisplayTA);

		//stub
		//should get list from controller
		TaskList list = LOLStorage.load();
		showInMainDisplayTA(list);
	}

	public void showInMainDisplayTA(TaskList taskList){
		String strToShow = "";
		Date previousDueDate = new Date(); //need to change

		for(int i = 0; i < taskList.size(); i++){
			Task task = taskList.get(i);
			String taskDescription = task.getTaskDescription();
			
			Date currentDueDate = task.getTaskDueDate();
			if(currentDueDate != null && !currentDueDate.equals(previousDueDate)){
				int dueDay = currentDueDate.getDay();
				int dueMonth = currentDueDate.getMonth();
				int dueYear = currentDueDate.getYear4Digit();
				
				strToShow = strToShow + dateFormatAsHeader(dueDay, dueMonth, dueYear);
				previousDueDate = currentDueDate;
			}

			strToShow = strToShow + formatString(taskDescription, i+1);
		}

		mainDisplayTA.setText(strToShow);
	}

	public String formatString(String str, int i){
		str = addNumbering(str, i);
		str = addNewLine(str);

		return str;
	}

	public String addNumbering(String str, int number){
		return number + ". " + str;
	}
	
	public String dateFormatAsHeader(int dueDay, int dueMonth, int dueYear){
		String str = "";

		str = str + "===============";
		str = addNewLine(str);
		str = str + dueDay + "/" + dueMonth + "/" + dueYear;
		str = addNewLine(str);
		str = str + "===============";
		str = addNewLine(str);
		
		return str;
	}

	public String addNewLine(String str){
		return str + "\n";
	}

	public void clear(JTextField TF){
		TF.setText("");
	}

	public void clear(JTextArea TA){
		TA.setText("");
	}
}
