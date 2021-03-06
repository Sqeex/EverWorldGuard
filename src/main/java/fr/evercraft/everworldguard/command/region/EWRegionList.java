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
package fr.evercraft.everworldguard.command.region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import fr.evercraft.everapi.EAMessage.EAMessages;
import fr.evercraft.everapi.message.format.EFormat;
import fr.evercraft.everapi.plugin.command.Args;
import fr.evercraft.everapi.plugin.command.ESubCommand;
import fr.evercraft.everapi.server.player.EPlayer;
import fr.evercraft.everapi.server.user.EUser;
import fr.evercraft.everapi.services.worldguard.region.ProtectedRegion;
import fr.evercraft.everapi.sponge.UtilsContexts;
import fr.evercraft.everworldguard.EWMessage.EWMessages;
import fr.evercraft.everworldguard.EWPermissions;
import fr.evercraft.everworldguard.EverWorldGuard;

public class EWRegionList extends ESubCommand<EverWorldGuard> {
	
	public static final String MARKER_WORLD = "-w";
	public static final String MARKER_PLAYER = "-p";
	public static final String MARKER_GROUP = "-g";
	
	private final Args.Builder pattern;
	
	public EWRegionList(final EverWorldGuard plugin, final EWRegion command) {
        super(plugin, command, "list");
        
        this.pattern = Args.builder()
    		.value(MARKER_WORLD, 
					(source, args) -> this.getAllWorlds(),
					(source, args) -> args.getArgs().size() <= 1)
			.value(MARKER_PLAYER, (source, args) -> this.getAllPlayers(source, false))
			.value(MARKER_GROUP, (source, args) ->  {
				List<String> suggests = new ArrayList<String>();
				Optional<String> optWorld = args.getArg(0);
				
				if (optWorld.isPresent()) {
					this.plugin.getEServer().getWorld(optWorld.get()).ifPresent(world -> 
						suggests.addAll(this.getAllGroups(world)));
				} else if (source instanceof Player) {
					suggests.addAll(this.getAllGroups(((Player) source).getWorld()));
				}
				
				return suggests;
			});
    }
	
	@Override
	public boolean testPermission(final CommandSource source) {
		return source.hasPermission(EWPermissions.REGION_INFO.get());
	}

	@Override
	public Text description(final CommandSource source) {
		return EWMessages.REGION_LIST_DESCRIPTION.getText();
	}

	@Override
	public Text help(final CommandSource source) {
		return Text.builder("/" + this.getName() + " [-w " + EAMessages.ARGS_WORLD.getString() + "]"
												 + " [-p " + EAMessages.ARGS_PLAYER.getString() + " | -g " + EAMessages.ARGS_GROUP.getString() + "]")
				.onClick(TextActions.suggestCommand("/" + this.getName() + " "))
				.color(TextColors.RED)
				.build();
	}
	
	@Override
	public Collection<String> tabCompleter(final CommandSource source, final List<String> args) throws CommandException {
		return this.pattern.suggest(source, args);
	}
	
