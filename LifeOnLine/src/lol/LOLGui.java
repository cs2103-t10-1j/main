package lol;

import javax.swing.*;
import java.awt.*;

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
		
		final JTextArea mainDisplayTA = new JTextArea(15, 19);
		mainDisplayTA.setEditable(false);
		mainDisplayTA.setText("**Upcoming Tasks List**");
		JScrollPane scrollPane = new JScrollPane(mainDisplayTA);
		displayPanel.add(scrollPane, BorderLayout.WEST);
		
		final JTextArea mainDisplayTA2 = new JTextArea(15, 19);
		mainDisplayTA2.setEditable(false);
		mainDisplayTA2.setText("**To-Do Anytime List**");
		JScrollPane scrollPane2 = new JScrollPane(mainDisplayTA2);
		displayPanel.add(scrollPane2, BorderLayout.EAST);
		
		feedbackPanel.add(new JLabel("Feedback: "));
		
		final JTextArea feedbackDisplayTA = new JTextArea(2, 28);
		feedbackDisplayTA.setEditable(false);
		feedbackDisplayTA.setText("Feedback will be displayed here.");
		feedbackDisplayTA.setLineWrap(true);
		feedbackDisplayTA.setBorder(BorderFactory.createLineBorder(Color.red));
		feedbackPanel.add(feedbackDisplayTA);
		
		final JTextField inputTF = new JTextField(35);
		inputTF.setText("add ");
		inputPanel.add("South", inputTF);
		
		feedbackAndInputPanel.add(feedbackPanel);
		feedbackAndInputPanel.add(inputPanel);
		
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(displayPanel, BorderLayout.CENTER);
		contentPane.add(feedbackAndInputPanel, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		inputTF.addActionListener(new InputTextFieldListener(mainDisplayTA,mainDisplayTA2, feedbackDisplayTA, inputTF));
	}
}

