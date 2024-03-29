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

package org.ambient.control;

import org.ambient.roomservice.RoomConfigService;
import org.ambientlight.ws.Room;


/**
 * @author Florian Bornkessel
 * 
 */
public interface IRoomServiceCallbackListener {

	public void onRoomServiceConnected(RoomConfigService service);


	/**
	 * will be called when any configuration changes on the server have been done by this client, a different user or by the
	 * server itself. The service holds an actual configuration already when this method is called and passes a reference into the
	 * fragment here if you want to use it to update the views datamodels.
	 * 
	 * @param roomName
	 * @param roomConfiguration
	 */
	public void onRoomConfigurationChange(String roomName, Room roomConfiguration);


	public void setRoomService(RoomConfigService roomService);
}
