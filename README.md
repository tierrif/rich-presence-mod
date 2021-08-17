# Rich Presence Mod for Minecraft (Fabric)
Show extra details about your Minecraft gameplay using Discord's
rich presence system.

## Why use this mod?
Minecraft still has no rich presence system implemented. Due to that,
this mod adds support for it for those who want to use this feature in
it.

This mod is highly configurable, with editable rich presence lines for 
both singleplayer and multiplayer.

## How do I use it?
- Download the latest release matching your current Minecraft version.
If your client version is `1.16`, for example, do not download the newest
`1.17` version; instead, get the latest `1.16` version you find in the 
[Releases](https://github.com/HotLava03/rich-presence-mod/releases)
section of this page.

- Download [Fabric Loader](https://fabricmc.net/use/) and [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) for your Minecraft version if you didn't yet.
- Download both [MaLiLib](https://www.curseforge.com/minecraft/mc-mods/malilib)
  and [ModMenu](https://www.curseforge.com/minecraft/mc-mods/modmenu) mods and place them in your `mods`
  folder, if you haven't yet.
- Don't forget to place the downloaded Rich Presence Mod `jar` file in the `mods` folder too.

## Config
- `clientId`: This shouldn't be changed at all unless you create your own
rich presence application in Discord's Dev portal.
- `largeImage`: In a future release, this setting will be useful. Changes
the large image key set in the Dev portal application.
- `smallImage`: Same as above, except it's for the small image.
- `largeImageText`: Tooltip for the large image.
- `smallImageText`: Tooltip for the small image.
- `stateIdle`: The text that appears when the player is idle (state).
- `stateInServer`: Shows the player count or nothing if the player count 
  doesn't load (known bug, read below).
- `detailsIdle`: The text that appears when the player is idle (details).
- `detailsInServer`: Shows the current server address or `singleplayer`
  when playing in singleplayer.
- `detailsInSingleplayer`: Text that appears when the user is on singleplayer.
  Usually empty, but any text can be set.
  
## Known bugs
The player count doesn't load, as mentioned above, exactly when the player
count didn't load correctly in the multiplayer screen for the specific server
the player is on. To avoid this, allow servers to load before clicking on them.
In a future release this will be fixed.

Any other issues should be reported in the [Issues](https://github.com/HotLava03/rich-presence-mod/issues)
section. Thank you for using Rich Presence Mod.

## License
This mod is licensed under the GNU General Public License v3. For more information,
read the LICENSE file in the root of this project.
