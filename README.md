# Presentation

This project is a continuity of the [command-tree](https://github.com/Pierre-Emmanuel41/command-tree.git) project in order to simplify the creation of custom minecraft command.

# Download

According to the Minecraft API version there is on the server, you should download this project by specifying the branch associated to the associated version if supported. To do so, you can use the following command line :

```git
git clone -b 1.0_MC_1.16.5-SNAPSHOT https://github.com/Pierre-Emmanuel41/minecraft-command-tree.git --recursive
```

and then double click on the deploy.bat file. This will deploy this project and all its dependencies on your computer. Which means it generates the folder associated to this project and its dependencies in your .m2 folder. Once this has been done, you can add the project as maven dependency on your maven project :

```xml
<dependency>
	<groupId>fr.pederobien</groupId>
	<artifactId>minecraft-command-tree</artifactId>
	<version>1.0_MC_1.16.5-SNAPSHOT</version>
</dependency>
```

To see how you can use thoses features, please have a look to [This tutorial](https://github.com/Pierre-Emmanuel41/minecraft-command-tree/blob/1.0_MC_1.16.5-SNAPSHOT/Tutorial.md)