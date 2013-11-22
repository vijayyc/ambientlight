package test;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ambientlight.config.device.drivers.LK35CLientDeviceConfiguration;
import org.ambientlight.config.device.led.LedPointConfiguration;
import org.ambientlight.config.process.EventProcessConfiguration;
import org.ambientlight.config.process.NodeConfiguration;
import org.ambientlight.config.process.events.SceneryEntryEvent;
import org.ambientlight.config.process.events.SwitchEvent;
import org.ambientlight.config.process.handler.actor.ConfigurationChangeHandlerConfiguration;
import org.ambientlight.config.process.handler.actor.PowerstateHandlerConfiguration;
import org.ambientlight.config.process.handler.actor.SimplePowerStateHandlerConfiguration;
import org.ambientlight.config.process.handler.event.EventGeneratorSensorAdapterConfiguration;
import org.ambientlight.config.process.handler.event.EventToBooleanHandlerConfiguration;
import org.ambientlight.config.process.handler.event.FireEventHandlerConfiguration;
import org.ambientlight.config.process.handler.expression.DecisionHandlerConfiguration;
import org.ambientlight.config.process.handler.expression.ExpressionConfiguration;
import org.ambientlight.config.room.ISwitchableRoomItem;
import org.ambientlight.config.room.RoomConfiguration;
import org.ambientlight.config.room.actors.LightObjectConfiguration;
import org.ambientlight.config.room.eventgenerator.SceneryEventGeneratorConfiguration;
import org.ambientlight.config.room.eventgenerator.SwitchEventGeneratorConfiguration;
import org.ambientlight.config.scenery.UserSceneryConfiguration;
import org.ambientlight.config.scenery.actor.renderingprogram.SimpleColorRenderingProgramConfiguration;
import org.ambientlight.device.drivers.DeviceDriverFactory;
import org.ambientlight.room.RoomConfigurationFactory;


