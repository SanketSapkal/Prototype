package pojos;

import org.json.JSONException;
import org.json.JSONObject;

public class ObjectsModel {

	private String name, description, image;
	private JSONObject obj;
	
	public void getData(JSONObject ob) {
		obj = ob;
		extractData();
	}
	
	private void extractData() {
		System.out.println("Extracting data from row object...");
		
		try {
			
			name = obj.getString("name");
			description = obj.getString("description");
			image = obj.getString("image");
			System.out.println("Data extracted successfully!!!");
			
		} catch (JSONException e) {
			e.printStackTrace();
			System.out.println("JSON Exception!");
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getImage() {
		return this.image;
	}
}
