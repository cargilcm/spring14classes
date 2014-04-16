package applet;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RefreshApplet extends Applet implements ActionListener{

	public void init(){
		Button refreshButton = new Button("Refresh User List");
		refreshButton.addActionListener(this);
		add(refreshButton);
	}
	
	public void actionPerformed(ActionEvent e){
		//this function will access the server and retrieve user list
		Button checkButton = (Button)e.getSource();
		
		if(checkButton.getLabel() == "Refresh User List"){
			//access server and retrieve user list
		}
	}
}
