package fr.pederobien.minecraft.commandtree.impl;

import java.util.List;
import java.util.function.Supplier;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import fr.pederobien.minecraft.commandtree.interfaces.ICodeSender;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeNode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftHelperNode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;

public class MinecraftCodeTree {

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 * @param helperNode  The helper associated to this root.
	 */
	public static MinecraftCodeTreeBuilder create(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable,
			IMinecraftHelperNode<IMinecraftCode> helperNode) {
		return new MinecraftCodeTreeBuilder(label, explanation, isAvailable, helperNode);
	}

	/**
	 * Creates a root node specified by the given parameters with an empty list of aliases.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	public static <T> MinecraftCodeTreeBuilder create(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable) {
		return new MinecraftCodeTreeBuilder(label, explanation, isAvailable);
	}

	/**
	 * Creates a root node specified by the given parameters with an empty list of aliases.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 */
	public static MinecraftCodeTreeBuilder create(String label, IMinecraftCode explanation) {
		return new MinecraftCodeTreeBuilder(label, explanation);
	}

	public static class MinecraftCodeTreeBuilder implements ICodeSender {
		private IMinecraftCodeNode root;

		/**
		 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param label       The name of the node.
		 * @param explanation The explanation of the node.
		 * @param isAvailable True if this node is available, false otherwise.
		 * @param helperNode  The helper associated to this root.
		 */
		private MinecraftCodeTreeBuilder(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable, IMinecraftHelperNode<IMinecraftCode> helperNode) {
			root = new MinecraftCodeRootNode(label, explanation, isAvailable, helperNode);
		}

		/**
		 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param label       The name of the node.
		 * @param explanation The explanation of the node.
		 * @param isAvailable True if this node is available, false otherwise.
		 */
		private MinecraftCodeTreeBuilder(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable) {
			root = new MinecraftCodeRootNode(label, explanation, isAvailable);
		}

		/**
		 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
		 * 
		 * @param label       The name of the node.
		 * @param explanation The explanation of the node.
		 */
		private MinecraftCodeTreeBuilder(String label, IMinecraftCode explanation) {
			root = new MinecraftCodeRootNode(label, explanation);
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 * @param isAvailable True if this node is available, false otherwise.
		 */
		public MinecraftCodeNodeBuilder addNode(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable) {
			return new MinecraftCodeNodeBuilder(root, label, explanation, isAvailable);
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 */
		public MinecraftCodeNodeBuilder addNode(String label, IMinecraftCode explanation) {
			return new MinecraftCodeNodeBuilder(root, label, explanation);
		}

		/**
		 * @return The root of this tree.
		 */
		public IMinecraftCodeNode build() {
			return root;
		}
	}

	public static class MinecraftCodeNodeBuilder {
		private IMinecraftCodeNode parent;
		private SetupNode node;

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param parent      The parent node associated to the underlying node.
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 * @param isAvailable True if this node is available, false otherwise.
		 */
		private MinecraftCodeNodeBuilder(IMinecraftCodeNode parent, String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable) {
			this.parent = parent;
			node = new SetupNode(new MinecraftCodeNode(label, explanation, isAvailable));
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param parent      The parent node associated to the underlying node.
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 */
		private MinecraftCodeNodeBuilder(IMinecraftCodeNode parent, String label, IMinecraftCode explanation) {
			this.parent = parent;
			node = new SetupNode(new MinecraftCodeNode(label, explanation));
		}

		/**
		 * Set the action to perform when method onTabComplete is called.
		 * 
		 * @param executor The action to perform.
		 * 
		 * @return this node builder.
		 */
		public MinecraftCodeNodeBuilder withCompleter(TabCompleter completor) {
			node.setCompletor(completor);
			return this;
		}

		/**
		 * Set the action to perform when method onCommand is called.
		 * 
		 * @param completor The action to perform.
		 */
		public MinecraftCodeNodeBuilder withExecutor(CommandExecutor executor) {
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
		public MinecraftCodeNodeBuilder addNode(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable) {
			return new MinecraftCodeNodeBuilder(node, label, explanation, isAvailable);
		}

		/**
		 * Creates a node specified by the given parameters.
		 * 
		 * @param label       The primary node name.
		 * @param explanation The explanation associated to this node.
		 */
		public MinecraftCodeNodeBuilder addNode(String label, IMinecraftCode explanation) {
			return new MinecraftCodeNodeBuilder(node, label, explanation);
		}

		/**
		 * Add the constructed node to the parent node associated to this builder.
		 * 
		 * @return this builder.
		 */
		public MinecraftCodeNodeBuilder append() {
			parent.add(node);
			return this;
		}

		/**
		 * Add the constructed node to the parent node associated to this builder.
		 * 
		 * @return the constructed node.
		 */
		public IMinecraftCodeNode addAndGet() {
			parent.add(node);
			return node;
		}
	}

	private static class SetupNode extends MinecraftCodeNodeWrapper {
		private TabCompleter completor;
		private CommandExecutor executor;

		protected SetupNode(IMinecraftCodeNode source) {
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
