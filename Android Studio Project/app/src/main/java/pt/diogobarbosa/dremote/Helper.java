package pt.diogobarbosa.dremote;

import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class Helper {

    protected void validateFirstMessage(JSONObject data, final String[]... serverInfo) {
        try {
            if (Boolean.parseBoolean(data.getString("error"))) {
                Toast.makeText(Dremote_main._context, data.getString("message"), Toast.LENGTH_LONG).show();
                restoreUI();
            } else {

                if (data.getString("door_command").equals("open")) {
                    Dremote_main._tvState.setText("Opened");
                    Toast.makeText(Dremote_main._context, "Correct Message.", Toast.LENGTH_LONG).show();

                    Dremote_main._formLayout.setVisibility(View.VISIBLE);
                    Dremote_main._serverFieldsLayout.setVisibility(View.INVISIBLE);
                    Dremote_main._buttonOpenLayout.setVisibility(View.INVISIBLE);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new AsyncUdpSecondaryMainThread().execute(serverInfo[0]);
                        }
                    }, 4000);

                } else {
                    Dremote_main._tvState.setText("Closed");
                    Toast.makeText(Dremote_main._context, "Command not found", Toast.LENGTH_LONG).show();

                }
            }

        } catch (JSONException e) {
            Log.e("Validation Message 1:", "Error:", e);
        }

    }

    protected void validateSecondMessage(JSONObject data) {
        try {

            if (!Boolean.parseBoolean(data.getString("error"))) {
                if (data.getString("door_command").equals("open")) {
                    Dremote_main._tvState.setText("Opened");
                } else {
                    Dremote_main._tvState.setText("Closed");
                }
            }
            restoreUI();

        } catch (JSONException e) {
            Log.e("Validation Message:", "Error:", e);
        }

    }

    private void restoreUI(){
        Dremote_main._progressBarLayout.setVisibility(View.GONE);
        Dremote_main._formLayout.setVisibility(View.VISIBLE);
        Dremote_main._serverFieldsLayout.setVisibility(View.VISIBLE);
        Dremote_main._buttonOpenLayout.setVisibility(View.VISIBLE);
    }

}
