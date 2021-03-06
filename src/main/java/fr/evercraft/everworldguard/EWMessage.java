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
package fr.evercraft.everworldguard;

import com.google.common.base.Preconditions;

import fr.evercraft.everapi.message.EMessageBuilder;
import fr.evercraft.everapi.message.EMessageFormat;
import fr.evercraft.everapi.message.format.EFormatString;
import fr.evercraft.everapi.plugin.file.EMessage;
import fr.evercraft.everapi.plugin.file.EnumMessage;
import fr.evercraft.everapi.services.worldguard.WorldGuardService;

public class EWMessage extends EMessage<EverWorldGuard> {

	public EWMessage(final EverWorldGuard plugin) {
		super(plugin, EWMessages.values());
	}
	
	public enum EWMessages implements EnumMessage {
		PREFIX("prefix", 				"[&4Ever&6&lWG&f] "),
		DESCRIPTION("description",		"Plugin de protection des régions"), 
		
		MIGRATE_DESCRIPTION("migrate.description",									"Transfère les données"),
		MIGRATE_SQL_CONFIRMATION("migrate.sqlConfirmation", 						"&7Souhaitez-vous vraiment transférer les données dans une base de données &6SQL&7 ? <confirmation>"),
		MIGRATE_SQL_CONFIRMATION_VALID("migrate.sqlConfirmationValid", 				"&2&n[Confirmer]"),
		MIGRATE_SQL_CONFIRMATION_VALID_HOVER("migrate.sqlConfirmationValidHover", 	"&cCliquez ici pour réaliser le transfert"),
		MIGRATE_CONF_CONFIRMATION("migrate.confConfirmation", 						"&7Souhaitez-vous vraiment transférer les données dans des &6fichiers de configuration&7 ? <confirmation>"),
		MIGRATE_CONF_CONFIRMATION_VALID("migrate.confConfirmationValid", 			"&2&n[Confirmer]"),
		MIGRATE_CONF_CONFIRMATION_VALID_HOVER("migrate.confConfirmationValidHover", "&cCliquez ici pour réaliser le transfert"),
		MIGRATE_SQL("migrate.sql", 													"&7Les données ont bien été transférées dans la base de données."),
		MIGRATE_SQL_LOG("migrate.sqlLog", 											"Les données ont été transférées dans la base de données par <player>."),
		MIGRATE_CONF("migrate.conf", 												"&7Les données ont bien été transférées dans les fichiers de configurations."),
		MIGRATE_CONF_LOG("migrate.confLog", 										"Les données ont été transférées dans les fichiers de configurations par <player>."),
		MIGRATE_DISABLE("migrate.disable", 											"&cErreur : Vous devez être connecté à une base de données pour faire le transfert des données."),
		
		CLEAR_DESCRIPTION("clear.description",										"Supprimé toutes les régions d'un monde"),
		CLEAR_WORLD_CONFIRMATION("clear.worldConfirmation", 						"&7Souhaitez-vous vraiment supprimer tous les régions du monde &6<world> &7? <confirmation>"),
		CLEAR_WORLD_CONFIRMATION_VALID("clear.worldConfirmationValid", 				"&2&n[Confirmer]"),
		CLEAR_WORLD_CONFIRMATION_VALID_HOVER("clear.worldConfirmationValidHover", 	"&cCliquez ici pour valider la suppression"),
		CLEAR_WORLD_PLAYER("clear.worldPlayer", 									"&7Tous les régions du monde <world> ont bien été supprimées."),
		CLEAR_WORLD_LOG("clear.worldLog", 											"Tous les régions du monde <world> ont été supprimées par <player>."),
		CLEAR_ALL_CONFIRMATION("clear.allConfirmation", 							"&7Souhaitez-vous vraiment supprimer tous les régions du serveur &7? <confirmation>"),
		CLEAR_ALL_CONFIRMATION_VALID("clear.allConfirmationValid", 					"&2&n[Confirmer]"),
		CLEAR_ALL_CONFIRMATION_VALID_HOVER("clear.allConfirmationValidHover", 		"&cCliquez ici pour valider la suppression"),
		CLEAR_ALL_PLAYER("clear.allPlayer", 										"&7Tous les régions du serveur ont bien été supprimées."),
		CLEAR_ALL_LOG("clear.allLog", 												"Tous les régions du serveur ont été supprimées par <player>."),
		
		GROUP_NOT_FOUND("groupNotFound",											"&cErreur : Le group '&6<group>&c' est introuvable."), 
		GROUP_INCOMPATIBLE("groupIncompatible",										"&cErreur : Le group '&6<group>&c' est incompatible avec le flag <flag>."), 
		FLAG_NOT_FOUND("flagNotFound",												"&cErreur : Le flag '&6<flag>&c' est introuvable."), 
		
		SELECT_DESCRIPTION("select.description",									"Permet de sélectionner une région"),
		
		SELECT_CUI_DESCRIPTION("select.cui.description",								"Permet de voir les régions (Require : WorldEdit CUI)"),
		
