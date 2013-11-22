package org.ambientlight.config.room.actors;

import java.io.Serializable;

import org.ambientlight.config.room.ISwitchableRoomItem;
import org.ambientlight.config.scenery.actor.ActorConductConfiguration;
import org.codehaus.jackson.annotate.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public abstract class ActorConfiguration implements ISwitchableRoomItem, Serializable {

	private static final long serialVersionUID = 1L;

	private boolean powerState;
	private String name;

	public ActorConductConfiguration actorConductConfiguration;


	@Override
	public boolean getPowerState() {
		return this.powerState;
	}


	@Override
	public void setPowerState(boolean powerState) {
		this.powerState = powerState;
	}


	@Override
	public String getName() {
		return this.name;
	}


	@Override
	public void setName(String name) {
		this.name = name;
	}
}
