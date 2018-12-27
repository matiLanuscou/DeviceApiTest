package com.device.api.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Response;

import com.device.api.db.DBConnection;
import com.device.api.exception.DeviceApiException;
import com.device.api.vo.DeviceVO;

public class DeviceService {

	DBConnection db = new DBConnection();

	public void initConection(ServletContext servletContext) {
		db.setServletContext(servletContext);
	}

	public DeviceVO saveDevice(DeviceVO device) throws DeviceApiException {
		this.validateDevice(device);
		db.saveDevice(device);
		return device;
	}

	public List<DeviceVO> getDevices() {
		return db.getDevices();
	}

	public DeviceVO getDeviceByParams(int id, String macAddress) throws DeviceApiException {
		if (id == 0 && macAddress == null) {
			String message = "Debe ingresar al menos un parámetro de búsqueda, id o mac.";
			throw new DeviceApiException(message, Response.Status.BAD_REQUEST);
		} else if (id != 0 && macAddress != null) {
			String message = "Debe ingresar solo un parámetro de búsqueda, id o mac.";
			throw new DeviceApiException(message, Response.Status.BAD_REQUEST);
		}
		DeviceVO device = null;
		if (id > 0) {
			device = db.getDeviceById(id);
		} else {
			device = db.getDeviceByMacAddress(macAddress);
		}

		return device;
	}

	private void validateDevice(DeviceVO device) throws DeviceApiException {
		// TODO - validaciones
		/*
		 * fecha posterior a 1-1-18 validar que
		 */
		boolean hasError = false;
		ArrayList<String> errors = new ArrayList<String>();
		if (!isValidMac(device.getMac())) {
			errors.add("Formato de MAC inválido (FF:FF:FF:FF:FF:FF)");
			hasError = true;
		}
		if(this.getDeviceByParams(0, device.getMac())!= null){
			errors.add("Ya existe un registro con la mac proporcionada");
			hasError = true;
		}
		String message = validateDate(device.getTime());
		if (!"".equals(message)) {
			errors.add(message);
			hasError = true;
		}
		if(hasError){
			throw new DeviceApiException(errors.toString(), Response.Status.BAD_REQUEST);
		}
	}

	private boolean isValidMac(String mac) {
		String patern = "[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}:[0-9a-fA-F]{2}";
		Pattern p = Pattern.compile(patern);
		Matcher m = p.matcher(mac);
		return m.matches();
	}
	
	private String validateDate(String time) {
		String message = "";
		try {
            SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
            formatDate.setLenient(false);
            Date date = formatDate.parse(time);
            if(d1BeforeD2(date, formatDate.parse("1/1/2018"))){
            	message = "La fecha debe ser posterios a 1/1/2018";
            }
        } catch (ParseException e) {
            message = "Formato de fecha inválido (dd/MM/yyyy)";
        }
        return message;
	}
	
	private boolean d1BeforeD2(Date d1, Date d2) {
		if (d1 != null && d2 != null) {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(d1);
			c2.setTime(d2);
			if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR) 
					|| (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH))
					|| (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)&& c1.get(Calendar.DAY_OF_MONTH) < c2.get(Calendar.DAY_OF_MONTH))){
				return true;
			}
		}
		return false;
	}
}
