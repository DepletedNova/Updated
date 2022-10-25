package com.depletednova.updated.foundation.registry.custom;

import com.depletednova.updated.foundation.registry.RegistryType;

import java.util.ArrayList;
import java.util.List;

public abstract class RegistryFactory {
	protected List<RegistryType> registriesToRun = new ArrayList<>();
	public void addToRegistry(RegistryType registry) { registriesToRun.add(registry); }
	
	public abstract void runFactory(boolean isClient);
	public abstract FactoryType getFactoryType();
	
	public enum FactoryType {
		CLIENT,
		SERVER,
		MIXED
	}
}
