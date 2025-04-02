# HelpRestored
[Our Discord](https://discord.gg/cCCEk7BX4W) | [Our Other Projects](https://github.com/Erallie) | [Modrinth](https://modrinth.com/plugin/help-restored) | [GitHub](https://github.com/Erallie/help-restored)

HelpRestored is a plugin for Minecraft Java Edition that restores compatility with Bukkit's `help.yml` to Purpur, since it appears to be broken for me otherwise.

This plugin reads Bukkit's `help.yml` from your server root directory. Make sure the file exists, or this plugin won't work!

If you want to know how to set up your `help.yml`, see [this page](https://bukkit.fandom.com/wiki/Help.yml) from Bukkit's wiki.

# Default Config.yml
```yml
# DO NOT CHANGE THIS
internal:
  plugin-version: "X.X.X"
# DO NOT CHANGE THIS

updater:
  # Whether to notify admins on new releases when they log in.
  notify-on-login: true
  # Whether to notify admins for new snapshot releases. Admins will still be notified on stable releases if this is set to "false".
  notify-on-dev-release: false
```

# Commands
- `/help` - Shows the help menu
  - **Usage:** `/help <topic> [page]`
  - **Permission:** `helprestored.use`
- `/helpreload` - Safely reloads `help.yml`
  - **Usage:** `/helpreload`
  - **Permission:** `helprestored.admin`

# Permissions
- `helprestored.use`
  - **Description:** Allows use of `/help`
  - **Default:** `true`
- `helprestored.admin`
  - **Description:** Allows use of `/helpreload`
  - **Default:** `op`

# Support
If you have any issues, or want to request a feature, please [create an issue](https://github.com/Erallie/help-restored/issues), and I will try my best to help!