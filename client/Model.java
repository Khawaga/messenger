package messenger.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class Model {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String serverIP;
	private Socket connection;
	private StringBuilder history = new StringBuilder();
	private String nickname;

	// Start connection

	void startConnection() {
		try {
			connection = new Socket(InetAddress.getByName(serverIP), 1911);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Get connection

	Socket getConnection() {
		return connection;
	}

	// Set server IP

	void setServerIP(String ip) {
		serverIP = ip;
	}

	// Setup streams

	void setup() {
		try {
			output = new ObjectOutputStream(connection.getOutputStream());
			input = new ObjectInputStream(connection.getInputStream());
			output.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Read input

	String readInput() throws ClassNotFoundException, IOException {
		String ip = (String) input.readObject();
		history.append(ip);
		history.append(System.getProperty("line.separator"));
		return ip;
	}

	// Write message

	void writeMessage(String message) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));
		String op = "[" + sdf.format(new Date()) + "] " + nickname + ": "
				+ message;
		output.writeObject(op);
		output.flush();
		history.append(op);
		history.append(System.getProperty("line.separator"));
	}

	// Save history

	void saveHistory() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("history.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(history);
		writer.close();
	}

	// Read history

	String readHistory() throws FileNotFoundException {
		StringBuilder previousHistory = new StringBuilder();
		File historyFile = new File("history.txt");
		Scanner fileReader = null;
		fileReader = new Scanner(historyFile);
		while (fileReader.hasNextLine()) {
			previousHistory.append(fileReader.nextLine());
			previousHistory.append(System.getProperty("line.separator"));

		}
		fileReader.close();
		return previousHistory.toString();
	}

	// Close streams and sockets

	void closeStreamsSockets() {
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	// Set nickname

	void setNickname(String nickname) {
		this.nickname = nickname;
	}

	// Get nickname

	String getNickname() {
		return nickname;
	}

	// Deletes history file

	void deleteHistory() {
		File historyFile = new File("history.txt");
		try {
			Files.deleteIfExists(historyFile.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
