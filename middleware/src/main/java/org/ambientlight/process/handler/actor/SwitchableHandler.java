/*  Copyright 2013 Florian Bornkessel

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.ambientlight.process.handler.actor;

import org.ambientlight.config.process.handler.DataTypeValidation;
import org.ambientlight.config.process.handler.actor.SwitchableHandlerConfiguration;
import org.ambientlight.process.Token;
import org.ambientlight.process.TokenSensorValue;
import org.ambientlight.process.handler.AbstractActionHandler;
import org.ambientlight.process.handler.ActionHandlerException;
import org.ambientlight.room.entities.features.EntityId;


/**
 * @author Florian Bornkessel
 * 
 */
public class SwitchableHandler extends AbstractActionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ambientlight.process.handler.AbstractActionHandler#performAction(org.ambientlight.process.Token)
	 */
	@Override
	public void performAction(Token token) throws ActionHandlerException {
		boolean powerState = false;
		if (getConfig().useTokenValue) {
			if (token.valueType != DataTypeValidation.SENSOR) {
				TokenSensorValue tokenValue = (TokenSensorValue) token.data;
				powerState = Boolean.parseBoolean(tokenValue.value);
			}
		} else {
			powerState = getConfig().powerState;
		}

		if (getConfig().invert) {
			powerState = !powerState;
		}

		for (EntityId currentId : getConfig().switcheables) {
			featureFacade.setSwitcheablePowerState(currentId, powerState, getConfig().fireEvent);
		}
	}


	SwitchableHandlerConfiguration getConfig() {
		return (SwitchableHandlerConfiguration) this.config;
	}
}
