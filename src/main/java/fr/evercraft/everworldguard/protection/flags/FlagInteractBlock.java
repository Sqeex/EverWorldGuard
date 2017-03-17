/*
 * This file is part of EverWorldGuard.
 *
 * EverWorldGuard is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * EverWorldGuard is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with EverWorldGuard.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.evercraft.everworldguard.protection.flags;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import fr.evercraft.everapi.services.worldguard.WorldWorldGuard;
import fr.evercraft.everapi.services.worldguard.flag.Flags;
import fr.evercraft.everapi.services.worldguard.flag.type.EntryFlag;
import fr.evercraft.everapi.services.worldguard.flag.value.EntryFlagValue;
import fr.evercraft.everworldguard.EWMessage.EWMessages;
import fr.evercraft.everworldguard.EverWorldGuard;

public class FlagInteractBlock extends EntryFlag<String, BlockType> {
	
	private static final String ALL = "ALL";
	
	private final EverWorldGuard plugin;
	private final Map<String, Set<BlockType>> groups;
	private EntryFlagValue<String, BlockType> defaults;
	
	public FlagInteractBlock(EverWorldGuard plugin) {
		super("INTERACT_BLOCK");
		
		this.plugin = plugin;
		
		this.groups = new ConcurrentHashMap<String, Set<BlockType>>();
		this.defaults = new EntryFlagValue<String, BlockType>();
		
		this.reload();
	}
	
	public void reload() {
		this.groups.clear();
		this.groups.putAll(this.plugin.getProtectionService().getConfigFlags().getInteracts());
		
		Set<String> keys = this.groups.keySet();
		Set<BlockType> values = new HashSet<BlockType>();
		this.groups.values().forEach(value -> values.addAll(value));
		this.defaults = new EntryFlagValue<String, BlockType>(keys, values);
	}
	
	@Override
	public String getDescription() {
		return EWMessages.FLAG_INTERACT_BLOCK.getString();
	}

	@Override
	public EntryFlagValue<String, BlockType> getDefault() {
		return this.defaults;
	}
	
	/*
	 * Suggest
	 */

	@Override
	public Collection<String> getSuggestAdd(List<String> args) {
		return Stream.concat(
				this.groups.keySet().stream(),
				Stream.of(ALL))
			.filter(suggest -> !args.stream().anyMatch(arg -> arg.equalsIgnoreCase(suggest)))
			.collect(Collectors.toList());
	}
	
	@Override
	public String serialize(EntryFlagValue<String, BlockType> value) {
		return String.join(",", value.getKeys());
	}

	@Override
	public EntryFlagValue<String, BlockType> deserialize(String value) throws IllegalArgumentException {
		if (value.equalsIgnoreCase(ALL)) return this.defaults;
		if (value.isEmpty()) return new EntryFlagValue<String, BlockType>();
		
		Set<String> keys = new HashSet<String>();
		Set<BlockType> values = new HashSet<BlockType>();
		for (String key : value.split(PATTERN_SPLIT)) {
			Set<BlockType> blocks = this.groups.get(key.toUpperCase());
			if (blocks != null) {
				keys.add(key.toUpperCase());
				values.addAll(blocks);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return new EntryFlagValue<String, BlockType>(keys, values);
	}
	
	/*
	 * InteractBlockEvent.Secondary
	 */

	@Listener(order=Order.FIRST)
	public void onInteractBlockSecondary(WorldWorldGuard world, InteractBlockEvent.Secondary event, Location<World> location) {
		if (event.isCancelled()) return;
		
		BlockType type = event.getTargetBlock().getState().getType();
		Optional<Player> optPlayer = event.getCause().first(Player.class);
		if (optPlayer.isPresent()) {
			this.onChangeBlockPlayer(world, event, location, type, optPlayer.get());
		} else {
			this.onChangeBlockNatural(world, event, location, type);
		}
	}
	
	private void onChangeBlockPlayer(WorldWorldGuard world, InteractBlockEvent.Secondary event, Location<World> location, BlockType type, Player player) {		
		if (this.getDefault().containsValue(type) && !world.getRegions(location.getPosition()).getFlag(player, Flags.INTERACT_BLOCK).containsValue(type)) {
			event.setUseBlockResult(Tristate.FALSE);
		}
	}
	
	private void onChangeBlockNatural(WorldWorldGuard world, InteractBlockEvent.Secondary event, Location<World> location, BlockType type) {
		if (this.getDefault().containsValue(type) && !world.getRegions(location.getPosition()).getFlagDefault(Flags.INTERACT_BLOCK).containsValue(type)) {
			event.setUseBlockResult(Tristate.FALSE);
		}
	}
	
	/*
	 * ChangeBlockEvent.Modify
	 */
	
	@Listener(order=Order.FIRST)
	public void onChangeBlockModify(WorldWorldGuard world, ChangeBlockEvent.Modify event) {
		if (event.isCancelled()) return;
		
		Optional<Player> optPlayer = event.getCause().first(Player.class);
		if (optPlayer.isPresent()) {
			this.onChangeBlockModifyPlayer(world, event, optPlayer.get());
		} else {
			this.onChangeBlockModifyNatural(world, event);
		}
	}
	
	private void onChangeBlockModifyPlayer(WorldWorldGuard world, ChangeBlockEvent.Modify event, Player player) {
		event.getTransactions().forEach(transaction -> {
			BlockType type = transaction.getOriginal().getState().getType();
			
			if (this.getDefault().containsValue(type) && !world.getRegions(transaction.getOriginal().getPosition()).getFlag(player, Flags.INTERACT_BLOCK).containsValue(type)) {
				event.setCancelled(true);
			}
		});
	}
	
	private void onChangeBlockModifyNatural(WorldWorldGuard world, ChangeBlockEvent.Modify event) {
		event.getTransactions().forEach(transaction -> {
			BlockType type = transaction.getOriginal().getState().getType();
			
			if (this.getDefault().containsValue(type) && !world.getRegions(transaction.getOriginal().getPosition()).getFlagDefault(Flags.INTERACT_BLOCK).containsValue(type)) {
				event.setCancelled(true);
			}
		});
	}
	
	/*
	 * Projectile
	 */
	
	@Listener(order=Order.FIRST)
	public void onChangeBlockBreak(WorldWorldGuard world, ChangeBlockEvent.Break event) {
		if (event.isCancelled()) return;
		if (!event.getCause().get(NamedCause.SOURCE, Projectile.class).isPresent()) return;
		
		Optional<Player> optPlayer = event.getCause().first(Player.class);
		if (optPlayer.isPresent()) {
			this.onChangeBlockBreakPlayer(world, event, optPlayer.get());
		} else {
			this.onChangeBlockBreakNatural(world, event);
		}
	}
	
	private void onChangeBlockBreakPlayer(WorldWorldGuard world, ChangeBlockEvent.Break event, Player player) {		
		event.getTransactions().forEach(transaction -> {
			BlockType type = transaction.getOriginal().getState().getType();
			
			if (this.getDefault().containsValue(type) && !world.getRegions(transaction.getOriginal().getPosition()).getFlag(player, Flags.INTERACT_BLOCK).containsValue(type)) {
				transaction.setValid(false);
			}
		});
	}
	
	private void onChangeBlockBreakNatural(WorldWorldGuard world, ChangeBlockEvent.Break event) {
		event.getTransactions().forEach(transaction -> {
			BlockType type = transaction.getOriginal().getState().getType();
			
			if (this.getDefault().containsValue(type) && !world.getRegions(transaction.getOriginal().getPosition()).getFlagDefault(Flags.INTERACT_BLOCK).containsValue(type)) {
				transaction.setValid(false);
			}
		});
	}
}
