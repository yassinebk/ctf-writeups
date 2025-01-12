package org.springframework.util.xml;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.AttributesImpl;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/xml/StaxStreamXMLReader.class */
class StaxStreamXMLReader extends AbstractStaxXMLReader {
    private static final String DEFAULT_XML_VERSION = "1.0";
    private final XMLStreamReader reader;
    private String xmlVersion = DEFAULT_XML_VERSION;
    @Nullable
    private String encoding;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StaxStreamXMLReader(XMLStreamReader reader) {
        int event = reader.getEventType();
        if (event != 7 && event != 1) {
            throw new IllegalStateException("XMLEventReader not at start of document or element");
        }
        this.reader = reader;
    }

    @Override // org.springframework.util.xml.AbstractStaxXMLReader
    protected void parseInternal() throws SAXException, XMLStreamException {
        boolean documentStarted = false;
        boolean documentEnded = false;
        int elementDepth = 0;
        int eventType = this.reader.getEventType();
        while (true) {
            int eventType2 = eventType;
            if (eventType2 != 7 && eventType2 != 8 && !documentStarted) {
                handleStartDocument();
                documentStarted = true;
            }
            switch (eventType2) {
                case 1:
                    elementDepth++;
                    handleStartElement();
                    break;
                case 2:
                    elementDepth--;
                    if (elementDepth >= 0) {
                        handleEndElement();
                        break;
                    }
                    break;
                case 3:
                    handleProcessingInstruction();
                    break;
                case 4:
                case 6:
                case 12:
                    handleCharacters();
                    break;
                case 5:
                    handleComment();
                    break;
                case 7:
                    handleStartDocument();
                    documentStarted = true;
                    break;
                case 8:
                    handleEndDocument();
                    documentEnded = true;
                    break;
                case 9:
                    handleEntityReference();
                    break;
                case 11:
                    handleDtd();
                    break;
            }
            if (this.reader.hasNext() && elementDepth >= 0) {
                eventType = this.reader.next();
            }
        }
        if (!documentEnded) {
            handleEndDocument();
        }
    }

    private void handleStartDocument() throws SAXException {
        if (7 == this.reader.getEventType()) {
            String xmlVersion = this.reader.getVersion();
            if (StringUtils.hasLength(xmlVersion)) {
                this.xmlVersion = xmlVersion;
            }
            this.encoding = this.reader.getCharacterEncodingScheme();
        }
        if (getContentHandler() != null) {
            final Location location = this.reader.getLocation();
            getContentHandler().setDocumentLocator(new Locator2() { // from class: org.springframework.util.xml.StaxStreamXMLReader.1
                @Override // org.xml.sax.Locator
                public int getColumnNumber() {
                    if (location != null) {
                        return location.getColumnNumber();
                    }
                    return -1;
                }

                @Override // org.xml.sax.Locator
                public int getLineNumber() {
                    if (location != null) {
                        return location.getLineNumber();
                    }
                    return -1;
                }

                @Override // org.xml.sax.Locator
                @Nullable
                public String getPublicId() {
                    if (location != null) {
                        return location.getPublicId();
                    }
                    return null;
                }

                @Override // org.xml.sax.Locator
                @Nullable
                public String getSystemId() {
                    if (location != null) {
                        return location.getSystemId();
                    }
                    return null;
                }

                @Override // org.xml.sax.ext.Locator2
                public String getXMLVersion() {
                    return StaxStreamXMLReader.this.xmlVersion;
                }

                @Override // org.xml.sax.ext.Locator2
                @Nullable
                public String getEncoding() {
                    return StaxStreamXMLReader.this.encoding;
                }
            });
            getContentHandler().startDocument();
            if (this.reader.standaloneSet()) {
                setStandalone(this.reader.isStandalone());
            }
        }
    }

