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

package org.ambientlight.room.entities;

import org.ambientlight.config.process.events.Event;
import org.ambientlight.config.process.events.SwitchEvent;
import org.ambientlight.config.room.eventgenerator.SwitchEventGeneratorConfiguration;


/**
 * @author Florian Bornkessel
 * 
 */
public class SwitchEventGenerator extends EventGenerator implements EventSensor {

	public void switchEventOccured(SwitchEvent event) {
		SwitchEvent correlation = new SwitchEvent();
		correlation.sourceName = config.name;
		eventManager.onEvent(event);

		((SwitchEventGeneratorConfiguration) this.config).setPowerState(event.powerState);

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ambientlight.room.entities.EventSensor#getValue()
	 */
	@Override
	public Event getValue() {
		// TODO Auto-generated method stub
		SwitchEvent config = new SwitchEvent();
		config.sourceName = this.config.name;
		config.powerState = ((SwitchEventGeneratorConfiguration) this.config).getPowerState();
		return config;
	}
}
