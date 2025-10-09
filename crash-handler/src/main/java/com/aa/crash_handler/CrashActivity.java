package com.aa.crash_handler;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.aa.crash_handler.databinding.ActivityCrashBinding;

public class CrashActivity extends AppCompatActivity {

    private String mLog;
    private ActivityCrashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTheme(R.style.Theme_CrashHandler);

        binding = ActivityCrashBinding.inflate(getLayoutInflater());
        mLog = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        setContentView(binding.getRoot());
        
//        setSupportActionBar(binding.toolbar);
//        if(getSupportActionBar() != null) {
//        	getSupportActionBar().setTitle("App Crash");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        }
//        
//        
        if (mLog != null) {
            binding.crashTextView.setText("\n\n\n"+mLog);
            binding.crashTextView.setTextIsSelectable(true);
            binding.crashTextView.setTextColor(Color.BLACK);
            binding.crashTextView.setBackgroundColor(Color.WHITE);
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, android.R.id.copy, 0, "Copiar log")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.copy) {
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Crash Log", mLog);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Log copiado", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Reiniciar aplicación")
                .setMessage("¿Quieres reiniciar la aplicación?")
                .setPositiveButton("Reiniciar", (dialog, which) -> restartApp())
                .setNegativeButton("Salir", (dialog, which) -> finishiApp())
                .setNeutralButton("Cancelar", null)
                .show();
    }

    private void restartApp() {
        try {
            Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        } catch (Exception e) {}
        finally{
            finishiApp();
        }
    }
    
    private void finishiApp() {
    	finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
