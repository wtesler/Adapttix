package com.teamrise.adapttix;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GetName extends Activity implements OnClickListener
{
	int MAXINDIVIDUALNAMESIZE = 25;
	Button next;
	ContextWrapper cw;
	EditText personName;
	TextView alertText;
	String individualsName, INDIVIDUALEVALUATIONFILENAME,
			SELFEVALUATIONFILENAME;
	String[] files;
	int filesSize;
	boolean nameGood;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get_name);

		setVariables();
	}

	private void setVariables()
	{
		SELFEVALUATIONFILENAME = getString(R.string.self_evaluation_FILENAME);

		next = (Button) findViewById(R.id.bNext);
		next.setOnClickListener(this);

		personName = (EditText) findViewById(R.id.etPersonName);
		personName.requestFocus();

		alertText = (TextView) findViewById(R.id.tvAlert);

		files = fileList();

		cw = new ContextWrapper(this);
	}

	public void onClick(View v)
	{
		nameGood = true;
		individualsName = personName.getText().toString().trim();

		//change all characters to lowercase.
		INDIVIDUALEVALUATIONFILENAME = individualsName.toLowerCase();
		
		//THESE ARE ALL PROTECTED CHARACTERS IN FILE NAMES
		if (INDIVIDUALEVALUATIONFILENAME.contains("/") ||
				INDIVIDUALEVALUATIONFILENAME.contains("?") ||
				INDIVIDUALEVALUATIONFILENAME.contains("<") ||
				INDIVIDUALEVALUATIONFILENAME.contains(">") ||
				INDIVIDUALEVALUATIONFILENAME.contains("\\") ||
				INDIVIDUALEVALUATIONFILENAME.contains(":") ||
				INDIVIDUALEVALUATIONFILENAME.contains("*") ||
				INDIVIDUALEVALUATIONFILENAME.contains("|") ||
				INDIVIDUALEVALUATIONFILENAME.contains("\"") ||
				INDIVIDUALEVALUATIONFILENAME.contains("_"))
		{
			alertText.setText("Error: Invalid character used");
			nameGood = false;
		}
		else if (INDIVIDUALEVALUATIONFILENAME.contains("individual"))
		{
			alertText.setText("Error: \"Individual\" is a protected word.");
			nameGood = false;
		}

		// Replaces whitespace with "_"
		INDIVIDUALEVALUATIONFILENAME = individualsName.replaceAll(" ", "_").toLowerCase();


		
		//If the person has already entered that individual, then alert them.
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].contentEquals(INDIVIDUALEVALUATIONFILENAME))
			{
				alertText.setText("Error: Name already used");
				nameGood = false;
			}
		}
		
		if (nameGood)
		{
			alertText.setText("");
			Bundle basket = new Bundle();
			basket.putString("Individual Name", individualsName);
			Intent intent = new Intent(GetName.this, IndividualEvaluationExplanation.class);
			intent.putExtras(basket);
			startActivityForResult(intent, 55);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 55 && resultCode == RESULT_OK)
		{
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
	}
}
