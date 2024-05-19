package pack;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Identifiant implements Serializable {
    private String identifiantPrincipal;
    private String edtAssocie;

    public Identifiant() {
    }

    public Identifiant(String identifiantPrincipal, String edtAssocie) {
        this.identifiantPrincipal = identifiantPrincipal;
        this.edtAssocie = edtAssocie;
    }

    

    public String getIdentifiantPrincipal() {
		return identifiantPrincipal;
	}

	public void setIdentifiantPrincipal(String identifiantPrincipal) {
		this.identifiantPrincipal = identifiantPrincipal;
	}

	public String getEdtAssocie() {
		return edtAssocie;
	}

	public void setEdtAssocie(String edtAssocie) {
		this.edtAssocie = edtAssocie;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifiant that = (Identifiant) o;
        return Objects.equals(identifiantPrincipal, that.identifiantPrincipal) &&
               Objects.equals(edtAssocie, that.edtAssocie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifiantPrincipal, edtAssocie);
    }
}

