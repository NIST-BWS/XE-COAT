package gov.nist.ixe.tests.XmlModel;

import java.util.Arrays;
import java.util.Collection;

import gov.nist.ixe.templates.ini.XmlModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.*;


@RunWith(value = Parameterized.class)
public class CleanupVariableNameTests {
	
	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "srvSrvSrvSrvSrv", "srv srv_srv-srv$srv" },
				{ "DayAtATime", "1_day_at_a_time" },
				{ "SQLServer2008", "SQLServer2008" },
				{ "abc1Def2Ghi3Jkl", "abc1def2ghi3jkl" }
		};
		return Arrays.asList(data);
	}
	
	private String expected;
	private String nameToCleanUp;
	public CleanupVariableNameTests(String expected, String nameToCleanUp) {
		this.expected = expected;
		this.nameToCleanUp = nameToCleanUp;
	}

	@Test
	public void VariableNamesAreCleanedUpCorrectly() {
		assertEquals(expected, XmlModel.cleanupVariableName(nameToCleanUp));
	}

}
