package com.example.gamecontrolsproyect;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aa.game_controls.GameButtonsView;
import com.aa.game_controls.Joystick;

import com.example.gamecontrolsproyect.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements Joystick.JoystickListener, GameButtonsView.ButtonsListener {
    private ActivityMainBinding binding;

    private GameView gameView;
    private GameButtonsView buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        gameView = binding.gameView;
        buttons = binding.gameButtons;
        buttons.addButton(1, 50f, 60f, Color.RED, null, "R");
        buttons.addButton(2, 50f, 60f, Color.BLUE, null, "B");
        
        // set content view to binding's root
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

    @Override
    public void onButtonPressed(int buttonId) {
        switch(buttonId){
            case 1:
                gameView.setPlayerColor(Color.RED);
                break;
            case 2:
                gameView.setPlayerColor(Color.BLUE);
                break;
        }
    }

    @Override
    public void onButtonReleased(int buttonId) {}
}
