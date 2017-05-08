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
package fr.evercraft.everworldguard.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import fr.evercraft.everworldguard.EverWorldGuard;

public class ItemStackListener {
	
	private EverWorldGuard plugin;

	public ItemStackListener(EverWorldGuard plugin) {
		this.plugin = plugin;
	}
	
	@Listener(order=Order.FIRST)
	public void onInteractEntity(UseItemStackEvent.Start event) {		
		// Debug
		/*List<Text> list = new ArrayList<Text>();
		event.getCause().getNamedCauses().forEach((key, value) -> {
			list.add(Text.builder(key)
					.onHover(TextActions.showText(Text.of(EChat.fixLength(value.toString(), 254))))
					.onClick(TextActions.suggestCommand(EChat.fixLength(value.toString(), 254)))
					.build());
		});
		this.plugin.getEServer().getBroadcastChannel().send(Text.of("UseItemStackEvent.Start : ").concat(Text.joinWith(Text.of(", "), list)));*/
	}
	
	@Listener(order=Order.FIRST)
	public void onInteractItem(InteractItemEvent event) {
		this.plugin.getManagerFlags().ENDERPEARL.onInteractItem(event);
		
		// Debug
		/*List<Text> list = new ArrayList<Text>();
		event.getCause().getNamedCauses().forEach((key, value) -> {
			list.add(Text.builder(key)
					.onHover(TextActions.showText(Text.of(EChat.fixLength(value.toString(), 254))))
					.onClick(TextActions.suggestCommand(EChat.fixLength(value.toString(), 254)))
					.build());
		});
		this.plugin.getEServer().getBroadcastChannel().send(Text.of("InteractItemEvent : ").concat(Text.joinWith(Text.of(", "), list)));*/
	}

}
