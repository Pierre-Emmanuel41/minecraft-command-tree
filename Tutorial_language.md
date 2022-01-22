In the previous [tutorial](https://github.com/Pierre-Emmanuel41/minecraft-command-tree/blob/1.0_MC_1.16.5-SNAPSHOT/Tutorial.md), we discovered how to create a simple minecraft command tree in order to modify the property of a person using four different commands: "./person new <name> <birthday>", "./person modify name <name>", "./person modify birthday <YYYY-MM-dd>", "./person show". But each returned message are hardcoded in English. It would be better to return a message translated in the player's language that is running the command. It is the goal of this tutorial to explain how to create a language sensitive command tree.

In this tutorial, we will use the same model <code>Person</code> as in the previous one.

### 1) Creating code enumeration

The first steps consists in creating an enumeration that implements the <code>IMinecraftCode</code> interface in order to gather to different message codes used to get translated message. Let's call it <code>EPersonCode</code>

```java
public enum EPersonCode implements IMinecraftCode {

	// PersonRootNode -------------------------------------------------

	// Code for the explanation of the person root command
	PERSON__ROOT__EXPLANATION,

	// NewPersonNode --------------------------------------------------

	// Code for the explanation of the "new" command
	PERSON__NEW__EXPLANATION,

	// Code for the completion for the <name> value
	PERSON__NEW__NAME__COMPLETE,

	// Code for the completion for the <birthday> value
	PERSON__NEW__BIRTHDAY_COMPLETE,

	// Code when the name of the person is missing
	PERSON__NEW__NAME_IS_MISSING,

	// Code when the birthday of the person is missing
	PERSON__NEW__BIRTHDAY_IS_MISSING,

	// Code when the birthday of the person has a bad format
	PERSON__NEW__BIRTHDAY_BAD_FORMAT,

	// Code when a new person is created
	PERSON__NEW__PERSON_CREATED(PlayerGroup.ALL),

	// ModifyPersonNode -----------------------------------------------

	// Code for the explanation of the "modify" command
	PERSON__MODIFY__EXPLANATION,

	// NamePersonNode -------------------------------------------------

	// Code for the explanation of the "modify name" command
	PERSON__MODIFY__NAME__EXPLANATION,

	// Code for the completion for the <new name> value
	PERSON__NEW_NAME__COMPLETE,

	// Code when the new name is missing
	PERSON__NEW_NAME_IS_MISSING,

	// Code when a person has been renamed
	PERSON__RENAMED(PlayerGroup.ALL),

	// BirthdayPersonNode ---------------------------------------------

	// Code for the explanation of the "modify birthday" command
	PERSON__MODIFY_BIRTHDAY__EXPLANATION,

	// Code for the completion for the <birthday> value
	PERSON__NEW_BIRTHDAY__COMPLETE,

	// Code when the new birthday is missing
	PERSON__NEW_BIRTHDAY_IS_MISSING,

	// Code when the birthday of a person has been updated
	PERSON__BIRTHDAY_UPDATED(PlayerGroup.ALL),

	// ShowPersonNode -------------------------------------------------

	// Code for the explanation of the "show" command
	PERSON__SHOW__EXPLANATION,

	// Code when the properties of a person are shown
	PERSON__SHOW(PlayerGroup.ALL);

	private IPlayerGroup group;

	private EPersonCode() {
		this(PlayerGroup.OPERATORS);
	}

	private EPersonCode(IPlayerGroup group) {
		this.group = group;
	}

	@Override
	public String value() {
		return name();
	}

	@Override
	public IPlayerGroup getGroup() {
		return group;
	}

	@Override
	public void setGroup(IPlayerGroup group) {
		this.group = group;
	}

	@Override
	public String toString() {
		return String.format("value=%s,group=%s", value(), getGroup());
	}
}
```

### 2) Creating dictionaries

The second step consists in creating dictionary files (the format does not matter). For this example we will create an English dictionary and a French dictionary with the XML format because this project used the [persistence project](https://github.com/Pierre-Emmanuel41/persistence.git) that proposes features to load/save xml files:

```xml
<?xml version="1.0" encoding="UTF-8"?>
	<dictionary>
		<version>1.0</version>
		<locales>
			<locale>en</locale>
			<locale>en-EN</locale>
			<locale>en-GB</locale>
			<locale>en-UK</locale>
			<locale>en-US</locale>
			<locale>en-CA</locale>
		</locales>
		<messages>
			<message>
				<code>PERSON__ROOT__EXPLANATION</code>
				<value>Command to create/modify the property of a person</value>
			</message>
			<message>
				<code>PERSON__NEW__EXPLANATION</code>
				<value>To create a new person</value>
			</message>
			<message>
				<code>PERSON__NEW__NAME__COMPLETE</code>
				<value>&lt;name&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW__BIRTHDAY_COMPLETE</code>
				<value>&lt;birthday&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW__NAME_IS_MISSING</code>
				<value>The name is missing</value>
			</message>
			<message>
				<code>PERSON__NEW__BIRTHDAY_IS_MISSING</code>
				<value>The birthday is missing</value>
			</message>
			<message>
				<code>PERSON__NEW__BIRTHDAY_BAD_FORMAT</code>
				<value>Bad format for the birthday</value>
			</message>
			<message>
				<code>PERSON__NEW__PERSON_CREATED</code>
				<value>New person created : name=%s, birthday=%s</value>
			</message>
			<message>
				<code>PERSON__MODIFY__EXPLANATION</code>
				<value>To modify the property of a person</value>
			</message>
			<message>
				<code>PERSON__MODIFY__NAME__EXPLANATION</code>
				<value>To change the name of the person</value>
			</message>
			<message>
				<code>PERSON__NEW_NAME__COMPLETE</code>
				<value>&lt;newName&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW_NAME_IS_MISSING</code>
				<value>The new name is missing</value>
			</message>
			<message>
				<code>PERSON__RENAMED</code>
				<value>Renaming %s as %s</value>
			</message>
			<message>
				<code>PERSON__MODIFY_BIRTHDAY__EXPLANATION</code>
				<value>To change the birthday of the person</value>
			</message>
			<message>
				<code>PERSON__NEW_BIRTHDAY__COMPLETE</code>
				<value>&lt;birthday&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW_BIRTHDAY_IS_MISSING</code>
				<value>The birthday is missing</value>
			</message>
			<message>
				<code>PERSON__BIRTHDAY_UPDATED</code>
				<value>Changing the birthday of %s (%s -> %s)</value>
			</message>
			<message>
				<code>PERSON__SHOW__EXPLANATION</code>
				<value>To show the properties of the person</value>
			</message>
			<message>
				<code>PERSON__SHOW</code>
				<value>Person properties : name=%s, birthday=%s</value>
			</message>
		</messages>
	</dictionary>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
	<dictionary>
		<version>1.0</version>
		<locales>
			<locale>fr</locale>
			<locale>fr-FR</locale>
			<locale>fr-CA</locale>
		</locales>
		<messages>
			<message>
				<code>PERSON__ROOT__EXPLANATION</code>
				<value>Command pour créer/modifier les charactéristiques d'une personne</value>
			</message>
			<message>
				<code>PERSON__NEW__EXPLANATION</code>
				<value>Pour créer une nouvelle personne</value>
			</message>
			<message>
				<code>PERSON__NEW__NAME__COMPLETE</code>
				<value>&lt;nom&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW__BIRTHDAY_COMPLETE</code>
				<value>&lt;date de naissance&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW__NAME_IS_MISSING</code>
				<value>Le nom est manquant</value>
			</message>
			<message>
				<code>PERSON__NEW__BIRTHDAY_IS_MISSING</code>
				<value>La date d'anniversaire est manquante</value>
			</message>
			<message>
				<code>PERSON__NEW__BIRTHDAY_BAD_FORMAT</code>
				<value>Mauvais format pour la date d'anniversaire</value>
			</message>
			<message>
				<code>PERSON__NEW__PERSON_CREATED</code>
				<value>Nouvelle personne créée : nom=%s, date de naissance=%s</value>
			</message>
			<message>
				<code>PERSON__MODIFY__EXPLANATION</code>
				<value>Pour modifier les charactéristiques d'une personne</value>
			</message>
			<message>
				<code>PERSON__MODIFY__NAME__EXPLANATION</code>
				<value>Pour changer le nom d'une personne</value>
			</message>
			<message>
				<code>PERSON__NEW_NAME__COMPLETE</code>
				<value>&lt;nouveau nom&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW_NAME_IS_MISSING</code>
				<value>Le nouveau nom est manquant</value>
			</message>
			<message>
				<code>PERSON__RENAMED</code>
				<value>%s a été renommé %s</value>
			</message>
			<message>
				<code>PERSON__MODIFY_BIRTHDAY__EXPLANATION</code>
				<value>Pour changer la date de naissance d'une personne</value>
			</message>
			<message>
				<code>PERSON__NEW_BIRTHDAY__COMPLETE</code>
				<value>&lt;date de naissance&gt;</value>
			</message>
			<message>
				<code>PERSON__NEW_BIRTHDAY_IS_MISSING</code>
				<value>La date d'anniversaire est manquante</value>
			</message>
			<message>
				<code>PERSON__BIRTHDAY_UPDATED</code>
				<value>La date d'anniversaire de %s a été mise à jour (%s -> %s)</value>
			</message>
			<message>
				<code>PERSON__SHOW__EXPLANATION</code>
				<value>Pour afficher les charactéristiques d'une personne</value>
			</message>
			<message>
				<code>PERSON__SHOW</code>
				<value>Charactéristiques : nom=%s, date de naissance=%s</value>
			</message>
		</messages>
	</dictionary>
```

Those dictionaries contains the association of a specific code and the translated message.

### 3) Creating the tree

Like in the previous tutorial there are two ways to create your own language sensitive command tree:
* With the <code>MinecraftCodeTreeBuilder</code>  
* Inheriting the <code>MinecraftCodeNode</code> class  

In all cases, the <code>ICodeSender</code> interface can be used to send translated messages to a player or a specific group of players.

#### 3.1) MinecraftCodeTreeBuilder

```java
public class PersonMinecraftCodeTree {
	private IMinecraftCodeNode root;
	private Person person;

	public PersonMinecraftCodeTree() {
		// FIRST: CREATION OF THE ROOT NODE "person" -------------------------------------------------------------------------------
		MinecraftCodeTreeBuilder builder = MinecraftCodeTree.create("person", EPersonCode.PERSON__ROOT__EXPLANATION, () -> true);

		// SECOND: CREATION OF THE FIRST CHILD "new" -------------------------------------------------------------------------------
		// Adding first node corresponding to the "new" argument
		// ()-> true: because this command should always be available.
		builder.addNode("new", EPersonCode.PERSON__NEW__EXPLANATION, () -> true).withCompleter((sender, command, alias, args) -> {
			// Code to execute for a completion (expected argument).
			switch (args.length) {
			case 0:
				return Arrays.asList(builder.getMessage(sender, EPersonCode.PERSON__NEW__NAME__COMPLETE));
			case 1:
				return Arrays.asList(builder.getMessage(sender, EPersonCode.PERSON__NEW__BIRTHDAY_COMPLETE));
			default:
				return Arrays.asList();
			}
		}).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			String name;
			try {
				name = args[0];
			} catch (IndexOutOfBoundsException e) {
				builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__NEW__NAME_IS_MISSING).build());
				return false;
			}

			LocalDate birthday;
			try {
				birthday = LocalDate.parse(args[1]);
			} catch (IndexOutOfBoundsException e) {
				builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_IS_MISSING).build());
				return false;
			} catch (DateTimeParseException e) {
				builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_BAD_FORMAT).build());
				return false;
			}

			person = new Person(name, birthday);
			builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__NEW__PERSON_CREATED, person.getName(), person.getBirthday()));
			return true;
		}).append();

		// THIRD: CREATION OF THE SECOND CHILD "modify" ----------------------------------------------------------------------------
		// Adding second node corresponding to the "modify" argument
		// () -> person != null: this argument should be available if a new person has been created before.
		MinecraftCodeNodeBuilder modifyBuilder = builder.addNode("modify", EPersonCode.PERSON__MODIFY__EXPLANATION, () -> person != null).append();

		// FOURTH: CREATION OF THE FIRST CHILD "name" ------------------------------------------------------------------------------
		// Adding first node corresponding to "name" argument to the modify node
		// () -> person != null: this argument should be available if a new person has been created before.
		modifyBuilder.addNode("name", EPersonCode.PERSON__MODIFY__NAME__EXPLANATION, () -> person != null).withCompleter((sender, command, alias, args) -> {
			// Code to execute for a completion (expected argument).
			switch (args.length) {
			case 0:
				return Arrays.asList(builder.getMessage(sender, EPersonCode.PERSON__NEW_NAME__COMPLETE));
			default:
				return Arrays.asList();
			}
		}).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			String newName;
			try {
				newName = args[0];
			} catch (IndexOutOfBoundsException e) {
				builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__NEW_NAME_IS_MISSING).build());
				return false;
			}

			String oldName = person.getName();
			person.setName(newName);
			builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__RENAMED, oldName, person.getName()));
			return true;
		}).append();

		// FIFTH: CREATION OF THE SECOND CHILD "birthday" --------------------------------------------------------------------------
		// Adding second node corresponding to the "birthday" argument to the modify node
		// () -> person != null: this argument should be available if a new person has been created before.
		modifyBuilder.addNode("birthday", EPersonCode.PERSON__MODIFY_BIRTHDAY__EXPLANATION, () -> person != null).withCompleter((sender, command, alias, args) -> {
			// Code to execute for a completion (expected argument).
			switch (args.length) {
			case 0:
				return Arrays.asList(builder.getMessage(sender, EPersonCode.PERSON__NEW_BIRTHDAY__COMPLETE));
			default:
				return Arrays.asList();
			}
		}).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			LocalDate birthday;
			try {
				birthday = LocalDate.parse(args[0]);
			} catch (IndexOutOfBoundsException e) {
				builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_IS_MISSING).build());
				return false;
			} catch (DateTimeParseException e) {
				builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_BAD_FORMAT).build());
				return false;
			}

			LocalDate oldBirthday = person.getBirthday();
			person.setBirthday(birthday);
			builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__BIRTHDAY_UPDATED, person.getName(), oldBirthday, person.getBirthday()));
			return true;
		}).append();

		// SIXTH: CREATION OF THE THIRD CHILD "show" -------------------------------------------------------------------------------
		// Adding third node corresponding to the "show" argument
		// () -> person != null: this argument should be available if a new person has been created before.
		builder.addNode("show", EPersonCode.PERSON__SHOW__EXPLANATION, () -> person != null).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			builder.send(builder.eventBuilder(sender, EPersonCode.PERSON__SHOW, person.getName(), person.getBirthday()));
			return true;
		}).append();

		root = builder.build();
	}

	/**
	 * @return The root of this minecraft tree.
	 */
	public IMinecraftCodeNode getRoot() {
		return root;
	}

	/**
	 * Executes the given command, returning its success
	 * 
	 * @param args Passed command arguments
	 * @return true if a valid command, otherwise false
	 */
	public boolean dispatch(String... args) {
		// Defined here to null because manipulating the tree outside from minecraft.
		// This method is useless because minecraft calls directly root.onCommand() with the right arguments when player validates a
		// command line.
		return root.onCommand(null, null, null, args);
	}
}
```

As you can see, there is no hardcoded message. Each message to return or to send to a player are defined by a code from the <code>EPersonCode</code> enumeration and then according to the Locale associated to the player the message is translated. If there is no dictionary registered for a specific locale, then the message is translated in English and then returned.

#### 3.2) Inheriting MinecraftCodeNode

Let's first create our own node from which each custom node will inherits:

```java
public class PersonNode extends MinecraftCodeNode {
	private static Person person;

	/**
	 * Create a custom node defined by a label, which correspond to its name, and an explanation.
	 * 
	 * @param label       The name of the node.
	 * @param explanation The explanation of the node.
	 * @param isAvailable True if this node is available, false otherwise.
	 */
	protected PersonNode(String label, IMinecraftCode explanation, Function<Person, Boolean> isAvailable) {
		super(label, explanation, () -> isAvailable.apply(person));
	}

	/**
	 * @return The person manipulated by this node
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * The the person manipulated by this node.
	 * 
	 * @param person The new person manipulated by this node.
	 */
	protected void setPerson(Person person) {
		PersonNode.person = person;
	}
}
```

The person attribute is declared static in order to be shared by each future custom node.  
Let's then create the NamePersonNode and the BirthdayPersonNode classes in order to modify the name and the birthday of a person. They correspond to the last children generation of our command tree.

```java
public class NamePersonNode extends PersonNode {

	protected NamePersonNode() {
		super("name", EPersonCode.PERSON__MODIFY__NAME__EXPLANATION, person -> person != null);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// Code to execute for a completion (expected argument).
		switch (args.length) {
		case 0:
			return Arrays.asList(getMessage(sender, EPersonCode.PERSON__NEW_NAME__COMPLETE));
		default:
			return Arrays.asList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Code to execute when the user validates the argument line.
		String newName;
		try {
			newName = args[0];
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, EPersonCode.PERSON__NEW_NAME_IS_MISSING).build());
			return false;
		}

		String oldName = getPerson().getName();
		getPerson().setName(newName);
		send(eventBuilder(sender, EPersonCode.PERSON__RENAMED, oldName, getPerson().getName()));
		return true;
	}
}
```

```java
public class BirthdayPersonNode extends PersonNode {

	protected BirthdayPersonNode() {
		super("birthday", EPersonCode.PERSON__MODIFY_BIRTHDAY__EXPLANATION, person -> person != null);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// Code to execute for a completion (expected argument).
		switch (args.length) {
		case 0:
			return Arrays.asList(getMessage(sender, EPersonCode.PERSON__NEW_BIRTHDAY__COMPLETE));
		default:
			return Arrays.asList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Code to execute when the user validates the argument line.
		LocalDate birthday;
		try {
			birthday = LocalDate.parse(args[0]);
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_IS_MISSING).build());
			return false;
		} catch (DateTimeParseException e) {
			send(eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_BAD_FORMAT).build());
			return false;
		}

		LocalDate oldBirthday = getPerson().getBirthday();
		getPerson().setBirthday(birthday);
		send(eventBuilder(sender, EPersonCode.PERSON__BIRTHDAY_UPDATED, getPerson().getName(), oldBirthday, getPerson().getBirthday()));
		return true;
	}
}
```

We have to create those node first because they do not depend on any other node. Then we can create the first children generation, that correspond to the nodes NewPersonNode, ModifyPersonNode and ShowPersonNode :

``` java
public class NewPersonNode extends PersonNode {

	protected NewPersonNode() {
		super("new", EPersonCode.PERSON__NEW__EXPLANATION, person -> true);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		// Code to execute for a completion (expected argument).
		switch (args.length) {
		case 0:
			return Arrays.asList(getMessage(sender, EPersonCode.PERSON__NEW__NAME__COMPLETE));
		case 1:
			return Arrays.asList(getMessage(sender, EPersonCode.PERSON__NEW__BIRTHDAY_COMPLETE));
		default:
			return Arrays.asList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Code to execute when the user valids the argument line.
		String name;
		try {
			name = args[0];
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, EPersonCode.PERSON__NEW__NAME_IS_MISSING).build());
			return false;
		}

		LocalDate birthday;
		try {
			birthday = LocalDate.parse(args[1]);
		} catch (IndexOutOfBoundsException e) {
			send(eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_IS_MISSING).build());
			return false;
		} catch (DateTimeParseException e) {
			send(eventBuilder(sender, EPersonCode.PERSON__NEW__BIRTHDAY_BAD_FORMAT).build());
			return false;
		}

		setPerson(new Person(name, birthday));
		send(eventBuilder(sender, EPersonCode.PERSON__NEW__PERSON_CREATED, getPerson().getName(), getPerson().getBirthday()));
		return true;
	}
}
```

```java
public class ModifyPersonNode extends PersonNode {

	protected ModifyPersonNode() {
		super("modify", EPersonCode.PERSON__MODIFY__EXPLANATION, person -> person != null);

		add(new NamePersonNode());
		add(new BirthdayPersonNode());
	}
}
```

```java
public class ShowPersonNode extends PersonNode {

	protected ShowPersonNode() {
		super("show", EPersonCode.PERSON__SHOW__EXPLANATION, person -> person != null);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Code to execute when the user valids the argument line.
		send(eventBuilder(sender, EPersonCode.PERSON__SHOW, getPerson().getName(), getPerson().getBirthday()));
		return true;
	}
}
```

And finally, we can create the PersonCodeTree :

```java
public class PersonCustomCodeTree {
	private IMinecraftCodeNode root;

	public PersonCustomCodeTree() {
		root = new MinecraftCodeRootNode("person", EPersonCode.PERSON__ROOT__EXPLANATION, () -> true);
		root.add(new NewPersonNode());
		root.add(new ModifyPersonNode());
		root.add(new ShowPersonNode());
	}

	/**
	 * Dispatch the following arguments in the underlying command tree.
	 * 
	 * @param args The argument line to execute.
	 * 
	 * @return True if the command is valid, false otherwise.
	 */
	public void dispatch(String... args) {
		// Defined here to null because manipulating the tree outside from minecraft.
		// This method is useless because minecraft calls directly root.onCommand() with the right arguments when player validates a
		// command line from minecraft.
		root.onCommand(null, null, null, args);
	}
	
	/**
	 * @return The root of this minecraft tree.
	 */
	public IMinecraftCodeNode getRoot() {
		return root;
	}
}
```

### 4) Registering dictionaries

The fourth step consist in registering the dictionaries for the <code>MinecraftDictionaryContext</code>.

#### 4.1) Exportation

I consider, but that's my point of view, that dictionaries should not be available from outside. That's why, I prefer to export the dictionaries in the jar file. I will here explain how to export resource files with maven. In the pom.xml, in the tag <code>&lt;build&gt;</code> you can specify some resources folder and a target path:

```xml
<resources>
	<resource>
		<targetPath>Path/to/dictionaries/folder/in/the/jar</targetPath>
		<filtering>true</filtering>
		<directory>${basedir}/Path/to/the/dictionaries/folder/in/the/project</directory>
		<includes>
			<include>*.xml</include>
		</includes>
	</resource>
</resources>
```

When running the command <code>mvn clean package install</code> it will integrates the dictionaries at the specified target path.

#### 4.2) Registration

To register a dictionary in the minecraft dictionary context, please have a look to this [tutorial](https://github.com/Pierre-Emmanuel41/minecraft-dictionary/blob/3.0_MC_1.16.5-SNAPSHOT/Tutorial.md). Finally, to link your tree with the minecraft person command described in your plugin.yml file, just do the following in the class that extends the <code>JavaPlugin</code> class:

```java
@Override
public void onEnable() {
	try {
		JarXmlDictionaryParser dictionaryParser = new JarXmlDictionaryParser(Paths.get("Path/to/the/jar/file"));

		MinecraftDictionaryContext context = MinecraftDictionaryContext.instance();
		context.register(dictionaryParser.parse(Paths.get("Path/to/the/dictionaries/folder/in/the/jar/English.xml")));
		context.register(dictionaryParser.parse(Paths.get("Path/to/the/dictionaries/folder/in/the/jar/French.xml")));

		PersonCustomCodeTree tree = new PersonCustomCodeTree();
		PluginCommand personCommand = getCommand("person");
		personCommand.setExecutor(tree.getRoot());
		personCommand.setTabCompleter(tree.getRoot());
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}
```