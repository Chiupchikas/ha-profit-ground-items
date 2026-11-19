# HA Profit Ground Items (RuneLite)

Color the entire **(HA: ... gp)** segment green when high alching an item is profitable, without hiding or replacing its **GE price**.

Profit per item = `High Alch value - GE price - Nature rune GE price`. This reflects the opportunity cost of alching instead of selling the item.

## Configuration

1. Enable RuneLite's **Ground Items**.
2. Set **Ground Items > Price Display Mode** to **Both** (GE + HA).
3. Enable **HA Profit Ground Items**.
4. Optionally change **Profitable HA color** and **Minimum profit per item**.

The plugin draws only the colored HA segment over the built-in label; it does not create a second GE/item-name label. It mirrors Ground Items positioning, but exact alignment can differ for hidden or specially filtered drops and while editing drop labels with `Alt`.

## Development run

Requires JDK 11 or newer. In the plugin directory:

```sh
./gradlew test
./gradlew run
```

On Windows, use `gradlew.bat test` and `gradlew.bat run` in PowerShell or Command Prompt.

`run` launches a development RuneLite client, not an installation into an already running one. For a Jagex Account, follow [RuneLite's Jagex Accounts instructions](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts).

This project is standalone source code; adding it to the public Plugin Hub requires a separate review and publication.

## In-game checks

- Profitable item: HA number green; item name and GE price unchanged.
- Unprofitable item: HA price retains its original color.
- Two item types on one tile; different stack quantities; noted items.
- Hidden items and `Alt` item editing: report any misalignment.

The plugin does not perform game input or cast spells. In-game rendering must be verified by the player.
