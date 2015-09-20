package com.lynas.app2;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	/** Called when the activity is first created. */
	private static String SOAP_ACTION1 = "http://tempuri.org/FahrenheitToCelsius";
	private static String SOAP_ACTION2 = "http://tempuri.org/CelsiusToFahrenheit";
	private static String NAMESPACE = "http://tempuri.org/";
	private static String METHOD_NAME1 = "FahrenheitToCelsius";
	private static String METHOD_NAME2 = "CelsiusToFahrenheit";
	private static String URL = "http://www.w3schools.com/webservices/tempconvert.asmx?WSDL";

	SoapSerializationEnvelope envelope;

	Button btnFar, btnCel, btnClear;
	EditText txtFar, txtCel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnFar = (Button) findViewById(R.id.btnFar);
		btnCel = (Button) findViewById(R.id.btnCel);
		btnClear = (Button) findViewById(R.id.btnClear);
		txtFar = (EditText) findViewById(R.id.txtFar);
		txtCel = (EditText) findViewById(R.id.txtCel);

		btnFar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Initialize soap request + add parameters
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME1);

				// Use this to add parameters
				request.addProperty("Fahrenheit", txtFar.getText().toString());

				// Declare the version of the SOAP request
				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;

				new RetrieveFeedTask().execute("");
			}
		});

		btnCel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Initialize soap request + add parameters
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME2);

				// Use this to add parameters
				request.addProperty("Celsius", txtCel.getText().toString());

				// Declare the version of the SOAP request
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);

				envelope.setOutputSoapObject(request);
				envelope.dotNet = true;

				try {
					HttpTransportSE androidHttpTransport = new HttpTransportSE(
							URL);

					// this is the actual part that will call the webservice
					androidHttpTransport.call(SOAP_ACTION2, envelope);

					// Get the SoapResult from the envelope body.
					SoapObject result = (SoapObject) envelope.bodyIn;

					if (result != null) {
						// Get the first property and change the label text
						txtFar.setText(result.getProperty(0).toString());
					} else {
						Toast.makeText(getApplicationContext(), "No Response",
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btnClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				txtCel.setText("");
				txtFar.setText("");
			}
		});
	}

	class RetrieveFeedTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... f_url) {
			try {
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

				// this is the actual part that will call the webservice
				androidHttpTransport.call(SOAP_ACTION1, envelope);

				// Get the SoapResult from the envelope body.
				SoapObject result = (SoapObject) envelope.bodyIn;

				if (result != null) {
					// Get the first property and change the label text
					// txtCel.setText(result.getProperty(0).toString());
					Toast.makeText(getApplicationContext(),
							" " + result.getProperty(0).toString(),
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "No Response",
							Toast.LENGTH_LONG).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "working";
		}
	}
}