		SELECT_INFO_DESCRIPTION("select.info.description",								"Affiche les informations sur la région sélectionnée"),
		SELECT_INFO_POS("select.info.pos",												"&7(&6<x>&7, &6<y>&7, &6<z>&7)"),
		SELECT_INFO_POS_HOVER("select.info.posHover",									"&7X : &6<x>[RT]&7Y : &6<y>[RT]&7Z : &6<z>"),
		SELECT_INFO_CUBOID_TITLE("select.info.cuboidTitle",								"&7Votre sélection &6CUDOID&7"),
		SELECT_INFO_CUBOID_POS1_AND_POS2("select.info.cuboidPos1AndPos2",				"    &6&l➤  &6Position 1 : &c<pos1>[RT]"
																					  + "    &6&l➤  &6Position 2 : &c<pos2>[RT]"
																					  + "    &6&l➤  &6Volume : &7<area>"),
		SELECT_INFO_CUBOID_POS1("select.info.cuboidPos1",								"    &6&l➤  &6Position 1 : &7<pos1>"),
		SELECT_INFO_CUBOID_POS2("select.info.cuboidPos2",								"    &6&l➤  &6Position 2 : &7<pos2>"),
		SELECT_INFO_CUBOID_EMPTY("select.info.cuboidEmpty",								"    &cAucune position sélectionnée."),
		SELECT_INFO_EXTEND_TITLE("select.info.extendTitle",								"&7Votre sélection &6EXTEND&7"),
		SELECT_INFO_EXTEND_POS1_AND_POS2("select.info.extendPos1AndPos2",				"    &6&l➤  &6Position 1 : &7<pos1>[RT]"
																					  + "    &6&l➤  &6Position 2 : &7<pos2>[RT]"
																					  + "    &6&l➤  &6Volume : &7<area>"),	
		SELECT_INFO_EXTEND_POS1("select.info.extendPos1",								"    &6&l➤  &6Position 1 : &7<pos1>"),
		SELECT_INFO_EXTEND_POS2("select.info.extendPos2",								"    &6&l➤  &6Position 2 : &7<pos2>"),
		SELECT_INFO_EXTEND_EMPTY("select.info.extendEmpty",								"    &cAucune position sélectionnée."),
		SELECT_INFO_POLY_TITLE("select.info.polyTitle",									"&7Votre sélection &6POLYGONAL &7(&6<area>&7)"),
		SELECT_INFO_POLY_LINE("select.info.polyLine",									"    &6&l➤  &6#<num> : &7<pos>"),
		SELECT_INFO_POLY_EMPTY("select.info.polyEmpty",									"    Aucune position sélectionnée."),
		SELECT_INFO_CYLINDER_TITLE("select.info.cylinderTitle",								"&7Votre sélection &6CYLINDER&7"),
		SELECT_INFO_CYLINDER_CENTER_AND_RADIUS("select.info.cylinderCenterAndRadius",	"    &6&l➤  &6Centre : &7<center>[RT]"
																					  + "    &6&l➤  &6Longueur : &7<width>[RT]"
																					  + "    &6&l➤  &6Hauteur : &7<height>[RT]"
																					  + "    &6&l➤  &6Profondeur : &7<length>[RT]"
																					  + "    &6&l➤  &6Volume : &7<area>"),
		SELECT_INFO_CYLINDER_CENTER("select.info.cylinderCenter",						"    &6&l➤  &6Centre : &7<center>"),
		SELECT_INFO_CYLINDER_EMPTY("select.info.cylinderEmpty",							"&cAucune position sélectionnée."),
		SELECT_INFO_ELLIPSOID_TITLE("select.info.ellipsoidTitle",						"&7Votre sélection &6ELLIPSOID&7"),
		SELECT_INFO_ELLIPSOID_CENTER_AND_RADIUS("select.info.ellipsoidCenterAndRadius",	"    &6&l➤  &6Centre : &7<center>[RT]"
																					  + "    &6&l➤  &6Longueur : &7<width>[RT]"
																					  + "    &6&l➤  &6Hauteur : &7<height>[RT]"
																					  + "    &6&l➤  &6Profondeur : &7<length>[RT]"
																					  + "    &6&l➤  &6Volume : &7<area>"),
		SELECT_INFO_ELLIPSOID_CENTER("select.info.ellipsoidCenter",						"    &6&l➤  &6Centre : &7<center>"),
		SELECT_INFO_ELLIPSOID_EMPTY("select.info.ellipsoidEmpty",						"    Aucune position sélectionnée."),
		SELECT_INFO_SPHERE_TITLE("select.info.sphereTitle",								"&7Votre sélection &6SPHERE&7"),
		SELECT_INFO_SPHERE_CENTER_AND_RADIUS("select.info.sphereCenterAndRadius",		"    &6&l➤  &6Centre : &7<center>[RT]"
																					  + "    &6&l➤  &6Rayon : &7<radius>[RT]"
																					  + "    &6&l➤  &6Volume : &7<area>"),
		SELECT_INFO_SPHERE_CENTER("select.info.sphereCenter",							"    &6&l➤  &6Centre : &7<center>"),
		SELECT_INFO_SPHERE_EMPTY("select.info.sphereEmpty",								"    Aucune position sélectionnée."),
		
		SELECT_POS1_DESCRIPTION("select.pos1.description",			"Défini  la première position"),
		SELECT_POS1_ONE("select.pos1.one",							"&7Sélection de la position 1 : <position>."),
		SELECT_POS1_TWO("select.pos1.two",							"&7Sélection de la position 1 : <position> &7(&6<area>&7)."),
		SELECT_POS1_POLY("select.pos1.poly",						"&7Sélection de la position #1 : <position>."),
		SELECT_POS1_CENTER("select.pos1.center",					"&7Sélection du centre : <position>."),
		SELECT_POS1_EQUALS("select.pos1.equals",					"&cErreur : Vous avez déjà sélectionnée une position."),
		SELECT_POS1_CANCEL("select.pos1.cancel",					"&cErreur : Impossible de sélectionner une position pour le moment."),
		
		SELECT_POS2_DESCRIPTION("select.pos2.description",			"Défini  la deuxième position"),
		SELECT_POS2_ONE("select.pos2.one",							"&7Sélection de la position 2 : <position>."),
		SELECT_POS2_TWO("select.pos2.two",							"&7Sélection de la position 2 : <position> &7(&6<area>&7)."),
		SELECT_POS2_POLY_ONE("select.pos2.polyOnde",				"&7Ajout de la position &6#<num> &7: <position>."),
		SELECT_POS2_POLY_ALL("select.pos2.polyAll",					"&7Ajout de la position &6#<num> &7: <position> &7(&6<area>&7)."),
		SELECT_POS2_POLY_ERROR("select.pos2.polyError",				"&cErreur : Vous avez déjà sélectionné le nombre maximum de position."),
		SELECT_POS2_RADIUS("select.pos2.radius",					"&7Sélection d'un rayon de <radius> &7block(s) : <position>."),
		SELECT_POS2_NO_CENTER("select.pos2.noCenter",				"&cErreur : Aucune position centrale sélectionnée."),
		SELECT_POS2_EQUALS("select.pos2.equals",					"&cErreur : Vous avez déjà sélectionnée cette position."),
		SELECT_POS2_CANCEL("select.pos2.cancel",					"&cErreur : Impossible de sélectionner une position pour le moment."),
		
		SELECT_CLEAR_DESCRIPTION("select.clear.description",		"Supprime toutes les positions sélectionnées"),
		SELECT_CLEAR_PLAYER("select.clear.player",					"&7Vous n'avez plus aucune position."),
		
		SELECT_REMOVE_DESCRIPTION("select.remove.description",		"Supprime la dernière  position sélectionnée d'un polygone"),
		SELECT_REMOVE_PLAYER("select.remove.player",				"&7Vous avez supprimée la position : <pos>."),
		SELECT_REMOVE_EMPTY("select.remove.empty",					"&4Erreur : Vous n'avez aucune position sélectionnée."),
		SELECT_REMOVE_ERROR("select.remove.error",					"&4Erreur : Uniquement pour le type &62D Polygonal&c."),
		
		
		SELECT_EXPAND_DESCRIPTION("select.expand.description",					"Permet d'étendre la sélection"),
		SELECT_EXPAND_VERT("select.expand.vert", 								"&7Vous avez étendu votre sélection de &6<size> &7block(s) [Bas-En-Haut]."),
		SELECT_EXPAND_DIRECTION("select.expand.direction", 						"&7Vous avez étendu votre sélection de &6<size> &7block(s) [<amount> <direction>]."),
		SELECT_EXPAND_DIRECTION_OPPOSITE("select.expand.directionOpposite",		"&7Vous avez étendu votre sélection de &6<size> &7block(s) [<amount> <direction>] [<amount_opposite> <direction_opposite>]."),
		SELECT_EXPAND_ERROR_OPERATION("select.expand.errorOperation",			"&4Erreur : Vous ne pouvez pas faire cette opération sur ce type de région."),
		SELECT_EXPAND_ERROR_NO_REGION("select.expand.errorNoRegion",			"&4Erreur : Vous devez d'abord sélectionner une région."),
		
		SELECT_CONTRACT_DESCRIPTION("select.contract.description",				"Permet de réduire la sélection"),
		SELECT_CONTRACT_DIRECTION("select.contract.direction", 					"&7Vous avez réduit votre sélection de &6<size> &7block(s) [<amount> <direction>]."),
		SELECT_CONTRACT_DIRECTION_OPPOSITE("select.contract.directionOpposite",	"&7Vous avez réduit votre sélection de &6<size> &7block(s) [<amount> <direction>] [<amount_opposite> <direction_opposite>]."),
		SELECT_CONTRACT_ERROR_OPERATION("select.contract.errorOperation",		"&4Erreur : Vous ne pouvez pas faire cette opération sur ce type de région."),
		SELECT_CONTRACT_ERROR_NO_REGION("select.contract.errorNoRegion",		"&4Erreur : Vous devez d'abord sélectionner une région."),
		
