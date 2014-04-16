package applet;

import java.applet.Applet;
import java.awt.TextArea;
import java.util.ArrayList;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;

/**
 * Enables user to fetch (or subscribe?) to display messages posted to the
 * system and exchanged among chat peers
 * @author Chris (claimed >= Rev26)
 */
public class MessageBoard extends Applet {
	
	TextArea bulletinBoard;
	ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	
	public MessageBoard() {
		bulletinBoard =new TextArea(5,20);
	}

	void getMessages(){
		try {
		    JSObject window = JSObject.getWindow(this);
		    window.eval("displayMessages('nice work!')");
		} catch (JSException jse) {
		}
	}
	
	//inner class
	private class ChatMessage{
		//variables
		String u;
		String mesg;
		ChatMessage(String user,String message){
			this.u = user;
			this.mesg=message;
		}
		
	}
}
