package org.openhab.io.innovationhub.services.zwave;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="action")
public class ActionBean {
	public String domain;
	public String action;
}
