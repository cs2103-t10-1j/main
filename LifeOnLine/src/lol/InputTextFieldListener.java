package lol;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import logic.LOLControl;


public class InputTextFieldListener implements ActionListener, KeyListener {
	JTextField inputTF;
	JTextPane mainDisplayTP1;
	static StyledDocument doc1 = new DefaultStyledDocument();
	JTextPane mainDisplayTP2;
	static StyledDocument doc2 = new DefaultStyledDocument();
	JTextPane mainDisplayTP3;
	static StyledDocument doc3 = new DefaultStyledDocument();
	JTextPane label;
	JLabel progressLabel;
	int size;
	static int lengthToBeScrolledTo = 0;

	final static boolean IS_HEADER = true;
	final Timer timer;
	final JProgressBar progressBar;

	public InputTextFieldListener(JTextPane mainDisplayTP, JTextPane mainDisplayTP2, JTextPane mainDisplayTP3, JTextPane label, JTextField inputTF, int size, Timer timer, JLabel progressLabel, JProgressBar progressBar){
		this.inputTF = inputTF;

		// Welcome to LifeOnLine
		label.setFont(Constants.TREBUCHET_BOLD_16);

		// Tasks with no date
		mainDisplayTP2.setFont(Constants.TREBUCHET_16);

		// Upcoming tasks
		mainDisplayTP.setFont(Constants.TREBUCHET_16);

		// Overdue tasks
		mainDisplayTP3.setFont(Constants.TREBUCHET_16);

		inputTF.setFont(Constants.TREBUCHET_BOLD_16);

		this.mainDisplayTP1 = mainDisplayTP;
		this.mainDisplayTP1.setDocument(doc1);
		addStyleToDoc(doc1);
		this.mainDisplayTP2 = mainDisplayTP2;
		this.mainDisplayTP2.setDocument(doc2);
		addStyleToDoc(doc2);
		this.mainDisplayTP3 = mainDisplayTP3;
		this.mainDisplayTP3.setDocument(doc3);
		addStyleToDoc(doc3);

		this.progressBar = progressBar;
		this.timer = timer;
		this.label = label;
		this.progressLabel = progressLabel;
		this.size = size;
		inputTF.addKeyListener(this);
	}

