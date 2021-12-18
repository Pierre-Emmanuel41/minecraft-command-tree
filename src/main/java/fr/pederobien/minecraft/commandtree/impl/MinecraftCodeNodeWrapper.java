package fr.pederobien.minecraft.commandtree.impl;

import fr.pederobien.minecraft.commandtree.interfaces.IMinecraftCodeNode;
import fr.pederobien.minecraft.dictionary.interfaces.IMinecraftCode;

public class MinecraftCodeNodeWrapper extends MinecraftNodeWrapper<IMinecraftCode> implements IMinecraftCodeNode {

	protected MinecraftCodeNodeWrapper(IMinecraftCodeNode source) {
		super(source);
	}
}
