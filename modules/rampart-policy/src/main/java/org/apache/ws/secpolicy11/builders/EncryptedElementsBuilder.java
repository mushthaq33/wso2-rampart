/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
package org.apache.ws.secpolicy11.builders;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.neethi.Assertion;
import org.apache.neethi.AssertionBuilderFactory;
import org.apache.neethi.Constants;
import org.apache.neethi.builders.AssertionBuilder;
import org.apache.ws.secpolicy.SP11Constants;
import org.apache.ws.secpolicy.SPConstants;
import org.apache.ws.secpolicy.model.SignedEncryptedElements;

import javax.xml.namespace.QName;
import java.util.Iterator;

public class EncryptedElementsBuilder implements AssertionBuilder {

    public Assertion build(OMElement element, AssertionBuilderFactory factory) throws IllegalArgumentException {
        SignedEncryptedElements signedEncryptedElements = new SignedEncryptedElements(false, SPConstants.SP_V11);
        
        OMAttribute attribute = element.getAttribute(SP11Constants.ATTR_XPATH_VERSION);
        if (attribute != null) {
            signedEncryptedElements.setXPathVersion(attribute.getAttributeValue());
        }
        
        OMAttribute isOptional = element.getAttribute(Constants.Q_ELEM_OPTIONAL_ATTR);
		if (isOptional != null) {
			signedEncryptedElements.setOptional(Boolean.valueOf(isOptional.getAttributeValue())
					.booleanValue());
		}
        
        for (Iterator iterator = element.getChildElements(); iterator.hasNext();) {
            processElement((OMElement) iterator.next(), signedEncryptedElements);
        }

        // Add declared namespaces
        Iterator namespaces = element.getAllDeclaredNamespaces();
        while (namespaces.hasNext()) {
            OMNamespace nm = (OMNamespace) namespaces.next();
            signedEncryptedElements.addDeclaredNamespaces(nm.getNamespaceURI(), nm.getPrefix());
        }

        return signedEncryptedElements;
    }
    
    
    
    public QName[] getKnownElements() {
        return new QName[] {SP11Constants.ENCRYPTED_ELEMENTS};
    }



    private void processElement(OMElement element, SignedEncryptedElements parent) {
        if (SP11Constants.XPATH.equals(element.getQName())) {
            parent.addXPathExpression(element.getText());   
            Iterator namespaces = element.getAllDeclaredNamespaces();
            while (namespaces.hasNext()) {
                OMNamespace nm = (OMNamespace) namespaces.next();
                parent.addDeclaredNamespaces(nm.getNamespaceURI(), nm.getPrefix());
            }
        }
    }
    
    
}
