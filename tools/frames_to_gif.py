#!/usr/bin/env python3
"""Turn a frame dump from DumpFrames.kt into an animated GIF of the Glyph Matrix.

Usage:
    python3 tools/frames_to_gif.py frames.dat docs/preview.gif [--scale 12]

The first line of the dump is a header (`# frames=.. size=.. ms=..`); every following line is one
frame as `size * size` brightness values, row-major, 0-255.
"""
from __future__ import annotations

import argparse
import math
from PIL import Image, ImageDraw

RADIUS = 12.5  # the 489 physical LEDs sit inside this circle


def parse(path: str):
    with open(path) as handle:
        header = handle.readline().strip().lstrip("#").split()
        meta = dict(part.split("=") for part in header)
        size = int(meta["size"])
        delay = int(meta["ms"])
        frames = [
            [int(v) for v in line.split()]
            for line in handle
            if line.strip() and not line.startswith("#")
        ]
    return size, delay, frames


def has_led(x: int, y: int, size: int) -> bool:
    center = size // 2
    return math.hypot(x - center, y - center) <= RADIUS


def render(frame, size: int, scale: int) -> Image.Image:
    image = Image.new("RGB", (size * scale, size * scale), (8, 8, 8))
    draw = ImageDraw.Draw(image)
    dot = scale * 0.38
    for y in range(size):
        for x in range(size):
            if not has_led(x, y, size):
                continue
            value = frame[y * size + x]
            cx, cy = (x + 0.5) * scale, (y + 0.5) * scale
            shade = (26, 26, 26) if value == 0 else (value, value, value)
            draw.ellipse((cx - dot, cy - dot, cx + dot, cy + dot), fill=shade)
    return image


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("dump")
    parser.add_argument("output")
    parser.add_argument("--scale", type=int, default=12)
    parser.add_argument("--sheet", help="also write a contact sheet of every Nth frame")
    parser.add_argument("--every", type=int, default=9)
    args = parser.parse_args()

    size, delay, frames = parse(args.dump)
    images = [render(frame, size, args.scale) for frame in frames]
    images[0].save(
        args.output,
        save_all=True,
        append_images=images[1:],
        duration=delay,
        loop=0,
        optimize=True,
    )
    print(f"{args.output}: {len(images)} frames @ {delay}ms")

    if args.sheet:
        picks = images[:: args.every]
        columns = min(5, len(picks))
        rows = math.ceil(len(picks) / columns)
        w, h = picks[0].size
        sheet = Image.new("RGB", (columns * w, rows * h), (0, 0, 0))
        for index, image in enumerate(picks):
            sheet.paste(image, ((index % columns) * w, (index // columns) * h))
        sheet.save(args.sheet)
        print(f"{args.sheet}: {len(picks)} frames")


if __name__ == "__main__":
    main()
