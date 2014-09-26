package lol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LOLGui {
	public LOLGui(){
		//line 10 to 46 create LOL's GUI
		JFrame frame = new JFrame("LOL - Life On Line");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		JPanel displayPanel = new JPanel(); 
		displayPanel.setLayout(new BorderLayout());
		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setLayout(new FlowLayout());
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		
		JPanel feedbackAndInputPanel = new JPanel();
		feedbackAndInputPanel.setLayout(new GridLayout(2,1));
		
		final JTextArea mainDisplayTxtFld = new JTextArea(5, 20);
		mainDisplayTxtFld.setEditable(false);
		mainDisplayTxtFld.setText("Welcome to LOL!");
		JScrollPane scrollPane = new JScrollPane(mainDisplayTxtFld);
		displayPanel.add(scrollPane, BorderLayout.CENTER);
		
		feedbackPanel.add("West", new JLabel("Feedback: "));
		
		final JTextField feedbackDisplayTxtFld = new JTextField();
		feedbackDisplayTxtFld.setEditable(false);
		feedbackDisplayTxtFld.setText("Feedback will be displayed here.");
		feedbackPanel.add("Center", feedbackDisplayTxtFld);
		
		final JTextField inputTxtFld = new JTextField(25);
		inputPanel.add("South", inputTxtFld);
		
		feedbackAndInputPanel.add(feedbackPanel);
		feedbackAndInputPanel.add(inputPanel);
		
		frame.add(displayPanel, BorderLayout.CENTER);
		frame.add(feedbackAndInputPanel, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
			
		//actionPerformed() is the method that determines what will happen if user presses enter
		inputTxtFld.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg){
				/*
				 * Below is just an example of what will happen when user presses enter after 
				 * typing something in the user input text field at the bottom of the GUI.
				 */
				String inputStr = inputTxtFld.getText(); //this is to get the user input text
				String displayStr = mainDisplayTxtFld.getText();
				
				if(displayStr.equals("Welcome to LOL!")){
					displayStr = "";
				}
				
				LOLMain control = new LOLMain(inputStr);
				String feedback = control.getFeedback();
				
				//this set the text of feedback
				feedbackDisplayTxtFld.setText(feedback);
				//this set the text of the main display text field
				mainDisplayTxtFld.setText(displayStr + inputStr + "\n");
				//this set the text of user input text field
				inputTxtFld.setText("");
				
			}
		});
	}
}

