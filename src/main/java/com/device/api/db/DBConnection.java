package com.device.api.db;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import com.device.api.vo.DeviceVO;

@SuppressWarnings("unchecked")
public class DBConnection {

	private ServletContext servletContext;

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	private void connect() throws Exception {
		// TODO - implementar desconexion a BD, por ahora usamos una hashTable en Memoria
		if (servletContext != null) {
			if(servletContext.getAttribute("db") == null){
				servletContext.setAttribute("db", new Hashtable<String, DeviceVO>());
			}
			if(servletContext.getAttribute("index") == null){
				servletContext.setAttribute("index", 1);
			}
		}
	}

	private void disconnect() throws Exception {
		// TODO -TODO - A implementar
	}

	public void saveDevice(DeviceVO device) {
		try {
			this.connect();
			Hashtable<String, DeviceVO> db = (Hashtable<String, DeviceVO>) servletContext.getAttribute("db");
			int id = (int) servletContext.getAttribute("index");
			device.setId(id);
			db.put(device.getMac(), device);
			id++;
			servletContext.setAttribute("index", id);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				this.disconnect();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public void updateDevice(DeviceVO device) {
		// TODO - A implementar
	}

	public DeviceVO getDeviceByMacAddress(String macAddress) {
		DeviceVO device = null;
		try {
			this.connect();
			Hashtable<String, DeviceVO> db = (Hashtable<String, DeviceVO>) servletContext.getAttribute("db");
			device = db.get(macAddress);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				this.disconnect();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return device;
	}
	
	public DeviceVO getDeviceById(int id) {
		DeviceVO device = null;
		try {
			this.connect();
			Hashtable<String, DeviceVO> db = (Hashtable<String, DeviceVO>) servletContext.getAttribute("db");
			Set<String> keys = db.keySet();
			for (String key : keys) {
				if(db.get(key).getId() == id){
					device = db.get(key);
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				this.disconnect();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return device;
	}

	public List<DeviceVO> getDevices() {
		ArrayList<DeviceVO> devices = new ArrayList<DeviceVO>();
		try {
			this.connect();
			Hashtable<String, DeviceVO> db = (Hashtable<String, DeviceVO>) servletContext.getAttribute("db");
			Set<String> keys = db.keySet();
			for (String key : keys) {
				devices.add(db.get(key));
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				this.disconnect();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

		return devices;
	}

}
