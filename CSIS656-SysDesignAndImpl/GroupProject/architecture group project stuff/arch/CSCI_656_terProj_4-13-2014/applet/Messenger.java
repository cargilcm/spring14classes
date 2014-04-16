package applet;

import java.applet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Messenger extends Applet implements ActionListener{
	/**
	 * Enables User to type in text and click send
	 */
	TextArea tMesseng;
	Button bSend;
	public void init()
	{
		tMesseng =new TextArea(5,20);
		bSend = new Button("Send");
		add(tMesseng);
		add(bSend);
		bSend.addActionListener(this);
	}
	public void actionPerformed(ActionEvent e)
	{
		String at = e.getActionCommand();
		if (at.equals("Send"))
		{
			
		}
		
	}

}