	@Override
	public CompletableFuture<Boolean> execute(final CommandSource source, final List<String> args_list) throws CommandException {
		Args args = this.pattern.build(args_list);
		
		if (args.getArgs().size() > 0) {
			source.sendMessage(this.help(source));
			return CompletableFuture.completedFuture(false);
		}
		
		World world = null;
		Optional<String> arg_world = args.getValue(MARKER_WORLD);
		if (arg_world.isPresent()) {
			Optional<World> optWorld = this.plugin.getEServer().getWorld(arg_world.get());
			if (optWorld.isPresent()) {
				world = optWorld.get();
			} else {
				EAMessages.WORLD_NOT_FOUND.sender()
					.prefix(EWMessages.PREFIX)
					.replace("<world>", arg_world.get())
					.sendTo(source);
				return CompletableFuture.completedFuture(false);
			}
		} else if (source instanceof EPlayer) {
			world = ((EPlayer) source).getWorld();
		} else {
			EAMessages.COMMAND_ERROR_FOR_PLAYER.sender()
				.prefix(EWMessages.PREFIX)
				.sendTo(source);
			return CompletableFuture.completedFuture(false);
		}
		
		Optional<String> arg_player = args.getValue(MARKER_PLAYER);
		Optional<String> arg_group = args.getValue(MARKER_GROUP);
		
		if (arg_player.isPresent() && arg_group.isPresent()) {
			source.sendMessage(this.help(source));
			return CompletableFuture.completedFuture(false);
		
		// Commande pour les régions d'un joueurs
		} else if (arg_player.isPresent()) {
			if (source.hasPermission(EWPermissions.REGION_LIST_OTHERS.get())) {
				return this.commandRegionListPlayer(source, world, arg_player.get());
			} else {
				EAMessages.NO_PERMISSION.sender()
					.prefix(EWMessages.PREFIX)
					.sendTo(source);
				return CompletableFuture.completedFuture(false);
			}
		
		// Commande pour les régions d'un groupe
		} else if (arg_group.isPresent()) {
			if (source.hasPermission(EWPermissions.REGION_LIST_OTHERS.get())) {
				return this.commandRegionListGroup(source, world, arg_group.get());
			} else {
				EAMessages.NO_PERMISSION.sender()
					.prefix(EWMessages.PREFIX)
					.sendTo(source);
				return CompletableFuture.completedFuture(false);
			}
		
		// Commande pour toutes les régions
		} else {
			if (source.hasPermission(EWPermissions.REGION_LIST_OTHERS.get())) {
				return this.commandRegionList(source, world);
			} else if(source instanceof EPlayer) {
				return this.commandRegionListPlayer(source, world, (EPlayer) source);
			} else {
				EAMessages.COMMAND_ERROR_FOR_PLAYER.sender()
					.prefix(EWMessages.PREFIX)
					.sendTo(source);
				return CompletableFuture.completedFuture(false);
			}
		}
	}

	private CompletableFuture<Boolean> commandRegionList(final CommandSource player, final World world) {
		TreeMap<String, Text> list = new TreeMap<String, Text>();
		for (ProtectedRegion region : this.plugin.getProtectionService().getOrCreateEWorld(world).getAll()) {
			list.put(region.getName(), EWMessages.REGION_LIST_ALL_LINE.getFormat()
					.toText("<region>", Text.builder(region.getName())
								.onShiftClick(TextActions.insertText(region.getName()))
								.build(),
							"<type>", region.getType().getNameFormat(),
							"<priority>", String.valueOf(region.getPriority()))
					.toBuilder()
					.onClick(TextActions.suggestCommand(
							"/" + this.getParentName() + " info -w \"" + world.getName() + "\" \"" + region.getName() + "\""))
					.build());
		}
		
		if (list.isEmpty()) {
			list.put("", EWMessages.REGION_LIST_ALL_EMPTY.getText());
		}
		
		this.plugin.getEverAPI().getManagerService().getEPagination().sendTo(
				EWMessages.REGION_LIST_ALL_TITLE.getFormat()
					.toText("<world>", world.getName())
					.toBuilder()
					.onClick(TextActions.runCommand("/" + this.getName() + " -w \"" + world.getName() + "\""))
					.build(), 
				new ArrayList<Text>(list.values()), player);
		
		return CompletableFuture.completedFuture(true);
	}

	private CompletableFuture<Boolean> commandRegionListPlayer(final CommandSource staff, final World world, final String playerString) {
		Optional<EUser> user = this.plugin.getEServer().getEUser(playerString);
		// Le joueur est introuvable
		if (!user.isPresent()) {
			EAMessages.PLAYER_NOT_FOUND.sender()
				.prefix(EWMessages.PREFIX)
				.replace("<player>", playerString)
				.sendTo(staff);
			return CompletableFuture.completedFuture(false);
		}
		
		return this.commandRegionListPlayer(staff, world, user.get());
	}
	
