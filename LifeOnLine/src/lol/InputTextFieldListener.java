package lol;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class InputTextFieldListener implements ActionListener, KeyListener {
	final String[] commands = {"", "add ", "delete ", "edit ", "done ", "undo", "redo"};
	JTextField inputTF;
	JTextPane mainDisplayTP;
	StyledDocument doc = new DefaultStyledDocument();
	JTextPane mainDisplayTP2;
	StyledDocument doc2 = new DefaultStyledDocument();
	JTextArea feedbackDisplayTA;
	Integer i;

	public InputTextFieldListener(JTextPane mainDisplayTP,JTextPane mainDisplayTP2, JTextArea feedbackDisplayTA, JTextField inputTF, Integer i){
		this.inputTF = inputTF;

		this.mainDisplayTP = mainDisplayTP;
		mainDisplayTP.setDocument(doc);
		this.mainDisplayTP2 = mainDisplayTP2;
		this.mainDisplayTP2.setDocument(doc2);

		this.feedbackDisplayTA = feedbackDisplayTA;
		this.i = i;
		inputTF.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event){ 
		String inputStr = inputTF.getText();

//		if(inputStr.trim().equalsIgnoreCase("exit")){ //this line should let control handle
//			System.exit(0);
//		}

		String feedback = passStringToControlAndGetFeedback(inputStr);
		feedbackDisplayTA.setText(feedback);

		refreshMainDisplayTA();

		clear(inputTF);
		i = new Integer(0);
	}

	public String passStringToControlAndGetFeedback(String inputStr){
		return LOLControl.executeUserInput(inputStr);
	}

	public void refreshMainDisplayTA(){
		clear(mainDisplayTP);
		clear(mainDisplayTP2);

		TaskList<Task> taskList = LOLControl.getTaskList();
		showInMainDisplayTA(taskList);
	}

	public void showInMainDisplayTA(TaskList<Task> taskList){
		String strToShow = "";
		String strToShow2 = "";
		Date previousDueDate = new Date(-1, -1, -9999, null);
	
		for(int i = 0; i < taskList.size(); i++){
			Task task = taskList.get(i);
			String taskDescription = task.getTaskDescription(); //useless line?
			Time dueStartTime = task.getStartTime();
			Time dueEndTime = task.getEndTime();

			Date currentDueDate = task.getTaskDueDate();
			
			assert (currentDueDate.getDay() == -1 && currentDueDate.getMonth() == -1 
					&& currentDueDate.getYear4Digit() == -9999) : "impossible date entered";
			
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

				if(task.getIsDone()){
					addStrikeThroughToTask(strToShow, str, doc);
					strToShow = "";
				}
				else{
					strToShow = strToShow + str;
				}
			}
			else if(currentDueDate != null && currentDueDate.equals(previousDueDate)){
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

				if(task.getIsDone()){
					addStrikeThroughToTask(strToShow, str, doc);
					strToShow = "";
				}
				else{
					strToShow = strToShow + str;
				}
			}
			else {
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
				
				if(task.getIsDone()){
					addStrikeThroughToTask(strToShow2, str, doc2);
					strToShow2 = "";
				}
				else{
					strToShow2 = strToShow2 + str;
				}
			}
		}

		addStrikeThroughToTask(strToShow, null, doc);
		addStrikeThroughToTask(strToShow2, null, doc2);
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

	public void addStrikeThroughToTask(String str, String doneTaskStr, StyledDocument doc){

		Style style = doc.addStyle("strike", null);
		StyleConstants.setStrikeThrough(style, true);

		try {
			doc.insertString(doc.getLength(), str, null);
			doc.insertString(doc.getLength(), doneTaskStr, doc.getStyle("strike"));
		} catch (BadLocationException badLocationException) {
			System.err.println("Oops");
		}
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

	public void clear(JTextPane TP){
		TP.setText("");
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
