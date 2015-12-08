/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.innovationhub.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openhab.io.innovationhub.services.rule.RuleTemplateBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class innovationhubConfigDatabase {
	private static final Logger logger = LoggerFactory.getLogger(innovationhubConfigDatabase.class);

	static private final String innovationhub_DATABASE_FILE = "webapps/innovationhub/openhab/configuration_database.xml";
	
	static private innovationhubConfigDatabaseBean configDb = null;

	/**
	 * Loads the database into memory. Once in memory, it isn't re-read - this
	 * speeds up subsequent operations.
	 * 
	 * @return true if the database is open
	 */
	static private boolean loadDatabase() {
		if (configDb != null)
			return true;

		logger.debug("Loading innovationhub database.");
		FileInputStream fin;
		try {
			long timerStart = System.currentTimeMillis();

			fin = new FileInputStream(innovationhub_DATABASE_FILE);

			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("innovationhubDatabase", innovationhubConfigDatabaseBean.class);
			xstream.processAnnotations(innovationhubConfigDatabaseBean.class);

			configDb = (innovationhubConfigDatabaseBean) xstream.fromXML(fin);

			fin.close();

			long timerStop = System.currentTimeMillis();
			logger.debug("innovationhub database loaded in {}ms.", timerStop - timerStart);

			return true;
		} catch (FileNotFoundException e) {
			logger.debug("innovationhub database not found - reinitialising.");

			configDb = new innovationhubConfigDatabaseBean();
			if(configDb == null)
				return false;
			configDb.items = new ArrayList<innovationhubItemBean>();
			if(configDb.items == null) {
				configDb = null;
				return false;
			}
			
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}
	}

	static public boolean saveDatabase() {
		// This possibly should be moved to a separate thread so that it can be
		// deferred to avoid multiple calls
		if (loadDatabase() == false)
			return false;

		FileOutputStream fout;
		try {
			long timerStart = System.currentTimeMillis();

			fout = new FileOutputStream(innovationhub_DATABASE_FILE);

			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("innovationhubDatabase", innovationhubConfigDatabaseBean.class);
			xstream.processAnnotations(innovationhubConfigDatabaseBean.class);

			xstream.toXML(configDb, fout);

			fout.close();

			long timerStop = System.currentTimeMillis();
			logger.debug("innovationhub database saved in {}ms.", timerStop - timerStart);
		} catch (FileNotFoundException e) {
			logger.debug("Unable to open innovationhub database for SAVE - ", e);

			return false;
		} catch (IOException e) {
			logger.debug("Unable to write innovationhub database for SAVE - ", e);

			return false;
		}

		return true;
	}

	/**
	 * Reads the innovationhub configuration "database" and returns the item requested
	 * by the item parameter.
	 * 
	 * @param item
	 *            name of the item to return
	 * @return innovationhubItemBean or null if not found
	 */
	synchronized static public innovationhubItemBean getItemConfig(String itemName) {
		if (loadDatabase() == false)
			return null;

		if(configDb.items == null)
			return null;
		
		innovationhubItemBean item = null;
		for (innovationhubItemBean bean : configDb.items) {
			if (bean.name == null)
				continue;
			if (bean.name.equals(itemName)) {
				item = bean;
				break;
			}
		}

		// If not found, create the new item
		if(item == null) {
			item = new innovationhubItemBean();
			item.name = itemName;
		
			configDb.items.add(item);
		}
		
		// Make sure rules is initialised
		if(item.rules == null)
			item.rules = new ArrayList<RuleTemplateBean>();
		if(item.rules == null)
			return null;
		
		// Not found
		return item;
	}

	/**
	 * Reads the innovationhub configuration "database" and returns the item requested
	 * by the item parameter.
	 * 
	 * @param item
	 *            name of the item to return
	 * @return innovationhubItemBean or null if not found
	 */
	synchronized static public boolean existsItemConfig(String item) {
		if (loadDatabase() == false)
			return false;

		if(configDb.items == null)
			return false;
		
		for (innovationhubItemBean bean : configDb.items) {
			if (bean.name == null)
				continue;
			if (bean.name.equals(item))
				return true;
		}

		// Not found
		return false;
	}

	/**
	 * Saves item data in the innovationhub "database" file. This will overwrite the
	 * existing data for this item
	 * 
	 * @param item
	 *            innovationhubItemBean with the item data
	 * @return true if item data saved successfully
	 */
	synchronized static public boolean setItemConfig(innovationhubItemBean item) {
		if (loadDatabase() == false)
			return false;

		innovationhubItemBean foundItem = null;

		// Find the required item
		for (innovationhubItemBean bean : configDb.items) {
			if (bean.name == null)
				continue;
			if (bean.name.equals(item))
				foundItem = bean;
		}

		if (foundItem != null) {
			// Found this item in the database, remove it
			configDb.items.remove(foundItem);
		}

		// Add the new data into the database
		configDb.items.add(item);

		saveDatabase();

		// Success
		return true;
	}

	synchronized static public List<innovationhubItemBean> getItems() {
		if (loadDatabase() == false)
			return null;
		
		return configDb.items;
	}

}
