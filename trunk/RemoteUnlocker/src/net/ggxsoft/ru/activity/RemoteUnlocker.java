/*
 * RemoteUnlocker (RemoteUnlocker Lib)
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

import net.ggxsoft.ru.client.Unlockable;
import android.content.Context;

/** 
 * RemoteUnlocker è un tool per sviluppatori Android che permette di implementare
 * un sistema di sblocco basato su validazione di un codice seriale online
 * </br></br>
 * EN: RemoteUnlocker is a tool for Android that allows developers to implement
 * an unlock system based on online validation of a serial code
 * 
 * @author Luigi Vaira - www.ggxsoft.net
 * @version 1.0
 * */
public class RemoteUnlocker {
	
	public static final String PREFERENCES_NAME = "net.ggxsoft.remoteunlocker",
								PREFERENCES_ATTRIBUTE = "net.ggxsoft.appulocked";
	
	/** 
	 * Il seriale è corretto (presente nel database) E l'ID del dispositivo
	 * corrisponde a quello associato (se presente)
	 * </br></br>
	 * EN: Serial is correct (it's in DB) and the device ID matches
	 * the one associated (if present)
	 * */
	public static final int SERIAL_OK = 10;
	
	/** 
	 * Il seriale non è corretto (non è presente nel database)
	 * </br></br>
	 * EN: Serial is NOT correct (it's not present in DB)
	 * */
	public static final int SERIAL_WRONG = 11;
	
	/** 
	 * Il seriale è presente nel database ma è associato all'ID
	 * di un altro dispositivo. Il seriale non è corretto per il
	 * dispositivo corrente.
	 * </br></br>
	 * EN: Serial is present in DB but it's associated to another
	 * device ID. Serial is not correct for the current device.
	 * */
	public static final int SERIAL_ALREADY_USED = 12;
	
	/** 
	 * Non è stato possibile stabilire una connessione col server
	 * o risposta sconosciuta.
	 * </br></br>
	 * EN: It was not possible to establish a connection to the server OR
	 * Unknown Answer
	 * */
	public static final int SERVER_NO_CONNECTION = 13;
	
	/** 
	 * Metodo statico da utilizzare ogniqualvolta sia necessaria una verifica
	 * offline dello stato di Unlock.
	 * 
	 * Viene richiamato automaticamente 
	 * il metodo <code>Unlockable.onUnlockResponse(int response)</code> (vedi {@link Unlockable})
	 * </br></br>
	 * EN: Static method to be used every time is necessary an offline verification
	 * of the unlock status.
	 * The following method will be invoked automatically: 
	 * <code>Unlockable.onUnlockResponse(int response)</code> (see {@link Unlockable})
	 * 
	 * @param context	il Context di riferimento</br>EN: App context
	 * */
	public static void checkUnlockStatus(Context context) {
		int unlocked = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(PREFERENCES_ATTRIBUTE, SERIAL_WRONG);
		if (context instanceof Unlockable) ((Unlockable)context).onUnlockResponse(unlocked);
	}
	
	/** 
	 * Metodo statico. Restituisce il codice di stato unlock memorizzato nelle SharedPreferences
	 * </br></br>
	 * EN: Static method. It return the unlock code stored into the SharedPreferences
	 * @param context	il Context di riferimento</br>EN: App context
	 * 
	 * @return	il codice di stato di unlock (costanti di {@link RemoteUnlocker})</br>
	 * EN: status unlock code (const of {@link RemoteUnlocker})
	 * */
	public static int getUnlockResultCode(Context context) {
		return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(PREFERENCES_ATTRIBUTE, SERIAL_WRONG);
	}
	
}
