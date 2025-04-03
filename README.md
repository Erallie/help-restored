# HelpRestored

[![Latest Release](https://img.shields.io/github/release-date/Erallie/help-restored?display_date=published_at&style=for-the-badge&label=Latest%20Release)](https://modrinth.com/plugin/help-restored/version/latest)
[![GitHub Downloads](https://img.shields.io/github/downloads/Erallie/help-restored/total?style=for-the-badge&logo=github&logoColor=ffffff&label=GitHub%20Downloads&color=hsl(0%2C%200%25%2C%2020%25))](https://github.com/Erallie/help-restored)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/zE5EbgF4?style=for-the-badge&logo=modrinth&logoColor=00af5c&label=Modrinth%20Downloads&color=00af5c)](https://modrinth.com/plugin/help-restored)
<br>
[![Our Discord](https://img.shields.io/discord/1102582171207741480?style=for-the-badge&logo=discord&logoColor=ffffff&label=Our%20Discord&color=5865F2)](https://discord.gg/cCCEk7BX4W)
[![Our Other Projects](https://img.shields.io/badge/Our%20Other%20Projects-%E2%9D%A4-563294?style=for-the-badge&logo=data%3Aimage%2Fwebp%3Bbase64%2CUklGRu4DAABXRUJQVlA4WAoAAAAQAAAAHwAAHwAAQUxQSGABAAABgFtbm5volyZTA%2BtibzK2H0w5sDkmhe3GmxrwxGg0839r%2FvkkOogIBW7bKB0c4%2BARYihzIqfd6dfO%2B%2B3XtHsq4jJhlIvcDRcgNB%2FeieQETorBHgghRtUYqwDs%2B4U4IpcvUB%2BVUPSK54uEnTwsUJoar2DeMpzLxQpeG5DH8lxyyfLivVYAwPBbkWdOBg3qFlqiLy679iHy9UDKMZRXmYxpCcusayTHG01K%2FEtatYWuj7oI9hL4BxsxVwhoP2mlAJJ%2BuuAflc6%2BEUCQTCX9EV87xBR2H75NxLZSpWiwzqdIm7ZO7uB3oEgZKbD9Nt3EmHweEPH1t1GNsZUbKeisiwjyTm5fA3SO1yCrADZXrV2PZQJPL1tjN4%2BxUL9ie1mJobzOnDwSx6ILiF%2FW%2BTUR4tcHx0UaV75JXC1a4g6Ky5dLcTSuy9q4HhTieF64Hy1A3GHB8gLLK2e92feuqnbfPK8IVlA4IGgCAACwDQCdASogACAAPk0cjEQioaEb%2BqwAKATEtgBOl7v9V3sHcA2wG4A3gD0APLP9jX9n%2F2jmqv5AZRh7J%2BN2fOx22iE%2F4TUsecFmY%2BSf1r%2BAP%2BTfzT%2FXdIB7KX7MtdIGr1A8H0jmrrfZvqButwOaYcLWYNRq5QgAAP7%2F%2FmIMpiVNn67QXpM1rrDmRS8Nr%2F6dhD%2Bq5e%2BM%2BAtUP1%2FxOj85Ol5y3ebjz%2BpHoOf%2FWW8a%2F2ojUaKVDkVqof%2Bv4f0f6ud8i58wusz%2Fyrj%2F%2BwnM3q0769dvK%2F%2BQe04xL49tkb9t6ylCqqezZtZGuGLJ%2F5iUrPqdYc%2F8VbYZfP%2FOpZP%2F4X4q%2BqS4gPOxzdINOe5PGv%2F0TS%2FJRf4LlFrFkrWtxlS8n40grV%2BKUu%2FiwzdQzImvwH81FxL1bZyTSsrYwMku1Pk9StTtWNjSR8ZWEYBH9eTn%2FvBERii5XaWOPJ%2FFVXtVQGbv%2BFRW5jbo9tfFDu%2BDHHf8LbgUd%2F8W8Id1AehBtRNsLQWbADmvF1QJU8x5tw%2FtTUwIoSaa%2F2jkcvyVHkAsb2qoIh1KF1pPdae%2BZaqjydy6nUa9agjrDk1G4pMhEUhH%2BV%2FIUe49MjhR%2FuxyFmwQ8dDogMyQ%2BdcSBa56Lwt1wyJ%2F22%2F5O98r6q6wiM63HyaYONd36W7br%2F0%2F6y2DZ3irAddj%2FRxntvr%2FbbChSYXAfEbO%2FD0G%2FFbMFqTHypodt9T6dAx%2BUjJYfHzFf%2FM3Ec%2FAtwbjc2gka6urN1MlSLb2VTS9Q5r8fkDzxZz6vu1OYUPUB1UFMIhYGvMATbxxoTmVhvpovzAc%2F8nbOjw3wAAA)](https://github.com/Erallie)
[![Donate](https://img.shields.io/badge/Donate-%24-563294?style=for-the-badge&logo=paypal&color=rgb(0%2C%2048%2C%20135))](https://www.paypal.com/donate/?hosted_button_id=PHHGM83BQZ8MA)

---

HelpRestored is a plugin for Minecraft Java Edition that restores compatility with Bukkit's `help.yml` to Purpur, since it appears to be broken for me otherwise.

This plugin reads Bukkit's `help.yml` from your server root directory. Make sure the file exists, or this plugin won't work!

If you want to know how to set up your `help.yml`, see [this page](https://bukkit.fandom.com/wiki/Help.yml) from Bukkit's wiki.

## Tab Completer
HelpRestored offers tab completion when using the `/help` command.
- When no topic is specified, it suggests all the `index-topics` defined in your `help.yml`
- When you start typing, it also suggests commands that match.

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
