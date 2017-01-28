package fr.evercraft.everworldguard.service.index;

import java.util.Set;

import com.flowpowered.math.vector.Vector3i;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

import fr.evercraft.everapi.services.worldguard.flag.Flag;
import fr.evercraft.everapi.services.worldguard.flag.FlagValue;
import fr.evercraft.everapi.services.worldguard.regions.ProtectedRegion;
import fr.evercraft.everapi.services.worldguard.regions.SetProtectedRegion;
import fr.evercraft.everworldguard.regions.EProtectedRegion;

public class ESetProtectedRegion implements SetProtectedRegion {

	private final Set<ProtectedRegion> regions;
	
	public ESetProtectedRegion(Vector3i positon, Set<EProtectedRegion> regions) {
		Builder<ProtectedRegion> builder = ImmutableSet.builder();
		regions.stream()
			.filter(region -> region.containsPosition(positon))
			.forEach(region -> builder.add(region));
		this.regions = builder.build();
	}
	
	@Override
	public <T extends Flag<V>, V> FlagValue<V> getFlag(T flag) {
		return null;
	}
	
	@Override
	public Set<ProtectedRegion> getAll() {
		return this.regions;
	}
}