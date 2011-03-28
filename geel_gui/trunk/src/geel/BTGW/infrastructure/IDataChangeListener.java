package geel.BTGW.infrastructure;

/**
 * This interface implements half of the Observer Design Pattern. Objects wishing to listen
 * for data changes in other objects need to implement the dataChanged() method. This method will
 * be called whenever data changes in the other object.
 * 
 * @author Steven Van Acker
 *
 */
public interface IDataChangeListener {
    public void dataChanged(Object o);
}