		SELECT_SHIFT_DESCRIPTION("select.shift.description",					"Permet de déplacer la sélection"),
		SELECT_SHIFT_DIRECTION("select.shift.direction", 						"&7Vous avez déplacé votre sélection de &6<amount> &7block(s) vers la &6<direction>&7."),
		SELECT_SHIFT_ERROR_OPERATION("select.shift.errorOperation",				"&4Erreur : Vous ne pouvez pas faire cette opération sur ce type de région."),
		SELECT_SHIFT_ERROR_NO_REGION("select.shift.errorNoRegion",				"&4Erreur : Vous devez d'abord sélectionner une région."),
		
		SELECT_TYPE_DESCRIPTION("select.type.description",		"&7Change le type de sélection"),
		
		SELECT_TYPE_CUBOID("select.type.cuboid",				"&7Cuboid : Clic gauche pour définir le point N°1 et clic droit pour définir le point N°2.",
																"&7Cuboid : left click for point 1, right for point 2."),
		SELECT_TYPE_EXTEND("select.type.extend",				"&7Extend : Clic gauche pour définir le point N°1 et clic droit pour définir le point N°2.",
																"&7Extend : left click for point 1, right for point 2."),
		SELECT_TYPE_POLYGONAL("select.type.poly",				"&72D Polygonal : Clic gauche pour définir le premier point et clic droit pour définir les points suivants.",
																"&72D polygon selector: Left/right click to add a point."),
		SELECT_TYPE_CYLINDER("select.type.cylinder",			"&7Cylindrique : Clic gauche pour définir le centre, clic droit pour définir le rayon.",
																"&7Cylindrical select: Left click=center, right click to extend."),
		SELECT_TYPE_ELLIPSOID("select.type.ellipsoid",			"&7Ellipsoid : Clic gauche pour définir le centre, clic droit pour définir les formes.",
																"&7Ellipsoid select: Left click=center, right click to extend."),
		SELECT_TYPE_SPHERE("select.type.sphere",				"&7Sphere : Clic gauche pour définir le centre, clic droit pour définir les formes.",
																"&7Sphere select: Left click=center, right click to extend."),
		SELECT_TYPE_EQUALS("select.type.equals",				"&cErreur : Sélection &6<type> &cdéjà activée"),
		SELECT_TYPE_CANCEL("select.type.cancel",				"&cErreur : Impossible de changer de type de sélection pour le moment."),
		
		REGION_DESCRIPTION("region.description",				"Gère les régions protégées"),
		REGION_NO_PERMISSION("region.noPermission",				"&cErreur : Vous n'avez pas la permission pour cette région &6<region>&c."),
		
		REGION_LOAD_DESCRIPTION("region.load.description",		"Rechargement des régions d'un monde"), 
		REGION_LOAD_MESSAGE("region.load.message",				"&7Rechargement du monde &6<world>&7."), 
		
		REGION_BYPASS_DESCRIPTION("region.bypass.description", 						"Active/Désactive le mode admin"),
		
		REGION_BYPASS_ON_PLAYER("region.bypass.onPlayer", 							"&7Vous avez activé le mode admin."),
		REGION_BYPASS_ON_PLAYER_ERROR("region.bypass.onPlayerError", 				"&cErreur : Le mode admin est déjà activé."),
		REGION_BYPASS_ON_OTHERS_PLAYER("region.bypass.onOthersPlayer", 				"&7Le mode admin est désormais activé grâce à &6<staff>&7."),
		REGION_BYPASS_ON_OTHERS_STAFF("region.bypass.onOthersStaff", 				"&7Vous avez activé le mode admin pour &6<player>&7."),
		REGION_BYPASS_ON_OTHERS_ERROR("region.bypass.onOthersError", 				"&cErreur : Le mode admin de &6<player> &cest déjà activé."),
		
		REGION_BYPASS_OFF_PLAYER("region.bypass.offPlayer", 						"&7Vous avez désactivé le mode admin."),
		REGION_BYPASS_OFF_PLAYER_ERROR("region.bypass.offPlayerError", 				"&cErreur : Le mode admin est déjà désactivé."),
		REGION_BYPASS_OFF_OTHERS_PLAYER("region.bypass.offOthersPlayer", 			"&7Le mode admin est désormais désactivé à cause de &6<staff>&7."),
		REGION_BYPASS_OFF_OTHERS_STAFF("region.bypass.offOthersStaff", 				"&7Vous avez désactivé le mode admin pour &6<player>&7."),
		REGION_BYPASS_OFF_OTHERS_ERROR("region.bypass.offOthersError", 				"&7Erreur : Le mode admin de &6<player> &cest déjà désactivé."),
		
		REGION_BYPASS_STATUS_PLAYER_ON("region.bypass.statusPlayerOn", 				"&7Le mode admin est activé."),
		REGION_BYPASS_STATUS_PLAYER_OFF("region.bypass.statusPlayerOff", 			"&7Le mode admin est désactivé.."),
		REGION_BYPASS_STATUS_OTHERS_ON("region.bypass.statusOthersOn", 				"&7Le mode admin de &6<player> &7est activé."),
		REGION_BYPASS_STATUS_OTHERS_OFF("region.bypass.statusOthersOff", 			"&7Le mode admin de &6<player> &7est désactivé."),
		
		REGION_CHECK_DESCRIPTION("region.check.description", 						"Permet savoir la valeur de chaque flag"),
		
		REGION_CHECK_GROUP_TITLE("region.check.groupTitle", 						"&aListe des flags : &6<group>"),
		REGION_CHECK_GROUP_LINE("region.check.groupLine", 							"        &a&l- <flag> : &c<value> &7(Région : &a<region>&7)"),
		REGION_CHECK_GROUP_LINE_DEFAULT("region.check.groupLineDefault", 			"        &a&l- <flag> : &c<value>"),
		
		REGION_CHECK_FLAG_TITLE("region.check.flagTitle", 							"&aListe des valeurs : &6<flag>"),
		REGION_CHECK_FLAG_DEFAULT("region.check.flagDefault", 						"    &6&l➤   &6Default : &c<value> &7(Région : &a<region>&7)"),
		REGION_CHECK_FLAG_DEFAULT_DEFAULT("region.check.flagDefaultDefault", 		"    &6&l➤   &6Default : &c<value>"),
		REGION_CHECK_FLAG_MEMBER("region.check.flagMember", 						"    &6&l➤   &6Member : &c<value> &7(Région : &a<region>&7)"),
		REGION_CHECK_FLAG_MEMBER_DEFAULT("region.check.flagMemberDefault", 			"    &6&l➤   &6Member : &c<value>"),
		REGION_CHECK_FLAG_OWNER("region.check.flagOwner", 							"    &6&l➤   &6Owner : &c<value> &7(Région : &a<region>&7)"),
		REGION_CHECK_FLAG_OWNER_DEFAULT("region.check.flagOwneDefault", 			"    &6&l➤   &6Owner : &c<value>"),
		
