package fr.pederobien.minecraft.commandtree.impl;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftHelperNode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftNode;

public class MinecraftTree {

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 * @param helperNode  The helper associated to this root.
	 */
	public static <T> MinecraftTreeBuilder<T> create(String label, T explanation, Supplier<Boolean> isAvailable, IMinecraftHelperNode<T> helperNode) {
		return new MinecraftTreeBuilder<T>(label, explanation, isAvailable, helperNode);
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
	public static <T> MinecraftTreeBuilder<T> create(String label, T explanation, Supplier<Boolean> isAvailable, BiFunction<CommandSender, INode<T>, String> displayer) {
		return new MinecraftTreeBuilder<T>(label, explanation, isAvailable, displayer);
	}

	/**
	 * Creates a root node specified by the given parameters with an empty list of aliases.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	public static <T> MinecraftTreeBuilder<T> create(String label, T explanation, Supplier<Boolean> isAvailable) {
		return new MinecraftTreeBuilder<T>(label, explanation, isAvailable);
	}

	/**
	 * Creates a root node specified by the given parameters with an empty list of aliases.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 */
	public static <T> MinecraftTreeBuilder<T> create(String label, T explanation) {
		return new MinecraftTreeBuilder<T>(label, explanation);
	}

	public static class MinecraftTreeBuilder<T> {
		private IMinecraftNode<T> root;

		/**
		 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param label       The name of the node.
		 * @param explanation The explanation of the node.
		 * @param isAvailable True if this node is available, false otherwise.
		 * @param helperNode  The helper associated to this root.
		 */
		private MinecraftTreeBuilder(String label, T explanation, Supplier<Boolean> isAvailable, IMinecraftHelperNode<T> helperNode) {
			root = new MinecraftRootNode<T>(label, explanation, isAvailable, helperNode);
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
		private MinecraftTreeBuilder(String label, T explanation, Supplier<Boolean> isAvailable, BiFunction<CommandSender, INode<T>, String> displayer) {
			root = new MinecraftRootNode<T>(label, explanation, isAvailable, displayer);
		}

		/**
		 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param label       The name of the node.
		 * @param explanation The explanation of the node.
		 * @param isAvailable True if this node is available, false otherwise.
		 */
		private MinecraftTreeBuilder(String label, T explanation, Supplier<Boolean> isAvailable) {
			root = new MinecraftRootNode<T>(label, explanation, isAvailable);
		}

		/**
		 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param label       The name of the node.
		 * @param explanation The explanation of the node.
		 */
		private MinecraftTreeBuilder(String label, T explanation) {
			root = new MinecraftRootNode<T>(label, explanation);
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 * @param isAvailable True if this node is available, false otherwise.
		 */
		public MinecraftNodeBuilder<T> addNode(String label, T explanation, Supplier<Boolean> isAvailable) {
			return new MinecraftNodeBuilder<T>(root, label, explanation, isAvailable);
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 */
		public MinecraftNodeBuilder<T> addNode(String label, T explanation) {
			return new MinecraftNodeBuilder<T>(root, label, explanation);
		}

		/**
		 * @return The root of this tree.
		 */
		public IMinecraftNode<T> build() {
			return root;
		}
	}

	public static class MinecraftNodeBuilder<T> {
		private IMinecraftNode<T> parent;
		private SetupNode<T> node;

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param parent      The parent node associated to the underlying node.
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 * @param isAvailable True if this node is available, false otherwise.
		 */
		private MinecraftNodeBuilder(IMinecraftNode<T> parent, String label, T explanation, Supplier<Boolean> isAvailable) {
			this.parent = parent;
			node = new SetupNode<T>(new MinecraftNode<T>(label, explanation, isAvailable));
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param parent      The parent node associated to the underlying node.
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 */
		private MinecraftNodeBuilder(IMinecraftNode<T> parent, String label, T explanation) {
			this.parent = parent;
			node = new SetupNode<T>(new MinecraftNode<T>(label, explanation));
		}

		/**
		 * Set the action to perform when method onTabComplete is called.
		 * 
		 * @param executor The action to perform.
		 * 
		 * @return this node builder.
		 */
		public MinecraftNodeBuilder<T> withCompleter(TabCompleter completor) {
			node.setCompletor(completor);
			return this;
		}

		/**
		 * Set the action to perform when method onCommand is called.
		 * 
		 * @param completor The action to perform.
		 */
		public MinecraftNodeBuilder<T> withExecutor(CommandExecutor executor) {
			node.setExecutor(executor);
			return this;
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 * @param isAvailable True if this node is available, false otherwise.
		 */
		public MinecraftNodeBuilder<T> addNode(String label, T explanation, Supplier<Boolean> isAvailable) {
			return new MinecraftNodeBuilder<T>(node, label, explanation, isAvailable);
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 */
		public MinecraftNodeBuilder<T> addNode(String label, T explanation) {
			return new MinecraftNodeBuilder<T>(node, label, explanation);
		}

		/**
		 * Add the constructed node to the parent node associated to this builder.
		 * 
		 * @return this builder.
		 */
		public MinecraftNodeBuilder<T> append() {
			parent.add(node);
			return this;
		}

		/**
		 * Add the constructed node to the parent node associated to this builder.
		 * 
		 * @return the constructed node.
		 */
		public IMinecraftNode<T> addAndGet() {
			parent.add(node);
			return node;
		}
	}

	private static class SetupNode<T> extends MinecraftNodeWrapper<T> {
		private TabCompleter completor;
		private CommandExecutor executor;

		protected SetupNode(IMinecraftNode<T> source) {
			super(source);
		}

		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
			return completor == null ? super.onTabComplete(sender, command, alias, args) : completor.onTabComplete(sender, command, alias, args);
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			return executor == null ? super.onCommand(sender, command, label, args) : executor.onCommand(sender, command, label, args);
		}

		/**
		 * Set the action to perform when method onTabComplete is called.
		 * 
		 * @param executor The action to perform.
		 */
		public void setCompletor(TabCompleter completor) {
			this.completor = completor;
		}

		/**
		 * Set the action to perform when method onCommand is called.
		 * 
		 * @param completor The action to perform.
		 */
		public void setExecutor(CommandExecutor executor) {
			this.executor = executor;
		}
	}
}