public class KuecheTestConfig {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		DeviceDriverFactory df = new DeviceDriverFactory();
		KuecheTestConfig test = new KuecheTestConfig();
		RoomConfigurationFactory.beginTransaction();
		RoomConfigurationFactory.commitTransaction(test.getTestRoom(), "Kochen");
	}


	public RoomConfiguration getTestRoom() {
		RoomConfiguration rc = new RoomConfiguration();

		LK35CLientDeviceConfiguration lk35 = new LK35CLientDeviceConfiguration();
		lk35.hostName = "ambi-lk35";
		lk35.port = 8899;
		LedPointConfiguration ledPoint = new LedPointConfiguration();
		ledPoint.xPosition = 0;
		ledPoint.yPosition = 0;
		ledPoint.port = 1;
		lk35.configuredLeds.add(ledPoint);
		rc.deviceConfigurations.add(lk35);

		LightObjectConfiguration lo = new LightObjectConfiguration();
		lo.setName("Highboard");
		lo.height = 10;
		lo.layerNumber = 1;
		lo.width = 10;
		lo.xOffsetInRoom = 0;
		lo.yOffsetInRoom = 0;
		lo.actorConductConfiguration = this.createSimpleColor();
		rc.actorConfigurations.put(lo.getName(), lo);

		SwitchEventGeneratorConfiguration triggerMainSwitch = new SwitchEventGeneratorConfiguration();
		triggerMainSwitch.name = "Lichtschalter";
		rc.eventGeneratorConfigurations.put("Lichtschalter", triggerMainSwitch);

		SceneryEventGeneratorConfiguration sceneryEventGenerator = new SceneryEventGeneratorConfiguration();
		sceneryEventGenerator.name = "RoomSceneryEventGenerator";
		rc.eventGeneratorConfigurations.put("RoomSceneryEventGenerator", sceneryEventGenerator);

		rc.height = 10;
		rc.width = 40;
		rc.roomName = "Kochen";

		UserSceneryConfiguration userScenario = new UserSceneryConfiguration();
		userScenario.id = "Kochen";
		sceneryEventGenerator.sceneries.add(userScenario);

		sceneryEventGenerator.currentScenery = userScenario;

		EventProcessConfiguration roomSwitchProcess = new EventProcessConfiguration();
		roomSwitchProcess.run = true;
		roomSwitchProcess.id = "Licht schalten";
		rc.processes.add(roomSwitchProcess);
		SwitchEvent triggerConfOn = new SwitchEvent();
		triggerConfOn.sourceName = triggerMainSwitch.name;
		triggerConfOn.powerState = true;

		SwitchEvent triggerConfOff = new SwitchEvent();
		triggerConfOff.sourceName = triggerMainSwitch.name;
		triggerConfOff.powerState = false;

		roomSwitchProcess.eventTriggerConfigurations.add(triggerConfOn);
		roomSwitchProcess.eventTriggerConfigurations.add(triggerConfOff);

		NodeConfiguration eventMapperNode = new NodeConfiguration();
		eventMapperNode.id = 0;
		EventToBooleanHandlerConfiguration eventMapper = new EventToBooleanHandlerConfiguration();
		eventMapperNode.nextNodeIds.add(1);
		eventMapperNode.actionHandler = eventMapper;
		roomSwitchProcess.nodes.put(0, eventMapperNode);

		NodeConfiguration decissionNode = new NodeConfiguration();
		decissionNode.id = 1;
		DecisionHandlerConfiguration decission = new DecisionHandlerConfiguration();
		decissionNode.nextNodeIds.add(2);
		decissionNode.nextNodeIds.add(3);
		ExpressionConfiguration expression = new ExpressionConfiguration();
		decission.expressionConfiguration = expression;
		expression.expression = "#{tokenValue}==1.0";
		decissionNode.actionHandler = decission;
		roomSwitchProcess.nodes.put(1, decissionNode);

		NodeConfiguration grabCurrentSceneryNode = new NodeConfiguration();
		grabCurrentSceneryNode.id = 2;
		EventGeneratorSensorAdapterConfiguration grabSceneryHandler = new EventGeneratorSensorAdapterConfiguration();
		grabSceneryHandler.eventSensorId = "RoomSceneryEventGenerator";
		grabCurrentSceneryNode.nextNodeIds.add(4);
		grabCurrentSceneryNode.actionHandler = grabSceneryHandler;
		roomSwitchProcess.nodes.put(2, grabCurrentSceneryNode);

		NodeConfiguration fireEventNode = new NodeConfiguration();
		fireEventNode.id = 4;
		FireEventHandlerConfiguration fireEventHandler = new FireEventHandlerConfiguration();
		fireEventHandler.event = null;
		fireEventNode.actionHandler = fireEventHandler;
		roomSwitchProcess.nodes.put(4, fireEventNode);

		NodeConfiguration turnOffNode = new NodeConfiguration();
		turnOffNode.id = 3;
		SimplePowerStateHandlerConfiguration powerDownHandler = new SimplePowerStateHandlerConfiguration();
		powerDownHandler.powerState = false;
		turnOffNode.actionHandler = powerDownHandler;
		roomSwitchProcess.nodes.put(3, turnOffNode);

		ArrayList<LightObjectConfiguration> changeConfigFor = new ArrayList<LightObjectConfiguration>();
		changeConfigFor.add(lo);
		ArrayList<ISwitchableRoomItem> turnLightOnFor = new ArrayList<ISwitchableRoomItem>();
		turnLightOnFor.add(lo);

		createUserScenario(rc, userScenario, changeConfigFor, turnLightOnFor);

		return rc;
	}


	private void createUserScenario(RoomConfiguration rc, UserSceneryConfiguration userScenario,
			List<LightObjectConfiguration> lo, List<ISwitchableRoomItem> itemsToPutOn) {

		EventProcessConfiguration process = new EventProcessConfiguration();
		process.run = true;
		process.id = "Kochen";
		NodeConfiguration startNode = new NodeConfiguration();
		startNode.id = 0;

		SceneryEntryEvent triggerSceneryChange = new SceneryEntryEvent();
		triggerSceneryChange.sceneryName = "Kochen";
		triggerSceneryChange.sourceName = "RoomSceneryEventGenerator";
		process.eventTriggerConfigurations.add(triggerSceneryChange);

		ConfigurationChangeHandlerConfiguration cHandler = new ConfigurationChangeHandlerConfiguration();
		for (LightObjectConfiguration current : lo) {
			cHandler.actorConfiguration.put(current.getName(), createSimpleColor());
		}
		startNode.actionHandler = cHandler;
		startNode.nextNodeIds.add(1);

		NodeConfiguration switchNode = new NodeConfiguration();
		switchNode.id = 1;

		PowerstateHandlerConfiguration powerstatehandler = new PowerstateHandlerConfiguration();
		for (ISwitchableRoomItem current : itemsToPutOn) {
			powerstatehandler.powerStateConfiguration.put(current.getName(), true);
		}
		switchNode.actionHandler = powerstatehandler;

		process.nodes.put(0, startNode);
		process.nodes.put(1, switchNode);

		rc.processes.add(process);
	}


	private SimpleColorRenderingProgramConfiguration createSimpleColor() {

		SimpleColorRenderingProgramConfiguration scL01 = new SimpleColorRenderingProgramConfiguration();
		int i = (int) (Math.random() * 256);

		Color color = new Color(255, 255, 255);
		scL01.rgb = color.getRGB();
		return scL01;
	}
}
