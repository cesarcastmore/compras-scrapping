package  com.http;

public class ConnectionException extends Exception {
	private int status;

	public ConnectionException(String name) {
		super(name);
		this.status = status;
	}

	public ConnectionException(String name, int status) {
		super(name);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
