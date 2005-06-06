/*
* Copyright 2004,2005 The Apache Software Foundation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.apache.axis.soap.impl.llom;

import org.apache.axis.om.*;
import org.apache.axis.om.impl.llom.OMElementImpl;
import org.apache.axis.om.impl.llom.traverse.OMChildrenWithSpecificAttributeIterator;
import org.apache.axis.soap.SOAPEnvelope;
import org.apache.axis.soap.SOAPHeader;
import org.apache.axis.soap.SOAPHeaderBlock;
import org.apache.axis.soap.impl.llom.soap11.SOAP11Constants;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class SOAPHeaderImpl
 */
public class SOAPHeaderImpl extends OMElementImpl implements SOAPHeader {
    /**
     * @param envelope
     */
    public SOAPHeaderImpl(SOAPEnvelope envelope) {
        super(envelope);

        // set the namespaces
        this.ns = envelope.getNamespace();
        this.localName = SOAPConstants.HEADER_LOCAL_NAME;
    }

    /**
     * Constructor SOAPHeaderImpl
     *
     * @param envelope
     * @param builder
     */
    public SOAPHeaderImpl(SOAPEnvelope envelope, OMXMLParserWrapper builder) {
        super(SOAPConstants.HEADER_LOCAL_NAME,
                (envelope == null) ? null : envelope.getNamespace(),
                envelope,
                builder);
    }

    /**
     * Creates a new <CODE>SOAPHeaderBlock</CODE> object initialized with the
     * specified name and adds it to this <CODE>SOAPHeader</CODE> object.
     *
     * @param localName
     * @param ns
     * @return the new <CODE>SOAPHeaderBlock</CODE> object that was inserted
     *         into this <CODE>SOAPHeader</CODE> object
     * @throws org.apache.axis.om.OMException if a SOAP error occurs
     * @throws OMException
     */
    public SOAPHeaderBlock addHeaderBlock(String localName, OMNamespace ns)
            throws OMException {
        if (ns == null || ns.getName() == null || "".equals(ns.getName())) {
            throw new OMException("All the SOAP Header blocks should be namespace qualified");
        }
        SOAPHeaderBlock soapHeaderBlock =
                new SOAPHeaderBlockImpl(localName, ns);
        this.addChild(soapHeaderBlock);
        soapHeaderBlock.setComplete(true);
        return soapHeaderBlock;
    }

    /**
     * Returns a list of all the <CODE>SOAPHeaderBlock</CODE> objects in this
     * <CODE>SOAPHeader</CODE> object that have the the specified actor. An
     * actor is a global attribute that indicates the intermediate parties to
     * whom the message should be sent. An actor receives the message and then
     * sends it to the next actor. The default actor is the ultimate intended
     * recipient for the message, so if no actor attribute is included in a
     * <CODE>SOAPHeader</CODE> object, the message is sent to its ultimate
     * destination.
     *
     * @param paramRole a <CODE>String</CODE> giving the URI of the actor for
     *                  which to search
     * @return an <CODE>Iterator</CODE> object over all the <CODE>
     *         SOAPHeaderBlock</CODE> objects that contain the specified actor
     * @see #extractHeaderBlocks(String) extractHeaderBlocks(java.lang.String)
     */
    public Iterator examineHeaderBlocks(String paramRole) {
        Iterator headerBlocksIter = this.getChildren();
        ArrayList headersWithGivenActor = new ArrayList();
        while (headerBlocksIter.hasNext()) {
            Object o = headerBlocksIter.next();
            if (o instanceof SOAPHeaderBlock) {
                SOAPHeaderBlock soapHeaderBlock = (SOAPHeaderBlock) o;
                String role = soapHeaderBlock.getRole();
                if ((role != null) && role.equalsIgnoreCase(paramRole)) {
                    headersWithGivenActor.add(soapHeaderBlock);
                }
            }
        }
        return headersWithGivenActor.iterator();
    }

    /**
     * Returns a list of all the <CODE>SOAPHeaderBlock</CODE> objects in this
     * <CODE>SOAPHeader</CODE> object that have the the specified actor and
     * detaches them from this <CODE> SOAPHeader</CODE> object. <P>This method
     * allows an actor to process only the parts of the <CODE>SOAPHeader</CODE>
     * object that apply to it and to remove them before passing the message on
     * to the next actor.
     *
     * @param actor a <CODE>String</CODE> giving the URI of the actor for which
     *              to search
     * @return an <CODE>Iterator</CODE> object over all the <CODE>
     *         SOAPHeaderBlock</CODE> objects that contain the specified actor
     * @see #examineHeaderBlocks(String) examineHeaderBlocks(java.lang.String)
     */
    public Iterator extractHeaderBlocks(String actor) {
        return new OMChildrenWithSpecificAttributeIterator(getFirstChild(),
                new QName(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI,
                        SOAPConstants.ATTR_ACTOR),
                actor,
                true);
    }

    /**
     * Returns an <code>Iterator</code> over all the <code>SOAPHeaderBlock</code>
     * objects in this <code>SOAPHeader</code> object that have the specified
     * actor and that have a MustUnderstand attribute whose value is equivalent
     * to <code>true</code>.
     *
     * @param actor a <code>String</code> giving the URI of the actor for which
     *              to search
     * @return an <code>Iterator</code> object over all the
     *         <code>SOAPHeaderBlock</code> objects that contain the specified
     *         actor and are marked as MustUnderstand
     */
    public Iterator examineMustUnderstandHeaderBlocks(String actor) {
        return new OMChildrenWithSpecificAttributeIterator(getFirstChild(),
                new QName(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI,
                        SOAPConstants.ATTR_ACTOR),
                actor,
                false);
    }

    /**
     * Returns an <code>Iterator</code> over all the <code>SOAPHeaderBlock</code>
     * objects in this <code>SOAPHeader</code> object. Not that this will return
     * elements containing the QName (http://schemas.xmlsoap.org/soap/envelope/,
     * Header)
     *
     * @return an <code>Iterator</code> object over all the
     *         <code>SOAPHeaderBlock</code> objects contained by this
     *         <code>SOAPHeader</code>
     */
    public Iterator examineAllHeaderBlocks() {
        return this.getChildrenWithName(null);
    }

    /**
     * Returns an <code>Iterator</code> over all the <code>SOAPHeaderBlock</code>
     * objects in this <code>SOAPHeader </code> object and detaches them from
     * this <code>SOAPHeader</code> object.
     *
     * @return an <code>Iterator</code> object over all the
     *         <code>SOAPHeaderBlock</code> objects contained by this
     *         <code>SOAPHeader</code>
     */
    public Iterator extractAllHeaderBlocks() {
        throw new UnsupportedOperationException(); // TODO implement this
    }

    public ArrayList getHeaderBolcksWithNSURI(String nsURI) {
        ArrayList headers = null;
        OMNode node = null;
        OMElement header = this.getFirstElement();

        if (header != null) {
            headers = new ArrayList();
        }
        
        node = header;
        
        while (node != null) {
            if (node.getType() == OMNode.ELEMENT_NODE) {
                header = (OMElement) node;
                if (nsURI.equals(header.getNamespace().getName())) {
                    headers.add(header);
                }
            }
            node = node.getNextSibling();

        }
        return headers;

    }

}