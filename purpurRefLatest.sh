#!/bin/bash
set -euo pipefail

set_property() {
  local key="$1"
  local value="$2"
  local file="$3"
  awk -v pat="^$key=" -v replacement="$key=$value" '
    $0 ~ pat { print replacement; next }
    { print }
  ' "$file" > "$file.tmp"
  mv "$file.tmp" "$file"
}

sha="$(curl --fail --silent --show-error https://api.github.com/repos/PurpurMC/Purpur/commits/ver/26.3 | jq -er .sha)"
set_property purpurRef "$sha" gradle.properties
printf 'Updated Purpur 26.3 ref: %s\n' "$sha"
