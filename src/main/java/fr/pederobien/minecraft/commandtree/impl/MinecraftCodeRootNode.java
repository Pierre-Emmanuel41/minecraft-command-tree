package fr.pederobien.minecraft.commandtree.impl;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.bukkit.entity.Player;

import fr.pederobien.commandtree.interfaces.INode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeNode;
import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftHelperNode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;

public class MinecraftCodeRootNode extends MinecraftRootNode<IMinecraftCode> implements IMinecraftCodeNode {

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
	 * @param displayer   The consumer that override the default behavior when the player attempt to get an explanation of one child
	 *                    of the given node.
	 */
	public MinecraftCodeRootNode(String label, IMinecraftCode explanation, Supplier<Boolean> isAvailable, BiConsumer<Player, INode<IMinecraftCode>> displayer) {
		super(label, explanation, isAvailable);
		setHelperNode(new MinecraftHelperNode<IMinecraftCode>(this, displayer));
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
		setHelperNode(new MinecraftHelperNode<IMinecraftCode>(this));
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
