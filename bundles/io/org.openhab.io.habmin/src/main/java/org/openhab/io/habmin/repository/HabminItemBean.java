/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.innovationhub.repository;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.openhab.io.innovationhub.services.rule.RuleTemplateBean;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
@XmlRootElement(name="item")
public class innovationhubItemBean {
	public String name;
	
	@XmlElement(name="extendedProperties")
	@XStreamImplicit(itemFieldName="extendedProperties")
	public List<String> extendedProperties;
	
	@XmlElement(name="rules")
	@XStreamImplicit(itemFieldName="rules")
	public List<RuleTemplateBean> rules;

	public innovationhubItemBean() {};
}
