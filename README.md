# HA Profit Ground Items (RuneLite)

Color the entire **(HA: ... gp)** segment green when the item's High Alchemy value is equal to or greater than its GE price, without hiding or replacing its **GE price**.

Difference per item = `High Alch value - GE price`. A difference of **0 gp or more** turns the HA segment green by default. The Nature rune cost is **not** deducted automatically.

| GE value | High Alch value | HA label at the default `0 gp` threshold |
| --- | --- | --- |
| 1,000 gp | 1,000 gp | Green |
| 1,000 gp | 1,200 gp | Green |
| 1,000 gp | 900 gp | Original color |

This is intended to identify ground items that can be alched instead of taken to the Grand Exchange. It compares item values, **not net alching profit**: the actual return after buying a Nature rune can be lower. Only the `(HA: ... gp)` portion is recolored; item name and `(GE: ... gp)` are untouched.

## Configuration

1. Enable RuneLite's **Ground Items**.
2. Set **Ground Items > Price Display Mode** to **Both** (GE + HA).
3. Enable **HA Profit Ground Items**.
4. Open the plugin settings. **Nature rune GE price (auto)** shows the current rune price for reference only. The value is refreshed on game ticks; reopen settings to refresh the visible field if the price changes while the panel is open. Manual edits are overwritten.
5. Set **Minimum HA - GE (gp)** to choose the minimum difference **per item** that turns the HA segment green. The default is **`0 gp`**, so equality is highlighted. If you saved an older threshold, manually set it to `0` to use the new rule.
6. Optionally change **HA highlight color**.

For example, an item with `HA = 1,200 gp` and `GE = 900 gp` has an `HA - GE` difference of `300 gp`. A threshold of `300 gp` highlights it, but `301 gp` does not. If `HA = GE`, the default threshold of `0 gp` still highlights it. A displayed Nature rune price of `200 gp` does not change that result; you may choose a threshold of `200 gp` if you want to account for the rune yourself.

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

- Item with HA greater than GE: HA segment green; item name and GE price unchanged.
- Item with HA equal to GE: HA segment green at the default `0 gp` threshold.
- Item with HA below GE: HA price retains its original color.
- Two item types on one tile; different stack quantities; noted items.
- Hidden items and `Alt` item editing: report any misalignment.

The plugin does not perform game input or cast spells. In-game rendering must be verified by the player.
