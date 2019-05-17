# Citi-Loader
A WIP mod loader for the abandoned Steam game Towns.


## Need a feature / Want to help?
Make an issue on the GitHub issues page.


## Info
Built with Towns, version 14e<br/>
Built with Java 8<br/>
Tested and debugged on Linux


## How to build
Build project with command:<br/>

Windows:<br/>
> gradlew.bat build jar<br/>

Linux:<br/>
> ./gradlew.sh build jar


## How to install/use
Add command line argument
>-javaagent:Citi-1.0.0.jar

when running the game.

Example:<br/>
> java -Djava.library.path=lib/native -javaagent:Citi-1.0.0.jar -jar ./lib/xaos.jar


## Goals (highest to lowest priority):
- [ ] Deobfuscate names during runtime
- [ ] Create boilerplate APIs for mods
- [ ] Support ASM transformers
