# HelpRestored
HelpRestored is a plugin for Minecraft Java Edition that restores compatility with Bukkit's `help.yml` to Purpur, since it appears to be broken for me otherwise.

No config! Just keep Bukkit's `help.yml` in your server root, and HelpRestored will restore its functionality to Purpur!

If you want to know how to set up your `help.yml`, see [this page](https://bukkit.fandom.com/wiki/Help.yml) from Bukkit's wiki.

## Commands
- `/help` - Shows the help menu
  - **Usage:** `/help <topic> [page]`
  - **Permission:** `helprestored.use`
- `/helpreload` - Safely reloads `help.yml`
  - **Usage:** `/helpreload`
  - **Permission:** `helprestored.admin`

## Permissions

- `helprestored.use`
  - **Description:** Allows use of `/help`
  - **Default:** `true`
- `helprestored.admin`
  - **Description:** Allows use of `/helpreload`
  - **Default:** `op`