package ara.sandy.candies;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

public class ProgressDialog extends Dialog {

    TextView txtMessage;

    String message = "";

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    public ProgressDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        txtMessage = findViewById(R.id.txtMessage);

        txtMessage.setText(message);
    }

}
