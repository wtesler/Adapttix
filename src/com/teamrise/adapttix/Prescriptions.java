package com.teamrise.adapttix;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Prescriptions extends FragmentActivity
{
	Bundle gotBundle;
	ArrayList<Integer> gotList = new ArrayList<Integer>();
	ArrayList<CheckBox> checkList = new ArrayList<CheckBox>();
	String gotCheckboxFILENAME, commentFILENAME, commentContent, endComments;
	String[] prescriptionList = new String[40];
	char isChecked;
	char commentChar;
	int commentInt;
	ContextWrapper cw = new ContextWrapper(this);

	BufferedInputStream bis;
	BufferedOutputStream bos;

	CheckBox[] checkBoxArray = new CheckBox[20];
	Button accept;

	EditText commentBox;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.prescriptions);

		gotBundle = getIntent().getExtras();
		gotList = gotBundle.getIntegerArrayList("prescriptions");
		gotCheckboxFILENAME = gotBundle.getString("checkbox");

		commentBox = (EditText) findViewById(R.id.etPrescriptionsComments);
		loadCheckBoxes();
		loadCommentBox();

	}

	private void loadCheckBoxes()
	{
		try
		{
			bis = new BufferedInputStream(openFileInput(gotCheckboxFILENAME));
			prescriptionList = getResources().getStringArray(R.array.lPrescriptions);
			for (int i = 0; i < gotList.size(); i++)
			{
				checkBoxArray[i] = (CheckBox) findViewById(getResources().getIdentifier("ctv" + i,
						"id", getPackageName()));
				checkBoxArray[i].setVisibility(View.VISIBLE);
				checkList.add(checkBoxArray[i]);
				checkBoxArray[i].setText(prescriptionList[gotList.get(i)]);
				isChecked = (char) bis.read();
				if (isChecked == 'Y')
				{
					checkBoxArray[i].setChecked(true);
				}
			}
			bis.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadCommentBox()
	{
		commentFILENAME = gotCheckboxFILENAME.replace("checkbox", "comment");
		try
		{
			bis = new BufferedInputStream(openFileInput(commentFILENAME));

			commentContent = "";
			commentInt = bis.read();
			commentChar = (char) commentInt;
			while (commentInt != -1)
			{
				commentContent += commentChar;
				commentInt = bis.read();
				commentChar = (char) commentInt;
			}
			bis.close();
			commentBox.setText(commentContent);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		try
		{
			// NEW CHECKBOX FILE
			bos = new BufferedOutputStream(openFileOutput(gotCheckboxFILENAME, MODE_PRIVATE));
			for (int i = 0; i < checkList.size(); i++)
			{

				if (checkList.get(i).isChecked())
				{
					bos.write('Y');
				} else
				{
					bos.write('N');
				}
			}
			bos.flush();
			bos.close();

			endComments = commentBox.getText().toString();
			bos = new BufferedOutputStream(openFileOutput(commentFILENAME, MODE_PRIVATE));
			for (int i = 0; i < endComments.length(); i++)
			{
				bos.write(endComments.charAt(i));
			}
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
