package org.openhab.io.innovationhub.services.status;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="status")
public class StatusBean {
	public Date openhabTime = new Date();

}
