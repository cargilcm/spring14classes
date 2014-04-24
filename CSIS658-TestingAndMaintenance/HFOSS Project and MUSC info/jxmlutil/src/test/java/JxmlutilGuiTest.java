import javax.swing.UIManager;

import jxmutil.gui.JxmlutilGui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JxmlutilGuiTest {

	JxmlutilGui mainGUI;
	String s;
	
	@Before
	public void setUp() throws Exception {
		s = UIManager.getSystemLookAndFeelClassName();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test(expected = Exception.class)
	public void testJxmlutilGui() {
		mainGUI = new JxmlutilGui();
	}

}
