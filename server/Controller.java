package messenger.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Controller {

	private final View view;
	private final Model model;

	// Constructor

	public Controller(View view, Model model) {
		this.view = view;
		this.model = model;
		this.view.addEnterListener(new enterListener());
		this.view.addBtnListener(new btnListener());
		this.view.addSaveHistoryListener(new saveHistoryListener());
		this.view.addLoadHistoryListener(new loadHistoryListener());
		this.view.addClearHistoryListener(new clearHistoryListener());
		this.view.addExitListener(new exitListener());
		this.view.addAboutListener(new aboutListener());
	}

	// Overriding toString()

	@Override
	public String toString() {
		return "This is the controller class.";
	}

	// Run server

	public void startRunning() {
		model.setNickname(view.getNickname());
		try {
			model.newServer();
			while (true) {
				try {
					awaitConnection();
					setupStreams();
					whileChatting();
				} catch (EOFException eofException) {
					view.showMessage("\nServer connection terminated.");
				} finally {
					closeServer();
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Wait for connection and display connection information

	private void awaitConnection() throws IOException {
		view.showMessage("Waiting for someone to connect...");
		model.acceptConnection();
		Socket connection = model.getConnection();
		view.showMessage("\nConnected to "
				+ connection.getInetAddress().getHostName());
	}

	// Initialize streams

	private void setupStreams() throws IOException {
		model.setup();
		view.showMessage("\nSetup successful!\n");
	}

	// During conversation

	private void whileChatting() throws IOException {
		String message;
		view.ableToType(true);
		do {
			try {
				message = model.readInput();
				view.showMessage("\n" + message);
			} catch (ClassNotFoundException classNotFoundException) {
				view.showMessage("\nInvalid input.");
			}
		} while (true);
	}

	// Send message to client

	private void sendMessage(String message) {
		try {
			model.writeMessage(message);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			sdf.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));
			view.showMessage("\n" + "[" + sdf.format(new Date()) + "] "
					+ model.getNickname() + " (Host): " + message);
		} catch (IOException ioException) {
			view.showMessage("\nSending failed.");
		}
	}

	// Close streams and sockets

	private void closeServer() {
		view.showMessage("\n\nConnection lost.\n");
		view.ableToType(false);
		model.closeStreamsSockets();
	}

	// Listen for Enter keyboard button click

	private class enterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			sendMessage(event.getActionCommand());
			view.clearText();
		}
	}

	// Listen for Send button click

	private class btnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			sendMessage(view.getText());
			view.clearText();
		}
	}

	// Listen for Load menu item button click

	private class loadHistoryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				view.showHistory(model.readHistory());
			} catch (FileNotFoundException e) {
				view.noHistoryPopup();
			}
		}
	}

	// Listen for Save menu item button click

	private class saveHistoryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			model.saveHistory();
		}
	}

	// Listen for Exit menu item button click

	private class exitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}

	// Listen for About menu item button click

	private class aboutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			view.about();
		}
	}

	// Listen for Clear menu item button click

	private class clearHistoryListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			model.deleteHistory();
		}
	}

}
