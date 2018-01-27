package mobile.filesharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	
	
	
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Button login = (Button)findViewById(R.id.login_button);
		final EditText field = (EditText)findViewById(R.id.name_field);
		
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String logicalName = field.getText().toString();
				Intent intent = new Intent(LoginActivity.this, MobileAppActivity.class);
				intent.putExtra("LOGICAL_NAME", logicalName);
				startActivity(intent);
			}
		});
		
	}
}
