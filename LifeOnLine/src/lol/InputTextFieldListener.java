package lol;

import java.awt.Color;
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

	final boolean isHeader = true;

	final String FORMAT_HEADER_OVERDUE = "overdue header";
	final String FORMAT_HEADER_FLOATING = "floating header";
	final String FORMAT_HEADER_NORMAL = "normal header";
	final String FORMAT_TIME = "time";
	final String FORMAT_DESCRIPTION = "description";
	final String FORMAT_LOCATION = "location";
	final String FORMAT_OVERDUE = "overdue";
	final String FORMAT_TIME_STRIKE = "time strike";
	final String FORMAT_DESCRIPTION_STRIKE = "description strike";
	final String FORMAT_LOCATION_STRIKE = "location strike";
	final String FORMAT_OVERDUE_STRIKE = "overdue strike";

	public InputTextFieldListener(JTextPane mainDisplayTP,JTextPane mainDisplayTP2, JTextArea feedbackDisplayTA, JTextField inputTF, Integer i){
		this.inputTF = inputTF;

		this.mainDisplayTP = mainDisplayTP;
		this.mainDisplayTP.setDocument(doc);
		this.mainDisplayTP2 = mainDisplayTP2;
		this.mainDisplayTP2.setDocument(doc2);

		this.feedbackDisplayTA = feedbackDisplayTA;
		this.i = i;
		inputTF.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent event){ 

		String inputStr = inputTF.getText();
		refreshFeedbackDisplay(inputStr);

		TaskList<Task> taskList = LOLControl.getTaskList();
		refreshMainDisplay(taskList);

		clear(inputTF);
		i = new Integer(0);
	}

	public void refreshFeedbackDisplay(String inputStr){
		String feedback = passStringToControlAndGetFeedback(inputStr);
		feedbackDisplayTA.setText(feedback);
	}

	public String passStringToControlAndGetFeedback(String inputStr){
		return LOLControl.executeUserInput(inputStr);
	}

	public void refreshMainDisplay(TaskList<Task> taskList){
		clear(mainDisplayTP);
		clear(mainDisplayTP2);

		showInMainDisplayTA(taskList);
	}

	public void showInMainDisplayTA(TaskList<Task> taskList){
		Date previousDueDate = new Date(-1, -1, -9999, null);

		FormatToString taskToFormat = new FormatToString();
		//below two lines should be removed and added to another class
		FormatToString.strToShow.clear(); 
		FormatToString.hasOverdueHeader = false;
		
		for(int i = 0; i < taskList.size(); i++){
			Task task = taskList.get(i);

			Date currentDueDate = task.getTaskDueDate();

			assert (currentDueDate.getDay() == -1 && currentDueDate.getMonth() == -1 
					&& currentDueDate.getYear4Digit() == -9999) : "impossible date entered";

			//add header
			if(task.getIsOverdue()){
				taskToFormat.format(isHeader, task, i);
			}
			else if(currentDueDate != null && !currentDueDate.equals(previousDueDate)){
				previousDueDate = currentDueDate;

				taskToFormat.format(isHeader, task, i);
			}
			else if(currentDueDate == null){
				taskToFormat.format(isHeader, task, i);
			}

			taskToFormat.format(!isHeader, task, i);
		}
		
		addToDisplay(doc);
	}

	public void addToDisplay(StyledDocument doc){
		Style style = doc.addStyle(FORMAT_HEADER_FLOATING, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.BLUE);

		style = doc.addStyle(FORMAT_HEADER_NORMAL, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.BLUE);

		style = doc.addStyle(FORMAT_HEADER_OVERDUE, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.RED);

		style = doc.addStyle(FORMAT_DESCRIPTION, null);

		style = doc.addStyle(FORMAT_TIME, null);
		StyleConstants.setForeground(style, Color.ORANGE);

		style = doc.addStyle(FORMAT_LOCATION, null);
		StyleConstants.setForeground(style, Color.YELLOW);
		
		style = doc.addStyle(FORMAT_HEADER_OVERDUE, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.RED);
		
		style = doc.addStyle(FORMAT_TIME_STRIKE, null);
		StyleConstants.setForeground(style, Color.ORANGE);
		StyleConstants.setStrikeThrough(style, true);
		
		style = doc.addStyle(FORMAT_DESCRIPTION_STRIKE, null);
		StyleConstants.setStrikeThrough(style, true);
		
		style = doc.addStyle(FORMAT_LOCATION_STRIKE, null);
		StyleConstants.setForeground(style, Color.YELLOW);
		StyleConstants.setStrikeThrough(style, true);

		style = doc.addStyle(FORMAT_OVERDUE_STRIKE, null);
		StyleConstants.setForeground(style, Color.RED);
		StyleConstants.setStrikeThrough(style, true);

		try {
			for(int i = 0; i < FormatToString.strToShow.size(); i++){
				doc.insertString(doc.getLength(), FormatToString.strToShow.get(i).getString() 
						, doc.getStyle(FormatToString.strToShow.get(i).getFormat()));
			}

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
