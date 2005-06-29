package org.apache.axis.clientapi;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.Location;
import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;

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
*
*/
public class StreamWrapper implements XMLStreamReader{

    private XMLStreamReader realReader = null;
    private static final int STATE_SWITCHED = 0;
    private static final int STATE_INIT = 1;
    private static final int STATE_SWITHC_AT_NEXT = 2;

    private int state = STATE_INIT;
    private int prevState = state;

    public StreamWrapper(XMLStreamReader realReader) {
        if (realReader==null){
            throw new UnsupportedOperationException("Reader cannot be null");
        }
        this.realReader = realReader;
    }

    public Object getProperty(String s) throws IllegalArgumentException {
        if(prevState!=STATE_INIT){
            return realReader.getProperty(s);
        }else{
            throw new IllegalArgumentException();
        }
    }

    public int next() throws XMLStreamException {

        prevState = state;

        if (state==STATE_SWITCHED){
            return realReader.next();
        }else if (state==STATE_INIT){
            if (realReader.getEventType()==START_DOCUMENT){
                state = STATE_SWITCHED;
                return  realReader.getEventType();
            }else{
                state = STATE_SWITHC_AT_NEXT;
                return START_DOCUMENT;
            }
        }else if (state==STATE_SWITHC_AT_NEXT) {
            state = STATE_SWITCHED;
            return realReader.getEventType();
        }else{
            throw new UnsupportedOperationException();
        }

    }

    public void require(int i, String s, String s1) throws XMLStreamException {
        if(prevState!=STATE_INIT){
            realReader.require(i, s, s1);
        }
    }

    public String getElementText() throws XMLStreamException {
        if(prevState!=STATE_INIT){
            return realReader.getElementText();
        }else{
            throw new XMLStreamException();
        }
    }

    public int nextTag() throws XMLStreamException {
        if(prevState!=STATE_INIT){
            return realReader.nextTag();
        }else{
            throw new XMLStreamException();
        }
    }

    public boolean hasNext() throws XMLStreamException {
        if(prevState!=STATE_INIT){
            return realReader.hasNext();
        }else{
          return true;
        }
    }

    public void close() throws XMLStreamException {
        if(prevState!=STATE_INIT){
            realReader.close();
        }else{
            throw new XMLStreamException();
        }
    }

    public String getNamespaceURI(String s) {
        if(prevState!=STATE_INIT){
            return realReader.getNamespaceURI(s);
        }else{
            return null;
        }
    }

    public boolean isStartElement() {
        if(prevState!=STATE_INIT){
            return realReader.isStartElement();
        }else{
            return false;
        }
    }

    public boolean isEndElement() {
        if(prevState!=STATE_INIT){
            return realReader.isEndElement();
        }else{
            return false;
        }
    }

    public boolean isCharacters() {
        if(prevState!=STATE_INIT){
            return realReader.isCharacters();
        }else{
            return false;
        }
    }

    public boolean isWhiteSpace() {
        if(prevState!=STATE_INIT){
            return realReader.isWhiteSpace();
        }else{
            return false;
        }
    }

    public String getAttributeValue(String s, String s1) {
        if(prevState!=STATE_INIT){
            return realReader.getAttributeValue(s,s1);
        }else{
            return null;
        }
    }

    public int getAttributeCount() {
        if(prevState!=STATE_INIT){
            return realReader.getAttributeCount();
        }else{
            return 0;
        }
    }