    private void handleStartElement() throws SAXException {
        if (getContentHandler() != null) {
            QName qName = this.reader.getName();
            if (hasNamespacesFeature()) {
                for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
                    startPrefixMapping(this.reader.getNamespacePrefix(i), this.reader.getNamespaceURI(i));
                }
                for (int i2 = 0; i2 < this.reader.getAttributeCount(); i2++) {
                    String prefix = this.reader.getAttributePrefix(i2);
                    String namespace = this.reader.getAttributeNamespace(i2);
                    if (StringUtils.hasLength(namespace)) {
                        startPrefixMapping(prefix, namespace);
                    }
                }
                getContentHandler().startElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName), getAttributes());
                return;
            }
            getContentHandler().startElement("", "", toQualifiedName(qName), getAttributes());
        }
    }

    private void handleEndElement() throws SAXException {
        if (getContentHandler() != null) {
            QName qName = this.reader.getName();
            if (hasNamespacesFeature()) {
                getContentHandler().endElement(qName.getNamespaceURI(), qName.getLocalPart(), toQualifiedName(qName));
                for (int i = 0; i < this.reader.getNamespaceCount(); i++) {
                    String prefix = this.reader.getNamespacePrefix(i);
                    if (prefix == null) {
                        prefix = "";
                    }
                    endPrefixMapping(prefix);
                }
                return;
            }
            getContentHandler().endElement("", "", toQualifiedName(qName));
        }
    }

    private void handleCharacters() throws SAXException {
        if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
            getLexicalHandler().startCDATA();
        }
        if (getContentHandler() != null) {
            getContentHandler().characters(this.reader.getTextCharacters(), this.reader.getTextStart(), this.reader.getTextLength());
        }
        if (12 == this.reader.getEventType() && getLexicalHandler() != null) {
            getLexicalHandler().endCDATA();
        }
    }

    private void handleComment() throws SAXException {
        if (getLexicalHandler() != null) {
            getLexicalHandler().comment(this.reader.getTextCharacters(), this.reader.getTextStart(), this.reader.getTextLength());
        }
    }

    private void handleDtd() throws SAXException {
        if (getLexicalHandler() != null) {
            Location location = this.reader.getLocation();
            getLexicalHandler().startDTD(null, location.getPublicId(), location.getSystemId());
        }
        if (getLexicalHandler() != null) {
            getLexicalHandler().endDTD();
        }
    }

    private void handleEntityReference() throws SAXException {
        if (getLexicalHandler() != null) {
            getLexicalHandler().startEntity(this.reader.getLocalName());
        }
        if (getLexicalHandler() != null) {
            getLexicalHandler().endEntity(this.reader.getLocalName());
        }
    }

    private void handleEndDocument() throws SAXException {
        if (getContentHandler() != null) {
            getContentHandler().endDocument();
        }
    }

    private void handleProcessingInstruction() throws SAXException {
        if (getContentHandler() != null) {
            getContentHandler().processingInstruction(this.reader.getPITarget(), this.reader.getPIData());
        }
    }

    private Attributes getAttributes() {
        String str;
        AttributesImpl attributes = new AttributesImpl();
        for (int i = 0; i < this.reader.getAttributeCount(); i++) {
            String namespace = this.reader.getAttributeNamespace(i);
            namespace = (namespace == null || !hasNamespacesFeature()) ? "" : "";
            String type = this.reader.getAttributeType(i);
            if (type == null) {
                type = "CDATA";
            }
            attributes.addAttribute(namespace, this.reader.getAttributeLocalName(i), toQualifiedName(this.reader.getAttributeName(i)), type, this.reader.getAttributeValue(i));
        }
        if (hasNamespacePrefixesFeature()) {
            for (int i2 = 0; i2 < this.reader.getNamespaceCount(); i2++) {
                String prefix = this.reader.getNamespacePrefix(i2);
                String namespaceUri = this.reader.getNamespaceURI(i2);
                if (StringUtils.hasLength(prefix)) {
                    str = "xmlns:" + prefix;
                } else {
                    str = "xmlns";
                }
                String qName = str;
                attributes.addAttribute("", "", qName, "CDATA", namespaceUri);
            }
        }
        return attributes;
    }
}
