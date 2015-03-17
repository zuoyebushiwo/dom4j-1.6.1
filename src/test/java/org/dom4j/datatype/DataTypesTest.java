/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.datatype;

import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

/**
 * Test harness to test the various data types supported in the XML Schema Data
 * Type integration.
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class DataTypesTest extends AbstractDataTypeTestCase {
	// Test case(s)
	// -------------------------------------------------------------------------

	@Test
	public void testgMonthDay() throws Exception {
		testNodes("//gMonthDayTag", Calendar.class);
	}

	@Test
	public void testgDay() throws Exception {
		testNodes("//gDayTag", Calendar.class);
	}

	@Test
	public void testgMonth() throws Exception {
		testNodes("//gMonthTag", Calendar.class);
	}

	@Test
	public void testDate() throws Exception {
		testNodes("//dateTag", Calendar.class);
	}

	@Test
	public void testTime() throws Exception {
		testNodes("//timeTag", Calendar.class);
	}

	@Test
	public void testDateTime() throws Exception {
		testNodes("//dateTimeTag", Calendar.class);
	}

	@Test
	public void testgYearMonth() throws Exception {
		testNodes("//gYearMonthTag", Calendar.class);
	}

	@Test
	public void testgYear() throws Exception {
		testNodes("//gYearTag", Calendar.class);
	}

	@Test
	public void testBoolean() throws Exception {
		testNodes("//booleanTag", Boolean.class);
	}

	@Test
	public void testBase64Binary() throws Exception {
		testNodes("//base64BinaryTag", byte[].class);
	}

	@Test
	public void testHexBinary() throws Exception {
		testNodes("//hexBinaryTag", byte[].class);
	}

	// Number types

	@Test
	public void testFloat() throws Exception {
		testNodes("//floatTag", Float.class);
	}

	@Test
	public void testDouble() throws Exception {
		testNodes("//doubleTag", Double.class);
	}

	@Test
	public void testDecimal() throws Exception {
		testNodes("//decimalTag", BigDecimal.class);
	}

	@Test
	public void testInteger() throws Exception {
		testNodes("//integerTag", BigInteger.class);
	}

	@Test
	public void testNonPositiveInteger() throws Exception {
		testNodes("//nonPositiveIntegerTag", BigInteger.class);
	}

	@Test
	public void testNegativeInteger() throws Exception {
		testNodes("//negativeIntegerTag", BigInteger.class);
	}

	@Test
	public void testLong() throws Exception {
		testNodes("//longTag", Long.class);
	}

	@Test
	public void testInt() throws Exception {
		testNodes("//intTag", Integer.class);
	}

	@Test
	public void testShort() throws Exception {
		testNodes("//shortTag", Short.class);
	}

	@Test
	public void testByte() throws Exception {
		testNodes("//byteTag", Byte.class);
	}

	@Test
	public void testNonNegativeInteger() throws Exception {
		testNodes("//nonNegativeIntegerTag", BigInteger.class);
	}

	@Test
	public void testUnsignedLong() throws Exception {
		testNodes("//unsignedLongTag", BigInteger.class);
	}

	@Test
	public void testUnsignedInt() throws Exception {
		testNodes("//unsignedIntTag", Long.class);
	}

	@Test
	public void testUnsignedShort() throws Exception {
		testNodes("//unsignedShortTag", Integer.class);
	}

	@Test
	public void testUnsignedByte() throws Exception {
		testNodes("//unsignedByteTag", Short.class);
	}

	@Test
	public void testPositiveInteger() throws Exception {
		testNodes("//positiveIntegerTag", BigInteger.class);
	}

	// Implementation methods
	// -------------------------------------------------------------------------

	@BeforeMethod
	protected void setUp() throws Exception {
		super.setUp();

		DocumentFactory factory = DatatypeDocumentFactory.getInstance();
		SAXReader reader = new SAXReader(factory);
		document = getDocument("/src/test/xml/test/schema/test.xml", reader);
	}
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://dom4j.sourceforge.net
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
