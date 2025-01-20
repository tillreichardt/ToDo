
  

# ToDo Project

  

This is a Java console program that allows you to create, display, delete, and update users, ToDos, and categories. Below you will find instructions on how to install and run the project.

**This Project will only run on Windows!**

  

## Installation and Execution

  

1.  **Download Project**

	Download the project repository from GitHub.

  

2.  **Run**

	Ensure that the folders in the storage path do not contain spaces.

-  **Windows Command Line Prompt (cmd)**

	1. Change to the project directory. For instance:

		```	
		C:\Users\user>cd /d E:\ToDo-main\ToDo-main
		```

	2. Execute the `.bat` file with the following command `todo`:

		```
		E:\ToDo-main\ToDo-main>todo
		```

-  **PowerShell**

	1. Also change to the project directory. For instance:

		```powershell
		PS C:\Users\user> cd E:\ToDo-main\ToDo-main\
		```

	2. Execute the `.bat` file with the following command `.\todo.bat`:

		```powershell
		PS E:\ToDo-main\ToDo-main> .\todo.bat
		```

  

## Usage

  

To use the program, call the `todo` command in the **Windows Command Line Prompt** and `.\todo.bat` in **PowerShell**, and pass the appropriate **Command** and **Type** with the corresponding **Options**.

  

The following assumes that **Windows Command Line Prompt** is being used.

  

```bash
Usage:  todo [COMMAND] [TYPE] [OPTIONS]

Type  of  items  available  for  all  commands: [user, todo,  category]

Commands:

show  		Show  an  entity  based  on  type
create  	Create  a  new  entity  based  on  type
delete  	Delete  an  entity  based  on  type
update  	Update  an  existing  entity  based  on  type
share 		Share a Todo with another user
login  		Log  in  to  your  account
logout 		Log out of your account
help  		Show  this  help  message 
```

### ShowCommand

Display a list of entries or a single entry, possibly sorted.

-  **Options:**
	
	-  `--sort-by <String>` : Possible values: `[priority, date]`

	-  `--order <String>` : Possible values: `[asc, desc]`
**Example:**

```bash
todo show todo --sort-by priority --order asc
```
Displays all ToDos, sorted by `priority` in `asc` order.
### CreateCommand
Create a new entity of type `user`, `todo`, or `category`.
-  **Options:**

	-  **user**:

		-  `--username <String>` : Username (max. 32 characters)

		-  `--password <String>` : Password

	-  **todo**:

		-  `--title <String>` : Title (max. 128 characters)

		-  `--description <String>` : Detailed description

		-  `--priority <Integer>` : Priority: `[0 = high, 1 = low]`

		-  `--category <String>` : Associated category

	-  **category**:

		-  `--title <String>` : Title (max. 32 characters)

**Example:**

```bash
todo create user --username Max --password secret
```
Creates a new user named _Max_ with the password _secret_.

or:
```bash
todo create todo --title newTitle
```
Creates a new todo with the Title _newTitle_.
  
### DeleteCommand

Deletes an entity (user, ToDo, or category) by its ID.

-  **Options:**
	
	-  `--id <Integer>` : ID of the item to be deleted

**Example:**

```bash
todo delete todo --id 42
```

Deletes the ToDo item with ID `42`.

### UpdateCommand
Updates an existing entity (user, ToDo, or category).

-  **Options:**

	-  `--id <Integer>` : ID of the item to be updated

	-  **user**:

		-  `--username <String>` : New username (max. 32 characters)

		-  `--password <String>` : New password

	-  **todo**:

		-  `--title <String>` : New title

		-  `--description <String>` : Updated description

		-  `--priority <Integer>` : New priority: `[0 = high, 1 = low]`

		-  `--category <String>` : New category

	-  **category**:

		-  `--title <String>` : New title

**Example:**
 
```bash
todo update todo --id 42 --title newTitle --priority 0
```
Updates the ToDo item with ID `42`, setting the title to _newTitle_ and the priority to _high_ (0).

### ShareCommand

Shares a ToDo with another user by its ID and the users publicID.

-  **Options:**
	
	 - `--todoid <Integer>`:       ID of todo you want to share

	 - `--publicid <String>`:       ID of user you want to share a ToDo with

**Example:**

```bash
todo  share todo  --todoid  42 --publicid 1a2b3c4
```
Shares the ToDo with ID `42` to the User whose publicID is `1a2b3c4`.


### Login
Log in with a username and password.

```bash
todo login --username Max --password secret
```

### Logout

If you no longer wish to use the program, log out of your account.
```bash
todo logout
```
### Show Help

Displays a help overview of all available commands.

```bash
todo help
```

  

---

  

Have fun using the ToDo Project!
