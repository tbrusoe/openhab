/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.innovationhub.services.chart;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;



/**
 * This is a java bean that is used with JAXB to serialize items
 * to XML or JSON.
 *  
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
@XmlRootElement(name="chart")
public class ChartConfigBean {
	public Integer id;
	public String name;
	public String title;
	public String icon;
	public Integer period;
	public Boolean legend;

	public List<ChartItemConfigBean> items;
	public List<ChartAxisConfigBean> axis;
}
