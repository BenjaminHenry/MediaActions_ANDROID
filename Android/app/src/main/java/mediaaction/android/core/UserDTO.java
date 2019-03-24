package mediaaction.android.core;

import java.io.Serializable;

/**
 * Data Transfert Object for users.
 * recuperted from http requests.
 */
public class UserDTO implements Serializable{
	public String id;
	public String email;
	public String username;
	public Integer wallet;
}
