package lol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LOLGui {
	final Integer i = new Integer(0);
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
		
		//a panel for mainDisplayTP and mainDisplayTP2
		JPanel mainDisplayPanel = new JPanel();
		mainDisplayPanel.setLayout(new GridLayout(1, 2));
		
		JPanel feedbackAndInputPanel = new JPanel();
		feedbackAndInputPanel.setLayout(new GridLayout(2,1));
		
		final JTextPane mainDisplayTP = new JTextPane();
		mainDisplayTP.setEditable(false);
		mainDisplayTP.setText("**Upcoming Tasks List**");
		JScrollPane scrollPane = new JScrollPane(mainDisplayTP);
		mainDisplayPanel.add(scrollPane);
		
		final JTextPane mainDisplayTP2 = new JTextPane();
		mainDisplayTP2.setEditable(false);
		mainDisplayTP2.setText("**To-Do Anytime List**");
		JScrollPane scrollPane2 = new JScrollPane(mainDisplayTP2);
		mainDisplayPanel.add(scrollPane2);
		
		displayPanel.add(mainDisplayPanel, BorderLayout.CENTER);
		
		feedbackPanel.add(new JLabel("Feedback: "));
		
		final JTextArea feedbackDisplayTA = new JTextArea(2, 28);
		feedbackDisplayTA.setEditable(false);
		feedbackDisplayTA.setText("Feedback will be displayed here.");
		feedbackDisplayTA.setLineWrap(true);
		feedbackDisplayTA.setBorder(BorderFactory.createLineBorder(Color.red));
		feedbackPanel.add(feedbackDisplayTA);
		
		
		final JTextField inputTF = new JTextField(35);
		inputPanel.add("South", inputTF);
		
		feedbackAndInputPanel.add(feedbackPanel);
		feedbackAndInputPanel.add(inputPanel);
		
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(displayPanel, BorderLayout.CENTER);
		contentPane.add(feedbackAndInputPanel, BorderLayout.SOUTH);
		
		frame.setSize(new Dimension(500, 500));
		//frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		frame.addWindowListener( new WindowAdapter() {
		    public void windowOpened( WindowEvent e ){
		        inputTF.requestFocus();
		    }
		});
	
		inputTF.addActionListener(new InputTextFieldListener(mainDisplayTP,mainDisplayTP2, feedbackDisplayTA, inputTF, i));	
	}  
}


