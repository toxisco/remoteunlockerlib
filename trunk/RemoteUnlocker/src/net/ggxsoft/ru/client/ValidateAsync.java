/*
 * ValidateAsync (RemoteUnlocker Lib)
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

package net.ggxsoft.ru.client;

import java.io.IOException;

import net.ggxsoft.remoteunlocker.R;
import net.ggxsoft.ru.activity.RemoteUnlocker;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class ValidateAsync extends AsyncTask<String, Void, Integer> {

	protected Context context;
	protected ProgressDialog dialog;
	protected Connecter link;
	
	/** 
	 * Crea un oggetto di tipo AsyncTask
	 * </br></br>
	 * EN: It instantiates an object of type AsyncTask
	 * 
	 * @param context	il Context di riferimento</br>
	 * EN: App context
	 * @param link	oggetto {@link Connecter} per la gestione della connessione</br>
	 * EN: {@link Connecter} object for the connection management
	 * */
	public ValidateAsync(Context context, Connecter link) {
		this.context = context;
		this.link = link;
	}
	
	/** 
	 * Viene impostata e visualizzata una ProgressDialog
	 * </br></br>
	 * EN: A ProgressDialog will be set up and shown
	 * */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setTitle(R.string.progressdialog_title);
		dialog.setMessage(context.getString(R.string.progressdialog_message));
		dialog.show();
	}
	
	/** 
	 * Elaborazione della connessione e controllo del codice online
	 * </br></br>
	 * EN: Connection processing and online code verification
	 * 
	 * @param arg0	param-arg composto da seriale (1° param) e imei (2° param)</br>
	 * EN: param-arg made by serial (1st param) and imei (2nd param)
	 * 
	 * @return	codice di stato del seriale (codici statici in {@link RemoteUnlocker})</br>
	 * EN: serial status code (static codes in {@link RemoteUnlocker})
	 * */
	@Override
	protected Integer doInBackground(String... arg0) {
		String serial = arg0[0], imei = arg0[1];
		int response = 0;
		try {
			response = link.getSerialStatus(serial,imei,context.getPackageName());
		} catch (ClientProtocolException e) {
			return RemoteUnlocker.SERVER_NO_CONNECTION;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/** 
	 * Il risultato del controllo online viene memorizzato nelle SharedPreferences:
	 * il nome delle preferenze e quello dell'attributo è memorizzato staticamente
	 * in {@link RemoteUnlocker}. La ProgressDialog viene chiusa.
	 * </br></br>
	 * EN: The result of the online verification will be stored into the SharedPreferences:
	 * the preferences' and attrib's name is statically represented in {@link RemoteUnlocker}.
	 * The ProgressDialog will be dismissed.
	 * 
	 * @param result	il codice di stato ricevuto dalla validazione</br>
	 * EN: the status code received after the validation process
	 * */
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		
		SharedPreferences prefs = context.getSharedPreferences(RemoteUnlocker.PREFERENCES_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(RemoteUnlocker.PREFERENCES_ATTRIBUTE, result);
		editor.commit();
		
		dialog.dismiss();
		
		if (context instanceof Unlockable) ((Unlockable)context).onUnlockResponse(result);
			
	}

}
