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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.BlockDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.FallingBlockDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;

import fr.evercraft.everapi.EAMessage.EAMessages;
import fr.evercraft.everapi.services.entity.EntityTemplate;
import fr.evercraft.everapi.services.worldguard.WorldWorldGuard;
import fr.evercraft.everapi.services.worldguard.flag.type.EntityTemplateFlag;
import fr.evercraft.everapi.services.worldguard.flag.value.EntityPatternFlagValue;
import fr.evercraft.everworldguard.EWMessage.EWMessages;
import fr.evercraft.everworldguard.EverWorldGuard;

public class FlagDamageEntity extends EntityTemplateFlag {
	
	private static final String ALL = "ALL";
	
	private final EverWorldGuard plugin;
	private final Map<String, Set<EntityTemplate>> groups;
	private EntityPatternFlagValue<EntityTemplate, Entity> defaults;
	
	public FlagDamageEntity(EverWorldGuard plugin) {
		super("DAMAGE_ENTITY");
		
		this.plugin = plugin;
		
		this.groups = new ConcurrentHashMap<String, Set<EntityTemplate>>();
		this.defaults = new EntityPatternFlagValue<EntityTemplate, Entity>();
		
		this.reload();
	}
	
	public void reload() {
		this.groups.clear();
		this.groups.putAll(this.plugin.getProtectionService().getConfigFlags().getDamageEntity());
		
		Set<String> keys = this.groups.keySet();
		Set<EntityTemplate> values = new HashSet<EntityTemplate>();
		this.groups.values().forEach(value -> values.addAll(value));
		this.defaults = new EntityPatternFlagValue<EntityTemplate, Entity>(keys, values);
	}
	
	@Override
	public String getDescription() {
		return EWMessages.FLAG_DAMAGE_ENTITY_DESCRIPTION.getString();
	}
	
	public boolean sendMessage(Player player, Entity entity) {
		Vector3i position = entity.getLocation().getPosition().toInt();
		return this.plugin.getProtectionService().sendMessage(player, this,
				EWMessages.FLAG_DAMAGE_ENTITY_MESSAGE.sender()
					.replace("<x>", position.getX())
					.replace("<y>", position.getY())
					.replace("<z>", position.getZ())
					.replace("<entity>", entity.getType().getName()));
	}

	@Override
	public EntityPatternFlagValue<EntityTemplate, Entity> getDefault() {
		return this.defaults;
	}
	
	/*
	 * Suggest
	 */

	@Override
	public Collection<String> getSuggestAdd(CommandSource source, List<String> args) {
		return Stream.concat(
				this.groups.keySet().stream(),
				Stream.of(ALL))
			.filter(suggest -> !args.stream().anyMatch(arg -> arg.equalsIgnoreCase(suggest)))
			.collect(Collectors.toList());
	}
	
	@Override
	public String serialize(EntityPatternFlagValue<EntityTemplate, Entity> value) {
		return String.join(",", value.getKeys());
	}