	/**
	 * Add the required Style to doc
	 * 
	 * @param doc
	 */
	public static void addStyleToDoc(StyledDocument doc){
		Style style = doc.addStyle(Constants.FORMAT_HEADER_FLOATING, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Constants.DARK_BLUE);

		style = doc.addStyle(Constants.FORMAT_HEADER_NORMAL, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.BLUE);

		style = doc.addStyle(Constants.FORMAT_HEADER_UPCOMING, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Constants.DARK_BLUE);

		style = doc.addStyle(Constants.FORMAT_HEADER_DATE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Constants.MEDIUM_BLUE);

		style = doc.addStyle(Constants.FORMAT_NUMBER, null);
		StyleConstants.setBold(style, true);
		StyleConstants.setItalic(style, true);
		StyleConstants.setForeground(style, Color.BLACK);

		style = doc.addStyle(Constants.FORMAT_TICK, null);
		StyleConstants.setForeground(style, Constants.BG);

		style = doc.addStyle(Constants.FORMAT_DESCRIPTION, null);
		StyleConstants.setFontSize(style, 16);

		style = doc.addStyle(Constants.FORMAT_TIME, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, Constants.DARK_ORANGE);

		style = doc.addStyle(Constants.FORMAT_LOCATION, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, Constants.PURPLE);

		style = doc.addStyle(Constants.FORMAT_HEADER_OVERDUE, null);
		StyleConstants.setFontSize(style, 18);
		StyleConstants.setBold(style, true);
		StyleConstants.setForeground(style, Color.RED);

		style = doc.addStyle(Constants.FORMAT_DONE, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setForeground(style, Color.GRAY);

		style = doc.addStyle(Constants.FORMAT_IS_JUST_ADDED, null);
		StyleConstants.setFontSize(style, 16);
		StyleConstants.setBackground(style, Color.YELLOW);
	}

	@Override
	public void actionPerformed(ActionEvent event){
		String inputStr = inputTF.getText();
		LOLGui.commands.add(inputStr);

		try {
			refreshFeedbackDisplay(inputStr);
		} catch (Exception e) {
			// do nothing
		}

		TaskList<Task> taskList = LOLControl.getTaskList();
		refreshMainDisplay(taskList);

		timer.setInitialDelay(60000);
		timer.restart();

		clear(inputTF);
		size = LOLGui.commands.size();
	}

	public void refreshFeedbackDisplay(String inputStr) throws Exception{
		String feedback = passStringToControlAndGetFeedback(inputStr);
		label.setText(feedback);
	}

	public String passStringToControlAndGetFeedback(String inputStr) throws Exception{
		return LOLControl.executeUserInput(inputStr);
	}

	public void refreshMainDisplay(TaskList<Task> taskList){
		resetScrollPanePosition();
		clear(mainDisplayTP1);
		clear(mainDisplayTP2);
		clear(mainDisplayTP3);

		LOLControl.refreshProgress();
		if(LOLControl.progressMaximum>0){
			progressLabel.setText("Today's report: "+LOLControl.progress+"/"+LOLControl.progressMaximum);
			progressBar.setMaximum(LOLControl.progressMaximum);
			progressBar.setValue(LOLControl.progressMaximum);
			progressBar.setValue(LOLControl.progress);
		}
		else{
			progressBar.setMaximum(1);
			progressBar.setValue(1);
			progressLabel.setText("No deadlines today");
		}

		showInMainDisplayTP(taskList);

		if(LOLControl.isAlertMode){
			Task alertTask = LOLControl.refreshAlert();
			if(alertTask!=null)
				JOptionPane.showMessageDialog(null, alertMessage(alertTask),
						"LOL Alert", JOptionPane.WARNING_MESSAGE);
			if(LOLControl.userEmail!=null && LOLControl.userEmail.length()>=11)
				LOLEmail.send(LOLControl.userEmail, alertMessage(alertTask));
		}
	}

	private String alertMessage(Task alertTask) {

		String message="YOU HAVE AN UPCOMING TASK";
		message+="\n"+alertTask.getTaskDescription();
		message+="\n Time: "+alertTask.getStartTime();
		if(alertTask.getEndTime()!=null)
			message+="-"+alertTask.getEndTime();
		if(alertTask.getTaskLocation()!=null)
			message+="\n Location: "+ alertTask.getTaskLocation();
		return message;
	}

	public void showInMainDisplayTP(TaskList<Task> taskList){
		FormatToString formatToString = new FormatToString();
		formatToString.format(taskList);
		
		addToDisplay(doc1, doc2, doc3);
	}

	public void addToDisplay(StyledDocument doc1, StyledDocument doc2, StyledDocument doc3){
		try {
			for(int j = 1; j <= FormatToString.getLinkedListNum(); j++){
				LinkedList<StringWithFormat> strToShow = FormatToString.getLinkedList(j);

				for(int i = 0; i < strToShow.size(); i++){
					if(j==1){
						if(strToShow.get(i).getIsJustAdded()){
							mainDisplayTP1.setCaretPosition(doc1.getLength());
						}
						doc1.insertString(doc1.getLength(), strToShow.get(i).getString(), doc1.getStyle(strToShow.get(i).getFormat()));
					}
					else if(j==2){
						if(strToShow.get(i).getIsJustAdded()){
							mainDisplayTP2.setCaretPosition(doc2.getLength());
						}
						doc2.insertString(doc2.getLength(), strToShow.get(i).getString(), doc2.getStyle(strToShow.get(i).getFormat()));
					}
					else if(j==3){
						if(strToShow.get(i).getIsJustAdded()){
							mainDisplayTP3.setCaretPosition(doc3.getLength());
						}
						doc3.insertString(doc3.getLength(), strToShow.get(i).getString(), doc3.getStyle(strToShow.get(i).getFormat()));
					}
				}
			}
		} 
		catch (BadLocationException badLocationException) {
			System.err.println("Oops");
		}
	}

	private void clear(JTextField TF){
		TF.setText("");
	}

	private void clear(JTextPane TP){
		TP.setText("");
	}
	
	private void resetScrollPanePosition(){
		mainDisplayTP1.setCaretPosition(0);
		mainDisplayTP2.setCaretPosition(0);
		mainDisplayTP3.setCaretPosition(0);
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

		if (e.getKeyCode() == KeyEvent.VK_UP) {
			if(!LOLGui.commands.isEmpty()){
				int index = size-1;
				if(index>=0){
					inputTF.setText(LOLGui.commands.get(index));
					inputTF.grabFocus();
					size--;
				}
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if(!LOLGui.commands.isEmpty()){
				int index =size+1;
				if(index>=0&&index<LOLGui.commands.size()){
					inputTF.setText(LOLGui.commands.get(index));
					inputTF.grabFocus();
					size++;
				}
			}
		}
	}
}
