package com.aa.game_controls.base;

public class ControlConfig {
    private int backgroundColor = 0x00000000; // Transparente por defecto
    private boolean backgroundTransparent = true;
    private int alpha = 255;
    private float controlScale = 1.0f;
    private boolean enabled = true;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundTransparent = (backgroundColor == 0x00000000);
    }

    public boolean isBackgroundTransparent() {
        return backgroundTransparent;
    }

    public void setBackgroundTransparent(boolean backgroundTransparent) {
        this.backgroundTransparent = backgroundTransparent;
        if (backgroundTransparent) {
            this.backgroundColor = 0x00000000;
        }
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = Math.max(0, Math.min(255, alpha));
    }

    public float getControlScale() {
        return controlScale;
    }

    public void setControlScale(float controlScale) {
        this.controlScale = Math.max(0.1f, Math.min(2.0f, controlScale));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}