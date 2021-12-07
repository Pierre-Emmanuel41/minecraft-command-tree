# Tutorial

A minecraft command tree is a specific representation for minecraft of a more generic tree as defined in the project [command-tree](https://github.com/Pierre-Emmanuel41/command-tree.git), represented by a root and a list of children. Each children is a node which means that children can themselves have children.  
There are two ways to create your own command tree :  
* With the command <code>MinecraftTreeBuilder</code>
* Inheriting the <code>MinecraftNode</code>

# TreeBuilder

The tree builder can be found is the <code>MinecraftTree</code> class. A tree is generic in order to let the developer to choose the type of the explanation. Because there are several constructors to create a minecraft root node, there are different possibility to create children.  

Example : Let's say you want to create a minecraft command tree that accept the following argument:  
person  
&ensp;new  
&ensp;modify  
&ensp;&ensp;name  
&ensp;&ensp;birthday  
&ensp;show  

The "person" argument correspond to the root of the tree. "new", "modify" and "show" are the first children generation and are attached to the root ("person"). Whereas "name" and "birthday" are the second children generation and are attached to the "modify" child.

Let's first create our own Person class :

```java
public static class Person {
	private String name;
	private LocalDate birthday;

	public Person() {
	}

	public Person(String name, LocalDate birthday) {
		this.name = name;
		this.birthday = birthday;
	}

	/**
	 * @return The person name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the person name.
	 * 
	 * @param name The person name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The person birthday.
	 */
	public LocalDate getBirthday() {
		return birthday;
	}

	/**
	 * Set the person birthday.
	 * 
	 * @param birthday The person birthday.
	 */
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
}
```

Then, let's create a class that gather the possible arguments for the command. Let's call it PersonMinecraftTree :

```java
public class PersonMinecraftTree {
	private IMinecraftNode<String> root;
	private Person person;

	public PersonMinecraftTree() {
		// FIRST: CREATION OF THE ROOT NODE "person" -------------------------------------------------------------------------------
		MinecraftTreeBuilder<String> builder = MinecraftTree.create("person", "Command to create/modify the property of a person", () -> true);

		// SECOND: CREATION OF THE FIRST CHILD "new" -------------------------------------------------------------------------------
		// Adding first node corresponding to the "new" argument
		// ()-> true: because this command should always be available.
		builder.addNode("new", "To create a new person", () -> true).withCompleter((sender, command, alias, args) -> {
			// Code to execute for a completion (expected argument).
			switch (args.length) {
			case 0:
				return Arrays.asList("<name>");
			case 1:
				return Arrays.asList("<birthday>");
			default:
				return Arrays.asList();
			}
		}).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			String name;
			try {
				name = args[0];
			} catch (IndexOutOfBoundsException e) {
				System.out.println("The name is missing");
				return false;
			}

			String birthday;
			try {
				birthday = args[1];
			} catch (IndexOutOfBoundsException e) {
				System.out.println("The birthday is missing");
				return false;
			}

			person = new Person(name, LocalDate.parse(birthday));
			System.out.println(String.format("New person created : name=%s, birthday=%s", person.getName(), person.getBirthday()));
			return true;
		}).append();

		// THIRD: CREATION OF THE SECOND CHILD "modify" ----------------------------------------------------------------------------
		// Adding second node corresponding to the "modify" argument
		// () -> person != null: this argument should be available if a new person has been created before.
		MinecraftNodeBuilder<String> modifyBuilder = builder.addNode("modify", "To modify the property of a person", () -> person != null).append();

		// FOURTH: CREATION OF THE FIRST CHILD "name" ------------------------------------------------------------------------------
		// Adding first node corresponding to "name" argument to the modify node
		// () -> person != null: this argument should be available if a new person has been created before.
		modifyBuilder.addNode("name", "To change the name of the person", () -> person != null).withCompleter((sender, command, alias, args) -> {
			// Code to execute for a completion (expected argument).
			switch (args.length) {
			case 0:
				return Arrays.asList("<newName>");
			default:
				return Arrays.asList();
			}
		}).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			String newName;
			try {
				newName = args[0];
			} catch (IndexOutOfBoundsException e) {
				System.out.println("The new name is missing");
				return false;
			}

			String oldName = person.getName();
			person.setName(newName);
			System.out.println(String.format("Renaming %s as %s", oldName, person.getName()));
			return true;
		}).append();

		// FIFTH: CREATION OF THE SECOND CHILD "birthday" --------------------------------------------------------------------------
		// Adding second node corresponding to the "birthday" argument to the modify node
		// () -> person != null: this argument should be available if a new person has been created before.
		modifyBuilder.addNode("birthday", "To change the birthday of the person", () -> person != null).withCompleter((sender, command, alias, args) -> {
			// Code to execute for a completion (expected argument).
			switch (args.length) {
			case 0:
				return Arrays.asList("<birthday>");
			default:
				return Arrays.asList();
			}
		}).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			String birthday;
			try {
				birthday = args[0];
			} catch (IndexOutOfBoundsException e) {
				System.out.println("The birthday is missing");
				return false;
			}

			LocalDate oldBirthday = person.getBirthday();
			person.setBirthday(LocalDate.parse(birthday));
			System.out.println(String.format("Changing the birthday of %s (%s -> %s)", person.getName(), oldBirthday, person.getBirthday()));
			return true;
		}).append();

		// SIXTH: CREATION OF THE THIRD CHILD "show" -------------------------------------------------------------------------------
		// Adding third node corresponding to the "show" argument
		// () -> person != null: this argument should be available if a new person has been created before.
		builder.addNode("show", "To show the properties of the person", () -> person != null).withExecutor((sender, command, alias, args) -> {
			// Code to execute when the user valids the argument line.
			System.out.println(String.format("Person properties : name=%s, birthday=%s", person.getName(), person.getBirthday()));
			return true;
		}).append();

		root = builder.build();
	}

	/**
	 * @return The root of this minecraft tree.
	 */
	public IMinecraftNode<String> getRoot() {
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

This class has constructed a minecraft command tree in 6 steps to manipulate a person. Now that the tree is constructed, we can register its root as a completer and as an executor for the "person" command in your own plugin that should extends the <code>JavaPlugin</code> class:

```java
@Override
public void onEnable() {
	PersonMinecraftTree tree = new PersonMinecraftTree();

	// Registering the tree as completer and as executor for the "person" command.
	// The "person" command should be described in the plugin.yml file in order to be found by the "getCommand()" method.
	getCommand(tree.getRoot().getLabel()).setTabCompleter(tree.getRoot());
	getCommand(tree.getRoot().getLabel()).setExecutor(tree.getRoot());

	// FOR DEMONSTRATION ONLY but should not be used outside from minecraft.
	tree.dispatch("help");
	System.out.println();

	tree.dispatch("new", "Obiwan", "2007-12-03");
	tree.dispatch("modify", "name", "Anakin");
	tree.dispatch("modify", "birthday", "2007-12-04");
	tree.dispatch("show");
	System.out.println();

	tree.dispatch("help");
	tree.dispatch("help", "modify", "name");
	tree.dispatch("help", "modify", "birthday");
}
```

In the same way as for a generic command tree, there is an external node associated only to the root of a minecraft command tree and display the explanation of one or several children. Here is the output of the <code>onEnable</code> method:

```java
// Only the explanation of the "new" argument because only this one is available.
new - To create a new person

New person created : name=Obiwan, birthday=2007-12-03
Renaming Obiwan as Anakin
Changing the birthday of Anakin (2007-12-03 -> 2007-12-04)
Person properties : name=Anakin, birthday=2007-12-04

// All arguments are available
new - To create a new person
modify - To modify the property of a person
show - To show the properties of the person

name - To change the name of the person
birthday - To change the birthday of the person
```

# Inheriting the MinecraftCommandNode

Let's first create our own node from which each custom node will inherits:

```java
public class PersonNode extends MinecraftNode<String> {
	private static Person person;

	protected PersonNode(String label, String explanation, Supplier<Boolean> isAvailable) {
		super(label, explanation, isAvailable);
	}

	protected PersonNode(String label, String explanation) {
		super(label, explanation);
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
		super("name", "To change the name of the person");
		setAvailable(() -> getPerson() != null);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 0:
			return Arrays.asList("<newName>");
		default:
			return Arrays.asList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String newName;
		try {
			newName = args[0];
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The new name is missing");
			return false;
		}

		String oldName = getPerson().getName();
		getPerson().setName(newName);
		System.out.println(String.format("Renaming %s as %s", oldName, getPerson().getName()));
		return true;
	}
}
```

```java
public class BirthdayPersonNode extends PersonNode {

	protected BirthdayPersonNode() {
		super("birthday", "To change the birthday of the getPerson()");
		setAvailable(() -> getPerson() != null);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 0:
			return Arrays.asList("<birthday>");
		default:
			return Arrays.asList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String birthday;
		try {
			birthday = args[0];
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The birthday is missing");
			return false;
		}

		LocalDate oldBirthday = getPerson().getBirthday();
		getPerson().setBirthday(LocalDate.parse(birthday));
		System.out.println(String.format("Changing the birthday of %s (%s -> %s)", getPerson().getName(), oldBirthday, getPerson().getBirthday()));
		return true;
	}
}
```

We have to create those node first because they do not depend on any other node. Then we can create the first children generation, that correspond to the nodes NewPersonNode, ModifyPersonNode and ShowPersonNode :

``` java
public class NewPersonNode extends PersonNode {

	protected NewPersonNode() {
		super("new", "To create a new person", () -> true);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		switch (args.length) {
		case 0:
			return Arrays.asList("<name>");
		case 1:
			return Arrays.asList("<birthday>");
		default:
			return Arrays.asList();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String name;
		try {
			name = args[0];
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The name is missing");
			return false;
		}

		String birthday;
		try {
			birthday = args[1];
		} catch (IndexOutOfBoundsException e) {
			System.out.println("The birthday is missing");
			return false;
		}

		setPerson(new Person(name, LocalDate.parse(birthday)));
		System.out.println(String.format("New person created : name=%s, birthday=%s", getPerson().getName(), getPerson().getBirthday()));
		return true;
	}
}
```

```java
public class ModifyPersonNode extends PersonNode {

	protected ModifyPersonNode() {
		super("modify", "To modify the property of a person");
		setAvailable(() -> getPerson() != null);

		add(new NamePersonNode());
		add(new BirthdayPersonNode());
	}
}
```

```java
public class ShowPersonNode extends PersonNode {

	protected ShowPersonNode() {
		super("show", "To show the properties of the getPerson()");
		setAvailable(() -> getPerson() != null);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		System.out.println(String.format("Person properties : name=%s, birthday=%s", getPerson().getName(), getPerson().getBirthday()));
		return true;
	}
}
```

And finally, we can create the PersonCommandTree :

```java
public class PersonCustomMinecraftTree {
	private IMinecraftNode<String> root;

	public PersonCustomMinecraftTree() {
		root = new MinecraftRootNode<String>("person", "Command to create/modify the property of a person", () -> true);
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
}
```

Warning : It is VERY important to note that there is an internal cast of IMinecraftNode in the class <code>MinecraftNode</code> in order to be sure that only MinecraftNode are added to another one. Also, only a minecraft node can be set as the parent of another minecraft node.