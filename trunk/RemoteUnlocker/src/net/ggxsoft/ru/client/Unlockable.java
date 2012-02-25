/*
 * Unlockable (RemoteUnlocker Lib)
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

/** 
 * Interfaccia da implementare in ogni Context del quale si vuole verificare
 * lo stato di unlock tramite seriale
 * </br></br>
 * EN: Interface to be implemented in every context where you want to check
 * the unlock status by serial code
 * */
public interface Unlockable {
	
	/** 
	 * Questo metodo viene richiamato ogniqualvolta viene effettuata una
	 * verifica online ({@link ValidateAsync}) oppure 
	 * offline (<code>RemoteUnlocker.checkUnlockStatus(int response)</code>) del codice di unlock
	 * </br></br>
	 * EN: This method will be invoked whenever a check of the unlock code is made online ({@link ValidateAsync})
	 * or offline (<code>RemoteUnlocker.checkUnlockStatus(int response)</code>)
	 * 
	 * @param response	il codice di risposta: i codici sono definiti staticamente
	 * nella classe RemoteUnlocker</br>
	 * EN: the response status code: the codes are statically defined in the RemoteUnlocker class
	 * */
	void onUnlockResponse(int response);
	
}
