#!/usr/bin/env bash
# Downloads Nothing's Glyph Matrix SDK into app/libs.
#
# The SDK is licensed, not open source, and its EULA forbids redistribution, so it is not
# committed to this repository. This script pulls it straight from Nothing's own repo.
set -euo pipefail

VERSION="glyph-matrix-sdk-2.0.aar"
URL="https://github.com/Nothing-Developer-Programme/GlyphMatrix-Developer-Kit/raw/main/${VERSION}"
DEST="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)/app/libs"

mkdir -p "${DEST}"
echo "Downloading ${VERSION} ..."
curl -fsSL "${URL}" -o "${DEST}/${VERSION}"
echo "Saved to app/libs/${VERSION}"
echo "By using it you accept Nothing's Glyph SDK EULA:"
echo "https://github.com/Nothing-Developer-Programme/GlyphMatrix-Developer-Kit/blob/main/LICENSE.md"
