# HA Profit Ground Items (RuneLite)

Color the entire **(HA: ... gp)** segment green when high alching an item is profitable, without hiding or replacing its **GE price**.

Profit per item = `High Alch value - GE price - Nature rune GE price`. This reflects the opportunity cost of alching instead of selling the item.

## Configuration

1. Enable RuneLite's **Ground Items**.
2. Set **Ground Items > Price Display Mode** to **Both** (GE + HA).
3. Enable **HA Profit Ground Items**.
4. Open the plugin settings. **Nature rune GE price (auto)** shows the current rune price used in the calculation. The value is refreshed on game ticks; reopen settings to refresh the visible field if the price changes while the panel is open. The field is informational; manual edits are overwritten.
5. Set **Color HA from profit (gp)** to choose the minimum profit **per item** that turns the HA segment green. The default is `1 gp`.
6. Optionally change **Profitable HA color**.

For example, an item with `HA = 1,200 gp`, `GE = 900 gp` and a Nature rune costing `200 gp` has `100 gp` profit. A threshold of `100 gp` highlights it, but `101 gp` does not.

The plugin draws only the colored HA segment over the built-in label; it does not create a second GE/item-name label. It mirrors Ground Items positioning, but exact alignment can differ for hidden or specially filtered drops and while editing drop labels with `Alt`.

## Development run

Requires JDK 11 or newer. In the plugin directory:

```sh
./gradlew test
./gradlew run
```

On Windows PowerShell, use `.\gradlew.bat test` and `.\gradlew.bat run`.

`run` launches a development RuneLite client, not an installation into an already running one. For a Jagex Account, follow [RuneLite's Jagex Accounts instructions](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts).

The source code is published in the RuneLite Plugin Hub ecosystem. Updates require separate Plugin Hub review.

## In-game checks

- Profitable item: HA number green; item name and GE price unchanged.
- Unprofitable item: HA price retains its original color.
- Two item types on one tile; different stack quantities; noted items.
- Hidden items and `Alt` item editing: report any misalignment.

The plugin does not perform game input or cast spells. In-game rendering must be verified by the player.
