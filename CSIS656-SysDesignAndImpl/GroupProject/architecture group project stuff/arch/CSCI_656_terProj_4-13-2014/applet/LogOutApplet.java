package applet;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LogOutApplet extends Applet implements ActionListener{

	public void init(){
		Button logOutButton = new Button("Log Out");
		logOutButton.addActionListener(this);
		add(logOutButton);
	}
	
	public void actionPerformed(ActionEvent e){
		//this function will access the server and log out
		Button checkButton = (Button)e.getSource();
		
		if(checkButton.getLabel() == "Log Out"){
			//access server and log out
		}
	}
}
