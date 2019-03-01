# Discord Game Bot
This bot is designed for game servers on the engine Source.

# Functions
1. Bot can create voice channel with players count in title.
2. Bot can create text channel and send there message with players name that are currently on server. He didn't spam, he send only one message and then edit it.
3. Bot can create text channel and listen there rankme commands. Player can write !rankme *steamid* and bot will type his statistic.


# Request
1. Bot written on Java, so you need to download it. [Download java you can here.](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

# Installing
1. Open file `config.properties` and type all info there by instruction.
2. Save file and close.
3. Launch `start.bat` if you using windows and `start.sh` if you using linux.
4. Enjoy your bot :)

# Supporting commands
* `exit`/`close`/`quit` - that command closes the program. I recommend to use it because before closing this command delete all channels that was created by bot before closing.
* `pause` - pausing all functions.
    * `pause count *id*` - pausing function count under id.
    * `pause message *id*` - pausing function message under id.
    * `pause rankme` - pausing functions rankme.
* `resume` - resuming all functions. Could be using with options (same as `pause`).

# TODO
1. Add ranking system.
