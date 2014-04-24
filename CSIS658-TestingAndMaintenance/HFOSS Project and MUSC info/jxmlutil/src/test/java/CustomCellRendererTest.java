import jxmutil.utility.CustomCellRenderer;

import org.junit.Test;

public class CustomCellRendererTest {

	@Test
	public void testCustomCellRenderer() {
		CustomCellRenderer c = new CustomCellRenderer();
		org.junit.Assert.assertEquals(CustomCellRenderer.class, c.getClass());
	}

	@Test
	public void testGetTreeCellRendererComponent() {
		CustomCellRenderer c = new CustomCellRenderer();
		org.junit.Assert.assertEquals(CustomCellRenderer.class, c.getClass());
	} 

}
