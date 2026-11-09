#!/usr/bin/env bash
# Renders the animation to docs/preview.gif without touching a phone.
#
# Needs kotlinc (brew install kotlin) and Pillow (pip install pillow).
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
OUT="${ROOT}/build/preview"
SRC="${ROOT}/app/src/main/java/com/valentina/siuuu"

mkdir -p "${OUT}"
kotlinc -nowarn -include-runtime -d "${OUT}/dump.jar" \
    "${SRC}/matrix/Sprite.kt" \
    "${SRC}/matrix/MatrixCanvas.kt" \
    "${SRC}/matrix/PixelFont.kt" \
    "${SRC}/matrix/Sprites.kt" \
    "${SRC}/animation/Celebration.kt" \
    "${ROOT}/tools/DumpFrames.kt"

java -cp "${OUT}/dump.jar" tools.DumpFrames > "${OUT}/frames.dat"
java -cp "${OUT}/dump.jar" tools.DumpFrames --ascii > "${OUT}/frames.txt"
python3 "${ROOT}/tools/frames_to_gif.py" "${OUT}/frames.dat" "${ROOT}/docs/preview.gif" --scale 12
echo "ASCII dump: ${OUT}/frames.txt"
