package me.andaragk.oceanoverhaulimmersion.client.depth;

public enum DepthZone {
    SURFACE(0, "surface"),
    LITTORAL(1, "littoral_epipelagic"),
    BATHYAL(16, "mesopelagic_bathyal"),
    MIDNIGHT(46, "bathypelagic_midnight"),
    ABYSSAL(76, "abyssopelagic"),
    HADAL(111, "hadal");

    private final int startDepth;
    private final String id;

    DepthZone(int startDepth, String id) {
        this.startDepth = startDepth;
        this.id = id;
    }

    public int startDepth() {
        return startDepth;
    }

    public String id() {
        return id;
    }

    public static DepthZone fromDepth(int depth) {
        if (depth >= HADAL.startDepth) {
            return HADAL;
        }
        if (depth >= ABYSSAL.startDepth) {
            return ABYSSAL;
        }
        if (depth >= MIDNIGHT.startDepth) {
            return MIDNIGHT;
        }
        if (depth >= BATHYAL.startDepth) {
            return BATHYAL;
        }
        if (depth >= LITTORAL.startDepth) {
            return LITTORAL;
        }
        return SURFACE;
    }
}
