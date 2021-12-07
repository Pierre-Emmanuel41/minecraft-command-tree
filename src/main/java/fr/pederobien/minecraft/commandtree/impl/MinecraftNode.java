package fr.pederobien.minecraft.commandtree.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.commandtree.impl.Node;
import fr.pederobien.commandtree.impl.NodeNotFoundException;
import fr.pederobien.commandtree.impl.NotAvailableArgumentException;
import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftNode;

public class MinecraftNode<T> extends Node<T> implements IMinecraftNode<T> {

	/**
	 * Create a minecraft node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	protected MinecraftNode(String label, T explanation, Supplier<Boolean> isAvailable) {
		super(label, explanation, isAvailable);
	}

	/**
	 * Create a minecraft node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 */
	protected MinecraftNode(String label, T explanation) {
		super(label, explanation);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!isAvailable())
			return emptyList();

		try {
			IMinecraftNode<T> node = getChildren().get(args[0]);

			// Node not recognized, display all available children nodes.
			if (node == null)
				return filter(getAvailableChildren().map(e -> e.getLabel()), args[0]);

			// Node not available, display nothing.
			if (!node.isAvailable())
				return emptyList();

			return node.onTabComplete(sender, command, alias, extract(args, 1));
		} catch (IndexOutOfBoundsException e) {
			// When args is empty -> args[0] throw an IndexOutOfBoundsException
			return emptyList();
		}
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @throws NodeNotFoundException         If the last string contains in the <code>args</code> parameter does not refer to a node
	 *                                       name.
	 * @throws NotAvailableArgumentException If this node is not available.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			String editionLabel = args[0];
			IMinecraftNode<T> node = getChildren().get(editionLabel);

			if (node == null)
				throw new NodeNotFoundException(label, editionLabel, args);

			if (!node.isAvailable())
				throw new NotAvailableArgumentException(command.getLabel(), editionLabel);

			return node.onCommand(sender, command, label, extract(args, 1));
		} catch (IndexOutOfBoundsException e) {
			// Do nothing
		}
		return true;
	}

	@Override
	public void setParent(INode<T> parent) {
		super.setParent((IMinecraftNode<T>) parent);
	}

	@Override
	public IMinecraftNode<T> getParent() {
		return (IMinecraftNode<T>) super.getParent();
	}

	@Override
	public IMinecraftNode<T> getRoot() {
		return (IMinecraftNode<T>) super.getRoot();
	}

	@Override
	public void add(INode<T> node) {
		super.add((IMinecraftNode<T>) node);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, IMinecraftNode<T>> getChildren() {
		return (Map<String, IMinecraftNode<T>>) super.getChildren();
	}
}
