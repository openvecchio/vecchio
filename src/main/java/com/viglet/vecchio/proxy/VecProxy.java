
package com.viglet.vecchio.proxy;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.vecchio.persistence.model.app.VecAccess;
import com.viglet.vecchio.persistence.model.app.VecMapping;
import com.viglet.vecchio.persistence.repository.app.VecAccessRepository;

@Component
public class VecProxy {
	@Autowired
	private VecAccessRepository vecAccessRepository;
	private static final int BUFFER_SIZE = 32768;

	public void run(URL url, OutputStream ops, VecMapping vecMapping) throws IOException {

		try {

			DataOutputStream out = new DataOutputStream(ops);
			BufferedReader rd = null;
			try {
				URLConnection conn = url.openConnection();
				conn.setDoInput(true);
				// not doing HTTP posts
				conn.setDoOutput(false);
		
				// Get the response
				InputStream is = null;
				try {
					long startTime = System.currentTimeMillis();
					is = conn.getInputStream();
					long elapsedTime = System.currentTimeMillis() - startTime;
					VecAccess vecAccess = new VecAccess();
					vecAccess.setDateRequest(Calendar.getInstance().getTime());
					vecAccess.setRequest(conn.getURL().toString());
					vecAccess.setResponseTime(elapsedTime);
					vecAccess.setVecMapping(null);
					
					vecAccessRepository.saveAndFlush(vecAccess);
					
					rd = new BufferedReader(new InputStreamReader(is));
				} catch (IOException ioe) {
					System.out.println("********* IO EXCEPTION **********: " + ioe);
				}
				byte by[] = new byte[BUFFER_SIZE];
				int index = is.read(by, 0, BUFFER_SIZE);
				while (index != -1) {
					out.write(by, 0, index);
					index = is.read(by, 0, BUFFER_SIZE);
				}
				out.flush();

			} catch (Exception e) {
				System.err.println("Encountered exception: " + e);
				out.writeBytes("");
			}

			if (rd != null) {
				rd.close();
			}
			if (out != null) {
				out.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		// do nothing.
	}
}
