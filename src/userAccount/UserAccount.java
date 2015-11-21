package userAccount; 

import java.io.Serializable;

public abstract class UserAccount implements Serializable {
	private String username;
	private String password;
	private boolean active;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccount other = (UserAccount) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return username;
	}

	UserAccount(String username, String password) {
		this.username = username;
		this.password = password;
		this.active = true;
	}

	public boolean checkPassword(String password) {
		if (password.equals(this.password))
			return true;
		else
			return false;
	}

	public void deactivateAccount() {
		active = false;
	}

	public abstract void getPasswordHelp();

}