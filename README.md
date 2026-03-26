# Task Tracker CLI

A command-line application to track and manage your tasks, built in Java.
Tasks are stored locally in a `tasks.json` file in the current directory.

## Features

- Add, update, and delete tasks
- Mark tasks as in-progress or done
- List all tasks or filter by status
- Persistent storage via a local JSON file

## Requirements

- Java 11 or higher

## Getting Started

Clone the repository and compile the source files:
```bash
git clone https://github.com/subomi234/Task-Tracker-Cli.git
cd task-tracker-cli
javac *.java
```

## Usage

Run the program using:
```bash
java TaskCli <command> [arguments]
```

### Commands

**Add a task**
```bash
java TaskCli add "Buy groceries"
# Output: Task added successfully (ID: 1)
```

**Update a task**
```bash
java TaskCli update 1 "Buy groceries and cook dinner"
```

**Delete a task**
```bash
java TaskCli delete 1
```

**Mark a task as in progress**
```bash
java TaskCli mark-in-progress 1
```

**Mark a task as done**
```bash
java TaskCli mark-done 1
```

**List tasks**
```bash
java TaskCli list              # all tasks
java TaskCli list todo         # not started
java TaskCli list in-progress  # currently in progress
java TaskCli list done         # completed
```

## Storage

Tasks are saved to `tasks.json` in the directory where you run the program.
The file is created automatically on first use. Each task contains:

| Field       | Description                        |
|-------------|------------------------------------|
| id          | Unique integer identifier          |
| description | Text description of the task       |
| status      | `todo`, `in-progress`, or `done`   |
| createdAt   | Timestamp when task was created    |
| updatedAt   | Timestamp when task was last edited|
