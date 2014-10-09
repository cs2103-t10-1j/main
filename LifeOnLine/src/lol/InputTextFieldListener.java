package lol;

import java.awt.event.*;

import javax.swing.*;

public class InputTextFieldListener implements ActionListener, KeyListener {
	final String[] commands = {"", "add ", "delete ", "edit ", "done ", "undo", "redo"};
	JTextField inputTF;
	JTextArea mainDisplayTA;
	JTextArea mainDisplayTA2;
	JTextArea feedbackDisplayTA;
	Integer i;

	public InputTextFieldListener(JTextArea mainDisplayTA,JTextArea mainDisplayTA2, JTextArea feedbackDisplayTA, JTextField inputTF, Integer i){
		this.inputTF = inputTF;
		this.mainDisplayTA = mainDisplayTA;
		this.feedbackDisplayTA = feedbackDisplayTA;
		this.mainDisplayTA2 = mainDisplayTA2;
		this.i = i;
		inputTF.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event){ 
		String inputStr = inputTF.getText();
		
		if(inputStr.trim().equalsIgnoreCase("exit")){ //this line should let control handle
			System.exit(0);
		}
		
		String feedback = passStringToControlAndGetFeedback(inputStr);

		refreshMainDisplayTA();

		feedbackDisplayTA.setText(feedback);

		clear(inputTF);
		i = new Integer(0);
		
	}

	public String passStringToControlAndGetFeedback(String inputStr){
		return LOLControl.executeUserInput(inputStr);
	}

	public void refreshMainDisplayTA(){
		clear(mainDisplayTA);
		clear(mainDisplayTA2);

		TaskList<Task> taskList = LOLControl.getTaskList();
		showInMainDisplayTA(taskList);
	}

	public void showInMainDisplayTA(TaskList<Task> taskList){
		String strToShow = "";
		String strToShow2 = "";
		Date previousDueDate = new Date(); //need to change: set date's parameter as impossible date

		for(int i = 0; i < taskList.size(); i++){
			Task task = taskList.get(i);
			String taskDescription = task.getTaskDescription(); //useless line? refer line 62
			Time dueStartTime = task.getStartTime();
			Time dueEndTime = task.getEndTime();

			Date currentDueDate = task.getTaskDueDate();
			if(currentDueDate != null && !currentDueDate.equals(previousDueDate)){
				int dueDay = currentDueDate.getDay();
				int dueMonth = currentDueDate.getMonth();
				int dueYear = currentDueDate.getYear4Digit();

				strToShow = strToShow + dateFormatAsHeader(dueDay, dueMonth, dueYear);
			    
				previousDueDate = currentDueDate;

				String str = taskDescription;
				if(dueStartTime != null && dueEndTime != null){
					str = addTimeStr(str, dueStartTime, dueEndTime);
				}
				else if(dueStartTime != null){
					str = addTimeStr(str, dueStartTime);
				}
				if(task.getTaskLocation() != null){
					str = addLocationStr(str, task.getTaskLocation());
				}
				str = formatString(str, i+1);

				strToShow = strToShow + str;
			}else if(currentDueDate != null && currentDueDate.equals(previousDueDate)){
				previousDueDate = currentDueDate;

				String str = taskDescription;
				if(dueStartTime != null && dueEndTime != null){
					str = addTimeStr(str, dueStartTime, dueEndTime);
				}
				else if(dueStartTime != null){
					str = addTimeStr(str, dueStartTime);
				}
				if(task.getTaskLocation() != null){
					str = addLocationStr(str, task.getTaskLocation());
				}
				str = formatString(str, i+1);

				strToShow = strToShow + str;
			}else {
				if(currentDueDate == null && previousDueDate != null){
				strToShow2 = strToShow2 + dateFormatAsHeader(0, 0, 0);
				}
			
			previousDueDate = currentDueDate;

			String str = taskDescription;
			if(dueStartTime != null && dueEndTime != null){
				str = addTimeStr(str, dueStartTime, dueEndTime);
			}
			else if(dueStartTime != null){
				str = addTimeStr(str, dueStartTime);
			}
			if(task.getTaskLocation() != null){
				str = addLocationStr(str, task.getTaskLocation());
			}
			str = formatString(str, i+1);

			
			strToShow2 = strToShow2 + str;
			}
		}

		mainDisplayTA.setText(strToShow);
		mainDisplayTA2.setText(strToShow2);
	}

	public String addTimeStr(String str, Time startTime, Time endTime){
		str = "[" + startTime.toString() + " - " + endTime.toString() + "] " + str;
		return str;
	}
	
	public String addTimeStr(String str, Time startTime){
		str = "[" + startTime.toString() + "] " + str;
		return str;
	}

	public String addLocationStr(String str, String location){
		str = str + " at [" + location + "]";
		return str;
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

		str = str + "===========================";
		str = addNewLine(str);

		if(dueDay != 0 && dueMonth != 0 && dueYear != 0){
			str = str + dueDay + "/" + dueMonth + "/" + dueYear;
			str = addNewLine(str);	
		}
		else{
			str = str + "To-Do Tasks (without due date)";
			str = addNewLine(str);
		}

		str = str + "===========================";
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
	
	   /** Handle the key typed event from the text field. */
 public void keyTyped(KeyEvent e) {
    ;
 }

 /** Handle the key-pressed event from the text field. */
 public void keyPressed(KeyEvent e) {
     ;
 }

 /** Handle the key-released event from the text field. */
 public void keyReleased(KeyEvent e) {
 	if (e.getKeyCode() == KeyEvent.VK_DOWN) {
 		i = new Integer((i.intValue() + 1)%commands.length);
			inputTF.setText(commands[i.intValue()]);
			inputTF.grabFocus();
		}
 	if (e.getKeyCode() == KeyEvent.VK_UP) {
 		if((i.intValue()-1)<0)
 			i = new Integer((i.intValue() - 1)+commands.length);
 		else
 			i = new Integer((i.intValue() - 1));
			inputTF.setText(commands[i.intValue()]);
			inputTF.grabFocus();
		}
 }
}
