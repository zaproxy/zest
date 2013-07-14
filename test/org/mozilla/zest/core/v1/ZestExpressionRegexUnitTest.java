package mozilla.zest.core.v1;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mozilla.zest.core.v1.ZestExpressionRegex;
import org.mozilla.zest.core.v1.ZestResponse;

public class ZestExpressionRegexUnitTest {
	public static final String BODY = "BODY";
	public static final String HEADER = "HEAD";

	@Test
	public void testIsLeaf() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "");
		assertTrue(regex.isLeaf());
	}

	@Test
	public void testIsInverse() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "");
		ZestExpressionRegex copy = regex.deepCopy();
		copy.setInverse(true);
		regex.setInverse(false);
		assertTrue(copy.isInverse() && !regex.isInverse());
	}

	@Test
	public void testSetInverse() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "", false);
		regex.setInverse(true);
		assertTrue(regex.isInverse());
	}

	@Test
	public void testDeepCopySameLocation() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "PING");
		ZestExpressionRegex copy = regex.deepCopy();
		assertTrue(regex.getLocation().equals(copy.getLocation()));
	}

	@Test
	public void testDeepCopySameRegex() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "PING");
		ZestExpressionRegex copy = regex.deepCopy();
		assertTrue(regex.getRegex().equals(copy.getRegex()));
	}

	@Test
	public void testDeepCopySameNoPointersRegex() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "PING");
		ZestExpressionRegex copy = regex.deepCopy();
		copy.setRegex("PONG");
		assertFalse(regex.getRegex().equals(copy.getRegex()));
	}

	@Test
	public void testDeepCopySameNoPointersLocation() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "PING");
		ZestExpressionRegex copy = regex.deepCopy();
		copy.setLocation(BODY);
		assertFalse(regex.getLocation().equals(copy.getLocation()));
	}

	@Test
	public void testIsTrueHeader() {
		ZestResponse response = new ZestResponse(null, "123456header654321",
				"987654body456789", 200, 100);
		ZestExpressionRegex regexExpr = new ZestExpressionRegex(HEADER, "head");
		assertTrue(regexExpr.isTrue(response));
	}

	@Test
	public void testIsTrueBody() {
		ZestResponse response = new ZestResponse(null, "123456header654321",
				"987654body456789", 200, 100);
		ZestExpressionRegex regexExpr = new ZestExpressionRegex(BODY, "body");
		assertTrue(regexExpr.isTrue(response));
	}

	@Test
	public void testIsTrueNullBody() {
		ZestResponse response = new ZestResponse(null, null, null, 0, 0);
		ZestExpressionRegex regexExpr = new ZestExpressionRegex(BODY, "");
		assertFalse(regexExpr.isTrue(response));
	}

	@Test
	public void testIsTrueNullHeader() {
		ZestResponse response = new ZestResponse(null, null, null, 0, 0);
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "");
		assertFalse(regex.isTrue(response));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentExcpetionNoParams() {
		@SuppressWarnings("unused")
		ZestExpressionRegex regex = new ZestExpressionRegex();
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgumentExceptionWrongParams() {
		ZestExpressionRegex regex = new ZestExpressionRegex(HEADER, "");
		regex.setLocation("My home");
		fail();
	}
}
