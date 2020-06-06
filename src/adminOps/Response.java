package adminOps;

public class Response {
	private String ID, Code, Message;
	
	public void setData (String id, String code, String message) {
		this.ID = id;
		this.Code = code;
		this.Message = message;
	}
	
	public String getCode() {
		return this.Code;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public String getMessage() {
		return this.Message;
	}
}
