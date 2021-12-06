package fr.pederobien.minecraft.commandtree.interfaces;

import org.bukkit.command.TabExecutor;

import fr.pederobien.commandtree.interfaces.IHelperNode;

public interface IMinecraftHelperNode<T> extends IHelperNode<T>, TabExecutor {

}
