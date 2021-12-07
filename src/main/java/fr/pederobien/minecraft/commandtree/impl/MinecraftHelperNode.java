package fr.pederobien.minecraft.commandtree.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.pederobien.commandtree.impl.HelperNode;
import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftHelperNode;

public class MinecraftHelperNode<T> extends HelperNode<T> implements IMinecraftHelperNode<T> {
	private BiConsumer<Player, INode<T>> displayer;
	private Player player;

	/**
	 * Creates an helper responsible to display the explanation of one or several children of the given source node. The default
	 * behavior for displaying node explanation is calling <code>System.out.println(node.getLabel() - node.getExplanation())</code>.
	 * 
	 * @param source The node source in order to display the explanation of one or several children.
	 */
	protected MinecraftHelperNode(INode<T> node) {
		super(node);
	}

	/**
	 * Creates an helper. An helper is defined a node used to iterate over its children or over the children of its children etc.
	 * 
	 * @param node      The node associated to this helper.
	 * @param displayer The consumer that override the default behavior when the player attempt to get an explanation of one child of
	 *                  the given node.
	 */
	public MinecraftHelperNode(INode<T> node, BiConsumer<Player, INode<T>> displayer) {
		super(node);
		this.displayer = displayer;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		try {
			Collection<? extends INode<T>> values = getSource().getChildren().values();
			INode<T> edition = getSource().getChildren().get(args[0]);

			for (int i = 1; i < args.length; i++) {
				if (edition != null) {
					values = edition.getChildren().values();
					edition = edition.getChildren().get(args[i]);
				}
			}
			return filter(values.stream()).filter(str -> str.contains(args[args.length - 1])).collect(Collectors.toList());
		} catch (IndexOutOfBoundsException | NullPointerException e) {
			return new ArrayList<String>();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player)
			this.player = (Player) sender;

		try {
			INode<T> child = getSource().getChildren().get(args[0]);
			for (int i = 1; i < args.length; i++)
				if (child != null)
					child = child.getChildren().get(args[i]);
			display(child);
		} catch (IndexOutOfBoundsException e) {
			for (Map.Entry<String, ? extends INode<T>> entry : getSource())
				display(entry.getValue());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private void display(INode<T> node) {
		if (player == null)
			super.displayExplanation(node);
		else if (displayer != null)
			displayer.accept(player, node);
		else
			player.sendMessage(node.getLabel() + " - " + node.getExplanation());

		// Reinitializing the player for the next help command.
		player = null;
	}
}
