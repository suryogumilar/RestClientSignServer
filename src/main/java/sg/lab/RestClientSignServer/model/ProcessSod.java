package sg.lab.RestClientSignServer.model;

public class ProcessSod {

	private String dataGroup[];

	public String[] getDataGroup() {
		return dataGroup;
	}

	public void setDataGroup(String dataGroup[]) {
		this.dataGroup = dataGroup;
	}
	public String toString() {
		String str = "";
		int ii = 1;
		for (String dg : dataGroup) {
			str += "DG"+ii+" = "+dg+"; ";
		}
		return str;
	}
}
