package fr.pederobien.minecraft.commandtree.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import fr.pederobien.commandtree.impl.HelperNode;
import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftHelperNode;

public class MinecraftHelperNode<T> extends HelperNode<T> implements IMinecraftHelperNode<T> {
	private BiFunction<CommandSender, INode<T>, String> displayer;

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
	public MinecraftHelperNode(INode<T> node, BiFunction<CommandSender, INode<T>, String> displayer) {
		super(node);
		this.displayer = displayer;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return onTabComplete(sender, getSource(), args);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			INode<T> child = getSource().getChildren().get(args[0]);
			for (int i = 1; i < args.length; i++)
				if (child != null)
					child = child.getChildren().get(args[i]);
			display(sender, child);
			child.getChildren().values().stream().filter(node -> node.isAvailable()).forEach(node -> display(sender, node));
		} catch (IndexOutOfBoundsException e) {
			getSource().getChildren().values().stream().filter(node -> node.isAvailable()).forEach(node -> display(sender, node));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private List<String> onTabComplete(CommandSender sender, INode<T> source, String... args) {
		switch (args.length) {
		case 0:
			return new ArrayList<String>();
		default:
			String label = args[0];
			INode<T> node = source.getChildren().get(label);
			return node == null ? filter(source.getChildren().values().stream(), args).collect(Collectors.toList()) : onTabComplete(sender, node, extract(args, 1));
		}
	}

	private void display(CommandSender sender, INode<T> node) {
		if (displayer != null)
			sender.sendMessage(displayer.apply(sender, node));
		else
			sender.sendMessage(node.getLabel() + " - " + node.getExplanation());
	}

	/**
	 * Copies the specified range of the specified array into a new array. The initial index of the range (<tt>from</tt>) must lie
	 * between zero and <tt>original.length</tt>, inclusive. The value at <tt>original[from]</tt> is placed into the initial element
	 * of the copy (unless <tt>from == original.length</tt> or <tt>from == to</tt>). Values from subsequent elements in the original
	 * array are placed into subsequent elements in the copy. The final index of the range (<tt>to</tt>), which must be greater than
	 * or equal to <tt>from</tt>, may be greater than <tt>original.length</tt>, in which case <tt>null</tt> is placed in all elements
	 * of the copy whose index is greater than or equal to <tt>original.length - from</tt>. The length of the returned array will be
	 * <tt>to - from</tt>.
	 * <p>
	 * The resulting array is of exactly the same class as the original array.
	 *
	 * @param original the array from which a range is to be copied.
	 * @param from     the initial index of the range to be copied, inclusive.
	 * @param to       the final index of the range to be copied, exclusive. (This index may lie outside the array.)
	 * @return a new array containing the specified range from the original array, truncated or padded with nulls to obtain the
	 *         required length
	 * @throws ArrayIndexOutOfBoundsException if {@code from < 0} or {@code from > original.length}
	 * @throws IllegalArgumentException       if <tt>from &gt; to</tt>
	 * @throws NullPointerException           if <tt>original</tt> is null
	 */
	protected String[] extract(String[] original, int from, int to) {
		return Arrays.copyOfRange(original, from, to);
	}

	/**
	 * Copy the specified array into a new array. This method is equivalent to call {@link #extract(String[], int, int)} with
	 * parameter "to" equals args.length.
	 * 
	 * @param original the array from which a range is to be copied.
	 * @param from     the initial index of the range to be copied, inclusive.
	 * 
	 * @return a new array containing the specified range from the original array, truncated or padded with nulls to obtain the
	 *         required length
	 */
	protected String[] extract(String[] original, int from) {
		return extract(original, from, original.length);
	}
}
