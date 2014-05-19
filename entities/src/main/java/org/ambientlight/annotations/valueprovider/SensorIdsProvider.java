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

package org.ambientlight.annotations.valueprovider;

import org.ambientlight.annotations.valueprovider.api.AlternativeValueProvider;
import org.ambientlight.annotations.valueprovider.api.AlternativeValues;
import org.ambientlight.ws.Room;


/**
 * @author Florian Bornkessel
 * 
 */
public class SensorIdsProvider implements AlternativeValueProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ambient.control.config.AlternativeValueProvider#getValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AlternativeValues getValue(Room config, Object entity) {
		return null;
	}

}
