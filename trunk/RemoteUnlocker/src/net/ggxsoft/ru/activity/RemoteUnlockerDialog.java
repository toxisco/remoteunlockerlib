/*
 * RemoteUnlockerDialog (RemoteUnlocker Lib)
 * Copyright (C) 2011 gGxSoft.net
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.ggxsoft.ru.activity;

import net.ggxsoft.remoteunlocker.R;
import net.ggxsoft.ru.client.Connecter;
import net.ggxsoft.ru.client.ValidateAsync;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RemoteUnlockerDialog extends Dialog {
	
	protected LinearLayout main, buttonsLayout;
	protected Connecter link;
	protected Button ok,
					cancel;
	protected EditText serial;
	protected Context context;
	protected TelephonyManager tm;
	protected TextView message, powered;
	
	protected boolean appUnlocked;
	
	/** 
	 * Crea un oggetto di tipo Dialog. Se è presente un codice di sblocco valido vengono 
	 * disabilitati sia il campo di inserimento del seriale che il pulsante "Ok".
	 * </br></br>
	 * EN: It instantiates an object of type Dialog. If a valid unlock code is present,
	 * input serial field and "Ok" button will be disabled.
	 * @param context	il Context di riferimento</br>EN: App context
	 * @param serverAddress	l'indirizzo dove è localizzato lo script rucheck.php</br>
	 * EN: the URL where rucheck.php script is located
	 * */
	public RemoteUnlockerDialog(Context context, String serverAddress) {
		super(context);
		this.context = context;
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		link = new Connecter(serverAddress);
		initViews();
		if (RemoteUnlocker.getUnlockResultCode(context) == RemoteUnlocker.SERIAL_OK) {
			setUnlockedStatusViews();
			appUnlocked = true;
		}
	}
	
	@Override
	public void show() {
		if (!appUnlocked) {
			if (RemoteUnlocker.getUnlockResultCode(context) == RemoteUnlocker.SERIAL_OK) {
				setUnlockedStatusViews();
				appUnlocked = true;
			}
		}
		super.show();
	}
	
	@Override
	public void dismiss() {
		if (!appUnlocked) serial.setText("");
		super.dismiss();
	}

	/** 
	 * Imposta alcune View allo stato di non editabile/non abilitato
	 * quando l'applicazione risulta sbloccata da un codice valido
	 * </br></br>
	 * EN: It sets some Views to the state of not editable/not enabled
	 * when the app is unlocked by a valid serial code
	 * */
	protected void setUnlockedStatusViews() {
		serial.setText(R.string.app_already_unlocked);
		serial.setEnabled(false);
		serial.setInputType(InputType.TYPE_NULL);
		ok.setEnabled(false);
	}

	/** 
	 * Crea un AsyncTask per la validazione del codice
	 * </br></br>
	 * EN: It creates an AsyncTask to validate the code
	 * */
	protected void validateProcedure() {
		if (serial.getText().toString().equals("")) return;
		new ValidateAsync(context, link).execute(serial.getText().toString(), tm.getDeviceId());
		this.dismiss();
	}

	/** 
	 * Metodo di setup degli elementi grafici del Dialog
	 * </br></br>
	 * EN: Method to setup Dialog's UI elements
	 * */
	protected void initViews() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
		main = new LinearLayout(context);
		main.setOrientation(LinearLayout.VERTICAL);
		buttonsLayout = new LinearLayout(context);
		ok = new Button(context);
		ok.setLayoutParams(params);
		cancel = new Button(context);
		cancel.setLayoutParams(params);
		serial = new EditText(context);
		message = new TextView(context);
		powered = new TextView(context);
		
		main.setPadding(5, 0, 5, 10);
		
		buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
		buttonsLayout.addView(ok);
		buttonsLayout.addView(cancel);
		buttonsLayout.setGravity(Gravity.CENTER);
		buttonsLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		buttonsLayout.setWeightSum(2);
		
		ok.setText(R.string.ok);
		cancel.setText(R.string.cancel);
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				validateProcedure();
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				RemoteUnlockerDialog.this.dismiss();
			}
		});
		
		serial.setGravity(Gravity.CENTER);
		serial.setWidth(240);
		//serial.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);	<-- no more valid from Android 2.2
		serial.setMaxLines(1);
		message.setGravity(Gravity.CENTER);
		message.setText(R.string.insert_unlock);
		message.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		powered.setText("Powered by RemoteUnlocker");
		powered.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		powered.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://code.google.com/p/remoteunlockerlib/")));
			}
		});
		
		main.addView(message);
		main.addView(serial);
		main.addView(buttonsLayout);
		main.addView(powered);
		
		this.setContentView(main);
		this.setTitle(R.string.unlock);
	}
		
}
