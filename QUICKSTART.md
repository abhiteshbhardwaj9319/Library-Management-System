# Quick Start Guide

Just want to run this thing? Here's how:

## Compile & Run

```bash
# Compile everything
javac -d bin src/main/java/**/*.java src/main/java/Main.java

# Run it
java -cp bin Main
```

## Windows PowerShell

```powershell
# Get all java files and compile
$files = Get-ChildItem -Recurse -File -Path "src/main/java" -Filter *.java | ForEach-Object { $_.FullName }
javac -d bin $files

# Run
java -cp bin Main
```

## What you'll see

The demo will show:
- Creating library branches
- Adding books
- Registering patrons
- Checking out books
- Reservations and notifications
- Book recommendations

That's it! No database setup needed - everything runs in memory.

## Troubleshooting

- Make sure you have Java installed: `java -version`
- If compilation fails, check you're in the project root directory
- The demo runs automatically - just wait for it to finish

## Notes

- This is a demo project for learning OOP
- No persistence - restart to reset everything
- Some features might have bugs (still learning!)
