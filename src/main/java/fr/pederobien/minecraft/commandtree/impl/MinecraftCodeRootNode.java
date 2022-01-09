package fr.pederobien.minecraft.commandtree.impl;

import java.util.function.Supplier;

import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeRootNode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftHelperNode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;

public class MinecraftCodeRootNode extends MinecraftRootNode<IMinecraftCode> implements IMinecraftCodeRootNode {

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 * @param helperNode  The helper associated to this root.
	 */
	protected MinecraftCodeRootNode(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable, IMinecraftHelperNode<IMinecraftCode> helperNode) {
		super(label, explanation, isAvailable, helperNode);
	}

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	public MinecraftCodeRootNode(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable) {
		super(label, explanation, isAvailable);
		setHelperNode(new MinecraftCodeHelperNode(this).getHelper());
	}

	/**
	 * Create a minecraft root node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 */
	public MinecraftCodeRootNode(String label, IMinecraftCode explanation) {
		this(label, explanation, () -> false);
	}
}
