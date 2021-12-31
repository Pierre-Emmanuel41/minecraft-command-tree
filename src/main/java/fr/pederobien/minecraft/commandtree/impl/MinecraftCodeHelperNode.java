package fr.pederobien.minecraft.commandtree.impl;

import java.util.function.BiFunction;

import org.bukkit.command.CommandSender;

import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.commandtree.interfaces.ICodeSender;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;
import fr.pederobien.minecraft.managers.EColor;

public class MinecraftCodeHelperNode implements ICodeSender {
	private MinecraftHelperNode<IMinecraftCode> helper;

	/**
	 * Creates an helper responsible to display the explanation of one or several children of the given source node. The default
	 * behavior for displaying node explanation is calling <code>System.out.println(node.getLabel() - node.getExplanation())</code>.
	 * 
	 * @param source The node source in order to display the explanation of one or several children.
	 */
	protected MinecraftCodeHelperNode(INode<IMinecraftCode> source) {
		BiFunction<CommandSender, INode<IMinecraftCode>, String> displayer = (sender, node) -> {
			String label = EColor.DARK_RED.getInColor(node.getLabel());
			String explanation = EColor.DARK_AQUA.getInColor(getMessage(sender, node.getExplanation()));
			return String.format("%s - %s", label, explanation);
		};
		helper = new MinecraftHelperNode<IMinecraftCode>(source, displayer);
	}

	/**
	 * @return The helper that display translated explanation of nodes.
	 */
	public MinecraftHelperNode<IMinecraftCode> getHelper() {
		return helper;
	}
}
