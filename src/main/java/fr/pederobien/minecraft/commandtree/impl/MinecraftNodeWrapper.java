package fr.pederobien.minecraft.commandtree.impl;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.commandtree.impl.NodeWrapper;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftNode;

public class MinecraftNodeWrapper<T> extends NodeWrapper<T> implements IMinecraftNode<T> {
	private IMinecraftNode<T> source;

	/**
	 * Wraps the given node.
	 * 
	 * @param source The node source of this wrapper.
	 */
	protected MinecraftNodeWrapper(IMinecraftNode<T> source) {
		super(source);
		this.source = source;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return source.onTabComplete(sender, command, alias, args);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return source.onCommand(sender, command, label, args);
	}
}
