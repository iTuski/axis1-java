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

package org.apache.axis.engine;

//todo

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.axis.Constants;
import org.apache.axis.addressing.AddressingConstants;
import org.apache.axis.addressing.EndpointReference;
import org.apache.axis.clientapi.MessageSender;
import org.apache.axis.context.MessageContext;
import org.apache.axis.description.OperationDescription;
import org.apache.axis.description.ServiceDescription;
import org.apache.axis.integration.UtilServer;
import org.apache.axis.om.OMAbstractFactory;
import org.apache.axis.om.OMElement;
import org.apache.axis.om.OMFactory;
import org.apache.axis.om.OMNamespace;
import org.apache.axis.soap.SOAPEnvelope;
import org.apache.axis.soap.SOAPFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OneWayRawXMLTest extends TestCase {
    private EndpointReference targetEPR =
             new EndpointReference(AddressingConstants.WSA_TO,
                     "http://127.0.0.1:"
             + (UtilServer.TESTING_PORT)
             + "/axis/services/EchoXMLService/echoOMElement");
    private Log log = LogFactory.getLog(getClass());
    private QName serviceName = new QName("EchoXMLService");
    private QName operationName = new QName("echoOMElement");
    private QName transportName = new QName("http://localhost/my", "NullTransport");

    private AxisConfiguration engineRegistry;
    private MessageContext mc;

    private SOAPEnvelope envelope;

    private boolean finish = false;

    public OneWayRawXMLTest() {
        super(OneWayRawXMLTest.class.getName());
    }

    public OneWayRawXMLTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        UtilServer.start();

        ServiceDescription service = new ServiceDescription(serviceName);
        OperationDescription operation = new OperationDescription(operationName);
        operation.setMessageReciever(new MessageReceiver() {
            public void recieve(MessageContext messgeCtx) throws AxisFault {
                envelope = messgeCtx.getEnvelope();
            }
        });
        service.addOperation(operation);
        UtilServer.deployService(service);
      }

   
    protected void tearDown() throws Exception {
        UtilServer.unDeployService(serviceName);
        UtilServer.stop();
    }

    private OMElement createEnvelope() {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMNamespace omNs = fac.createOMNamespace("http://localhost/my", "my");
        OMElement method = fac.createOMElement("echoOMElement", omNs);
        OMElement value = fac.createOMElement("myValue", omNs);
        value.addChild(fac.createText(value, "Isaac Assimov, the foundation Sega"));
        method.addChild(value);

        return method;
    }

    public void testEchoXMLSync() throws Exception {
        SOAPFactory fac = OMAbstractFactory.getSOAP11Factory();

        OMElement payload = createEnvelope();

        MessageSender sender = new MessageSender();

        sender.setTo(targetEPR);
        sender.setSenderTransport(Constants.TRANSPORT_HTTP);

        sender.send(operationName.getLocalPart(), payload);
        int index = 0;
        while(envelope == null){
            Thread.sleep(4000);
            index ++;
            if(index == 5){
                throw new AxisFault("error Occured");
            }
        }
    }

}