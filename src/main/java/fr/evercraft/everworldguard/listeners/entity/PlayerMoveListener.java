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
package fr.evercraft.everworldguard.listeners.entity;

import java.util.Optional;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import fr.evercraft.everapi.registers.MoveType;
import fr.evercraft.everapi.registers.MoveType.MoveTypes;
import fr.evercraft.everworldguard.EverWorldGuard;
import fr.evercraft.everworldguard.protection.subject.EUserSubject;

public class PlayerMoveListener {
	
	private EverWorldGuard plugin;

	public PlayerMoveListener(EverWorldGuard plugin) {
		this.plugin = plugin;
	}
	
	/*
	 * RespawnPlayerEvent
	 */
	
	@Listener(order=Order.FIRST)
	public void onRespawnPlayerFirst(RespawnPlayerEvent event) {
		Optional<EUserSubject> optSubject = this.plugin.getProtectionService().getSubject(event.getOriginalPlayer().getUniqueId());
		if (!optSubject.isPresent()) return;
		EUserSubject subject = optSubject.get();
		
		// Non cancellable
		subject.moveToPre(event.getTargetEntity(), event.getToTransform().getLocation(), MoveTypes.RESPAWN, event.getCause());
	}
	
	@Listener(order=Order.POST)
	public void onRespawnPlayerPost(RespawnPlayerEvent event) {
		Optional<EUserSubject> optSubject = this.plugin.getProtectionService().getSubject(event.getOriginalPlayer().getUniqueId());
		if (!optSubject.isPresent()) return;
		EUserSubject subject = optSubject.get();
		
		subject.moveToPost(event.getTargetEntity(), event.getToTransform().getLocation(), MoveTypes.RESPAWN, event.getCause());
	}
	
	/*
	 * MoveEntityEvent
	 */
	
	@Listener(order=Order.FIRST)
	public void onMoveEntityFirst(MoveEntityEvent event, @Getter("getTargetEntity") Player player) {
		if (event.isCancelled()) return;
		
		Optional<EUserSubject> optSubject = this.plugin.getProtectionService().getSubject(player.getUniqueId());
		if (!optSubject.isPresent()) return;
		EUserSubject subject = optSubject.get();

		subject.moveToPre(player, event.getToTransform().getLocation(), this.getMoveType(event, player, subject), event.getCause())
			.ifPresent(location -> {
				Transform<World> transform = event.getFromTransform()
					.setLocation(location.add(Vector3d.from(0.5, 0, 0.5)));
				event.setToTransform(transform);
				
				Entity vehicle = player.getVehicle().orElse(null);
				if (vehicle != null) {
					while (vehicle != null) {
						vehicle.clearPassengers();
						vehicle.setVelocity(Vector3d.ZERO);
						
						// Permet de pas teleport les entités en dehors de la region
						// TODO Minecart : Source == Player
						if (event.getCause().get(NamedCause.SOURCE, vehicle.getClass()).isPresent()) {
							if (vehicle instanceof Living) {
								vehicle.setTransform(transform);
							} else {
								vehicle.setTransform(transform.addTranslation(Vector3d.from(0, 1, 0)));
							}
						}
						
						vehicle = vehicle.getVehicle().orElse(null);
					}
					event.setCancelled(true);
					player.setTransform(transform.addTranslation(Vector3d.from(0, 1, 0)));
				}
			});
	}
	
	@Listener(order=Order.POST)
	public void onMoveEntityPost(MoveEntityEvent event, @Getter("getTargetEntity") Player player) {
		if (event.isCancelled()) return;
		
		Optional<EUserSubject> optSubject = this.plugin.getProtectionService().getSubject(player.getUniqueId());
		if (!optSubject.isPresent()) return;
		EUserSubject subject = optSubject.get();

		subject.moveToPost(player, event.getToTransform().getLocation(), this.getMoveType(event, player, subject), event.getCause());
	}
	
	private MoveType getMoveType(MoveEntityEvent event, Player player, EUserSubject subject) {
		// TELEPORT
		if (event instanceof MoveEntityEvent.Teleport) {
			return MoveTypes.TELEPORT;
		}
		
		// RIDE
		if (player.getVehicle().isPresent()) {
			if (subject.getLastRide()) {
				return MoveTypes.RIDE;
			} else {
				subject.setLastRide(true);
				return MoveTypes.EMBARK;
			}
		}
		
		subject.setLastRide(false);
		return MoveTypes.MOVE;
	}
}
