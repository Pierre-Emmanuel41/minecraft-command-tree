package fr.pederobien.minecraft.commandtree.interfaces;

import org.bukkit.command.TabExecutor;

import fr.pederobien.commandtree.interfaces.INode;

public interface IMinecraftNode<T> extends INode<T>, TabExecutor {
}
