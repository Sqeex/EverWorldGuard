package fr.evercraft.everworldguard.service.index;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

import fr.evercraft.everapi.services.worldguard.regions.ProtectedRegion;
import fr.evercraft.everapi.sponge.UtilsChunk;
import fr.evercraft.everapi.util.LongHashTable;
import fr.evercraft.everworldguard.EverWorldGuard;
import fr.evercraft.everworldguard.regions.EProtectedRegion;
import fr.evercraft.everworldguard.service.storage.RegionStorage;
import fr.evercraft.everworldguard.service.storage.conf.RegionStorageConf;
import fr.evercraft.everworldguard.service.storage.sql.RegionStorageSql;

public class EManagerWorld {
	
	private final EverWorldGuard plugin;
	
	private RegionStorage storage;
	
	private final ConcurrentHashMap<String, EProtectedRegion> regions;
	private final LongHashTable<EManagerChunk> cache;
	
	private final World world;
	
	public EManagerWorld(EverWorldGuard plugin, World world) {
		Preconditions.checkNotNull(plugin, "plugin");
		
		this.plugin = plugin;
		this.world = world;
		this.regions = new ConcurrentHashMap<String, EProtectedRegion>();		
		this.cache = new LongHashTable<EManagerChunk>();
		
		if (this.plugin.getDataBase().isEnable()) {
			this.storage = new RegionStorageSql(this.plugin, this.world);
		} else {
			this.storage = new RegionStorageConf(this.plugin, this.world);
		}
		
		this.start();
	}

	public void reload() {
		if (this.plugin.getDataBase().isEnable() && !(this.storage instanceof RegionStorageSql)) {
			this.storage = new RegionStorageSql(this.plugin, this.world);
		} else if (!this.plugin.getDataBase().isEnable() && !(this.storage instanceof RegionStorageConf)) {
			this.storage = new RegionStorageConf(this.plugin, this.world);
		}
		
		this.start();
	}
	
	public void start() {
		this.plugin.getLogger().info("Loading region for world '" + this.world.getName() + "' ...");
		
		this.regions.clear();
		this.storage.getAll().forEach(region -> this.regions.put(region.getIdentifier().toLowerCase(), region));
		
		this.plugin.getLogger().info("Loading " + this.regions.size() + " region(s) for world '" + this.world.getName() + "'.");
	}
	
	public void stop() {
		this.plugin.getLogger().info("Region data changes made in '" + this.world.getName() + "' have been background saved.");
	}
	
	public Set<ProtectedRegion> getAll() {
		return ImmutableSet.copyOf(this.regions.values());
	}
	
	/*
	 * Chunk
	 */
	public EManagerChunk getChunk(final Vector3i chunk) {
		return this.getChunk(chunk.getX(), chunk.getZ());
	}
	
	public EManagerChunk getChunk(final int x, final int z) {
		EManagerChunk value = this.cache.get(x, z);
		if (value == null) {
			value = new EManagerChunk(this.plugin, Vector3i.from(x, 0, z), this.regions);
		}
		return value;
	}
	
	public EManagerChunk loadChunk(final Vector3i chunk) {
		EManagerChunk value = this.cache.get(chunk.getX(), chunk.getZ());
		if (value == null) {
			value = new EManagerChunk(this.plugin, chunk, this.regions);
			this.cache.put(chunk.getX(), chunk.getZ(), value);
		}
		return value;
	}
	
	public boolean unLoadChunk(final int x, final int z) {
		return this.cache.remove(x, z) != null;
	}
	
	/*
	 * Block
	 */
	
	public ESetProtectedRegion getRegions(final Vector3i position) {
		return this.getChunk(position.getX() >> UtilsChunk.CHUNK_SHIFTS, position.getX() >> UtilsChunk.CHUNK_SHIFTS).getPosition(position);
	}

	/*
	 * Region
	 */
	
	public Optional<ProtectedRegion> getRegion(String region) {
		return Optional.ofNullable(this.regions.get(region));
	}
}