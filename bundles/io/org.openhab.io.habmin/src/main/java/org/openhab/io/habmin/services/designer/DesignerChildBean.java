/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.innovationhub.services.designer;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Chris Jackson
 * @since 1.5.0
 *
 */
@XmlRootElement(name="children")
public class DesignerChildBean {
	public String type;
	public String name;
	public DesignerBlockBean block;
}

