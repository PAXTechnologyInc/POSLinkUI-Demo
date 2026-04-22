# ADB device serials for this workspace (install / pytest --device-sn / ANDROID_SERIAL).
# Update when lab hardware changes.

DEVICES = {
    "A35": "2290085315",
    "A920Pro": "1854501105",
    "A920proPCI7": "1854501105",
}


def get_serial(name_or_sn: str) -> str:
    """Resolve a short name to serial, or pass through if it looks like a serial."""
    key = name_or_sn.strip()
    if key in DEVICES:
        return DEVICES[key]
    if key.isdigit() and len(key) >= 8:
        return key
    raise KeyError(f"Unknown device {key!r}; known: {sorted(DEVICES)}")


if __name__ == "__main__":
    import sys

    if len(sys.argv) != 2:
        print("Usage: python scripts/device.py <name_or_serial>", file=sys.stderr)
        print("Known:", ", ".join(f"{k}={v}" for k, v in sorted(DEVICES.items())), file=sys.stderr)
        sys.exit(2)
    print(get_serial(sys.argv[1]))
