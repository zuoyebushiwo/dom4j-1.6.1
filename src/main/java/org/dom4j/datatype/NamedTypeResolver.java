/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */
package org.dom4j.datatype;

import com.sun.msv.datatype.xsd.XSDatatype;
import org.dom4j.DefaultDocumentFactory;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <code>NamedTypeResolver</code> resolves named types for a given QName.
 * </p>
 *
 * @author Yuxin Ruan
 * @version $Revision: 1.8 $
 */
class NamedTypeResolver {

	protected Map<QName, DocumentFactory> complexTypeMap = new HashMap<QName, DocumentFactory>();
	protected Map<QName, XSDatatype> simpleTypeMap = new HashMap<QName, XSDatatype>();
	protected Map<Element, QName> typedElementMap = new HashMap<Element, QName>();
	protected Map<Element, DocumentFactory> elementFactoryMap = new HashMap<Element, DocumentFactory>();
	protected DocumentFactory documentFactory;

	NamedTypeResolver(DocumentFactory documentFactory) {
		this.documentFactory = documentFactory;
	}

	void registerComplexType(QName type, DefaultDocumentFactory factory) {
		complexTypeMap.put(type, factory);
	}

	void registerSimpleType(QName type, XSDatatype datatype) {
		simpleTypeMap.put(type, datatype);
	}

	void registerTypedElement(Element element, QName type,
	                          DefaultDocumentFactory parentFactory) {
		typedElementMap.put(element, type);
		elementFactoryMap.put(element, parentFactory);
	}

	void resolveElementTypes() {
		for (Map.Entry<Element, QName> entry : typedElementMap.entrySet()) {
			Element element = entry.getKey();
			QName elementQName = getQNameOfSchemaElement(element);
			QName type = entry.getValue();

			if (complexTypeMap.containsKey(type)) {
				DocumentFactory factory = complexTypeMap.get(type);
				elementQName.setDocumentFactory(factory);
			} else if (simpleTypeMap.containsKey(type)) {
				XSDatatype datatype = (XSDatatype) simpleTypeMap.get(type);
				DocumentFactory factory = elementFactoryMap.get(element);

				if (factory instanceof DatatypeElementFactory) {
					((DatatypeElementFactory) factory).setChildElementXSDatatype(elementQName, datatype);
				}
			}
		}
	}

	void resolveNamedTypes() {
		resolveElementTypes();
	}

	private QName getQNameOfSchemaElement(Element element) {
		String name = element.attributeValue("name");

		return getQName(name);
	}

	private QName getQName(String name) {
		return documentFactory.createQName(name);
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
