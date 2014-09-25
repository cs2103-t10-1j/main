package lol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LOLGui {
	public LOLGui(){
		JFrame frame = new JFrame("LOL - Life On Line");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		final JTextField mainDisplayTxtFld = new JTextField();
		mainDisplayTxtFld.setEditable(false);
		mainDisplayTxtFld.setText("Welcome to LOL!");
		panel.add("North", mainDisplayTxtFld);
		
		panel.add("West", new JLabel("Feedback: "));
		
		final JTextField feedbackDisplayTxtFld = new JTextField();
		feedbackDisplayTxtFld.setEditable(false);
		feedbackDisplayTxtFld.setText("Feedback will be displayed here.");
		panel.add("Center", feedbackDisplayTxtFld);
		
		final JTextField inputTxtFld = new JTextField(50);
		panel.add("South", inputTxtFld);
		
		inputTxtFld.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg){
				String inputStr = inputTxtFld.getText();
				
				feedbackDisplayTxtFld.setText("Read input --> "+ inputStr);
				mainDisplayTxtFld.setText(inputStr);
				inputTxtFld.setText("");
			}
		});
		
		frame.add(panel);
		
		frame.pack();
		frame.setVisible(true);
	}
}

