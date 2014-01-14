package org.ambientlight.room;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ambientlight.AmbientControlMW;
import org.ambientlight.callback.CallBackManager;
import org.ambientlight.config.device.drivers.DeviceConfiguration;
import org.ambientlight.config.events.DailyAlarmEvent;
import org.ambientlight.config.room.RoomConfiguration;
import org.ambientlight.config.room.entities.alarm.AlarmManagerConfiguration;
import org.ambientlight.config.room.entities.scenery.SceneryManagerConfiguration;
import org.ambientlight.config.room.entities.switches.SwitchManagerConfiguration;
import org.ambientlight.device.drivers.DeviceDriver;
import org.ambientlight.device.drivers.DeviceDriverFactory;
import org.ambientlight.eventmanager.EventManager;
import org.ambientlight.messages.DispatcherManager;
import org.ambientlight.messages.QeueManager;
import org.ambientlight.process.ProcessManager;
import org.ambientlight.room.entities.alarm.AlarmManager;
import org.ambientlight.room.entities.climate.ClimateFactory;
import org.ambientlight.room.entities.lightobject.LightObjectManager;
import org.ambientlight.room.entities.lightobject.effects.RenderingEffectFactory;
import org.ambientlight.room.entities.sceneries.SceneryManager;
import org.ambientlight.room.entities.switches.SwitchManager;


public class RoomFactory {

	DeviceDriverFactory deviceFactory;


	public RoomFactory(DeviceDriverFactory deviceFactory, ProcessManager processFactory) {
		this.deviceFactory = deviceFactory;
	}


	public Room initRoom(RoomConfiguration roomConfig) throws UnknownHostException, IOException {
		// init room
		Room room = new Room();
		room.config = roomConfig;
		AmbientControlMW.setRoom(room);

		// init rooms pixelmap
		BufferedImage pixelMap = new BufferedImage(roomConfig.width, roomConfig.height, BufferedImage.TYPE_INT_ARGB);
		room.setRoomBitMap(pixelMap);

		// init lightObject rendering system
		RenderingEffectFactory effectFactory = new RenderingEffectFactory(room);
		room.lightObjectManager = new LightObjectManager(effectFactory);

		// start queueManager
		room.qeueManager = new QeueManager();
		room.qeueManager.dispatcherManager = new DispatcherManager();
		room.qeueManager.startQeues();

		// start climate Manager
		ClimateFactory climateFactory = new ClimateFactory();
		climateFactory.initClimateManager(room, roomConfig, room.qeueManager);

		// init CallbackManager
		CallBackManager callbackManager = new CallBackManager();
		room.callBackMananger = callbackManager;



		// initialize the device drivers
		List<DeviceDriver> devices = new ArrayList<DeviceDriver>();
		for (DeviceConfiguration currentDeviceConfig : roomConfig.deviceConfigurations) {
			devices.add(this.initializeDevice(currentDeviceConfig, room));
		}
		room.setDevices(devices);



		room.eventManager = new EventManager();

		createEventGenerators(room, room.eventManager);

		System.out.println("RoomFactory initRoom(): initialized ClimateManager");

		return room;
	}


	/**
	 * @param roomConfig
	 * @param room
	 */
	public void createEventGenerators(Room room, EventManager eventManager) {
		// initialize eventGenerators
		room.eventGenerators = new HashMap<String, EventGenerator>();

		for (EventGeneratorConfiguration currentConfig : room.config.eventGeneratorConfigurations.values()) {
			EventGenerator generator = null;
			if (currentConfig instanceof AlarmManagerConfiguration) {
				generator = new AlarmManager();
				AlarmManagerConfiguration alarmConfig = (AlarmManagerConfiguration) currentConfig;
				((AlarmManager) generator).createAlarm(new DailyAlarmEvent(alarmConfig.hour, alarmConfig.min, alarmConfig.name));
			}
			if (currentConfig instanceof SwitchManagerConfiguration) {
				generator = new SwitchManager();
			}
			if (currentConfig instanceof SceneryManagerConfiguration) {
				generator = new SceneryManager();
			}
			generator.config = currentConfig;
			generator.eventManager = eventManager;
			room.eventGenerators.put(generator.config.name, generator);
		}
	}





	private DeviceDriver initializeDevice(DeviceConfiguration deviceConfig, Room room) throws UnknownHostException, IOException {

		DeviceDriver device = deviceFactory.createByName(deviceConfig, room);

		return device;
	}

}
