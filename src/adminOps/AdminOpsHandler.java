package adminOps;

import org.json.JSONException;
import org.json.JSONObject;

import tableOps.Objects;
import pojos.ObjectsModel;

public class AdminOpsHandler {
	
	private String table;
	private JSONObject json;
	private Response res = new Response();
	
	public Response getInfo(JSONObject obj) {
		
		try {
			table = obj.getString("table");
			json = obj.getJSONObject("row");
			selectTable(obj);
			
			return res;
		} catch (JSONException e) {
			res.setData("0", "JSON_EXCEPTION", "JSON Exception occured..");
			e.printStackTrace();
			return res;
		}		
	}
	
	private void selectTable(JSONObject obj) {
		table = table.toLowerCase();
		System.out.println("Inside Switch case selecting table...");
		
		switch(table) {
		
		case "objects" :
			Objects object = new Objects();
			ObjectsModel om = new ObjectsModel();
			
			System.out.println("Objects table is selected");
			om.getData(json);
			res = object.selectOp(obj, om);
			break;
			
		default:
			System.out.println("Invalid table name");
			res.setData("0", "INVALID_TABLE", "Invalid table name");
			break;
		}
	}
	
}
