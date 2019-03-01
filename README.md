# Discord Game Bot
This bot is designed for game servers on the engine Source. He could create voice channels with title - count of players on your servers, create text chanel where print all players on your server (he didn't spam, he send only one message and then edit it), create text chanel and support there command: !rankme *steamid*. After receiving that message bot will send information about player from database.

# Request
1. Bot written on Java, so you need to download it. [Download java you can here.](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

# Installing
1. Open file `config.properties` and type all info there by instruction.
2. Save file and close.
3. Launch `start.bat` if you using windows and `start.sh` if you using linux.
4. Enjoy your bot :)

#Supporting commands
* `exit`/`close`/`quit` - that command closes the program. I recommend to use it because before closing this command delete all channels that was created by bot before closing.
* `pause` - pausing all functions.
    * `pause count *id*` - pausing function count under id.
    * `pause message *id*` - pausing function message under id.
    * `pause rankme` - pausing functions rankme.
* `resume` - resuming all functions. Could be using with options (same as `pause`).

# TODO
1. Add ranking system.
