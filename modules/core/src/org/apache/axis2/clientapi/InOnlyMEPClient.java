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
 *
 *  Runtime state of the engine
 */
package org.apache.axis2.clientapi;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.addressing.MessageInformationHeadersCollection;
import org.apache.axis2.addressing.miheaders.RelatesTo;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.OperationDescription;
import org.apache.axis2.description.TransportOutDescription;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisEngine;
import org.apache.axis2.engine.AxisFault;
import org.apache.wsdl.WSDLConstants;

import javax.xml.namespace.QName;

public class InOnlyMEPClient extends MEPClient {
    protected MessageInformationHeadersCollection messageInformationHeaders;
    protected TransportOutDescription senderTransport;
    
    
    public InOnlyMEPClient(ServiceContext service) {
        super(service,WSDLConstants.MEP_URI_IN_ONLY);
        messageInformationHeaders = new MessageInformationHeadersCollection();
    }

    public void send(OperationDescription axisop, final MessageContext msgctx) throws AxisFault {
        verifyInvocation(axisop,msgctx);
        msgctx.setSoapAction(soapAction);
        msgctx.setMessageInformationHeaders(messageInformationHeaders);
        msgctx.setServiceContext(serviceContext);
        ConfigurationContext syscontext = serviceContext.getEngineContext();
        
        if(senderTransport == null ){
            senderTransport = inferTransport(messageInformationHeaders.getTo());
        }
        
        msgctx.setTransportOut(senderTransport);

        ConfigurationContext sysContext = serviceContext.getEngineContext();
        AxisConfiguration registry = sysContext.getAxisConfiguration();

        AxisEngine engine = new AxisEngine(sysContext);
        msgctx.setOperationContext(axisop.findOperationContext(msgctx,serviceContext));

        engine.send(msgctx);
    }

    /**
     * @param action
     */
    public void setWsaAction(String action) {
        messageInformationHeaders.setAction(action);
    }

    /**
     * @param faultTo
     */
    public void setFaultTo(EndpointReference faultTo) {
        messageInformationHeaders.setFaultTo(faultTo);
    }

    /**
     * @param from
     */
    public void setFrom(EndpointReference from) {
        messageInformationHeaders.setFrom(from);
    }

    /**
     * @param messageId
     */
    public void setMessageId(String messageId) {
        messageInformationHeaders.setMessageId(messageId);
    }

    /**
     * @param relatesTo
     */
    public void setRelatesTo(RelatesTo relatesTo) {
        messageInformationHeaders.setRelatesTo(relatesTo);
    }

    /**
     * @param replyTo
     */
    public void setReplyTo(EndpointReference replyTo) {
        messageInformationHeaders.setReplyTo(replyTo);
    }

    /**
     * @param to
     */
    public void setTo(EndpointReference to) {
        messageInformationHeaders.setTo(to);
    }
    
    public void engageModule(QName name) throws AxisFault{
        serviceContext.getEngineContext().getAxisConfiguration().engageModule(name);
    }
    
    public void setSenderTransport(String senderTransport) throws AxisFault{
        this.senderTransport = serviceContext.getEngineContext().getAxisConfiguration().getTransportOut(new QName(senderTransport));
    }
 }