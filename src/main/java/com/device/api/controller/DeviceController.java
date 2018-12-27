package com.device.api.controller;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.device.api.exception.DeviceApiException;
import com.device.api.service.DeviceService;
import com.device.api.vo.DeviceVO;

@Path("device")
public class DeviceController {

	@Context
	private ServletContext servletContext;
	private DeviceService service = new DeviceService();

	@GET
	@Path("/test")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response testBase(@Context final HttpServletResponse response) throws JSONException {
		initConection();
		JSONObject json = new JSONObject();
		json.put("status", "success");
		json.put("code", Response.Status.OK.getStatusCode());
		return Response.status(Response.Status.OK).entity(json.toString()).build();
	}

	@POST
	@Path("/save")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response saveDevice(DeviceVO device) throws JSONException {
		initConection();
		JSONObject json = new JSONObject();
		Status status = Response.Status.OK;
		try {
			DeviceVO resp = service.saveDevice(device);
			json.put("id", resp.getId());
			json.put("mac", resp.getMac());
			json.put("time", resp.getTime());
		} catch (DeviceApiException e) {
			json.put("code", e.getStatus().getStatusCode());
			json.put("message", e.getMessage());
			status = e.getStatus();
		}
		return Response.status(status).entity(json.toString()).build();
	}

	@GET
	@Path("/getDevices")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getDevices() throws JSONException {
		initConection();
		JSONObject json = new JSONObject();
		Status status = Response.Status.OK;
		List<DeviceVO> devices = service.getDevices();
		if (devices.size() == 0) {
			status = Response.Status.NOT_FOUND;
		}
		json.put("found", devices.size());
	 	JSONArray array = new JSONArray();
	 	for(DeviceVO device : devices){
	 		JSONObject jsonDevice = new JSONObject();
	 		jsonDevice.put("id", device.getId());
	 		jsonDevice.put("mac", device.getMac());
	 		jsonDevice.put("time", device.getTime());
	 		array.put(jsonDevice);
	 	}
		json.put("devices", array);
		return Response.status(status).entity(json.toString()).build();
	}

	@GET
	@Path("/getDevice")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getDevice(@QueryParam("id") int id, @QueryParam("mac") String mac) throws JSONException {
		Status status = Response.Status.OK;
		JSONObject json = new JSONObject();
		initConection();
		try {
			DeviceVO resp = service.getDeviceByParams(id, mac);
			if(resp == null){
				status = Response.Status.NOT_FOUND;
				json.put("code", status.getStatusCode());
				json.put("message", "No se encontraron resultados");
			} else {
				json.put("id", resp.getId());
				json.put("mac", resp.getMac());
				json.put("time", resp.getTime());
			}
		} catch (DeviceApiException e) {
			json.put("code", e.getStatus().getStatusCode());
			json.put("message", e.getMessage());
			status = e.getStatus();
		}
		return Response.status(status).entity(json.toString()).build();
	}

	private void initConection() {
		// TODO - usar sprint
		service.initConection(servletContext);
	}
}