	@Override
	public EntityPatternFlagValue<EntityTemplate, Entity> deserialize(String value) throws IllegalArgumentException {
		if (value.equalsIgnoreCase(ALL)) return this.defaults;
		if (value.isEmpty()) return new EntityPatternFlagValue<EntityTemplate, Entity>();
		
		Set<String> keys = new HashSet<String>();
		Set<EntityTemplate> values = new HashSet<EntityTemplate>();
		for (String key : value.split(PATTERN_SPLIT)) {
			Set<EntityTemplate> blocks = this.groups.get(key.toUpperCase());
			if (blocks != null) {
				keys.add(key.toUpperCase());
				values.addAll(blocks);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return new EntityPatternFlagValue<EntityTemplate, Entity>(keys, values);
	}
	
	@Override
	public Text getValueFormat(EntityPatternFlagValue<EntityTemplate, Entity> value) {
		if (value.getKeys().isEmpty()) {
			return EAMessages.FLAG_ENTITYTEMPLATE_EMPTY.getText();
		}
		
		List<Text> groups = new ArrayList<Text>();
		for (String group : value.getKeys()) {
			List<Text> entities = new ArrayList<Text>();
			for (EntityTemplate entity : this.groups.get(group)) {
				entities.add(EAMessages.FLAG_ENTITYTEMPLATE_HOVER.getFormat().toText("<entity>", entity.getId()));
			}
			groups.add(EAMessages.FLAG_ENTITYTEMPLATE_GROUP.getFormat().toText("<group>", group).toBuilder()
				.onHover(TextActions.showText(Text.joinWith(Text.of("\n"), entities)))
				.build());
		}
		
		return Text.joinWith(EAMessages.FLAG_ENTITYTEMPLATE_JOIN.getText(), groups);
	}
	
	/*
	 * CollideEntity
	 */
	
	public void onCollideEntity(WorldWorldGuard world, CollideEntityEvent event) {
		if (event.isCancelled()) return;
		
		Optional<Player> optPlayer = event.getCause().get(NamedCause.OWNER, Player.class);
		if (optPlayer.isPresent()) {
			this.onCollideEntityPlayer(world, event, optPlayer.get());
		} else {
			this.onCollideEntityNatural(world, event);
		}
	}
	
	public void onCollideEntityPlayer(WorldWorldGuard world, CollideEntityEvent event, Player player) {
		if (event.getCause().get(NamedCause.SOURCE, Projectile.class).isPresent()) {
			List<? extends Entity> filter = event.filterEntities(entity -> {
				if (this.getDefault().contains(entity) && !world.getRegions(entity.getLocation().getPosition()).getFlag(player, this).contains(entity, player)) {
					return false;
				}
				return true;
			});
			
			if (!filter.isEmpty()) {
				this.sendMessage(player, filter.get(0));
			}
		}
	}
	
	public void onCollideEntityNatural(WorldWorldGuard world, CollideEntityEvent event) {		
		if (event.getCause().get(NamedCause.SOURCE, Projectile.class).isPresent()) {
			event.filterEntities(entity -> {
				if (this.getDefault().contains(entity) && !world.getRegions(entity.getLocation().getPosition()).getFlagDefault(this).contains(entity)) {
					return false;
				}
				return true;
			});
		}
	}
	
	/*
	 * DamageEntity
	 */
	
	public void onDamageEntity(WorldWorldGuard world, DamageEntityEvent event) {
		if (event.isCancelled()) return;
		if (!this.getDefault().contains(event.getTargetEntity())) return;
		
		Entity entity = event.getTargetEntity();
		
		Object source = event.getCause().root();
		if (source instanceof FallingBlockDamageSource) {				
			FallingBlockDamageSource damageSource = (FallingBlockDamageSource) source;
			
			Optional<UUID> creator = damageSource.getSource().getCreator();
			if (creator.isPresent()) {
				Optional<Player> player = this.plugin.getEServer().getPlayer(creator.get());
				if (player.isPresent()) {
					this.onDamageEntity(world, event, entity, player.get());
				} else {
					this.onDamageEntity(world, event, entity);
				}
			} else {
				this.onDamageEntity(world, event, entity);
			}
		} else if (source instanceof IndirectEntityDamageSource) {				
			IndirectEntityDamageSource damageSource = (IndirectEntityDamageSource) source;
			
			if (damageSource.getIndirectSource() instanceof Player) {
				this.onDamageEntity(world, event, entity, (Player) damageSource.getIndirectSource());
			} else {
				this.onDamageEntity(world, event, entity);
			}
		} else if (source instanceof EntityDamageSource) {				
			EntityDamageSource damageSource = (EntityDamageSource) source;
			
			if (damageSource.getSource() instanceof Player) {
				if (this.onDamageEntity(world, event, entity, (Player) damageSource.getSource())) {
					// Message
					this.sendMessage((Player) damageSource.getSource(), entity);
				}
			} else {
				this.onDamageEntity(world, event, entity);
			}
		} else if (source instanceof BlockDamageSource) {
			BlockDamageSource damageSource = (BlockDamageSource) source;
			
			// TODO Bug BUCKET : no creator
			Optional<UUID> creator = damageSource.getBlockSnapshot().getCreator();
			if (creator.isPresent()) {
				Optional<Player> player = this.plugin.getEServer().getPlayer(creator.get());
				
				if (player.isPresent()) {
					if (this.onDamageEntity(world, event, entity, player.get())) {
						// TODO Bug IgniteEntityEvent : no implemented
						if (damageSource.getType().equals(DamageTypes.FIRE)) {
							entity.offer(Keys.FIRE_TICKS, 0);
						}
					}
				} else {
					if (this.onDamageEntity(world, event, entity)) {
						// TODO Bug IgniteEntityEvent : no implemented
						if (damageSource.getType().equals(DamageTypes.FIRE)) {
							entity.offer(Keys.FIRE_TICKS, 0);
						}
					}
				}
			} else {
				if (this.onDamageEntity(world, event, entity)) {
					// TODO Bug IgniteEntityEvent : no implemented
					if (damageSource.getType().equals(DamageTypes.FIRE)) {
						entity.offer(Keys.FIRE_TICKS, 0);
					}
				}
			}
		} else if (source instanceof DamageSource) {
			DamageSource damageSource = (DamageSource) source;
			
			if (damageSource.getType().equals(DamageTypes.SUFFOCATE)) {
				Location<World> location = entity.getLocation().add(Vector3d.from(0, 2, 0));
				Optional<UUID> creator = location.getExtent().getCreator(location.getBlockPosition());
				if (creator.isPresent()) {
					Optional<Player> player = this.plugin.getEServer().getPlayer(creator.get());
					if (player.isPresent()) {
						this.onDamageEntity(world, event, entity, player.get());
					} else {
						this.onDamageEntity(world, event, entity);
					}
				} else {
					this.onDamageEntity(world, event, entity);
				}
			}
		}
	}
	
	public boolean onDamageEntity(WorldWorldGuard world, DamageEntityEvent event, Entity entity, Player player) {
		if (!world.getRegions(entity.getLocation().getPosition()).getFlag(player, this).contains(event.getTargetEntity(), player)) {
			event.setCancelled(true);
			return true;
		}
		return false;
	}
	
	public boolean onDamageEntity(WorldWorldGuard world, DamageEntityEvent event, Entity entity) {
		if (!world.getRegions(entity.getLocation().getPosition()).getFlagDefault(this).contains(event.getTargetEntity())) {
			event.setCancelled(true);
			return true;
		}
		return false;
	}
}
