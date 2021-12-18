package fr.pederobien.minecraft.commandtree.interfaces;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.minecraft.dictionary.impl.MinecraftDictionaryContext;
import fr.pederobien.minecraft.dictionary.impl.MinecraftMessageEvent;
import fr.pederobien.minecraft.dictionary.impl.MinecraftMessageEvent.MinecraftMessageEventBuilder;
import fr.pederobien.minecraft.dictionary.impl.PlayerGroup;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftMessageEvent;
import fr.pederobien.minecraft.dictionary.interfaces.IPlayerGroup;

public interface ICodeSender {
	public static final String DEFAULT_PREFIX = "---------------------------------------------------------\n";
	public static final String DEFAULT_SUFFIX = "\n---------------------------------------------------------";

	/**
	 * Get a message corresponding to the given message code.
	 * 
	 * @param sender Generally a player, it is used to get a message in his language.
	 * @param code   The code used to get the translation of the message in the player's language.
	 * @param args   Arguments that could be useful to send dynamic messages.
	 * 
	 * @return The message associated to the specified code. If the sender is not a player, then it returns the english message.
	 */
	public default String getMessage(CommandSender sender, IMinecraftCode code, Object... args) {
		return MinecraftDictionaryContext.instance().getMessage(eventBuilder(sender, code, args));
	}

	/**
	 * Creates a new minecraft message event based on the given parameters.
	 * 
	 * @param sender The entity that run a command.
	 * @param code   The code used to get the right message to translate.
	 * @param args   Some arguments (optional) used for dynamic messages.
	 * 
	 * @return A new minecraft message event.
	 */
	public default IMinecraftMessageEvent eventBuilder(CommandSender sender, IMinecraftCode code, Object... args) {
		return MinecraftMessageEvent.builder(sender, code).build(args);
	}

	/**
	 * Creates a new instance of a minecraft message event builder based on the specific code.
	 * 
	 * @param sender The entity that run a command.
	 * @param code   The code used to get a translated message.
	 * 
	 * @return A new minecraft message event builder instance.
	 */
	public default MinecraftMessageEventBuilder eventBuilder(CommandSender sender, IMinecraftCode code) {
		return MinecraftMessageEvent.builder(sender, code);
	}

	/**
	 * Creates a new instance of a minecraft message event builder based on the specific code.
	 * 
	 * @param code The code used to get a translated message.
	 * 
	 * @return A new minecraft message event builder instance.
	 */
	public default MinecraftMessageEventBuilder eventBuilder(IMinecraftCode code) {
		return MinecraftMessageEvent.builder(code);
	}

	/**
	 * Creates a new minecraft message event based on the given parameters.
	 * 
	 * @param sender The entity that run a command.
	 * @param code   The code used to get the right message to translate.
	 * @param args   Some arguments (optional) used for dynamic messages.
	 * 
	 * @return A new minecraft message event.
	 */
	public default IMinecraftMessageEvent defaultEventBuilder(CommandSender sender, IMinecraftCode code, Object... args) {
		return MinecraftMessageEvent.builder(sender, code).withPrefix(DEFAULT_PREFIX).withSuffix(DEFAULT_SUFFIX).build(args);
	}

	/**
	 * Creates a new instance of a minecraft message event builder based on the specific code.
	 * 
	 * @param sender The entity that run a command.
	 * @param code   The code used to get a translated message.
	 * 
	 * @return A new minecraft message event builder instance.
	 */
	public default MinecraftMessageEventBuilder defaultEventBuilder(CommandSender sender, IMinecraftCode code) {
		return MinecraftMessageEvent.builder(sender, code).withPrefix(DEFAULT_PREFIX).withSuffix(DEFAULT_SUFFIX);
	}

	/**
	 * Creates a new instance of a minecraft message event builder based on the specific code.
	 * 
	 * @param code The code used to get a translated message.
	 * 
	 * @return A new minecraft message event builder instance.
	 */
	public default MinecraftMessageEventBuilder defaultEventBuilder(IMinecraftCode code) {
		return MinecraftMessageEvent.builder(code).withPrefix(DEFAULT_PREFIX).withSuffix(DEFAULT_SUFFIX);
	}

	/**
	 * Creates a player group that contains only the sender cast as Player if and only if the sender is a player, or an empty group if
	 * the sender is not a player.
	 * 
	 * @param sender The command sender that run a command.
	 * @return A player group.
	 */
	public default IPlayerGroup toPlayerGroup(CommandSender sender) {
		return sender instanceof Player ? PlayerGroup.of("sender", player -> player.equals((Player) sender)) : PlayerGroup.EMPTY;
	}

	/**
	 * Send a message based on the given event.
	 * 
	 * @param event The event that contains the message to send and its modifiers (italic, bold, color).
	 */
	public default void send(IMinecraftMessageEvent event) {
		MinecraftDictionaryContext.instance().send(event);
	}
}
