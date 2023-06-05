package mock;

import javax.swing.DefaultListModel;

import processing.Entry;

public class MongoEntryMock {
	
	String email;
	String password;
	DefaultListModel<Entry> entries;
	
	MongoEntryMock(String email, String pass) {
		this.email = email;
		this.password = pass;
		this.entries = new DefaultListModel<>();
	}
	
	public void addEntry(Entry entry) {
		entries.addElement(entry);
	}

}
