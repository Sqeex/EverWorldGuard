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
package fr.evercraft.everworldguard.selection;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;

import com.google.common.base.Preconditions;

import fr.evercraft.everapi.services.selection.CUIMessage;
import fr.evercraft.everapi.services.selection.CUIRegion;
import fr.evercraft.everapi.services.selection.SelectionType;
import fr.evercraft.everapi.services.selection.SubjectSelection;
import fr.evercraft.everworldguard.EverWorldGuard;
import fr.evercraft.everworldguard.selection.cui.ShapeCuiMessage;
import fr.evercraft.everworldguard.selection.selector.ECuboidSelector;
import fr.evercraft.everworldguard.selection.selector.ECylinderSelector;
import fr.evercraft.everworldguard.selection.selector.EEllipsoidSelector;
import fr.evercraft.everworldguard.selection.selector.EPolygonalSelector;
import fr.evercraft.everworldguard.selection.selector.ESelector;

public class ESelectionSubject implements SubjectSelection {
	
	private final EverWorldGuard plugin;
	
	private final UUID identifier;
	private ESelector selector;
	
	private boolean cuiSupport;
	private int cuiVersion;
	
	public ESelectionSubject(final EverWorldGuard plugin, final UUID identifier) {
		Preconditions.checkNotNull(plugin, "plugin");
		Preconditions.checkNotNull(identifier, "identifier");
		
		this.plugin = plugin;
		this.identifier = identifier;
		
		this.cuiSupport = false;
		this.cuiVersion = -1;
		
		this.selector = new ECuboidSelector(this);
	}
	
	@Override
	public ESelector getSelector() {
		return this.selector;
	}
	
	@Override
	public void setType(SelectionType type) {
		Preconditions.checkNotNull(type, "type");
				
		if (this.selector.getType().equals(type)) return;
		
		if (type.equals(SelectionType.CUBOID)) {
			this.selector = new ECuboidSelector(this);
		} else if (type.equals(SelectionType.POLYGONAL)) {
			this.selector = new EPolygonalSelector(this);
		} else if (type.equals(SelectionType.CYLINDER)) {
			this.selector = new ECylinderSelector(this);
		} else if (type.equals(SelectionType.ELLIPSOID)) {
			this.selector = new EEllipsoidSelector(this);
		}
		
		if (this.selector instanceof CUIRegion) {
			CUIRegion cui = (CUIRegion) this.selector;
			this.dispatchCUIEvent(new ShapeCuiMessage(cui.getTypeID()));
		}
	}

	@Override
	public SelectionType getType() {
		return this.selector.getType();
	}
	
	@Override
	public boolean isCuiSupport() {
		return this.cuiSupport;
	}
	
	@Override
	public void setCuiSupport(boolean cuiSupport) {
		this.cuiSupport = cuiSupport;
	}
	
	@Override
	public int getCUIVersion() {
		return this.cuiVersion;
	}
	
	public void setCUIVersion(int version) {
		this.cuiVersion = version;
	}
	
	public void describeCUI() {
		this.plugin.getEServer().getPlayer(this.getUniqueId()).ifPresent(player -> this.describeCUI(player));
	}
	
	@Override
	public void describeCUI(Player player) {
		Preconditions.checkNotNull(player, "player");
		
		if (!this.isCuiSupport()) return;
		
		if (this.selector instanceof CUIRegion) {
			CUIRegion cui = (CUIRegion) this.selector;

            if (cui.getProtocolVersion() > this.cuiVersion) {
            	cui.describeLegacyCUI();
            } else {
            	cui.describeCUI();
            }
		}
	}
	
	public void describeCUISelection(Player player) {
		Preconditions.checkNotNull(player, "player");
		
		if (!this.isCuiSupport()) return;
		
		if (this.selector instanceof CUIRegion) {
			CUIRegion cui = (CUIRegion) this.selector;

            if (cui.getProtocolVersion() > this.cuiVersion) {
            	this.dispatchCUIEvent(player, new ShapeCuiMessage(cui.getLegacyTypeID()));
            	cui.describeLegacyCUI();
            } else {
            	this.dispatchCUIEvent(player, new ShapeCuiMessage(cui.getTypeID()));
            	cui.describeCUI();
            }
		}
	}
	
	public void dispatchCUIEvent(CUIMessage message) {
		this.plugin.getEServer().getPlayer(this.getUniqueId()).ifPresent(player -> this.dispatchCUIEvent(player, message));
	}
	
	public void dispatchCUIEvent(Player player, CUIMessage message) {
		Preconditions.checkNotNull(message, "message");
		
		 String[] params = message.getParameters();
	     String send = message.getTypeId();
	     if (params.length > 0) {
	    	 send = send + "|" + String.join("|", params);
	     }

	     String finalData = send;
	     this.plugin.getSelectionService().getCUIChannel().getChannel()
	     	.sendTo(player, buffer -> buffer.writeBytes(finalData.getBytes(StandardCharsets.UTF_8)));
	}
	
	public String getIdentifier() {
		return this.identifier.toString();
	}
	
	public UUID getUniqueId() {
		return this.identifier;
	}
}
