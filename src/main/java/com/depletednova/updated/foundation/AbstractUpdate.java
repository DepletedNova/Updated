package com.depletednova.updated.foundation;

import com.depletednova.updated.Updated;
import net.minecraft.util.Identifier;

public abstract class AbstractUpdate {
	public void registerUpdate() {
		this.titleIdentifier = Updated.ID("textures/gui/title/"+getUpdateID()+"_card.png");
		registerServer();
	}
	public Identifier titleIdentifier;
	
	public abstract void registerServer();
	public abstract void registerClient();
	
	public abstract String getUpdateID();
	public abstract int getIteration();
	public abstract int getTitleWidth();
}
