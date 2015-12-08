/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.innovationhub.services.designer.blocks;

import org.openhab.io.innovationhub.services.designer.DesignerBlockBean;
import org.openhab.io.innovationhub.services.designer.DesignerChildBean;
import org.openhab.io.innovationhub.services.designer.DesignerFieldBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Chris Jackson
 * @since 1.5.0
 * 
 */
public class OpenhabItemSetBlock extends DesignerRuleCreator {
	private static final Logger logger = LoggerFactory.getLogger(OpenhabItemSetBlock.class);

	String processBlock(RuleContext ruleContext, DesignerBlockBean block) {
		DesignerFieldBean varField = findField(block.fields, "ITEM");
		if (varField == null) {
			logger.error("ITEM SET contains no VAR");
			return null;
		}

		DesignerChildBean child = findChild(block.children, "VALUE");
		if (child == null) {
			logger.error("ITEM SET contains no VALUE");
			return null;
		}
		String value = callBlock(ruleContext, child.block);

		return startLine(ruleContext.level) + "postUpdate(" + varField.value + ", " + value + ")" + EOL;
	}
}
