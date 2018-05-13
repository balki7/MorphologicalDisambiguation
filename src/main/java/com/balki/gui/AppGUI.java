package com.balki.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import com.balki.App;

public class AppGUI extends JFrame {
	private static final long serialVersionUID = 6287474161743889260L;

	private JButton btnAnalyze = new JButton("Analyze");
	private JTextField inputText = new JTextField();
	private JLabel labelInput = new JLabel("Sentence");
	private JTextArea rootResultText = new JTextArea();
	private JTextArea igResultText = new JTextArea();
	private JTextArea reportText = new JTextArea();

	public AppGUI() {
		setTitle("Morphological Disambiguation");
		setSize(500, 500);
		setLocation(new Point(500, 500));
		setLayout(new GridLayout(2, 1));
		setResizable(false);

		initComponent();
	}

	private void initComponent() {
		JPanel firstPanel = new JPanel(new BorderLayout(2, 1));
		JPanel inputPanel = new JPanel(new BorderLayout(3, 1));
		inputPanel.add(labelInput, BorderLayout.NORTH);
		inputPanel.add(inputText, BorderLayout.CENTER);
		inputPanel.add(btnAnalyze, BorderLayout.SOUTH);
		inputText.setEditable(false);
		btnAnalyze.setEnabled(false);
		firstPanel.add(inputPanel, BorderLayout.NORTH);
		
		JPanel resultPanel = new JPanel(new GridLayout(1, 2));
		rootResultText.setEditable(false);
		resultPanel.add(rootResultText);
		
		igResultText.setEditable(false);
		resultPanel.add(igResultText);
		firstPanel.add(resultPanel, BorderLayout.CENTER);
		
		add(firstPanel);

		JPanel reportPanel = new JPanel(new BorderLayout(1, 1));
		DefaultCaret caret = (DefaultCaret) reportText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		reportText.setEditable(false);
		JScrollPane scroll = new JScrollPane(reportText);
		reportPanel.add(scroll);
		reportText.setBackground(Color.LIGHT_GRAY);
		add(reportPanel);
	}

	private void initEvent() {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});

		btnAnalyze.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnTutupClick(e);
			}
		});
	}

	private void btnTutupClick(ActionEvent evt) {
		String sentence = inputText.getText();
		rootResultText.setText("");
		igResultText.setText("");
		inputText.setText("");
		App.analyze(sentence);
	}

	public void appendReport(String text) {
		reportText.append(text + "\n");
		reportText.setCaretPosition(reportText.getDocument().getLength());
	}
	
	public void appendRootResult(String text) {
		rootResultText.append(text + "\n");
	}
	
	public void appendIGResult(String text) {
		igResultText.append(text + "\n");
	}

	public void ready() {
		inputText.setText("Enter a sentence here");
		inputText.setEditable(true);
		btnAnalyze.setEnabled(true);
		
		initEvent();
	}
}