		REGION_INFO_DESCRIPTION("region.info.description",													"Permet de voir la liste des régions sur votre position"),
		REGION_INFO_ONE_TITLE("region.info.one.title",														"&aRégion Info : &6<region>"),
		REGION_INFO_ONE_WORLD("region.info.one.world",														"    &6&l➤  &6World : &c<world>"),
		REGION_INFO_ONE_TYPE("region.info.one.type",														"    &6&l➤  &6Type : &c<type>"),
		REGION_INFO_ONE_POINTS("region.info.one.points",													"    &6&l➤  &6Points : &c<positions>"),
		REGION_INFO_ONE_POINTS_CUBOID("region.info.one.pointsCuboid",										"&6(&c<min_x>&6, &c<min_y>&6, &c<min_z>&6) (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_INFO_ONE_POINTS_CUBOID_HOVER("region.info.one.pointsCuboidHover",							"&6Min : (&c<min_x>&6, &c<min_y>&6, &c<min_z>&6)[RT]&6Max : (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_INFO_ONE_POINTS_POLYGONAL("region.info.one.pointsPolygonal",									"&6(&c<min_x>&6, &c<min_y>&6, &c<min_z>&6) (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_INFO_ONE_POINTS_POLYGONAL_HOVER("region.info.one.pointsPolygonalHover",						"&6Les positions : [RT]<positions>"),
		REGION_INFO_ONE_POINTS_POLYGONAL_HOVER_POSITIONS("region.info.one.pointsPolygonalHoverPositions",	"&6#<num> : (&c<x>&6, &c<y>&6, &c<z>&6)"),
		REGION_INFO_ONE_POINTS_POLYGONAL_HOVER_JOIN("region.info.one.pointsPolygonalHoverJoin",				"[RT]"),
		REGION_INFO_ONE_PRIORITY("region.info.one.priority",												"    &6&l➤  &6Priorité : &c<prority>"),
		REGION_INFO_ONE_PARENT("region.info.one.parent",													"    &6&l➤  &6Parent : &c<parent>"),
		REGION_INFO_ONE_HERITAGE("region.info.one.heritage",												"    &6&l➤  &6Héritage :"),
		REGION_INFO_ONE_HERITAGE_LINE("region.info.one.heritageLine",										"        &c└ <region> : &7<type>"),
		REGION_INFO_ONE_HERITAGE_PADDING("region.info.one.heritagePadding",									"  "),
		REGION_INFO_ONE_OWNERS("region.info.one.owners",													"    &6&l➤  &6Owners : &c<owners>"),
		REGION_INFO_ONE_OWNERS_JOIN("region.info.one.ownersJoin",											"&6, &c"),
		REGION_INFO_ONE_GROUP_OWNERS("region.info.one.groupOwners",											"    &6&l➤  &6Groupes Owners : &c<owners>"),
		REGION_INFO_ONE_GROUP_OWNERS_JOIN("region.info.one.groupOwnersJoin",								"&6, &c"),
		REGION_INFO_ONE_MEMBERS("region.info.one.members",													"    &6&l➤  &6Members : &c<members>"),
		REGION_INFO_ONE_MEMBERS_JOIN("region.info.one.membersJoin",											"&6, &c"),
		REGION_INFO_ONE_GROUP_MEMBERS("region.info.one.groupMembers",										"    &6&l➤  &6Groupes Members : &c<members>"),
		REGION_INFO_ONE_GROUP_MEMBERS_JOIN("region.info.one.membersJoin",									"&6, &c"),
		REGION_INFO_ONE_FLAGS("region.info.one.flags",														"    &6&l➤  &6Flag :"),
		REGION_INFO_ONE_FLAGS_LINE("region.info.one.flagsLine",												"            &a&l- <flag> : &c<value>"),
		REGION_INFO_ONE_FLAGS_DEFAULT("region.info.one.flagsDefault",										"        &6&l●   &6Default:"),
		REGION_INFO_ONE_FLAGS_MEMBER("region.info.one.flagsMember",											"        &6&l●   &6Member:"),
		REGION_INFO_ONE_FLAGS_OWNER("region.info.one.flagsOwner",											"        &6&l●   &6Owner:"),
		REGION_INFO_ONE_HERITAGE_FLAGS("region.info.one.heritageFlags",										"    &6&l➤  Flag Héritage :"),
		REGION_INFO_ONE_HERITAGE_FLAGS_LINE("region.info.one.heritageFlagsLine",							"            &a&l- <flag> : &c<value>"),
		REGION_INFO_ONE_HERITAGE_FLAGS_DEFAULT("region.info.one.heritageFlagsDefault",						"        &6&l●   &6Default:"),
		REGION_INFO_ONE_HERITAGE_FLAGS_MEMBER("region.info.one.heritageFlagsMember",						"        &6&l●   &6Member:"),
		REGION_INFO_ONE_HERITAGE_FLAGS_OWNER("region.info.one.heritageFlagsOwner",							"        &6&l●   &6Owner:"),
		REGION_INFO_LIST_TITLE("region.info.list.title",													"&aListe des régions"),
		REGION_INFO_LIST_LINE("region.info.list.line",														"    &6&l➤  &6<region> : [RT]            &7(Type : &a<type>&7, Priorité : &a<priority>&7)"),
		REGION_INFO_EMPTY("region.info.empty",																"&cErreur : Vous n'avez accès aux informations sur ces régions."),
		
		REGION_LIST_DESCRIPTION("region.list.description",										"Permet de voir la liste des régions dans le monde"),
		REGION_LIST_ALL_TITLE("region.list.allTitle",											"&aLa liste des régions : &6<world>"),
		REGION_LIST_ALL_LINE("region.list.allLine",												"    &6&l➤  &6<region> : [RT]            &7(Type : &a<type>&7, Priorité : &a<priority>&7)"),
		REGION_LIST_ALL_EMPTY("region.list.allEmpty",											"    &7Aucune région"),
		REGION_LIST_PLAYER_TITLE_EQUALS("region.list.playerTitleEquals",						"&aLa liste de vos régions : &6<world>"),
		REGION_LIST_PLAYER_TITLE_OTHERS("region.list.playerTitleOthers",						"&aListe des régions : &6<world>"),
		REGION_LIST_PLAYER_LINE("region.list.playerLine",										"    &6&l➤  &6<region> : [RT]            &7(Type : &a<type>&7, Priorité : &a<priority>&7)"),
		REGION_LIST_PLAYER_EMPTY("region.list.playerEmpty",										"    &7Aucune région"),
		REGION_LIST_GROUP_TITLE("region.list.groupTitle",										"&aLa liste des régions du groupe &6<group> &7: &6<world>"),
		REGION_LIST_GROUP_LINE("region.list.groupLine",											"    &6&l➤  &6<region> : [RT]            &7(Type : &a<type>&7, Priorité : &a<priority>&7)"),
		REGION_LIST_GROUP_EMPTY("region.list.groupLine",										"    &7Aucune région"),
		
		REGION_DEFINE_DESCRIPTION("region.define.description",									"Permet de définir une nouvelle région"),
		REGION_DEFINE_CUBOID_CREATE("region.define.cuboid.create",								"&7Vous venez de créer la région &6<points> &7de type &6<type>&7."),
		REGION_DEFINE_CUBOID_POINTS("region.define.cuboid.points",								"&6<region>"),
		REGION_DEFINE_CUBOID_POINTS_HOVER("region.define.cuboid.pointsHover",					"&6Min : (&c<min_x>&6, &c<min_y>&6, &c<min_z>&6)[RT]&6Max : (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_DEFINE_CUBOID_ERROR_POSITION("region.define.cuboid.errorPosition",				"&cErreur : Vous devez sélectionner 2 positions pour définir une région &6<type>&c."),
		REGION_DEFINE_POLYGONAL_CREATE("region.define.polygonal.create",						"&7Vous venez de créer la région &6<points> &7de type &6<type>&7."),
		REGION_DEFINE_POLYGONAL_POINTS("region.define.polygonal.points",						"&6<region>"),
		REGION_DEFINE_POLYGONAL_POINTS_HOVER("region.define.polygonal.pointsHover",				"&6Les positions : [RT]<positions>"),
		REGION_DEFINE_POLYGONAL_POINTS_HOVER_LINE("region.define.polygonal.pointsHoverLine",	"&6#<num> : (&c<x>&6, &c<y>&6, &c<z>&6)"),
		REGION_DEFINE_POLYGONAL_POINTS_HOVER_JOIN("region.define.polygonal.pointsHoverJoin",	"[RT]"),
		REGION_DEFINE_POLYGONAL_ERROR_POSITION("region.define.polygonal.errorPosition",			"&cErreur : Vous devez sélectionner au moins 2 positions pour définir une région &6<type>&c."),
		REGION_DEFINE_TEMPLATE_CREATE("region.define.template.create",							"&7Vous venez de créer la région &6<region> &7de type &6<type>&7."),
		REGION_DEFINE_ERROR_IDENTIFIER_EQUALS("region.define.errorIdentifierEquals",			"&cErreur : &6<region> &cexiste déjà."),
		REGION_DEFINE_ERROR_IDENTIFIER_INVALID("region.define.cuboid.errorIdentifierInvalid",	"&cErreur : Le nom de région &6'<region>' &cest invalide."),
		REGION_DEFINE_ERROR_SELECT_TYPE("region.define.errorSelectType",						"&cErreur : Impossible de créer une région de type &6<type>&c."),
		
		REGION_REDEFINE_DESCRIPTION("region.redefine.description",									"Permet de redéfinir une région"),
		REGION_REDEFINE_CUBOID_CREATE("region.redefine.cuboid.create",								"&7Vous venez de redéfinir la région &6<region> &7en type &6<type>&7."),
		REGION_REDEFINE_CUBOID_POINTS("region.redefine.cuboid.points",								"&6(&c<min_x>&6, &c<min_y>&6, &c<min_z>&6) (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_REDEFINE_CUBOID_POINTS_HOVER("region.redefine.cuboid.pointsHover",					"&6Min : (&c<min_x>&6, &c<min_y>&6, &c<min_z>&6)[RT]&6Max : (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_REDEFINE_CUBOID_ERROR_POSITION("region.redefine.cuboid.errorPosition",				"&cErreur : Vous devez sélectionner 2 positions pour redéfinir la région &6<region>&c."),
		REGION_REDEFINE_POLYGONAL_CREATE("region.redefine.polygonal.create",						"&7Vous venez de redéfinir la région &6<region> &7de type &6<type>."),
		REGION_REDEFINE_POLYGONAL_POINTS("region.redefine.polygonal.points",						"&6(&c<min_x>&6, &c<min_y>&6, &c<min_z>&6) (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_REDEFINE_POLYGONAL_POINTS_HOVER("region.redefine.polygonal.pointsHover",				"&6Les positions : [RT]<positions"),
		REGION_REDEFINE_POLYGONAL_POINTS_HOVER_LINE("region.redefine.polygonal.pointsHoverLine",	"&6#<num> : (&c<x>&6, &c<y>&6, &c<z>&6)"),
		REGION_REDEFINE_POLYGONAL_POINTS_HOVER_JOIN("region.redefine.polygonal.pointsHoverJoin",	"[RT]"),
		REGION_REDEFINE_POLYGONAL_ERROR_POSITION("region.redefine.polygonal.errorPosition",			"&cErreur : Vous devez sélectionner au moins 3 positions pour redéfinir la région &6<region>&c."),
		REGION_REDEFINE_TEMPLATE_CREATE("region.redefine.template.create",							"&7Vous venez de redéfinir la région &6<region> &7de type &6<type>."),
		REGION_REDEFINE_ERROR_GLOBAL("region.redefine.errorGlobal",									"&cErreur : Impossible de redéfinir la région &6<region> &ccar elle est de type &6<type>&c."),
		REGION_REDEFINE_ERROR_SELECT_TYPE("region.redefine.errorSelectType",						"&cErreur : Impossible de redéfinir une région en type &6<type>&c."),
		
		REGION_RENAME_DESCRIPTION("region.rename.description",									"Renomme une région"),
		REGION_RENAME_SET("region.rename.set",													"&7Vous avez renommée la région &6<region> &7en &6<identifier> &7dans le monde &6<world>&7."),
		REGION_RENAME_ERROR_IDENTIFIER_EQUALS("region.rename.errorIdentifierEquals",			"&cErreur : Impossible de renommer la région &6<region> &cen &6<identifier> &ccar une autre région porte déjà ce nom."),
		REGION_RENAME_ERROR_IDENTIFIER_INVALID("region.rename.errorIdentifierInvalid",			"&cErreur : Impossible de renommer la région &6<region> &cen &6<identifier> &ccar le nom est invalide."),
		REGION_RENAME_ERROR_GLOBAL("region.rename.errorGlobal",									"&cErreur : Impossible de renommer la région &6<region> &cde type &6<type>&c."),
		
		REGION_FLAGS_DESCRIPTION("region.flags.description",									"Affiche la liste des flags"),
		REGION_FLAGS_LIST_TITLE("region.flags.listTitle",										"&aListe des flags"),
		REGION_FLAGS_LIST_LINE("region.flags.listLine",											"    &6&l➤  &6<flag> : &7<description>"),
		REGION_FLAGS_MESSAGE("region.flags.message",											"&6<flag> : &7<description>"),
		
		REGION_FLAG_ADD_DESCRIPTION("region.addflag.description",								"Défini un flag d'une région"),
		REGION_FLAG_ADD_PLAYER("region.addflag.player",											"&7Vous avez défini le flag &6<flag> &7sur la région &6<region> &7dans le monde &6<world> &7pour le groupe &6<group> &7avec la valeur &6<value>&7."),
		REGION_FLAG_ADD_ERROR("region.addflag.error",											"&cErreur : La valeur est &6'<value>&6' &cest invalide."),
		
		REGION_FLAG_REMOVE_DESCRIPTION("region.removeflag.description",							"Supprime un flag d'une région"),
		REGION_FLAG_REMOVE_UPDATE("region.removeflag.update",									"&7Vous avez défini le flag &6<flag> &7sur la région &6<region> &7dans le monde &6<world> &7pour le groupe &6<group> &7avec la valeur &6<value>&7."),
		REGION_FLAG_REMOVE_PLAYER("region.removeflag.player",									"&7Vous avez supprimé le flag &6<flag> &7sur la région &6<region> &7dans le monde &6<world> &7pour le groupe &6<group>&7."),
		REGION_FLAG_REMOVE_EMPTY("region.removeflag.empty",										"&cErreur : Il n'y a pas de flag &6<flag> &csur la région &6<region> &cdans le monde &6<world> &cpour le groupe &6<group>&c."),
		REGION_FLAG_REMOVE_ERROR("region.removeflag.error",										"&cErreur : La valeur est &6'<value>&6' &cest invalide."),
		
		REGION_OWNER_ADD_DESCRIPTION("region.addowner.description",								"Ajoute un owner à une région"),
		REGION_OWNER_ADD_PLAYER("region.addowner.player",										"&7Vous avez ajouté le joueur &6<player> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_ADD_PLAYER_ERROR("region.addowner.playerError",							"&cErreur : Le joueur &6<player> &cest déjà &6OWNER &cde la région &6<region>&c."),
		REGION_OWNER_ADD_PLAYERS("region.addowner.players",										"&7Vous avez ajouté les joueur(s) &6<players> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_ADD_PLAYERS_JOIN("region.addowner.playersJoin",							"&7, &6"),
		REGION_OWNER_ADD_ERROR_MAX("region.addowner.errorMax",									"&cErreur : La région &6<region> &ccontient déjà le nombre maximum de &6OWNER&c."),
		REGION_OWNER_ADD_GROUP("region.addowner.group",											"&7Vous avez ajouté le groupe &6<group> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_ADD_GROUP_ERROR("region.addowner.groupError",								"&cErreur : Le groupe &6<group> &cest déjà &6OWNER &cde la région &6<region>&c."),
		REGION_OWNER_ADD_GROUPS("region.addowner.groups",										"&7Vous avez ajouté les groupes &6<groups> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_ADD_GROUPS_JOIN("region.addowner.groupsJoin",								"&7, &6"),
		
		REGION_OWNER_REMOVE_DESCRIPTION("region.removeowner.description",						"Supprime un owner à une région"),
		REGION_OWNER_REMOVE_PLAYER("region.removeowner.player",									"&7Vous avez supprimé le joueur &6<player> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_REMOVE_PLAYER_ERROR("region.removeowner.playerError",						"&cErreur : Le joueur &6<player> &cn'est pas &6OWNER &cde la région &6<region>&c."),
		REGION_OWNER_REMOVE_PLAYERS("region.removeowner.players",								"&7Vous avez supprimé les joueur(s) &6<players> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_REMOVE_PLAYERS_JOIN("region.removeowner.playersJoin",						"&7, &6"),
		REGION_OWNER_REMOVE_GROUP("region.removeowner.group",									"&7Vous avez supprimé le groupe &6<group> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_REMOVE_GROUP_ERROR("region.removeowner.groupError",						"&cErreur : Le groupe &6<group> &cn'est pas &6OWNER &cde la région &6<region>&c."),
		REGION_OWNER_REMOVE_GROUPS("region.removeowner.groups",									"&7Vous avez supprimé les groupes &6<groups> &7en tant que &6OWNER &7de la région &6<region>&7."),
		REGION_OWNER_REMOVE_GROUPS_JOIN("region.removeowner.groupsJoin",						"&7, &6"),

		REGION_MEMBER_ADD_DESCRIPTION("region.addmember.description",							"Ajoute un member à une région"),
		REGION_MEMBER_ADD_PLAYER("region.addmember.player",										"&7Vous avez ajouté le joueur &6<player> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_ADD_PLAYER_ERROR("region.addmember.playerError",							"&cErreur : Le joueur &6<player> &cest déjà &6MEMBER &cde la région &6<region>&c."),
		REGION_MEMBER_ADD_PLAYERS("region.addmember.players",									"&7Vous avez ajouté les joueur(s) &6<players> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_ADD_PLAYERS_JOIN("region.addmember.playersJoin",							"&7, &6"),
		REGION_MEMBER_ADD_ERROR_MAX("region.addmember.errorMax",								"&cErreur : La région &6<region> &ccontient déjà le nombre maximum de &6MEMBER&c."),
		REGION_MEMBER_ADD_GROUP("region.addmember.group",										"&7Vous avez ajouté le groupe &6<group> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_ADD_GROUP_ERROR("region.addmember.groupError",							"&cErreur : Le groupe &6<group> &cest déjà &6MEMBER &cde la région &6<region>&c."),
		REGION_MEMBER_ADD_GROUPS("region.addmember.groups",										"&7Vous avez ajouté les groupes &6<groups> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_ADD_GROUPS_JOIN("region.addmember.groupsJoin",							"&7, &6"),
		
		REGION_MEMBER_REMOVE_DESCRIPTION("region.removemember.description",						"Supprime un member à une région"),
		REGION_MEMBER_REMOVE_PLAYER("region.removemember.player",								"&7Vous avez supprimé le joueur &6<player> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_REMOVE_PLAYER_ERROR("region.removemember.playerError",					"&cErreur : Le joueur &6<player> &cn'est pas &6MEMBER &cde la région &6<region>&c."),
		REGION_MEMBER_REMOVE_PLAYERS("region.removemember.players",								"&7Vous avez supprimé les joueur(s) &6<players> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_REMOVE_PLAYERS_JOIN("region.removemember.playersJoin",					"&7, &6"),
		REGION_MEMBER_REMOVE_GROUP("region.removemember.group",									"&7Vous avez supprimé le groupe &6<group> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_REMOVE_GROUP_ERROR("region.removemember.groupError",						"&cErreur : Le groupe &6<group> &cn'est pas &6MEMBER &cde la région &6<region>&c."),
		REGION_MEMBER_REMOVE_GROUPS("region.removemember.groups",								"&7Vous avez supprimé les groupes &6<groups> &7en tant que &6MEMBER &7de la région &6<region>&7."),
		REGION_MEMBER_REMOVE_GROUPS_JOIN("region.removemember.groupsJoin",						"&7, &6"),
		
		REGION_PARENT_DESCRIPTION("region.parent.description",									"Défini le parent d'une région"),
		REGION_PARENT_SET("region.parent.set",													"&7Vous avez défini la région &6<parent> &7en tant que parent de la région &6<region>&7."),
		REGION_PARENT_SET_HERITAGE("region.parent.setHeritage",									"&7Vous avez défini la région &6<parent> &7en tant que parent de la région &6<region> : [RT]<heritage>"),
		REGION_PARENT_SET_HERITAGE_LINE("region.parent.setHeritageLine",						"    &c└ <region> : &7<type>"),
		REGION_PARENT_SET_HERITAGE_PADDING("region.parent.setHeritagePadding",					"  "),
		REGION_PARENT_SET_CIRCULAR("region.parent.setCircular",									"&cErreur : Impossible de définir la région &6<parent> &cen tant que parent de &6<region>&c."),
		REGION_PARENT_SET_EQUALS("region.parent.setEquals",										"&cErreur : Impossible de définir une région parent."),
		REGION_PARENT_SET_EQUALS_PARENT("region.parent.setEqualsParent",						"&cErreur : La région &6<parent> &cest déjà la région parent de &6<region>&c."),
		REGION_PARENT_REMOVE("region.parent.remove",											"&7Vous avez supprimé le parent de la région &6<region>&7."),
		REGION_PARENT_REMOVE_EMPTY("region.parent.removeEmpty",									"&cErreur : La région &6<region> &cn'a déjà aucun parent."),
		
		REGION_PRIORITY_DESCRIPTION("region.priority.description",								"Défini la priorité d'une région"),
		REGION_PRIORITY_SET("region.priority.set",												"&7Vous avez défini la priorité de la région &6<region> &7à &6<priority>&7."),
		
		REGION_REMOVE_DESCRIPTION("region.remove.description",									"Supprime une région"),
		REGION_REMOVE_REGION("region.remove.region",											"&7Vous avez supprimé la région &6<region> &7dans le monde &6<world>&7."),
		REGION_REMOVE_CHILDREN_REMOVE("region.remove.childrenRemove",							"&7Vous avez supprimé la région &6<region> &7et ces enfants &7dans le monde &6<world>&7."),
		REGION_REMOVE_CHILDREN_UNSET("region.remove.childrenUnset",								"&7Vous avez supprimé la région &6<region> &7et gardé ces enfants &7dans le monde &6<world>&7."),
		REGION_REMOVE_ERROR_GLOBAL("region.remove.errorGlobal",									"&cErreur : Impossible de supprimer la région &6<region> &ccar elle est de type &6<type>&c."),
		REGION_REMOVE_ERROR_CHILDREN("region.remove.errorChildren",								"&cErreur : La région &6<region> &cpossède au moins une région enfant :[RT]"
																							  + "    -f : Permet de supprimer aussi les régions enfants[RT]"
																							  + "    -u : Permet de supprimer uniquement la région parent"),
		
		REGION_TELEPORT_DESCRIPTION("region.teleport.description",								"Téléporte le joueur à la région séléctionnée"),
		REGION_TELEPORT_TELEPORT("region.teleport.teleport",									"&7Vous avez été <position> &7à la région &6<region>&7."),
		REGION_TELEPORT_TELEPORT_POSITION("region.teleport.teleportPosition",					"&6téléporté"),
		REGION_TELEPORT_TELEPORT_POSITION_HOVER("region.teleport.teleportPositionHover",		"&7World : &6<world>[RT]&7X : &6<x>[RT]&7Y : &6<y>[RT]&7Z : &6<z>"),
		REGION_TELEPORT_TELEPORT_ERROR("region.teleport.teleportError",							"&cErreur : La position de téléportation de la région &6<region> &cest invalide."),
		REGION_TELEPORT_SPAWN("region.teleport.spawn",											"&7Vous avez été téléporté <position> &7de la région &6<region>&7."),
		REGION_TELEPORT_SPAWN_POSITION("region.teleport.spawnPosition",							"&6spawn"),
		REGION_TELEPORT_SPAWN_POSITION_HOVER("region.teleport.spawnPositionHover",				"&7World : &6<world>[RT]&7X : &6<x>[RT]&7Y : &6<y>[RT]&7Z : &6<z>"),
		REGION_TELEPORT_SPAWN_ERROR("region.teleport.spawnError",								"&cErreur : La position du spawn de la région &6<region> &cest invalide."),
		REGION_TELEPORT_SPAWN_EMPTY("region.teleport.spawnEmpty",								"&cErreur : La région &6<region> &cn'a aucun spawn."),
		
		REGION_SELECT_DESCRIPTION("region.select.description",									"Permet de sélectionner une région"),
		REGION_SELECT_CUBOID("region.select.cuboid",											"&7Vous venez de sélectionner la région &6<positions> &7de type &6<type>&7."),
		REGION_SELECT_CUBOID_POINTS("region.select.cuboidPoints",								"&6<region>"),
		REGION_SELECT_CUBOID_POINTS_HOVER("region.select.cuboidPointsHover",					"&6Min : (&c<min_x>&6, &c<min_y>&6, &c<min_z>&6)[RT]&6Max : (&c<max_x>&6, &c<max_y>&6, &c<max_z>&6)"),
		REGION_SELECT_POLYGONAL("region.select.polygonal",										"&7Vous venez de sélectionner la région &6<positions> &7de type &6<type>&7."),
		REGION_SELECT_POLYGONAL_POINTS("region.select.polygonalPoints",							"&6<region>"),
		REGION_SELECT_POLYGONAL_POINTS_HOVER("region.select.polygonalPointsHover",				"&6Les positions : [RT]<positions>"),
		REGION_SELECT_POLYGONAL_POINTS_HOVER_LINE("region.select.polygonalPointsHoverLine",		"&6#<num> : (&c<x>&6, &c<y>&6, &c<z>&6)"),
		REGION_SELECT_POLYGONAL_POINTS_HOVER_JOIN("region.select.polygonalPointsHoverJoin",		"[RT]"),
		REGION_SELECT_GLOBAL("region.select.global",											"&cErreur : Impossible de sélectionner une région de type &6<type>&c."),
		REGION_SELECT_TEMPLATE("region.select.template",										"&cErreur : Impossible de sélectionner une région de type &6<type>&c."),
		
		
		FLAG_BLOCK_PLACE_DESCRIPTION("flag.blockPlace.description",				"Autorise/Interdit de placer des blocs"),
		FLAG_BLOCK_PLACE_MESSAGE("flag.blockPlace.message",						EMessageFormat.builder()
																					.actionbarMessageString("&cPlacer des blocs de <block> est interdit (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_BLOCK_BREAK_DESCRIPTION("flag.blockBreak.description",				"Autorise/Interdit de détruire des blocs"),
		FLAG_BLOCK_BREAK_MESSAGE("flag.blockBreak.message",						EMessageFormat.builder()
																					.actionbarMessageString("&cCasser des blocs de <block> est interdit (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_BUILD_DESCRIPTION("flag.build.description",						"Autorise/Interdit les constructions"),
		FLAG_BUILD_MESSAGE("flag.build.message",								EMessageFormat.builder()
																					.actionbarMessageString("&cConstruction désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
				
		FLAG_CHAT_DESCRIPTION("flag.chatSend.description",						"Active/Désactive le chat"),
		FLAG_CHAT_SEND_MESSAGE("flag.chatSend.sendMessage",						"&cL'envoi de message est désactivé dans cette région (<x>, <y>, <z>)"),
		
		FLAG_COMMAND_DESCRIPTION("flag.commands.description",					"Active/Désactive l'execution des commandes"),
		FLAG_COMMAND_MESSAGE("flag.commands.message",							"&cLa commande <command> est désactivé dans cette région (<x>, <y>, <z>)"),
		
		FLAG_DAMAGE_ENTITY_DESCRIPTION("flag.damageEntity.description",			"Active/Désactive les dégats infligés aux entités"),
		FLAG_DAMAGE_ENTITY_MESSAGE("flag.damageEntity.message",					EMessageFormat.builder()
																					.actionbarMessageString("&cLes dégats infligés aux entités sont désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_ENDERDRAGON_GRIEF_DESCRIPTION("flag.enderDragonGrief.description",	"Active/Désactive la destruction des blocs par l'EnderDragon"),
		
		FLAG_ENDERPEARL_DESCRIPTION("flag.enderpearl.description",				"Active/Désactive la téléportation avec une enderperle"),
		FLAG_ENDERPEARL_MESSAGE("flag.enderpearl.message",						EMessageFormat.builder()
																					.actionbarMessageString("&cL'utilisation des enderperles est désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_ENTITY_DAMAGE_DESCRIPTION("flag.entityDamage.description",			"Active/Désactive les dégats infligés par une entité"),
		
		FLAG_ENDERMAN_GRIEF_DESCRIPTION("flag.endermanGrief.description",		"Active/Désactive la destruction des blocs de l'Enderman"),
			
		FLAG_INTERACT_BLOCK_DESCRIPTION("flag.interactBlock.description",		"Active/Désactive l'interaction avec les blocs"),
		FLAG_INTERACT_BLOCK_MESSAGE("flag.interactBlock.message",				EMessageFormat.builder()
																					.actionbarMessageString("&cL'Interaction avec les blocs est désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_INTERACT_ENTITY_DESCRIPTION("flag.interactEntity.description",		"Active/Désactive les interactions avec les entités"),
		FLAG_INTERACT_ENTITY_MESSAGE("flag.interactEntity.message",				EMessageFormat.builder()
																					.actionbarMessageString("&cL'Interactions avec les entités est désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_ENTRY_DESCRIPTION("flag.entry.description",						"Autorise/Interdit le joueur d'entrer dans la région"),
		
		FLAG_ENTRY_MESSAGE_DESCRIPTION("flag.entryMessage.description",			"Autorise/Interdit le joueur d'entrer dans la région"),
		
		FLAG_ENTRY_DENY_MESSAGE_DESCRIPTION("flag.entryDenyMessage.description","Interdit le joueur d'entrer dans la région"),
		FLAG_ENTRY_DENY_MESSAGE_DEFAULT("flag.entryDenyMessage.default",		EMessageFormat.builder()
																					.actionbarMessageString("&cImpossible d'entrer dans la région (<region>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_EXIT_DESCRIPTION("flag.exit.description",							"Autorise/Interdit le joueur de sortir dans la région"),
		
		FLAG_EXIT_MESSAGE_DESCRIPTION("flag.exitMessage.description",			"Autorise/Interdit le joueur de sortir dans la région"),
		
		FLAG_EXIT_DENY_MESSAGE_DESCRIPTION("flag.exitDenyMessage.description",	"Interdit le joueur de sortir dans la région"),
		FLAG_EXIT_DENY_MESSAGE_DEFAULT("flag.exitDenyMessage.default",			EMessageFormat.builder()
																					.actionbarMessageString("&cImpossible de sortir dans la région (<region>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_EXP_DROP_DESCRIPTION("flag.expDrop.description",					"Active/Désactive la perte d'expérience au sol"),
		
		FLAG_EXPLOSION_DESCRIPTION("flag.explosion.description",				"Active/Désactive les explosions dans la région"),
		
		FLAG_EXPLOSION_BLOCK_DESCRIPTION("flag.explosionBlock.description",		"Active/Désactive la destruction des blocs lors d'une explosion"),
		
		FLAG_EXPLOSION_DAMAGE_DESCRIPTION("flag.explosionDamage.description",	"Active/Désactive les dégats lors d'une explosion"),
		
		FLAG_FIRE_DESCRIPTION("flag.fire.description",							"Active/Désactive le fait d'allumer un feu"),
		
		FLAG_FIRE_MESSAGE("flag.fire.message",									EMessageFormat.builder()
																					.actionbarMessageString("&cLe feu est désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_ICE_DESCRIPTION("flag.ice.description",							"Active/Désactive la formation de glace"),
		
		FLAG_KEEP_INVENTORY_DESCRIPTION("flag.keekInventory.description",		"Active/Désactive la perte de l'inventaire"),
		
		FLAG_INVINCIBILITY_DESCRIPTION("flag.invincibity.description",			"Active/Désactive l'invincibilité"),
		
		FLAG_ITEM_DROP_DESCRIPTION("flag.itemDrop.description",					"Active/Désactive le fait de jeter des objets"),
		FLAG_ITEM_DROP_MESSAGE("flag.itemDrop.message",							EMessageFormat.builder()
																					.actionbarMessageString("&cJeter les objets de <item> est interdit (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_ITEM_PICKUP_DESCRIPTION("flag.itemPickup.description",				"Active/Désactive le fait de rammasser des objets"),
		FLAG_ITEM_PICKUP_MESSAGE("flag.itemPickup.message",						EMessageFormat.builder()
																					.actionbarMessageString("&cRamasser les objets de <item> est interdit (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		
		FLAG_LIGHTNING_DESCRIPTION("flag.invincibity.description",				"Active/Désactive la foudre"),
		
		FLAG_POTION_SPLASH_DESCRIPTION("flag.potionSplash.description",				"Active/Désactive les potions splash"),
		FLAG_POTION_SPLASH_MESSAGE("flag.potionSplash.message",						EMessageFormat.builder()
																					.actionbarMessageString("&cL'utilisation des potions <potion> sont désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_PROPAGATION_DESCRIPTION("flag.propagation.description",			"Active/Désactive la propagation"),
		FLAG_PROPAGATION_MESSAGE("flag.propagation.message",					EMessageFormat.builder()
																					.actionbarMessageString("&cPropagation désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_PVP_DESCRIPTION("flag.pvp.description",							"Active/Désactive le PVP"),
		FLAG_PVP_MESSAGE("flag.pvp.message",									EMessageFormat.builder()
																					.actionbarMessageString("&cPVP désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG)),
		
		FLAG_SNOW_DESCRIPTION("flag.snow.description",							"Active/Désactive la neige"),
		
		FLAG_TELEPORT_DESCRIPTION("flag.teleport.description",					"Sauvegarde une position pour se téléporter à la région"),
		
		FLAG_SPAWN_DESCRIPTION("flag.spawn.description",						"Défini un spawn pour la région"),
		
		FLAG_SPAWN_ENTITY_DESCRIPTION("flag.spawnEntity.description",			"Active/Désactive l'apparition des entités"),
		FLAG_SPAWN_ENTITY_MESSAGE("flag.spawnEntity.message",					EMessageFormat.builder()
																					.actionbarMessageString("&cL'apparition de l'entité <entity> est désactivé (<x>, <y>, <z>)")
																					.actionbarStay(3 * 1000)
																					.actionbarPriority(WorldGuardService.MESSAGE_FLAG));
		
		private final String path;
	    private final EMessageBuilder french;
	    private final EMessageBuilder english;
	    private EMessageFormat message;
	    private EMessageBuilder builder;
	    
	    private EWMessages(final String path, final String french) {   	
	    	this(path, EMessageFormat.builder().chat(new EFormatString(french), true));
	    }
	    
	    private EWMessages(final String path, final String french, final String english) {   	
	    	this(path, 
	    		EMessageFormat.builder().chat(new EFormatString(french), true), 
	    		EMessageFormat.builder().chat(new EFormatString(english), true));
	    }
	    
	    private EWMessages(final String path, final EMessageBuilder french) {   	
	    	this(path, french, french);
	    }
	    
	    private EWMessages(final String path, final EMessageBuilder french, final EMessageBuilder english) {
	    	Preconditions.checkNotNull(french, "Le message '" + this.name() + "' n'est pas défini");
	    	
	    	this.path = path;	    	
	    	this.french = french;
	    	this.english = english;
	    	this.message = french.build();
	    }

	    public String getName() {
			return this.name();
		}
	    
		public String getPath() {
			return this.path;
		}

		public EMessageBuilder getFrench() {
			return this.french;
		}

		public EMessageBuilder getEnglish() {
			return this.english;
		}
		
		public EMessageFormat getMessage() {
			return this.message;
		}
		
		public EMessageBuilder getBuilder() {
			return this.builder;
		}
		
		public void set(EMessageBuilder message) {
			this.message = message.build();
			this.builder = message;
		}
	}
}
