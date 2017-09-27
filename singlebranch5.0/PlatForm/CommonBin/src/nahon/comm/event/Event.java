/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nahon.comm.event;

/**
 *
 * @author jiche
 */
public class Event<E> {
    private final E event;
    private final Object info;
    public Event(E event, Object info){
        this.event = event;
        this.info = info;
    }
    
    public E GetEvent(){
        return this.event;
    }
    
    public Object Info(){
        return this.info;
    }
}
