package messenger.server;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JMenuBar;
import javax.swing.JTextArea;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class View extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField userText;
	private JTextArea chatWindow;
	private JButton btnSend;
	private JMenuItem mntmLoad;
	private JMenuItem mntmSave;
	private JMenuItem mntmClear;
	private JMenuItem mntmExit;
	private JMenuItem mntmAbout;

	// Constructor

	public View() {
		super("AAST Messenger [Server]");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 500);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenu mnHistory = new JMenu("History");
		mnFile.add(mnHistory);

		mntmLoad = new JMenuItem("Load");
		mnHistory.add(mntmLoad);

		mntmSave = new JMenuItem("Save");
		mnHistory.add(mntmSave);

		mntmClear = new JMenuItem("Clear");
		mnHistory.add(mntmClear);

		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);

		chatWindow = new JTextArea();
		scrollPane.setViewportView(chatWindow);

		userText = new JTextField();
		userText.setToolTipText("");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 0, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		contentPane.add(userText, gbc_textField);
		userText.setColumns(10);

		btnSend = new JButton("Send");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		contentPane.add(btnSend, gbc_btnSend);

		userText.setEditable(false);
		chatWindow.setEditable(false);
	}

	// Overriding toString()

	@Override
	public String toString() {
		return "These are the user interface bits\n";
	}

	// Update chat window

	void showMessage(final String text) {
		SwingUtilities.invokeLater(() -> {
			chatWindow.append(text);
		});
	}

	// Get nickname

	String getNickname() {
		String nickname = JOptionPane.showInputDialog(this,
				"Please enter a nickname: ");
		if (nickname == null || nickname.isEmpty())
			return "Server";
		return nickname;
	}

	// Show history window

	void showHistory(String previousHistory) {
		JFrame historyWindow = new JFrame("History");

		historyWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		historyWindow.setBounds(200, 200, 450, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		historyWindow.setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		textArea.append(previousHistory);
		textArea.setEditable(false);

		historyWindow.setVisible(true);
	}

	// Allow user to type

	void ableToType(final boolean toggle) {
		SwingUtilities.invokeLater(() -> {
			userText.setEditable(toggle);
		});
	}

	// Get text input

	String getText() {
		return userText.getText();
	}

	// Clear text after sending

	void clearText() {
		userText.setText("");
	}

	// No history found pop-up

	void noHistoryPopup() {
		JOptionPane.showMessageDialog(this, "No history found!", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	// Open dialog with information about this application

	void about() {
		JOptionPane.showMessageDialog(this, "Created by: Khaled Elkhawaga",
				"About", JOptionPane.INFORMATION_MESSAGE);
	}

	// Add Enter button listener

	void addEnterListener(ActionListener actionListener) {
		userText.addActionListener(actionListener);
	}

	// Add Send button listener

	void addBtnListener(ActionListener actionListener) {
		btnSend.addActionListener(actionListener);
	}

	// Add Save button listener

	void addSaveHistoryListener(ActionListener actionListener) {
		mntmSave.addActionListener(actionListener);
	}

	// Add Load button listener

	void addLoadHistoryListener(ActionListener actionListener) {
		mntmLoad.addActionListener(actionListener);
	}

	// Add Clear button listener

	void addClearHistoryListener(ActionListener actionListener) {
		mntmClear.addActionListener(actionListener);
	}

	// Add Exit button listener

	void addExitListener(ActionListener actionListener) {
		mntmExit.addActionListener(actionListener);
	}

	// Add About button listener

	void addAboutListener(ActionListener actionListener) {
		mntmAbout.addActionListener(actionListener);
	}

}