package com.aa.game_controls.joystick;

public class JoystickConfig {
    private float baseRatio = 0.4f;
    private float hatRatio = 0.15f;
    private int baseColor = 0xFF888888; // Gray
    private int hatColor = 0xFF000000;  // Black
    private boolean returnToCenter = true;

    public float getBaseRatio() {
        return baseRatio;
    }

    public void setBaseRatio(float baseRatio) {
        this.baseRatio = Math.max(0.1f, Math.min(0.8f, baseRatio));
    }

    public float getHatRatio() {
        return hatRatio;
    }

    public void setHatRatio(float hatRatio) {
        this.hatRatio = Math.max(0.05f, Math.min(0.4f, hatRatio));
    }

    public int getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(int baseColor) {
        this.baseColor = baseColor;
    }

    public int getHatColor() {
        return hatColor;
    }

    public void setHatColor(int hatColor) {
        this.hatColor = hatColor;
    }

    public boolean isReturnToCenter() {
        return returnToCenter;
    }

    public void setReturnToCenter(boolean returnToCenter) {
        this.returnToCenter = returnToCenter;
    }
}