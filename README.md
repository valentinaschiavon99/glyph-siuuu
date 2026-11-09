# SIUUUU — a Glyph Toy for the Nothing Phone (3)

A goal celebration for the Glyph Matrix on the back of the Nothing Phone (3): a footballer runs in,
jumps, lands in the pose, the shirt number **7** rises into place, and **SIUUUU** scrolls across the
489 LEDs.

![preview](docs/preview.gif)

*(the GIF is rendered from the exact frames the phone receives — see [Rendering a preview](#rendering-a-preview))*

## What it does

| Phase | Frames | What you see |
|---|---|---|
| Run-in | 14 | the figure runs in from the right, two-frame cycle |
| Jump | 5 | knees tucked, up and down |
| Landing | 4 | one full-matrix flash, then a shockwave ring over the pose |
| Pose | 16 | arms out, legs planted, brightness breathing |
| Number | 22 | the **7** wipes in from the bottom and holds |
| Shout | 70 | **SIUUUU!** scrolls right to left in a 5x7 pixel font |
| Out | 4 | two strobes, back to the resting number |

One run is 135 frames at 20 fps, so about 6.8 seconds.

## Controls

| Action | Result |
|---|---|
| Select the toy with the Glyph Button | plays once, then rests on a dimmed **7** |
| Tap the Glyph Button | replay |
| Long press the Glyph Button | toggle continuous loop |
| Selected as an AOD toy | shows the dimmed **7** |

## Build it

You need Android Studio (or just the Android SDK) and a Nothing Phone (3) running Nothing OS 3.5 or
later.

```bash
git clone https://github.com/<you>/glyph-siuuu.git
cd glyph-siuuu
./tools/fetch-sdk.sh          # pulls Nothing's Glyph Matrix SDK into app/libs
./gradlew installDebug        # or open the project in Android Studio and hit Run
```

The Glyph Matrix SDK is licensed by Nothing and its EULA forbids redistribution, so the `.aar` is
**not** committed here — `tools/fetch-sdk.sh` downloads it from
[Nothing's own developer kit repo](https://github.com/Nothing-Developer-Programme/GlyphMatrix-Developer-Kit).

## Turn it on

1. Install the app on the phone.
2. `Settings` → `Glyph Interface` → `Glyph Toys`.
3. Enable **SIUUUU** and drag it where you want it in the carousel.
4. Press the Glyph Button on the back of the phone until you reach it.

The app itself has no features beyond an on-screen preview of the animation and these steps.

## How it works

The matrix is addressed as a 25 x 25 grid, but only the 489 cells inside a circle of radius 12.5
have an LED behind them (π · 12.5² ≈ 491, which is exactly how the count works out).
`MatrixCanvas` knows that, so anything drawn outside the circle is dropped instead of silently
wrapping.

```
app/src/main/java/com/valentina/siuuu/
├── matrix/
│   ├── Sprite.kt          pixel art written as strings: '#' on, '.' off
│   ├── MatrixCanvas.kt    25x25 surface, circular LED mask, brightness 0-255
│   ├── PixelFont.kt       5x7 bitmap font (only the letters this toy needs)
│   └── Sprites.kt         the run cycle, the jump, the pose, the 7
├── animation/
│   └── Celebration.kt     the choreography as a pure function of a frame index
├── glyph/
│   └── GlyphToyService.kt SDK boilerplate: bind, register, Glyph Button events
├── toy/
│   └── SiuuuToyService.kt the toy: playback, looping, idle and AOD states
└── ui/                    on-screen preview of the same frames
```

`Celebration.render(frame)` is pure Kotlin with no Android dependency, which is why the same code
drives the matrix, the in-app Compose preview and the GIF generator.

### Editing the art

The sprites are literally pictures in the source:

```kotlin
val CELEBRATION: Sprite = Sprite.of(
    ".....#####.....",
    "....#######....",
    ...
    "..###.....###..",
    ".####.....####.",
)
```

Change a character, re-render, done. Timings live as constants at the top of `Celebration.kt`.

### Rendering a preview

Needs `kotlinc` and Pillow:

```bash
./tools/render_preview.sh     # writes docs/preview.gif and an ASCII dump of every frame
```

## Notes

A fan project, made for fun, not affiliated with or endorsed by Nothing, any football club, or any
player. All the pixel art here is original: a generic silhouette, a shirt number and a shout — no
photograph, likeness, badge or logo is reproduced. Nothing's Glyph Matrix SDK is used under its own
EULA, which also restricts commercial use.

`glyph/GlyphToyService.kt` is adapted from the MIT-licensed
[GlyphMatrix-Example-Project](https://github.com/Nothing-Developer-Programme/GlyphMatrix-Example-Project)
by Nothing Technology Limited. Everything else is MIT, see [LICENSE](LICENSE).
