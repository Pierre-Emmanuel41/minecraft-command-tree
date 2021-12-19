package fr.pederobien.minecraft.commandtree.impl;

import org.bukkit.ChatColor;

import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;

public class MinecraftCodeHelperNode extends MinecraftHelperNode<IMinecraftCode> {

	/**
	 * Creates an helper responsible to display the explanation of one or several children of the given source node. The default
	 * behavior for displaying node explanation is calling <code>System.out.println(node.getLabel() - node.getExplanation())</code>.
	 * 
	 * @param source The node source in order to display the explanation of one or several children.
	 */
	protected MinecraftCodeHelperNode(INode<IMinecraftCode> source) {
		super(source, (player, node) -> String.format("%s%s - %s%s\n", ChatColor.DARK_RED, node.getLabel(), ChatColor.DARK_AQUA, node.getExplanation()));
	}

}