    public QName getAttributeName(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getAttributeName(i);
        }else{
            return null;
        }
    }

    public String getAttributeNamespace(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getAttributeNamespace(i);
        }else{
            return null;
        }
    }

    public String getAttributeLocalName(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getAttributeLocalName(i);
        }else{
            return null;
        }
    }

    public String getAttributePrefix(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getAttributePrefix(i);
        }else{
            return null;
        }
    }

    public String getAttributeType(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getAttributeType(i);
        }else{
            return null;
        }
    }

    public String getAttributeValue(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getAttributeValue(i);
        }else{
            return null;
        }
    }

    public boolean isAttributeSpecified(int i) {
        if(prevState!=STATE_INIT){
            return realReader.isAttributeSpecified(i);
        }else{
            return false;
        }
    }

    public int getNamespaceCount() {
        if(prevState!=STATE_INIT){
            return realReader.getNamespaceCount();
        }else{
            return 0;
        }
    }

    public String getNamespacePrefix(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getNamespacePrefix(i);
        }else{
            return null;
        }
    }

    public String getNamespaceURI(int i) {
        if(prevState!=STATE_INIT){
            return realReader.getNamespaceURI(i);
        }else{
            return null;
        }
    }

    public NamespaceContext getNamespaceContext() {
        if(prevState!=STATE_INIT){
            return realReader.getNamespaceContext();
        }else{
            return null;
        }
    }

    public int getEventType() {
        if(prevState==STATE_INIT){
            return START_DOCUMENT;
        }else{
            return realReader.getEventType();
        }
    }

    public String getText() {
        if(prevState!=STATE_INIT){
            return realReader.getText();
        }else{
            return null;
        }
    }

    public char[] getTextCharacters() {
        if(prevState!=STATE_INIT){
            return realReader.getTextCharacters();
        }else{
            return new char[0] ;
        }
    }

    public int getTextCharacters(int i, char[] chars, int i1, int i2) throws XMLStreamException {
        if(prevState!=STATE_INIT){
            return realReader.getTextCharacters(i,chars,i1,i2);
        }else{
            return 0 ;
        }
    }

    public int getTextStart() {
        if(prevState!=STATE_INIT){
            return realReader.getTextStart();
        }else{
            return 0 ;
        }
    }

    public int getTextLength() {
        if(prevState!=STATE_INIT){
            return realReader.getTextStart();
        }else{
            return 0 ;
        }
    }

    public String getEncoding() {
        if(prevState!=STATE_INIT){
            return realReader.getEncoding();
        }else{
            return null ;
        }
    }

    public boolean hasText() {
        if(prevState!=STATE_INIT){
            return realReader.hasText();
        }else{
            return false ;
        }
    }

    public Location getLocation() {
        if(prevState!=STATE_INIT){
            return realReader.getLocation();
        }else{
            return null ;
        }
    }

    public QName getName() {
        if(prevState!=STATE_INIT){
            return realReader.getName();
        }else{
            return null ;
        }
    }

    public String getLocalName() {
        if(prevState!=STATE_INIT){
            return realReader.getLocalName();
        }else{
            return null ;
        }
    }

    public boolean hasName() {
        if(prevState!=STATE_INIT){
            return realReader.hasName();
        }else{
            return false ;
        }
    }

    public String getNamespaceURI() {
        if(prevState!=STATE_INIT){
            return realReader.getNamespaceURI();
        }else{
            return null ;
        }
    }

    public String getPrefix() {
        if(prevState!=STATE_INIT){
            return realReader.getPrefix();
        }else{
            return null ;
        }
    }

    public String getVersion() {
        if(prevState!=STATE_INIT){
            return realReader.getVersion();  
        }else{
            return null ;
        }
    }

    public boolean isStandalone() {
        if(prevState!=STATE_INIT){
            return realReader.isStandalone();
        }else{
            return false ;
        }
    }

    public boolean standaloneSet() {
        if(prevState!=STATE_INIT){
            return realReader.standaloneSet();
        }else{
            return false ;
        }
    }

    public String getCharacterEncodingScheme() {
        if(prevState!=STATE_INIT){
            return realReader.getCharacterEncodingScheme();
        }else{
            return null ;
        }
    }

    public String getPITarget() {
        if(prevState!=STATE_INIT){
            return realReader.getPITarget();
        }else{
            return null ;
        }
    }

    public String getPIData() {
        if(prevState!=STATE_INIT){
            return realReader.getPIData();
        }else{
            return null ;
        }
    }
}