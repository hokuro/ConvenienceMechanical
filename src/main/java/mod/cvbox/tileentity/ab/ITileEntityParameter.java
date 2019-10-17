package mod.cvbox.tileentity.ab;

public interface ITileEntityParameter {

	int getField(int id);
	void setField(int id, int value);
	int getFieldCount();
}
