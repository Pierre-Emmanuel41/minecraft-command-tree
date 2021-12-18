package fr.pederobien.minecraft.commandtree.impl;

import java.util.function.Supplier;

import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeNode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;

public class MinecraftCodeNode extends MinecraftNode<IMinecraftCode> implements IMinecraftCodeNode {

	/**
	 * Create a minecraft node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	protected MinecraftCodeNode(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable) {
		super(label, explanation, isAvailable);
	}

	/**
	 * Create a minecraft node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 */
	protected MinecraftCodeNode(String label, IMinecraftCode explanation) {
		super(label, explanation);
	}
}
