package com.teamrise.adapttix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IndividualEvaluationExplanation extends Activity implements OnClickListener
{
	TextView tv;
	Button b;
	Bundle gotBundle, sendBundle;
	String message, name;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_explanation);
		gotBundle = getIntent().getExtras();
		name = gotBundle.getString("Individual Name");
		message = "The next screen has pairs of traits. Choose the one that you think would be the most influential on " + name + ". If you think neither are applicable, then select 'Neither'.";
		tv = (TextView) findViewById(R.id.tvExplanation);
		tv.setPadding(15, 15, 15, 15);
		tv.setText(message);
		b = (Button) findViewById(R.id.bExplanation);
		b.setOnClickListener(this);
		
	}

	public void onClick(View arg0)
	{
		sendBundle = new Bundle();
		sendBundle.putString("Individual Name", name);
		Intent i = new Intent(IndividualEvaluationExplanation.this,IndividualEvaluation.class);
		i.putExtras(sendBundle);
		startActivity(i);
		finish();
	}
}
