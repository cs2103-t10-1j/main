package lol;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class LOLGui {
	public LOLGui(){
		//line 10 to 46 create LOL's GUI
		JFrame frame = new JFrame("LOL - Life On Line");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel displayPanel = new JPanel(); 
		displayPanel.setLayout(new BorderLayout());
		JPanel feedbackPanel = new JPanel();
		feedbackPanel.setLayout(new FlowLayout());
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());
		
		JPanel feedbackAndInputPanel = new JPanel();
		feedbackAndInputPanel.setLayout(new GridLayout(2,1));
		
		final JTextArea mainDisplayTxtFld = new JTextArea(10, 20);
		mainDisplayTxtFld.setEditable(false);
		mainDisplayTxtFld.setText("Welcome to LOL!");
		JScrollPane scrollPane = new JScrollPane(mainDisplayTxtFld);
		displayPanel.add(scrollPane, BorderLayout.CENTER);
		
		feedbackPanel.add(new JLabel("Feedback: "));
		
		final JTextArea feedbackDisplayTxtFld = new JTextArea(2, 28);
		feedbackDisplayTxtFld.setEditable(false);
		feedbackDisplayTxtFld.setText("Feedback will be displayed here.");
		feedbackDisplayTxtFld.setLineWrap(true);
		feedbackDisplayTxtFld.setBorder(BorderFactory.createLineBorder(Color.gray));
		feedbackPanel.add(feedbackDisplayTxtFld);
		
		final JTextField inputTxtFld = new JTextField(35);
		inputPanel.add("South", inputTxtFld);
		
		feedbackAndInputPanel.add(feedbackPanel);
		feedbackAndInputPanel.add(inputPanel);
		
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(displayPanel, BorderLayout.CENTER);
		contentPane.add(feedbackAndInputPanel, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
			
		//actionPerformed() is the method that determines what will happen if user presses enter
		inputTxtFld.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg){
				String inputStr = inputTxtFld.getText(); //this is to get the user input text
				String displayStr = mainDisplayTxtFld.getText();
				
				if(displayStr.equals("Welcome to LOL!")){
					displayStr = "";
				}
				
				LOLMain.passStringToControl(inputStr);
				String feedback =  LOLMain.getFeedback();
				
				//this set the text of feedback
				feedbackDisplayTxtFld.setText(feedback);
				
				//this set the text of the main display text field
				TaskList list = LOLStorage.load();
				
				mainDisplayTxtFld.setText("");
				
				for(int i = 0 ; i < list.size(); i++)
				{
					mainDisplayTxtFld.append(list.get(i).toString() + "\n");
				}
				//this set the text of user input text field
				inputTxtFld.setText("");
			}
		});
	}
}

