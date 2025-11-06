package com.example.gamecontrolsproyect;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aa.game_controls.joystick.Joystick;

import com.example.gamecontrolsproyect.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements Joystick.JoystickListener/*, GameButtonsView.ButtonsListener*/ {
    private ActivityMainBinding binding;

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        gameView = binding.gameView;
        
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }

    @Override
    public void onJoystickMoved(float xPorcent, float yPorcent, int id) {
        gameView.updatePlayer(xPorcent, yPorcent);
    }
}
