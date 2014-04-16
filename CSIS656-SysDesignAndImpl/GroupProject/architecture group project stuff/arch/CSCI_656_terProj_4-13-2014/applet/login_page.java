
package applet;

import java.applet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class login_page extends Applet implements ActionListener {
	/**
	 * Creating label, text field and button for login page
	 */
	String Uname = "";
	String Pswd ="";
	Label lUname, lPswd;
	TextField tUname, tPswd;
	Button blogin;
	String logb;
	
	public void init()
	{
		lUname = new Label("UserName:");
		lPswd = new Label("Password:");
		tUname = new TextField(20);
		tPswd = new TextField(10);
		blogin = new Button("Login");
		add(lUname);
		add(lPswd);
		add(tUname);
		add(tPswd);		
		add (blogin);
		
		blogin.addActionListener(this);
		
	}
	public void actionPerformed(ActionEvent e)
	{
		String s = e.getActionCommand();
		if (s.equals("Login"))
		{
			Uname = tUname.getText();
			Pswd = tPswd.getText();
			if (Uname != "" && Pswd!="")
			{
				/**
				 * calls server and sends the login info and server will return boolean if login pass or fail
				 */
				if (logb=="False")
				{
					System.out.println("Login Failed - Enter Valid Username and Password");
					repaint(); }
			}
			else{
			// if any of the field is null - throws error message to enter valid credentials
				System.out.println("Enter Valid Username and Password");
				repaint();}
		}
			
	}
	
}
