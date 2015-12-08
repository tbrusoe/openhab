/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.innovationhub.services.designer.blocks;

import org.openhab.core.items.ItemNotFoundException;
import org.openhab.io.innovationhub.innovationhubApplication;
import org.openhab.io.innovationhub.services.designer.DesignerBlockBean;
import org.openhab.io.innovationhub.services.designer.DesignerFieldBean;
import org.openhab.io.innovationhub.services.designer.blocks.RuleContext.TriggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Chris Jackson
 * @since 1.5.0
 * 
 */
public class OpenhabItemGetBlock extends DesignerRuleCreator {
	private static final Logger logger = LoggerFactory.getLogger(OpenhabItemGetBlock.class);

	String processBlock(RuleContext ruleContext, DesignerBlockBean block) {
		DesignerFieldBean varField = findField(block.fields, "ITEM");
		if (varField == null) {
			logger.error("ITEM GET contains no NUM");
			return null;
		}

		// If this is a valid item, then add .state
		String val = varField.value;
		try {
			if(innovationhubApplication.getItemUIRegistry().getItem(val) != null) {
				val += ".state";
				ruleContext.addTrigger(varField.value, TriggerType.CHANGED);
			}
		} catch (ItemNotFoundException e) {
		}

		return val;
	}
}
