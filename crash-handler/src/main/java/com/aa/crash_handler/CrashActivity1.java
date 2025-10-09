package com.aa.crash_handler;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CrashActivity1 extends Activity {

    private String mLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_DeviceDefault);
        setTitle("App Crash");

        mLog = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        TextView textView = new TextView(this);
        textView.setText(mLog);
        textView.setTextIsSelectable(true);
        textView.setTypeface(android.graphics.Typeface.MONOSPACE);
        textView.setPadding(32, 32, 32, 32);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);
        setContentView(scrollView);
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
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
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
            .setNegativeButton("Salir", (dialog, which) -> finish())
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
            finish();
        } catch (Exception e) {
            finish();
        }
    }
}