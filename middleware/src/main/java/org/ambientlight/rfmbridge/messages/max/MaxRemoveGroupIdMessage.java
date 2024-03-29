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

package org.ambientlight.rfmbridge.messages.max;

/**
 * @author Florian Bornkessel
 * 
 */
public class MaxRemoveGroupIdMessage extends MaxMessage {

	public final static int DEFAULT_GROUP_ID = 0;
	public final static int MAX_GROUP_ID = 255;


	public MaxRemoveGroupIdMessage() {
		payload = new byte[11];
		setMessageType(MaxMessageType.REMOVE_GROUP_ID);
		payload[10] = DEFAULT_GROUP_ID;
	}

}
