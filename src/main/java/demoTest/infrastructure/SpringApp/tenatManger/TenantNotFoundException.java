package demoTest.infrastructure.SpringApp.tenatManger;

public class TenantNotFoundException extends Exception {
	public TenantNotFoundException(String message) {
		super(message);
	}
}
