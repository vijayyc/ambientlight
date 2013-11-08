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

package test;

import org.ambientlight.device.drivers.MaxVCubeDeviceConfiguration;
import org.ambientlight.messages.DispatcherManager;
import org.ambientlight.messages.DispatcherType;
import org.ambientlight.messages.QeueManager;


/**
 * @author Florian Bornkessel
 * 
 */
public class MessageQueueTest {

	public static void main(String[] args) {
		QeueManager manager = new QeueManager();
		MessageDump dump = new MessageDump();
		manager.registerMessageListener(DispatcherType.MAX, dump);

		MaxVCubeDeviceConfiguration config = new MaxVCubeDeviceConfiguration();
		config.hostName = "ambi-schlafen";
		config.port = 30000;
		DispatcherManager df = new DispatcherManager();
		manager.dispatcherManager = df;
		df.createDispatcher(config, manager);
		manager.startQeues();

	}
}