	private CompletableFuture<Boolean> commandRegionListPlayer(final CommandSource staff, final World world, final EUser user) {
		TreeMap<String, Text> list = new TreeMap<String, Text>();
		for (ProtectedRegion region : this.plugin.getProtectionService().getOrCreateEWorld(world).getAll()) {
			if (region.isOwnerOrMember(user, UtilsContexts.get(world.getName()))) {
				list.put(region.getName(), EWMessages.REGION_LIST_PLAYER_LINE.getFormat()
						.toText("<region>", Text.builder(region.getName())
									.onShiftClick(TextActions.insertText(region.getName()))
									.build(),
								"<type>", region.getType().getNameFormat(),
								"<priority>", String.valueOf(region.getPriority()))
						.toBuilder()
						.onClick(TextActions.suggestCommand(
								"/" + this.getParentName() + " info -w \"" + world.getName() + "\" \"" + region.getName() + "\""))
						.build());
			}
		}
		
		if (list.isEmpty()) {
			list.put("", EWMessages.REGION_LIST_PLAYER_EMPTY.getText());
		}
		
		EFormat title = null;
		if (user.getIdentifier().equalsIgnoreCase(staff.getIdentifier())) {
			title = EWMessages.REGION_LIST_PLAYER_TITLE_EQUALS.getFormat();
		} else {
			title = EWMessages.REGION_LIST_PLAYER_TITLE_OTHERS.getFormat();
		}
		
		this.plugin.getEverAPI().getManagerService().getEPagination().sendTo(
				title
					.toText("<world>", world.getName(),
							"<player>", user.getName())
					.toBuilder()
					.onClick(TextActions.runCommand("/" + this.getName() + " -w \"" + world.getName() + "\" -p \"" + user.getIdentifier() + "\""))
					.build(), 
				new ArrayList<Text>(list.values()), staff);
		
		return CompletableFuture.completedFuture(true);
	}
	
	private CompletableFuture<Boolean> commandRegionListGroup(final CommandSource player, final World world, final String groupString) {
		Subject group = this.plugin.getEverAPI().getManagerService().getPermission().getGroupSubjects().get(groupString);
		// Le joueur est introuvable
		if (group == null){
			EAMessages.GROUP_NOT_FOUND.sender()
				.prefix(EWMessages.PREFIX)
				.replace("<player>", groupString)
				.sendTo(player);
			return CompletableFuture.completedFuture(false);
		}
		
		TreeMap<String, Text> list = new TreeMap<String, Text>();
		for (ProtectedRegion region : this.plugin.getProtectionService().getOrCreateEWorld(world).getAll()) {
			if (region.isOwnerOrMember(group)) {
				list.put(region.getName(), EWMessages.REGION_LIST_GROUP_LINE.getFormat()
						.toText("<region>", Text.builder(region.getName())
									.onShiftClick(TextActions.insertText(region.getName()))
									.build(),
								"<type>", region.getType().getNameFormat(),
								"<priority>", String.valueOf(region.getPriority()))
						.toBuilder()
						.onClick(TextActions.suggestCommand(
								"/" + this.getParentName() + " info -w \"" + world.getName() + "\" \"" + region.getName() + "\""))
						.build());
			}
		}
		
		if (list.isEmpty()) {
			list.put("", EWMessages.REGION_LIST_GROUP_EMPTY.getText());
		}
		
		this.plugin.getEverAPI().getManagerService().getEPagination().sendTo(
				EWMessages.REGION_LIST_GROUP_TITLE.getFormat()
					.toText("<world>", world.getName(),
							"<group>", group.getIdentifier())
					.toBuilder()
					.onClick(TextActions.runCommand("/" + this.getName() + " -w \"" + world.getName() + "\" -g \"" + group.getIdentifier() + "\""))
					.build(), 
				new ArrayList<Text>(list.values()), player);
		
		return CompletableFuture.completedFuture(true);
	}
}
