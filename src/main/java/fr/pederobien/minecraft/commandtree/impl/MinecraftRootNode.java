package fr.pederobien.minecraft.commandtree.impl;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.commandtree.impl.NodeNotFoundException;
import fr.pederobien.commandtree.impl.NotAvailableArgumentException;
import fr.pederobien.commandtree.impl.NotAvailableCommandException;
import fr.pederobien.commandtree.impl.RootNode;
import fr.pederobien.commandtree.interfaces.IHelperNode;
import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftHelperNode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftNode;

public class MinecraftRootNode<T> extends RootNode<T> implements IMinecraftNode<T> {

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 * @param helperNode  The helper associated to this root.
	 */
	protected MinecraftRootNode(String label, T explanation, Supplier<Boolean> isAvailable, IMinecraftHelperNode<T> helperNode) {
		super(label, explanation, isAvailable, helperNode);
	}

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 * @param displayer   The consumer that override the default behavior when the player attempt to get an explanation of one child
	 *                    of the given node.
	 */
	public MinecraftRootNode(String label, T explanation, Supplier<Boolean> isAvailable, BiConsumer<Player, INode<T>> displayer) {
		super(label, explanation, isAvailable);
		setHelperNode(new MinecraftHelperNode<T>(this, displayer));
	}

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	public MinecraftRootNode(String label, T explanation, Supplier<Boolean> isAvailable) {
		super(label, explanation, isAvailable);
		setHelperNode(new MinecraftHelperNode<T>(this));
	}

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 */
	public MinecraftRootNode(String label, T explanation) {
		this(label, explanation, () -> false);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (!isAvailable())
			return emptyList();

		try {
			IMinecraftNode<T> node = getChildren().get(args[0]);

			// Node not recognized, display all available children nodes, including the helper.
			if (node == null)
				return filter(concat(getAvailableChildren().map(e -> e.getLabel()), Stream.of(getHelper().getLabel())), args[0]);

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
	 * @throws NotAvailableCommandException  If this command is not available.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String first = "";
		if (!isAvailable())
			throw new NotAvailableCommandException(command.getLabel());

		try {
			// If the label correspond to "help" then execute its method onCommand.
			first = args[0];
			if (first.equals(getHelper().getLabel()))
				return getHelper().onCommand(sender, command, label, extract(args, 1));

			IMinecraftNode<T> node = getChildren().get(first);
			if (node == null)
				throw new NodeNotFoundException(label, first, args);

			if (!node.isAvailable())
				throw new NotAvailableArgumentException(command.getLabel(), node.getLabel());

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

	@Override
	protected IMinecraftHelperNode<T> getHelper() {
		return (IMinecraftHelperNode<T>) super.getHelper();
	}

	@Override
	protected void setHelperNode(IHelperNode<T> helperNode) {
		super.setHelperNode((IMinecraftHelperNode<T>) helperNode);
	}
}
