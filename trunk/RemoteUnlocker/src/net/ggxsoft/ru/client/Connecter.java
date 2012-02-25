/*
 * Connecter (RemoteUnlocker Lib)
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.ggxsoft.ru.activity.RemoteUnlocker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

public class Connecter {
	
	protected DefaultHttpClient client = new DefaultHttpClient();
	protected HttpPost post;
	
	/** 
	 * Crea un oggetto di tipo Connecter utile alla gestione della connessione Http
	 * </br></br>
	 * EN: It instantiates an object of type Connecter that takes care of
	 * the HTTP connection
	 * @param serverAddress	l'url che contiene lo script rucheck.php</br>
	 * EN: the URL where rucheck.php script is located
	 * */
	public Connecter(String serverAddress) {
		try {
			URI uri = new URI("http://" + serverAddress + "/rucheck.php");
			post = new HttpPost(uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
	}
	
	/** 
	 * Metodo che viene richiamato in {@link ValidateAsync} per il controllo
	 * del codice seriale
	 * </br></br>
	 * EN: This method will be invoked in {@link ValidateAsync} to validate
	 * the serial code
	 * @param serialNumber	il codice seriale da controllare</br>
	 * EN: serial code to be checked
	 * @param IMEI	l'ID/IMEI del dispositivo da controllare</br>
	 * EN: ID/IMEI to be checked along with the serial code
	 * @param appPackage	il package name dell'applicazione da controllare</br>
	 * EN: application package name to be checked
	 * 
	 * @return	il codice di stato del seriale (codici statici in {@link RemoteUnlocker})</br>
	 * EN: status unlock code (static codes in {@link RemoteUnlocker})
	 * */
	public int getSerialStatus(String serialNumber, String IMEI, String appPackage) throws IllegalStateException, ClientProtocolException, IOException {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("serial", serialNumber));
		list.add(new BasicNameValuePair("imei", IMEI));
		list.add(new BasicNameValuePair("appPackage", appPackage));
		post.setEntity(new UrlEncodedFormEntity(list));
		ResponseReader reader = new ResponseReader(client.execute(post));
		return reader.getResponse();
	}
	
	/** 
	 * Classe di supporto per la lettura della risposta del server
	 * </br></br>
	 * EN: Support class for server response reading
	 * */
	protected class ResponseReader {
		
		BufferedReader breader;
		
		/** 
		 * Costruttore di default
		 * </br></br>
		 * EN: Default constructor
		 * 
		 * @param resp	oggetto HttpResponse creato in seguito della connessione al server</br>
		 * EN: HttpResponse object created by the connection to the server
		 * */
		ResponseReader(HttpResponse resp) throws IllegalStateException, IOException {
			breader = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		}
		
		/** 
		 * Metodo di lettura della risposta
		 * </br></br>
		 * EN: Reading method for the response
		 * @return la stringa di risposta del server (da parsare)</br>
		 * EN: answering server string (to be parsed)
		 * */
		int getResponse() throws IOException {
			String svrresp = breader.readLine();
			return Integer.parseInt(svrresp);
		}
		
	}
	
}