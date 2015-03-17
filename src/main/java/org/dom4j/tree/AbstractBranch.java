/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */
package org.dom4j.tree;

import org.dom4j.*;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <p>
 * <code>AbstractBranch</code> is an abstract base class for tree implementors
 * to use for implementation inheritence.
 * </p>
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.44 $
 */
public abstract class AbstractBranch extends AbstractNode implements Branch {

	protected static final int DEFAULT_CONTENT_LIST_SIZE = 5;

	public AbstractBranch() {
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}

	@Override
	public boolean hasContent() {
		return nodeCount() > 0;
	}

	public List<Node> content() {
		List<Node> backingList = contentList();
		return new ContentListFacade<Node>(this, backingList);
	}

	@Override
	public String getText() {
		List<Node> content = contentList();

		if (content != null) {
			int size = content.size();

			if (size >= 1) {
				Node first = content.get(0);
				String firstText = getContentAsText(first);

				if (size == 1) {
					// optimised to avoid StringBuilder creation
					return firstText;
				} else {
					StringBuilder buffer = new StringBuilder(firstText);

					for (int i = 1; i < size; i++) {
						Node node = content.get(i);
						buffer.append(getContentAsText(node));
					}

					return buffer.toString();
				}
			}
		}

		return "";
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param content DOCUMENT ME!
	 * @return the text value of the given content object as text which returns
	 *         the text value of CDATA, Entity or Text nodes
	 */
	protected String getContentAsText(Node node) {
		switch (node.getNodeTypeEnum()) {
			case CDATA_SECTION_NODE:
				// case ENTITY_NODE:
			case ENTITY_REFERENCE_NODE:
			case TEXT_NODE:
				return node.getText();

			default:
				return "";
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param content DOCUMENT ME!
	 * @return the XPath defined string-value of the given content object
	 */
	protected String getContentAsStringValue(Node node) {
		switch (node.getNodeTypeEnum()) {
			case CDATA_SECTION_NODE:
				// case ENTITY_NODE:
			case ENTITY_REFERENCE_NODE:
			case TEXT_NODE:
			case ELEMENT_NODE:
				return node.getStringValue();
			default:
				return "";
		}
	}

	public String getTextTrim() {
		String text = getText();

		StringBuilder textContent = new StringBuilder();
		StringTokenizer tokenizer = new StringTokenizer(text);

		while (tokenizer.hasMoreTokens()) {
			String str = tokenizer.nextToken();
			textContent.append(str);

			if (tokenizer.hasMoreTokens()) {
				textContent.append(" "); // separator
			}
		}

		return textContent.toString();
	}

	public void setProcessingInstructions(List<ProcessingInstruction> listOfPIs) {
		for (Iterator iter = listOfPIs.iterator(); iter.hasNext();) {
			ProcessingInstruction pi = (ProcessingInstruction) iter.next();
			addNode(pi);
		}
	}

	public Element addElement(String name) {
		Element node = getDocumentFactory().createElement(name);
		add(node);

		return node;
	}

	public Element addElement(String qualifiedName, String namespaceURI) {
		Element node = getDocumentFactory().createElement(qualifiedName,
				namespaceURI);
		add(node);

		return node;
	}

	public Element addElement(QName qname) {
		Element node = getDocumentFactory().createElement(qname);
		add(node);

		return node;
	}

	public Element addElement(String name, String prefix, String uri) {
		Namespace namespace = Namespace.get(prefix, uri);
		QName qName = getDocumentFactory().createQName(name, namespace);

		return addElement(qName);
	}

	// polymorphic node methods

	public void add(Node node) {
		switch (node.getNodeTypeEnum()) {
			case ELEMENT_NODE:
				add((Element) node);
				break;
			case COMMENT_NODE:
				add((Comment) node);
				break;
			case PROCESSING_INSTRUCTION_NODE:
				add((ProcessingInstruction) node);
				break;
			default:
				invalidNodeTypeAddException(node);
		}
	}

	public boolean remove(Node node) {
		switch (node.getNodeTypeEnum()) {
			case ELEMENT_NODE:
				return remove((Element) node);
			case COMMENT_NODE:
				return remove((Comment) node);
			case PROCESSING_INSTRUCTION_NODE:
				return remove((ProcessingInstruction) node);
			default:
				invalidNodeTypeAddException(node);
				return false;
		}
	}

	// typesafe versions using node classes

	public void add(Comment comment) {
		addNode(comment);
	}

	public void add(Element element) {
		addNode(element);
	}

	public void add(ProcessingInstruction pi) {
		addNode(pi);
	}

	public boolean remove(Comment comment) {
		return removeNode(comment);
	}

	public boolean remove(Element element) {
		return removeNode(element);
	}

	public boolean remove(ProcessingInstruction pi) {
		return removeNode(pi);
	}

	public Element elementByID(String elementID) {
		//TODO
		for (int i = 0, size = nodeCount(); i < size; i++) {
			Node node = node(i);

			if (node instanceof Element) {
				Element element = (Element) node;
				String id = elementID(element);

				if ((id != null) && id.equals(elementID)) {
					return element;
				} else {
					element = element.elementByID(elementID);

					if (element != null) {
						return element;
					}
				}
			}
		}

		return null;
	}

	public void appendContent(Branch branch) {
		for (Node node : branch) {
			add((Node) node.clone());
		}
	}

	public Node node(int index) {
		Node node = contentList().get(index);
		return node;
	}

	public int nodeCount() {
		return contentList().size();
	}

	public int indexOf(Node node) {
		return contentList().indexOf(node);
	}

	public Iterator<Node> nodeIterator() {
		return contentList().iterator();
	}

	public Iterator<Node> iterator() {
		return nodeIterator();
	}

	// Implementation methods

	/**
	 * DOCUMENT ME!
	 *
	 * @param element DOCUMENT ME!
	 * @return the ID of the given <code>Element</code>
	 */
	protected String elementID(Element element) {
		//TODO
		// XXX: there will be other ways of finding the ID
		// XXX: should probably have an IDResolver or something
		return element.attributeValue("ID");
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return the internal List used to manage the content
	 */
	protected abstract List<Node> contentList();

	/**
	 * A Factory Method pattern which creates a List implementation used to
	 * store content
	 *
	 * @param size DOCUMENT ME!
	 * @return DOCUMENT ME!
	 */
	protected List<Node> createContentList() {
		return new LazyList<Node>();
	}

	/**
	 * A Factory Method pattern which creates a BackedList implementation used
	 * to store results of a filtered content query.
	 *
	 * @return DOCUMENT ME!
	 */
	protected <T extends Node> BackedList<T> createResultList() {
		return new BackedList<T>(this, contentList());
	}

	protected abstract void addNode(Node node);

	protected abstract void addNode(int index, Node node);

	protected abstract boolean removeNode(Node node);

	/**
	 * Called when a new child node has been added to me to allow any parent
	 * relationships to be created or events to be fired.
	 *
	 * @param node DOCUMENT ME!
	 */
	protected abstract void childAdded(Node node);

	/**
	 * Called when a child node has been removed to allow any parent
	 * relationships to be deleted or events to be fired.
	 *
	 * @param node DOCUMENT ME!
	 */
	protected abstract void childRemoved(Node node);

	/**
	 * Called when the given List content has been removed so each node should
	 * have its parent and document relationships cleared
	 */
	protected void contentRemoved() {
		for (Node node : contentList()) {
			childRemoved(node);
		}
	}

	/**
	 * Called when an invalid node has been added. Throws an {@link
	 * IllegalAddException}.
	 *
	 * @param node DOCUMENT ME!
	 * @throws IllegalAddException DOCUMENT ME!
	 */
	protected void invalidNodeTypeAddException(Node node) {
		throw new IllegalAddException("Invalid node type. Cannot add node: " + node + " to this branch: " + this);
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
