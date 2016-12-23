package com.zfliu.fallingsnow.Utils;

public class Random {
    private static final java.util.Random RANDOM = new java.util.Random();

    public static float getRandom(float lower, float upper) {
        float min = Math.min(lower, upper);
        float max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    public static int getRandom(int lower,int upper){
        int min = Math.min(lower, upper);
        int max = Math.max(lower, upper);
        return getRandom(max - min) + min;
    }

    public static float getRandom(float upper) {
        return RANDOM.nextFloat() * upper;
    }

    public static int getRandom(int upper) {
        return RANDOM.nextInt(upper);
    }

}
