package com.example.gamecontrolsproyect;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.aa.game_controllers.Joystick;
import com.aa.game_controllers.ABButtonsView;
import com.example.gamecontrolsproyect.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements Joystick.JoystickListener, ABButtonsView.ABListener {
    private ActivityMainBinding binding;

    private GameView gameView;
//    private Joystick joystick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate and get instance of binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        gameView = binding.gameView;
//        joystick = binding.joystick;

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
    public void onAButtonPressed() {
        gameView.setPlayerColor(Color.BLUE);
    }

    @Override
    public void onBButtonPressed() {
        gameView.setPlayerColor(Color.YELLOW);
    }
}
