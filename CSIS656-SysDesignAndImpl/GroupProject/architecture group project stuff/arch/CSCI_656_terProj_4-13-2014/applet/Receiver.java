package applet;
import java.applet.Applet;
import java.awt.TextArea;

import javax.swing.JTextField;


public class Receiver extends Applet {
	int ctr=0;
	JTextField ctrLbl;
	TextArea bulletinBoard;
	
	public void init(){
		bulletinBoard = new TextArea();
		ctrLbl = new JTextField(20);
		ctrLbl.setText("hello receiver");
		this.add(ctrLbl);
		
	}

	public void incrementCounter() {
	    ctr++;
	    String text = " Current Value Of Counter: " + (new Integer(ctr)).toString();
	    ctrLbl.setText(text);
	}
}
